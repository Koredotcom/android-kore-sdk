package kore.botssdk.extensions

fun String.formatEmojis(): String {
    return this.replace(":)", "\uD83D\uDE42")
        .replace(":thumbsup", "\uD83D\uDC4D")
        .replace(":(", "☹️")
}