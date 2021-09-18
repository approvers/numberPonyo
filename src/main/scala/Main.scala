import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.util.Validated._
import query.GetScp
import scala.util.Failure
import scala.util.Success
import query.GetPokemon
import bot.Bot

object Main extends App {
  Bot()
  for (i <- 100 until 500) {
    println(i)
    GetScp(i) match {
      case Failure(exception) => println(exception.getMessage)
      case Success(scp)       => println(scp)
    }
    GetPokemon(i) match {
      case Failure(exception) => println(exception.getMessage)
      case Success(pokemon)   => println(pokemon)
    }
  }
}
