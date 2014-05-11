package com.hiq.scala.intermediate

/**
 * Created: 2014-05-11 20:14
 *
 * @author Claes Jonsson
 */
class HelloWorld {
  def greet = "Hello World"
}

object HelloWorld {
  def apply() = new HelloWorld();
}
