import java.util.Scanner

import akka.actor.{Props, ActorRef, ActorSystem, Actor}
import akka.routing.RoundRobinPool

class WorkerUpRouted extends Actor{
  def receive = {
    case message:String => {
      println(s"$self() got message $message")
      sender ! message.toUpperCase()
      while(true){}
      //Thread.sleep(5000)
    }
  }
}

object UpperPoolApp extends App {

  // Create the 'demo' actor system
  val system = ActorSystem("demo")

  // Create the actors
  val router: ActorRef = system.actorOf(RoundRobinPool(5).props(Props[WorkerUpRouted]), "router")
  val callback = system.actorOf(Props[Callback], "callback")

  val sc = new Scanner(System.in)

  while (sc.hasNext) {
    val message = sc.nextLine()

    message match {
      case "exit" => {
        system.terminate()
        System.exit(0)
      }
      case other => router.tell(other, callback)
    }
  }

}