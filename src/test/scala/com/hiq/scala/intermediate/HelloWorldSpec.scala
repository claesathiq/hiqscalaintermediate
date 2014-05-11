package com.hiq.scala.intermediate

import org.specs2.mutable.Specification

/**
 * HelloWorld Specs
 */
class HelloWorldSpec extends Specification {

  "Running HelloWorld" should {
    "say 'Hello World'" in {
      HelloWorld().greet must equalTo("Hello World")
    }
  }
}
