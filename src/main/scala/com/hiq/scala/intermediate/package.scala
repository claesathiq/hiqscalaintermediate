package com.hiq.scala

/**
 * package object
 */
package object intermediate {
  type Tree[A] = List[A]

  object Tree {
    def apply[A](el: A*) = List(el:_*)
  }

  implicit class TreeList[A](l: List[A]) {
    def removeNodes(remNode: A*) = {
      val seq = for {
        n <- l
        if !remNode.contains(n)
      } yield n
      List(seq:_*)
    }
  }
}
