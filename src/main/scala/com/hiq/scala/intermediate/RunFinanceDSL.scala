package com.hiq.scala.intermediate

import FinanceDSL._

/**
 * Run the Finance DSL
 */
object RunFinanceDSL extends App {

  def premiumPricing(qty: Int, price: Int) = qty match {
    case q if q > 100 => q * price - 100
    case _ => qty * price
  }

  def defaultPricing(qty: Int, price: Int): Int = qty * price

  // Exercise the DSL!
  val orders = List[Order] (

    new Order to buy(100 sharesOf "IBM")
      maxUnitPrice 300
      using premiumPricing,

    new Order to buy(200 sharesOf "CISCO")
      maxUnitPrice 300
      using premiumPricing,

    new Order to buy(200 sharesOf "GOOGLE")
      maxUnitPrice 300
      using defaultPricing,

    new Order to sell(200 bondsOf "Sun")
      maxUnitPrice 300
      using {
      (qty, unit) => qty * unit - 500
    }
  )
  println((0 /: orders)(_ + _.totalValue))

}
