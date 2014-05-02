package com.hiq.scala.intermediate

/**
 * Views
 */
object Views extends App {
  implicit def strToInt(x: String) = x.toInt
  val str = "123"
  println(str.getClass + ": " + str)
  val y: Int = "123"
  println(y.getClass + ": " + y)
  println(Math.max(str, y))
}