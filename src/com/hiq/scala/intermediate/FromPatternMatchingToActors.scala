package com.hiq.scala.intermediate

/**
 * From Pattern Matching to Actors
 */
object FromPatternMatchingToActors extends App {
  // Decompose with tuple
  val tup = ("one", "two", "three")
  println(tup)
  val (a, b, c) = tup
  println("Decompose with tuple: " + a + ", " + b + ", " + c)

  // Decompose with binding on sub match
  case class ABeeCee(a: String, b: String, c: String)
  val abc = ABeeCee("one", "two", "three")
  abc match {
    case ABeeCee(x, y, z) => println("Match with sub-match: " + x + ", " + y + ", " + z)
  }



  // Note how we can match on specific values:
  abc match {
    case ABeeCee(x, y, "three") => println("Match on a, b and \"three\"")
  }
  abc match {
    case ABeeCee(x, y, "four") => println("Match on a, b and \"four\"")
    case _ => println("NO match on a, b and \"four\"")
  }


  // Match on class
  abstract class Shape
  case class Circle(radius: Int) extends Shape
  case class Rectangle(width: Int, height: Int) extends Shape

  def area(shape: Shape): Double = shape match {
    case Circle(radius) => Math.PI * radius * radius
    case Rectangle(width, height) => width * height
  }
  println(area(Circle(2)))
  println(area(Rectangle(3, 4)))
}
