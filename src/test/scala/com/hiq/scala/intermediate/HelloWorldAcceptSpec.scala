package com.hiq.scala.intermediate

import org.specs2._
import org.specs2.specification.Fragments

/**
 * Acceptance Specs
 */
class HelloWorldAcceptSpec extends Specification {  def is: Fragments = s2"""

  This is a specification to check the 'Hello World' string
    contain 11 characters                                           $e1
    start with 'Hello'                                              $e2
    end with 'world'                                                $e3
                                                                    """

  def e1 = "Hello world" must have size 11
  def e2 = "Hello world" must startWith("Hello")
  def e3 = "Hello world" must endWith("world")
}
