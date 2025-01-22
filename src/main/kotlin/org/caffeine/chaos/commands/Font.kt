package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen

class Font : Command(
    arrayOf("font", "style"),
    CommandInfo(
        "Funny Font",
        "ambypass <Message> [Type]",
        """
            Automatically bypasses Auto Mod in servers by changing letters in words.
            The bypass types are :
            - Unicode (Default) : Replaces a each letter in each word with a slightly different unicode equivalent.
            - Regional : Uses 🇹🇭🇪🇸🇪 letters (emojis).
            """
    )
) {
    val unicode: HashMap<String, String> = hashMapOf(
        Pair("a", "A"),
        Pair("b", "ɓ"),
        Pair("c", "ㄈ"),
        Pair("d", "ȡ"),
        Pair("e", "ȇ"),
        Pair("f", "ӻ"),
        Pair("g", "ɠ"),
        Pair("h", "ȟ"),
        Pair("i", "׀"),
        Pair("j", "ǰ"),
        Pair("k", "ҟ"),
        Pair("l", "ȴ"),
        Pair("m", "ɱ"),
        Pair("n", "冂"),
        Pair("o", "ό"),
        Pair("p", "ᵽ"),
        Pair("q", "ԛ"),
        Pair("r", "ŗ"),
        Pair("s", "ȿ"),
        Pair("t", "丅"),
        Pair("u", "ȕ"),
        Pair("v", "ѵ"),
        Pair("w", "ώ"),
        Pair("x", "ẍ"),
        Pair("y", "ƴ"),
        Pair("z", "ȥ"),
    )

    val regional: HashMap<String, String> = hashMapOf(
        Pair("a", "\uD83C\uDDE6 "),
        Pair("b", "\uD83C\uDDE7 "),
        Pair("c", "\uD83C\uDDE8 "),
        Pair("d", "\uD83C\uDDE9 "),
        Pair("e", "\uD83C\uDDEA "),
        Pair("f", "\uD83C\uDDEB "),
        Pair("g", "\uD83C\uDDEC "),
        Pair("h", "\uD83C\uDDED "),
        Pair("i", "\uD83C\uDDEE "),
        Pair("j", "\uD83C\uDDEF "),
        Pair("k", "\uD83C\uDDF0 "),
        Pair("l", "\uD83C\uDDF1 "),
        Pair("m", "\uD83C\uDDF2 "),
        Pair("n", "\uD83C\uDDF3 "),
        Pair("o", "\uD83C\uDDF4 "),
        Pair("p", "\uD83C\uDDF5 "),
        Pair("q", "\uD83C\uDDF6 "),
        Pair("r", "\uD83C\uDDF7 "),
        Pair("s", "\uD83C\uDDF8 "),
        Pair("t", "\uD83C\uDDF9 "),
        Pair("u", "\uD83C\uDDFA "),
        Pair("v", "\uD83C\uDDFB "),
        Pair("w", "\uD83C\uDDFC "),
        Pair("x", "\uD83C\uDDFD "),
        Pair("y", "\uD83C\uDDFE "),
        Pair("z", "\uD83C\uDDFF "),
    )

    override suspend fun onCalled(
        client: Client,
        event: ClientEvent.MessageCreate,
        args: List<String>,
        cmd: String,
    ) {
        val error = if (args.isEmpty()) {
            "Not enough args."
        } else ""
        if (error.isNotBlank()) {
            event.message.channel.sendMessage(
                error(
                    client,
                    event,
                    error,
                    commandInfo
                )
            ).awaitThen {
                onComplete(it, false)
            }
            return
        }
        val text: List<String>
        val type: HashMap<String, String>
        if (args.last().lowercase().matches(Regex("regional"))) {
            text = args.dropLast(1); type = regional
        } else {
            text = args; type = unicode
        }
        val stylised = text.joinToString(" ").map { letter -> type[letter.lowercase()] ?: letter }
        event.message.channel.sendMessage(stylised.joinToString("")).awaitThen { message -> onComplete(message, true) }
    }
}