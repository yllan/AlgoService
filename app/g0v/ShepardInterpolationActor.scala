package g0v

import akka.actor._

import com.nr.interp._

case class ShepardParameter(p: Double, samples: Seq[(Double, Double, Double)])

class ShepardInterpolationActor extends Actor with ActorLogging {
  def receive = {
    case ShepardParameter(p, samples) ⇒ {
      val ptss: Array[Array[Double]] = samples.map { tuple ⇒ Array(tuple._1, tuple._2) }.toArray
      val valss: Array[Double] = samples.map { tuple ⇒ tuple._3 }.toArray
      val shepard = new Shep_interp(ptss, valss, p)

      sender ! shepard
    }
  }

}