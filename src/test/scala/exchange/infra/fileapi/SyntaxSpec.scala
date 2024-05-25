package exchange.infra.fileapi

import exchange.domain.model.AssetAmounts._
import exchange.domain.model.AssetNames._
import exchange.domain.model.AssetPrices._
import exchange.domain.model.ClientNames._
import exchange.domain.model.UsdAmounts._
import exchange.domain.model._
import exchange.infra.fileapi._
import zio.prelude._
import zio.test._

object SyntaxSpec extends ZIOSpecDefault {

  def spec: Spec[Any, Nothing] = suite("Syntax")(
    test("ClientBalanceRecord") {
      val input = "C1	1000	10	5	15	0"

      val expectedOutput = for {
        usdBalance <- UsdAmount(1000)
        balanceA   <- AssetAmount(10)
        balanceB   <- AssetAmount(5)
        balanceC   <- AssetAmount(15)
        balanceD   <- AssetAmount(0)
      } yield ClientBalanceRecord(ClientName("C1"), usdBalance, balanceA, balanceB, balanceC, balanceD)

      val parsingResult        = clientBalanceRecordSyntax.parseString(input).toOption
      val parsedAndPrintedBack = parsingResult.flatMap(x => clientBalanceRecordSyntax.printString(x).toOption)

      assertTrue(parsingResult === expectedOutput)
      && assertTrue(parsedAndPrintedBack === Some(input))
    },
    test("Buy Order") {
      val input = "C1	b	A	10	12"

      val expectedOutput = for {
        assetAmount <- AssetAmount(10)
        assetPrice  <- AssetPrice(12)
      } yield Order(ClientName("C1"), OrderSide.Buy, AssetName("A"), assetAmount, assetPrice)

      val parsingResult = orderSyntax.parseString(input).toOption

      assertTrue(parsingResult === expectedOutput)
    },
    test("Sell Order") {
      val input = "C2	s	A	8	10"

      val expectedOutput = for {
        assetAmount <- AssetAmount(8)
        assetPrice  <- AssetPrice(10)
      } yield Order(ClientName("C2"), OrderSide.Sell, AssetName("A"), assetAmount, assetPrice)

      val parsingResult = orderSyntax.parseString(input).toOption

      assertTrue(parsingResult === expectedOutput)
    }
  )

}