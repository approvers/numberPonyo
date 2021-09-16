package query

import types.scp
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.util.Validated._
import scala.util.Try

object getScp {

  def apply(number: Int): Try[scp] = {

    Try {
      val browser = JsoupBrowser()
      val doc = browser.get(s"http://scp-jp.wikidot.com/scp-${number.toString}")
      val doc2 =
        browser.get(
          s"http://scp-jp.wikidot.com/scp-series-${(number / 1000 + 1).toString}"
        )

      val title = (doc >> text("#page-title"))
      val metaTitle =
        (doc2 >> elementList("li"))
          .filter(x => x.text.contains(title))
          .apply(0)
          .text
          .split("-")
          .apply(2)
          .trim()
      val objectClass =
        (doc >> elementList("p"))
          .filter(x => x.text.contains("オブジェクトクラス"))
          .apply(0)
          .text
          .split(" ")
          .apply(1)
      val rate = (doc >?> text(".rate-points")).get

      scp(title, metaTitle, objectClass, rate)
    }

  }
}
