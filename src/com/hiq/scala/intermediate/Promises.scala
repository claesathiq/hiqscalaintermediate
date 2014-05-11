package com.hiq.scala.intermediate

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.duration._

/**
 * Promises
 */
object Promises extends App {
  case class TaxCut(reduction: Int)
  // either give the type as a type parameter to the factory method:
  val taxcut = Promise[TaxCut]()
  // or give the compiler a hint by specifying the type of your val:
  val taxcut2: Promise[TaxCut] = Promise()

  // Notice how the Future is returned immediately
  val taxcutF: Future[TaxCut] = taxcut.future

  taxcutF.onComplete {
    case Success(TaxCut(reduction)) => println(s"As promised, a tax reduction by $reduction")
    case Failure(ex) => println(s"Broken promise due to ${ex.getMessage}")
  }

  // Notice how this line prints before the above line
  println("Fulfilling promise")

  // Normally the promise would be fulfilled or failed on a separate thread.
  taxcut.success(TaxCut(20))
  Await.ready(taxcutF, 100 millis)
}
