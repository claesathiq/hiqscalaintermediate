package com.hiq.scala.intermediate

/**
 * DSL example: a very simple Finance DSL
 */
object FinanceDSL {

  abstract class Instrument(name: String) { def stype: String }
  case class Stock(name: String) extends Instrument(name) {
    override val stype = "equity"
  }
  case class Bond(name: String) extends Instrument(name) {
    override val stype = "bond"
  }

  abstract class TransactionType { def value: String }
  case class buyT() extends TransactionType {
    override val value = "bought"
  }
  case class sellT() extends TransactionType {
    override val value = "sold"
  }

  class TradeInt(qty: Int) {
    def sharesOf(name: String) = {
      (qty, Stock(name))
    }

    def bondsOf(name: String) = {
      (qty, Bond(name))
    }
  }

  implicit def tradeInt(i: Int) = new TradeInt(i)

  class Order {
    var price = 0
    var ins: Instrument = null
    var qty = 0
    var totalValue = 0
    var trn: TransactionType = null
    var account: String = null

    def to(i: (Instrument, Int, TransactionType)) = {
      ins = i._1
      qty = i._2
      trn = i._3
      this
    }
    def maxUnitPrice(p: Int) = { price = p; this }

    def using(pricing: (Int, Int) => Int) = {
      totalValue = pricing(qty, price)
      this
    }

    def forAccount(a: String)(implicit pricing: (Int, Int) => Int) = {
      account = a
      totalValue = pricing(qty, price)
      this
    }
  }

  def buy(qi: (Int, Instrument)) = (qi._2, qi._1, buyT())
  def sell(qi: (Int, Instrument)) = (qi._2, qi._1, sellT())
}