package query

import types.Scp
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.util.Validated._
import scala.util.Try

object GetScp {

  def apply(number: Int): Try[Scp] = {
    Try {
      val browser = JsoupBrowser()
      val link =
        s"http://scp-jp.wikidot.com/scp-${number.toString}"
      val doc = browser.get(link)
      val doc2 =
        browser.get(
            s"http://scp-jp.wikidot.com/scp-series${if (number >= 1000) {
              "-" + (number / 1000 + 1).toString
            }}"
        )
      val title = (doc >> text("#page-title"))
      val exception = ((doc >> elementList("#page-content > p"))
        .filter(x => x.text.contains("説明"))
        .apply(0)
        .text
        .drop(4)
        .take(100)) + "..."
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

      Scp(title, exception, link, metaTitle, objectClass, rate)
    }
  }
}
