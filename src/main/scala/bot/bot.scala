package bot

import com.typesafe.config.ConfigFactory
import org.javacord.api.DiscordApi
import org.javacord.api.DiscordApiBuilder
import query.GetPokemon
import scala.util.Failure
import scala.util.Success
import query.GetScp
import types.Pokemon
import types.Scp
import scala.util.Random
import scala.util.Try
import org.javacord.api.entity.message.embed.EmbedBuilder
import java.awt.Color
import org.javacord.api.entity.channel.TextChannel

object Bot {
  val TOKEN = ConfigFactory.load().getString("TOKEN")

  val client: DiscordApi =
    new DiscordApiBuilder().setToken(TOKEN).login.join

  def apply(): Unit = {
    client.addMessageCreateListener(event => {
      if (!event.getMessageAuthor.isBotUser) {
        val number =
          "(?<!\\d)\\d{1,4}(?!\\d)".r.findFirstIn(event.getMessageContent)
        number match {
          case Some(value) => sendNumbers(value.toInt, event.getChannel)
          case None        => {}
        }
      }
    })
  }

  def sendNumbers(
      number: Int,
      channel: TextChannel
    ): Unit = {
    val pokemon: Option[EmbedBuilder] = generatePokemonEmbed(number)
    val scp: Option[EmbedBuilder] = generateScpEmbed(number)
    val sendMessage =
      Random.shuffle(
          List(scp, pokemon).filter(x => x != None).map(x => x.get)
      )
    if (!sendMessage.isEmpty) {
      channel.sendMessage(sendMessage.apply(0))
    }
  }

  def generatePokemonEmbed(number: Int): Option[EmbedBuilder] = {
    GetPokemon(number) match {
      case Success(value) =>
        Option(
            new EmbedBuilder()
              .setAuthor(
                  s"No.${number} ${value.calling}",
                  value.link,
                  "https://deliver.commons.nicovideo.jp/thumbnail/nc139223?size=l"
              )
              .setTitle(value.name)
              .setDescription(value.explanation)
              .setUrl(value.link)
              .setColor(Color.PINK)
              .setThumbnail(value.img)
              .addField("タイプ", value.types.mkString("\n"))
              .addField("特性", value.characteristic.mkString("\n"))
        )
      case Failure(_) => None
    }
  }

  def generateScpEmbed(number: Int): Option[EmbedBuilder] = {
    GetScp(number) match {
      case Success(value) =>
        Option(
            new EmbedBuilder()
              .setAuthor(
                  s"SCP-${"%03d".format(number)}",
                  value.link,
                  "https://upload.wikimedia.org/wikipedia/en/thumb/0/0a/Logo_of_the_SCP_Foundation.png/600px-Logo_of_the_SCP_Foundation.png"
              )
              .setTitle(value.metaTitle)
              .setDescription(value.explanation)
              .setUrl(value.link)
              .setColor(Color.PINK)
              .setThumbnail(
                  "https://upload.wikimedia.org/wikipedia/en/thumb/0/0a/Logo_of_the_SCP_Foundation.png/600px-Logo_of_the_SCP_Foundation.png"
              )
              .addField("Rate", value.rate)
              .addField("オブジェクトクラス", value.objectClass)
        )
      case Failure(_) => None
    }
  }
}
