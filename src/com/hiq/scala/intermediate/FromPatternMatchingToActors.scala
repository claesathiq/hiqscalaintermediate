package com.hiq.scala.intermediate

import akka.actor.{Actor, ActorSystem, Props}

/**
 * From Pattern Matching to Actors - Actors refresh
 */
object FromPatternMatchingToActors extends App {
  class HelloWorld extends Actor {

    def receive = {
      case Greeter.Done => context.system.shutdown()
    }

    override def preStart(): Unit = {
      val greeter = context.actorOf(Props[Greeter], "greeter")
      greeter ! Greeter.Greet
    }
  }

  object Greeter {
    case object Greet
    case object Done
  }

  class Greeter extends Actor {
    def receive = {
      case Greeter.Greet =>
        println("Hello World!")
        sender ! Greeter.Done
    }
  }

  val system = ActorSystem("actors")
  system.actorOf(Props[HelloWorld], "helloworld")
}
