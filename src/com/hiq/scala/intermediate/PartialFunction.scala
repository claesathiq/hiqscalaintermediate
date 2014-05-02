package com.hiq.scala.intermediate

/**
 * Partial functions, composing
 */
object PartialFunction extends App {
  val one: PartialFunction[Int, String] = { case 1 => "one" }
  val two: PartialFunction[Int, String] = { case 2 => "two" }
  val three: PartialFunction[Int, String] = { case 3 => "three" }
  val wildcard: PartialFunction[Int, String] = { case _ => "something else" }

  // Compose a partial function
  val partial = one orElse two orElse three orElse wildcard

  println(partial(1))
  println(partial(3))
  println(partial(0))
  println(partial(5))

  // Note the similarity to a pattern match
  def patternMatch(x: Int): String = x match {
    case 1 => "one"
    case 2 => "two"
    case 3 => "three"
    case _ => "something else"
  }

  println(patternMatch(1))
  println(patternMatch(3))
  println(patternMatch(0))
  println(patternMatch(5))
}
