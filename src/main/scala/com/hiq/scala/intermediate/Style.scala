package com.hiq.scala.intermediate

import java.net.{SocketAddress, Socket, InetSocketAddress}

/**
 * Collections and Option
 */
object Style extends App {

  // Option
  val numbers = Map(1 -> "one", 2 -> "two")
  println(numbers.get(2).get)
  println(numbers.get(3)) // calling get here throws exception, NoSuchElement

  // Don't use get! Use getOrElse, if all you need is a default value
  println(s"default values is ${numbers.get(3).getOrElse(0)}")

  // Or if you need conditional execution (only if there is a value), this is preferred
  numbers.get(2).foreach(n =>
    println(s"foreach found $n")
  )

  // Or, if you need different action on either path, pattern matching
  val cn = numbers.get(3) match {
    case Some(n) => n
    case None => 0
  }
  println(s"Case resolved to $cn")

  // Combinators
  // flatMap is often used to collapse a chain of options
  val host: Option[String] = Some("localhost")
  val port: Option[Int] = Some(22)

  val addrFlat: Option[InetSocketAddress] =
    host flatMap { h =>
      port map { p =>
        new InetSocketAddress(h, p)
      }
    }
  println("flatMap Inet " + addrFlat)

  // as often with flatMap, a for comprehension is more succinct
  val addrFor: Option[InetSocketAddress] = for {
    h <- host
    p <- port
  } yield new InetSocketAddress(h, p)
  println("for Inet " + addrFor)

  // Return type annotations, always use when instantiating objects with mixins; the compiler creates singleton types
  trait Service
  def makeNoType() = new Service {  // The compiler will create a singleton return type Object with Service{def getId: Int}!!
    def getId = 123
  }
  // This is better: it makes it possible to mix more traits in, with no change in type, also preserves compatibility
  def makeWithType(): Service = new Service{
    def getId = 123
  }

  // Don’t use subclassing when an alias will do.
  trait SocketFactoryTrait extends (SocketAddress => Socket) // Duh, a SocketFactory IS a function return a Socket
  // Using an alias instead, will allow function literals and function composition
  type SocketFactory = InetSocketAddress => Socket

  val addrToInet: InetSocketAddress => String = (sa: InetSocketAddress) => sa.toString
  val inetToSocket: String => Socket = (s: String) => new Socket(s, 80)
  val factory: SocketFactory = addrToInet andThen inetToSocket

  // When a type alias need to be bound to top level names, use a package object
  package object net {
    type SocketFactory = InetSocketAddress => Socket
  }


}
