import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.util.Validated._
import query.getScp
import scala.util.Failure
import scala.util.Success

object Main extends App {
  for (i <- 2100 until 2500) {
    getScp(i) match {
      case Failure(exception) => println(exception.getMessage)
      case Success(scp)       => println(scp)
    }
  }
}
