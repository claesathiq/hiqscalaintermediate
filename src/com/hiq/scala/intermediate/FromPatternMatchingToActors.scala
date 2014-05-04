package com.hiq.scala.intermediate

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinRouter

/**
 * From Pattern Matching to Actors - Actor routing the easy way
 */
object FromPatternMatchingToActors extends App {

  case class Work()
  case class Result(value: Int)

  class Worker extends Actor {
    def receive = {
      case w: Work => println("Working...")
    }
  }

  class Master extends Actor {

    // To specify a supervisor strategy, do
    // RoundRobinRouter(5).withSupervisorStrategy(yourStrategy)
    val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(5)), name = "workerRouter")

    def receive = {
      case w: Work =>
        for (i <- 0 until 10000) workerRouter ! w
      case Result(value) =>
        println("Result is " + value)
    }
  }
}
