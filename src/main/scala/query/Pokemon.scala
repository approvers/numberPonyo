package query

import types.Pokemon
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.util.Validated._
import scala.util.Try

object GetPokemon {
  def apply(number: Int): Try[Pokemon] = {
    Try {
      val browser = JsoupBrowser()
      val link = s"https://zukan.pokemon.co.jp/detail/${number}"
      val doc =
        browser.get(s"https://yakkun.com/swsh/zukan/n${number}")
      val name = (doc >> element("th")).text
      val doc2 = browser.get(s"https://wiki.xn--rckteqa2e.com/wiki/${name}")
      val explanation =
        (doc2 >> elementList("dd"))
          .filter(x => x.text.contains("(漢字)"))
          .apply(0)
          .text
          .drop(5)
      val types =
        (doc >> elementList("tbody > .center > td > .type > li > a > img"))
          .map(x => x.attr("alt"))
      val characteristic =
        (doc >> elementList(".center > tbody > tr > .c1 > a"))
          .map(x => x.text)
      val img =
        s"https://78npc3br.user.webaccel.jp/poke/icon96/n${number.toString}.gif"

      Pokemon(name, link, types, explanation, characteristic, img)
    }
  }
}
