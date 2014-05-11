package com.hiq.scala.intermediate

import java.net.{MalformedURLException, URL}
import scala.util.{Success, Failure, Try}
import scala.io.Source
import java.io.{FileNotFoundException, InputStream}

/**
 * Idiomatic Error handling in Scala
 */
object ErrorHandlingWithTry extends App {
  case class Program(code: String)
  case class NotAllowedNearTheKeyboardException(msg: String) extends Exception(msg)
  abstract class Employee
  case class Programmer() extends Employee
  case class NonProgrammer() extends Employee
  def writeProgram(employee: Employee): Program = employee match {
    case Programmer() => Program("Very nifty code here")
    case NonProgrammer() => throw NotAllowedNearTheKeyboardException("Stay away from that keyboard!")
  }

  val employee = NonProgrammer()
  val program =
  try {
    writeProgram(employee)
  } catch {
    case NotAllowedNearTheKeyboardException(msg) => msg
  }
  println(program)

  // Using scala.util.Try
  def parseURL(url: String): Try[URL] = Try(new URL(url))
  println(parseURL("http://someurl.com"))
  println(parseURL("notaurl"))

  // Try is very similar to an Option, and the same constructs can be used with Try as with Option (and some more)
  // Replace "notaurl" with StdIn.readLine("URL: ") to try out on the command line
  val url = parseURL("notaurl") getOrElse new URL("http://someurl.com")
  println(url)

  // Mapping and flatMapping
  println(parseURL("http://someurl.com").map(_.getProtocol))
  println(parseURL("notaurl").map(_.getProtocol))

  // map can yield clumsy return types
  def inputStreamForURLwithMap(url: String): Try[Try[Try[InputStream]]] = parseURL(url).map { u =>
    Try(u.openConnection()).map(conn => Try(conn.getInputStream))
  }
  println(inputStreamForURLwithMap("http://google.com"))

  // flatMap to the rescue
  def inputStreamForURL(url: String): Try[InputStream] = parseURL(url).flatMap { u =>
    Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream))
  }
  println(inputStreamForURL("http://google.com"))

  // filter
  def parseHttpURL(url: String) = parseURL(url).filter(_.getProtocol == "http")
  println(parseHttpURL("http://google.com"))
  println(parseHttpURL("ftp://google.com"))

  // forEach
  parseHttpURL("http://google.com").foreach(println)
  parseHttpURL("ftp://google.com").foreach(println)   // Nothing will be printed

  // Of course, map, flatMap and filter means that we use for comprehensions
  // NOTE!! All the examples with connections do not close or otherwise clean up - DO NOT USE AS IS!!!
  def getURLContent(url: String): Try[Iterator[String]] =
    for {
      url <- parseURL(url)
      connection <- Try(url.openConnection())
      is <- Try(connection.getInputStream)
      source = Source.fromInputStream(is)
    } yield source.getLines()
  println(getURLContent("http://google.com"))  // Type is Try[Iterator[String]]

  // If you need to know whether you have a success or failure, a pattern match is often useful
  getURLContent("notaurl") match {
    case Success(lines) => lines.foreach(println)
    case Failure(ex) => println(s"Problem rendering URL content: ${ex.getMessage}")
  }

  // Recovering from Failure; effectively transforming a Failure to a Success
  // Note that this is the first real difference from Option
  val content = getURLContent("garbage") recover {
    case e: FileNotFoundException => Iterator("Requested page does not exist")
    case e: MalformedURLException => Iterator("Please make sure to enter a valid URL")
    case _ => Iterator("An unexpected error has occurred. We are so sorry!")
  }
  // Notice that type is Success[Iterator[String]], not Failure...
  content foreach {iter =>
    iter foreach print
  }
}
