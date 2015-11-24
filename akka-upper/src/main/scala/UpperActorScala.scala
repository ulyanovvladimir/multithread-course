import java.util.Scanner

import akka.actor._
import akka.routing.{RoundRobinPool, SmallestMailboxPool}

/**
  *
  */

class WorkerUpper extends Actor {
  def receive = {
    case message:String => sender ! message.toUpperCase()
  }
}

class Callback extends Actor {
  def receive = {
    case message => println(s"result: $message")
  }
}


object UpperApp extends App {

  // Create the 'demo' actor system
  val system = ActorSystem("demo")

  // Create the actors
  val worker = system.actorOf(Props[WorkerUpper], "worker")

  val callback = system.actorOf(Props[Callback], "callback")

  val sc = new Scanner(System.in)

  while (sc.hasNext) {
    val message = sc.nextLine()

    message match {
      case "exit" => {
        system.terminate()
        System.exit(0)
      }
      case other => worker.tell(other, callback)
    }
  }

}

