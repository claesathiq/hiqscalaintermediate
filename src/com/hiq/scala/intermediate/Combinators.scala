package com.hiq.scala.intermediate

/**
 * Functional Combinators
 */
object Combinators extends App {
  val numbers = List(1, 2, 3, 4)
  def timesTwo(i: Int): Int = i * 2
  def isEven(i: Int): Boolean = i % 2 == 0

  // map, evaluates each element and return a list with the same number of elements
  println("map" + numbers.map((i: Int) => i * 2))
  println("map" + numbers.map(timesTwo _))

  // forEach, evaluates each element and returns nothing, for side-effects only
  println("print forEach" + numbers.foreach((i: Int) => i * 2))  // prints (), the Unit (or void) type
  numbers.foreach((i: Int) => println(s"in forEach: ${i * 2}"))

  // filter, removes all elements where the predicate is false
  println("filter" + numbers.filter(isEven _))

  // zip zip aggregates the contents of two lists into a single list of pairs
  println("zip" + List(1, 2, 3).zip(List("a", "b", "c")))

  // partition,  splits a list based on a predicate function
  println(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).partition(_ %2 == 0))

  // find returns the first element of a collection that matches, returns an Option
  println("find" + numbers.find((i: Int) => i > 2))

  // drop drops the first n elements
  println("drop"+ numbers.drop(2))

  // dropWhile removes the first elements that match
  println("dropWhile"+ numbers.dropWhile(_ % 2 != 0))  // Only 1, is dropped, since 3 is not first: it is "shielded" by 2

  // foldLeft; folds each element into the element to the left. Here 0 is the starting number, can be any value
  println("foldLeft" + numbers.foldLeft(0)((m: Int, n: Int) => m + n))

  // foldRight, same as foldLeft but opposite direction
  println("foldRight" + numbers.foldRight(0)((m: Int, n: Int) => m + n))

  // flatten collapses one level of nested structure
  println("flat" + List(List(1, 2), List(3, 4)).flatten)

  // flatMap combines mapping and flattening
  println("flatMap" + List(List(1, 2), List(3, 4)).flatMap(x => x.map(_ * 2)))
}
