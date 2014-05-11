package com.hiq.scala.intermediate

import scala.collection.mutable.HashMap
import scala.collection.mutable.Stack
import scala.collection.mutable

/**
 * A Scala Basic DSL
 */
class ScalaBasic {
  abstract sealed class BasicLine
  case class PrintString(num: Int, s: String) extends BasicLine
  case class PrintResult(num:Int, fn:Function0[String]) extends BasicLine
  case class PrintVariable(num: Int, s: Symbol) extends BasicLine
  case class PrintNumber(num: Int, number: BigInt) extends BasicLine
  case class Goto(num: Int, to: Int) extends BasicLine
  case class Input(num: Int, name: Symbol) extends BasicLine
  case class Let(num:Int, fn:Function0[Unit]) extends BasicLine
  case class If(num:Int, fn:Function0[Boolean], thenJmp:Int) extends BasicLine
  case class End(num: Int) extends BasicLine

  /**
   * Bindings holds the two types of values provided, atoms and numerics.
   * It takes a type parameter on initialization corresponding to the
   * actual type.
   */
  class Bindings[T,U] {
    val atoms = mutable.HashMap[Symbol, T]()
    val numerics = mutable.HashMap[Symbol, U]()

    /**
     * set uses a little hack to allow the storage of either one type or
     * another, but none other.
     */
    def set[X >: T with U](k:Symbol, v:X) = v match {
      case u:U => numerics(k) = u
      case t:T => atoms(k) = t
    }
    def atom(k:Symbol):T = atoms(k)
    def num(k:Symbol):U = numerics(k)

    /**
     * Technically, you can have two variables with the same name with
     * different types at the same time, but for this version that does
     * not come into play.
     */
    def any(k:Symbol):Any = {
      (atoms.get(k), numerics.get(k)) match {
        case (Some(x), None) => x
        case (None, Some(y)) => y
        case (None, None) => None
        case (Some(x), Some(y)) => Some(x,y)
      }
    }
  }

  val lines = new mutable.HashMap[Int, BasicLine]
  val binds = new Bindings[String, Int]
  val returnStack = new mutable.Stack[Int]

  /**
   * The Assignment class is used by the `symbol2Assignment` implicit to
   * stand-in for a Scala symbol in the LET form.  This class returns
   * a function of () => Unit that does the appropriate binding.
   */
  case class Assignment(sym:Symbol) {
    def :=(v:String):Function0[Unit] = () => binds.set(sym, v)
    def :=(v:Int):Function0[Unit] = () => binds.set(sym, v)
    def :=(v:Function0[Int]):Function0[Unit] = () => binds.set(sym, v())
  }

  /**
   * The MathFunction class is used by the `symbol2MathFunction` and
   * `fnOfInt2MathFunction` implicits to stand in for Scala symbols and
   * functions of type () => Int, the latter being constructed at run-time.
   */
  case class MathFunction(lhs:Function0[Int]) {
    def *(rhs:Int):Function0[Int] = () => lhs() * rhs
    def *(rhs:Function0[Int]):Function0[Int] = () => lhs() * rhs()
    def /(rhs:Int):Function0[Int] = () => lhs() / rhs
    def /(rhs:Function0[Int]):Function0[Int] = () => lhs() / rhs()
    def +(rhs:Symbol):Function0[Int] = () => lhs() + binds.num(rhs)
    def +(rhs:Function0[Int]):Function0[Int] = () => lhs() + rhs()
    def -(rhs:Symbol):Function0[Int] = () => lhs() - binds.num(rhs)
    def -(rhs:Function0[Int]):Function0[Int] = () => lhs() - rhs()
  }

  /**
   * The BinaryRelation class is used by the `symbol2BinaryRelation` and
   * `fnOfInt2BinaryRelation` implicits to stand in for Scala symbols and
   * functions of type () => Int, the latter being constructed at run-time.
   */
  case class BinaryRelation(lhs:Function0[Int]) {
    def ===(rhs:Int):Function0[Boolean] = () => lhs()  == rhs
    def <=(rhs:Int):Function0[Boolean] = () => lhs() <= rhs
    def <=(rhs:Symbol):Function0[Boolean] = () => lhs() <= binds.num(rhs)
    def >=(rhs:Int):Function0[Boolean] = () => lhs() >= rhs
    def >=(rhs:Symbol):Function0[Boolean] = () => lhs() >= binds.num(rhs)
    def <(rhs:Int):Function0[Boolean] = () => lhs() < rhs
    def >(rhs:Int):Function0[Boolean] = () => lhs() > rhs
  }

  /**
   * Branch provides the THEN part of an IF form which creates the If class
   * with the appropriate branching components.
   *
   * @param num The line number of the IF form
   * @param fn  The boolean function determining where the branch goes
   *
   */
  case class Branch(num:Int, fn:Function0[Boolean]) {
    def THEN(loc:Int) = lines(num) = If(num, fn, loc)
  }

  def stringify(x:Any*):String = x.mkString("")

  /**
   * Appendr allows for the stringing together of expressions using the
   * `%` function.
   */
  case class Appendr(lhs:Any) {
    /**
     * <code>appendage</code> refers to the LHS value to be appended, <b>at
     * runtime</b>.  This is done, by setting it to a function which performs
     * lookups (for symbols) and toString conversion.
     *
     */
    var appendage = lhs match {
      case sym:Symbol => () => binds.any(sym).toString
      case fn:Function0[Any] => fn
      case _ => () => lhs.toString
    }

    def %(rhs:Any):Function0[String] = {
      /**
       * Check the type of the RHS.  For symbols, do a lookup, then
       * concatenate it to the result of the appendage function.
       */
      () => rhs match {
        case sym:Symbol => stringify(appendage(), binds.any(sym))
        case fn:Function0[Any] => stringify(appendage(), fn())
        case _ => stringify(appendage(), rhs)
      }
    }
  }

  /**
   * Math Functions
   */
  def SQRT(i:Int):Function0[Int] = () => Math.sqrt(i.intValue).intValue
  def SQRT(s:Symbol):Function0[Int] = () => Math.sqrt(binds.num(s)).intValue
  def ABS(i:Int):Function0[Int] = () => Math.abs(i)
  def ABS(s:Symbol):Function0[Int] = () => Math.abs(binds.num(s))

  def RUN() = gotoLine(lines.keys.toList.sortWith((l, r) => l < r).head)

  /**
   * LineBuilder is the jump off point for the line number syntax of
   * BASIC expressions.  Simply put, the `int2LineBuilder` implict
   * puts an instance of this class in place when it finds an Int
   * followed by one of the provided methods.  That is, this *only* handles
   * the forms that follow a line number and no other forms.  If you need to
   * add new functions that can be put elsewhere, then follow the form
   * used by SQRT and ABS.  For functions that can go *anywhere*, they should
   * be handled here and as separate functions as SQRT.
   */
  case class LineBuilder(num: Int) {
    def END() = lines(num) = End(num)

    object PRINT {
      def apply(str:String) = lines(num) = PrintString(num, str)
      def apply(number: BigInt) = lines(num) = PrintNumber(num, number)
      def apply(s: Symbol) = lines(num) = PrintVariable(num, s)
      def apply(fn:Function0[String]) = lines(num) = PrintResult(num, fn)
    }

    object INPUT {
      def apply(name: Symbol) = lines(num) = Input(num, name)
    }

    object LET {
      def apply(fn:Function0[Unit]) = lines(num) = Let(num, fn)
    }

    object GOTO {
      def apply(to: Int) = lines(num) = Goto(num, to)
    }

    object IF {
      def apply(fn:Function0[Boolean]) = Branch(num, fn)
    }
  }

  /**
   * This is the runtime evaluator of the built Scala classes from the
   * original BASIC forms.  Currently, lines can only be incremented by
   * 10, otherwise you program might not act the way you expect.
   */
  private def gotoLine(line: Int) {
    lines(line) match {
      case PrintNumber(_, number:BigInt) => {
        println(number)
        gotoLine(line + 10)
      }
      case PrintString(_, s:String) => {
        println(s)
        gotoLine(line + 10)
      }
      case PrintResult(_, fn:Function0[String]) => {
        println(fn())
        gotoLine(line + 10)
      }
      case PrintVariable(_, s:Symbol) => {
        println(binds.any(s))
        gotoLine(line + 10)
      }
      case Input(_, name) => {
        var entry = readLine

        // Temporary hack
        try {
          binds.set(name, java.lang.Integer.parseInt(entry))
        }
        catch {
          case _ => binds.set(name, entry)
        }

        gotoLine(line + 10)
      }
      case Let(_, fn:Function0[Unit]) => {
        fn()
        gotoLine(line + 10)
      }
      case If(_, fn:Function0[Boolean], thenJmp:Int) => {
        if(fn()) {
          gotoLine(thenJmp)
        }
        else {
          gotoLine(line + 10)
        }
      }
      case Goto(_, to) => gotoLine(to)
      case End(_) => {
        println()
        println("BREAK IN LINE " + line)
      }
    }
  }

  implicit def int2LineBuilder(i: Int) = LineBuilder(i)
  implicit def toAppendr(key:Any) = Appendr(key)
  implicit def symbol2Assignment(sym:Symbol) = Assignment(sym)
  implicit def symbol2BinaryRelation(sym:Symbol) = BinaryRelation(() => binds.num(sym))
  implicit def fnOfInt2BinaryRelation(fn:Function0[Int]) = BinaryRelation(fn)
  implicit def symbol2MathFunction(sym:Symbol) = MathFunction(() => binds.num(sym))
  implicit def fnOfInt2MathFunction(fn:Function0[Int]) = MathFunction(fn)

}
