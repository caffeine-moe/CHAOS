package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

class Pride : Command(
    arrayOf("pride"),
    CommandInfo("Pride", "pride <@User>", "Overlays the lgbtq flag over someones pfp.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        try {
            val user = event.message.mentions.values.firstOrNull() ?: return
            val pfp = withContext(Dispatchers.IO) {
                ImageIO.read(URL(user.avatarUrl()))
                    .getScaledInstance(512, 512, BufferedImage.SCALE_SMOOTH)
            }
            val gayflag = withContext(Dispatchers.IO) {
                ImageIO.read(URL("https://upload.wikimedia.org/wikipedia/commons/8/85/Pride_Flag.png"))
                    .getScaledInstance(512, 512, BufferedImage.TYPE_INT_ARGB)
            }

            val scaledpfp = BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB)
            scaledpfp.createGraphics().drawImage(pfp, 0, 0, null)
            val g = scaledpfp.createGraphics()
            g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f)
            g.drawImage(gayflag, 0, 0, null)
            g.dispose()
            val baos = ByteArrayOutputStream()
            ImageIO.write(scaledpfp, "png", baos)
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("`${user.username}` is now gay")
                    .addAttachment(baos.toByteArray(), "gaypfp.png")
            )
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}