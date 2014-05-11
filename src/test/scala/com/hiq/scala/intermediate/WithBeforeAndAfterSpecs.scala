package com.hiq.scala.intermediate

import org.specs2.mutable.{BeforeAfter, After, Before, Specification}

/**
 * Before and After without isolation
 *
 * There are many helpful traits: Before, After, BeforeAfter and many more
 */
class WithBeforeAndAfterSpecs extends Specification with BeforeAfter {

  def cleanDatabase() = {}
  def dropConnection()  = {}

  override def after = cleanDatabase()
  override def before = dropConnection()

  "This is a specification where the database is cleaned up before each example" >> {
    "first example" in { success }
    "second example" in { success }
  }
}
