package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.FileOutputStream
import java.net.URL
import javax.imageio.ImageIO

class GayPFP : Command(
    arrayOf("gaypfp"),
    CommandInfo("GayPFP", "gaypfp <@autoDeleteUser>", "Overlays a random lgbtq flag over someones pfp.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        try {
            val pfp = withContext(Dispatchers.IO) {
                ImageIO.read(URL(event.message.mentions.values.first().avatarUrl()))
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
            val out = withContext(Dispatchers.IO) {
                FileOutputStream("final.png")
            }
            withContext(Dispatchers.IO) {
                ImageIO.write(scaledpfp, "png", out)
                out.close()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}
