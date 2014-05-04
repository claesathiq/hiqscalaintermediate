package com.hiq.scala.intermediate

import akka.actor._
import akka.routing.RoundRobinPool
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory

/**
 * From Pattern Matching to Actors - the Actor exercise
 */
object FromPatternMatchingToActors extends App {
  // nrOfWorkers = 4,       the number of workers to run in the pool, tweak according to available CPUs
  // nrOfElements = 10000,  the number of elements in a chunk: 1/n, 1/(n+1) ... 1/(n + nrOfElements)
  // nrOfMessages = 10000,  the number of chunks (or put another way, the number of messages sent to Worker)
  calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)

  // Suggested messages
  sealed trait PiMessage
  // Starts the show
  case object Calculate extends PiMessage
  // Calculate a chunk
  case class Work(start: Int, nrOfElements: Int) extends PiMessage
  // Return value from a Worker
  case class Result(value: Double) extends PiMessage
  // The end result from Master
  case class PiApproximation(pi: Double, duration: Duration)

  class Worker extends Actor {

    // The calculation of a chunk
    def calculatePiFor(start: Int, nrOfElements: Int): Double = {
      var acc = 0.0
      for (i <- start until (start + nrOfElements))
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      acc
    }

    def receive = {
      // Receive Work, calculate, send Result back
  }

  class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {

    // result accumulator
    var pi: Double = _
    // track number of returned results
    var nrOfResults: Int = _
    // timing
    val start: Long = System.currentTimeMillis

    val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

    def receive = {
      // Receive Calculate,  immediately send appropriate chunks to all workers, via the router

      // Receive Result from each Worker, accumulate in pi, check how many results we have
      //    if we have all results, send PiApproximation to listener and stop self and all children
    }
  }

  class Listener extends Actor {
    def receive = {
      // Receive PiApproximation, print and shut down system
  }

  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    // Create an Akka system
    val system = ...

    // create the result listener, which will print the result and shutdown the system
    val listener = ...

    // create the master
    val master = ...

    // start the calculation
    master ! Calculate

  }
}
