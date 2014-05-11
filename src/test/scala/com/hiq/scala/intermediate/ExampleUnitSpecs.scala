package com.hiq.scala.intermediate

import org.specs2.mutable._
import org.specs2.specification.Scope

/**
 * Unit Specs
 */
class ExampleUnitSpecs extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size 11
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

  "Arithmetic" should {
    "add" in {
      "two numbers" in {
        1 + 1 mustEqual 2
      }
      "three numbers" in {
        1 + 1 + 1 mustEqual 3
      }
    }
  }

  "Mutations can be made isolated with the 'isolated' specification" should {
    isolated
    var x = 0
    "x equals 1 if we set it." in {
      x = 1
      x mustEqual 1
    }
    "x is the default value if we don't change it" in {
      x mustEqual 0
    }
  }

  "this is the first example" in new trees {
    tree.removeNodes(2, 3) must have size 2
  }
  "this is the first example" in new trees {
    tree.removeNodes(2, 3, 4) must have size 1
  }

  /** the `trees` context */
  trait trees extends Scope {
    val tree = Tree(1, 2, 3, 4)
  }
}
