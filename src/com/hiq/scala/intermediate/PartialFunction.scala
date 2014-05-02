package com.hiq.scala.intermediate

/**
 * Partial functions, continued
 */
object PartialFunction extends App {
  val one: PartialFunction[Int, String] = { case 1 => "one" }
  println(one.isDefinedAt(1))
  println(one.isDefinedAt(2))
  println(one(1))
  // println(one(2))  // Throws a scala.MatchError
}
