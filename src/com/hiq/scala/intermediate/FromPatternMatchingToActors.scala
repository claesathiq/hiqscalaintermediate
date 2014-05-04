package com.hiq.scala.intermediate

import akka.actor.{Terminated, Actor, Props}
import akka.routing.{ActorRefRoutee, Router, RoundRobinRoutingLogic}

/**
 * From Pattern Matching to Actors - Actor routing the canonical way
 */
object FromPatternMatchingToActors extends App {

  case class Work()

  class Worker extends Actor {
    def receive = {
      case w: Work => println("Working...")
    }
  }

  class Master extends Actor {
    var router = {
      val routees = Vector.fill(5) {
        val r = context.actorOf(Props[Worker])
        context watch r
        ActorRefRoutee(r)
      }
      Router(RoundRobinRoutingLogic(), routees)
    }

    def receive = {
      case w: Work =>
        router.route(w, sender())
      case Terminated(a) =>
        router = router.removeRoutee(a)
        val r = context.actorOf(Props[Worker])
        context watch r
        router = router.addRoutee(r)
    }
  }
}
