package tracker

import akka.actor.Actor

import scala.concurrent.duration._

class ViewActor extends Actor {

  var viewRefreshRate = new FrequencyMeter()

  var nextUpdate = 2000000L
  val minPeriod = 1000000L

  def spinnerActor = context.actorSelection("/user/spinnerActor")

  def receive = {
    case ViewActorUpdate =>
      val guiPeriod = GUI.fps.period

      nextUpdate -= (viewRefreshRate.period - guiPeriod * 4) / 1
      nextUpdate = if (nextUpdate < minPeriod) minPeriod else nextUpdate

      implicit val ec = scala.concurrent.ExecutionContext.global
      context.system.scheduler.scheduleOnce(nextUpdate nanoseconds, self, ViewActorUpdate)
      spinnerActor ! SpinnerActorQuery(self)
      println(f"[view] update $nextUpdate current rate: $viewRefreshRate")
      viewRefreshRate.tick()

//    case ViewActorDrawPoint(point) =>
//      self ! ViewActorDrawImage(matrgb)

    case ViewActorDrawImage(matrgb) =>
      GUI.drawImage(matrgb)
    //      println("[view] draw")

    case _ => println("duh")
  }
}
