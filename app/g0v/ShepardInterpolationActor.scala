package g0v

import akka.actor._

import com.nr.interp._

class ShepardInterpolationActor extends Actor with ActorLogging {
  def receive = {
    case (samples: Seq[(Double, Double, Double)]) ⇒ {
      val ptss: Array[Array[Double]] = samples.map { tuple ⇒ Array(tuple._1, tuple._2) }.toArray
      val valss: Array[Double] = samples.map { tuple ⇒ tuple._3 }.toArray
      val shepard = new Shep_interp(ptss, valss)

      sender ! shepard
    }
  }

}