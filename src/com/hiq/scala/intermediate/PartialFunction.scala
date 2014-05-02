package com.hiq.scala.intermediate

/**
 * Partial functions, introduction
 */
object PartialFunction extends App {
  val salaries = Map("junior" -> 35000, "normal" -> 45000, "senior" -> 55000, "boss" -> 70000)

  // Map.filter takes a predicate function, in this case p: ((String, Int)) => Boolean
  //   we get a tuple, so we have get key with position                           ↓
  val highSalaryFunc = salaries.filter((nameSalary: (String, Int)) => nameSalary._2 > 50000)
  println(highSalaryFunc)

  // we can actually use pattern matching to extract key and value
  val highSalaryMatch = salaries.filter({case (name, salary) => salary > 50000})
  println(highSalaryMatch)

  // So if Map.filter takes a function, how can a pattern match work?
}
