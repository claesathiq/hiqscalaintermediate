package com.hiq.scala.intermediate

/**
 * Views bounds
 */
object Views extends App {
  implicit def strToInt(x: String) = x.toInt

  // Specify a view bound with <%
  class Container[A <% Int] { def addIt(x: A) = 123 + x }

  val stringCont = (new Container[String]).addIt("123")
  println(stringCont.getClass + ": " + stringCont)

  val intCont = (new Container[Int]).addIt(123)
  println(intCont.getClass + ": " + intCont)

  // (new Container[Float]).addIt(123.2F)  // Throws "No implicit view available from Float => Int
}