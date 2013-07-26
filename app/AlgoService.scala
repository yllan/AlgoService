package controllers

import play.api._
import play.api.Play.current

import play.api.mvc._
import play.api.libs.concurrent.Akka 

import play.api.libs.concurrent._
import play.api.libs.ws.WS
import play.api.libs.json._
import play.api.libs.iteratee._

import akka.actor._
import akka.pattern.{ ask, pipe }
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

import g0v._
import com.nr.interp._
import play.api.libs.json

object AlgoService extends Controller {
  val shepardActor = Akka.system.actorOf(Props[ShepardInterpolationActor], name = "shepard_actor")

  def index = Action {
    Logger.info("index!")
    Ok("Welcome to algo service.")
  }
  
  def interpolate = Action { implicit req =>
    val minLongitude = req.queryString.get("min_long").map(l => try {l.head.toDouble} catch { case t: Throwable => 120.0}).getOrElse(120.0)
    val maxLongitude = req.queryString.get("max_long").map(l => try {l.head.toDouble} catch { case t: Throwable => 122.5}).getOrElse(122.5)
    val minLatitude = req.queryString.get("min_lat").map(l => try {l.head.toDouble} catch { case t: Throwable => 22.0}).getOrElse(22.0)
    val maxLatitude = req.queryString.get("max_lat").map(l => try {l.head.toDouble} catch { case t: Throwable => 25.5}).getOrElse(22.5)
    val epsilon = req.queryString.get("epsilon").map(l => try {l.head.toDouble} catch { case t: Throwable => 0.02}).getOrElse(0.02)
    val selector = req.queryString.get("selector").map(_.head).getOrElse("today")

    (for (url <- req.queryString.get("url").map(_.head))
     yield {
      Logger.info("url: " + url)
    
      val shepardF: Future[Shep_interp] =
        WS.url(url).get.map(req => try { req.json } catch { case t: Throwable => Json.obj() }).flatMap((data: JsValue) => {
        import g0v.Geo._

        val rainfallSamples: Seq[(Double, Double, Double)] =
          (for (kv <- data.as[Map[String, Map[String, String]]];
               loc <- stations.get(kv._1))
          yield
            (loc.longitude, loc.latitude, try { (kv._2)(selector).toDouble } catch { case t: Throwable => 0.0 })
          ).toSeq
        implicit val timeout = Timeout(120.seconds)
        Logger.info("samples calculated!")
        (shepardActor ask rainfallSamples).mapTo[Shep_interp]
      })

      val (enumerator, channel) = Concurrent.broadcast[String]

      shepardF.map(shepard => {
      
        def generateGrids(longitude: Double, latitude: Double, first: Boolean): Unit = {
          val z = shepard.interp(Array(longitude, latitude))
          channel.push((if (!first) "," else "") + f"[$longitude%.4f,$latitude%.4f,$z%.4f]")
          if (longitude >= maxLongitude && latitude >= maxLatitude) {
            channel.push("]")
            channel.eofAndEnd()
          } else if (latitude >= maxLatitude)
            generateGrids(longitude + epsilon, minLatitude, false)
          else
            generateGrids(longitude, latitude + epsilon, false)
        }

        generateGrids(minLongitude, minLatitude, true)
      })

      channel.push("[")
      Ok.stream(enumerator)

    }).getOrElse(Ok("Ooops")) // Error, return empty result
  }
  
}