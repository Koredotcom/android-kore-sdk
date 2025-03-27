package com.kore.ui.utils

object EmojiUtils {
    private val emoticonToEmojiMap: MutableMap<String, String> = HashMap()

    init {
        emoticonToEmojiMap[":)"] = "\uD83D\uDE0A" // 😊
        emoticonToEmojiMap[":("] = "\uD83D\uDE1E" // 😞
        emoticonToEmojiMap[":D"] = "\uD83D\uDE03" // 😃
        emoticonToEmojiMap[";)"] = "\uD83D\uDE09" // 😉
        emoticonToEmojiMap[":P"] = "\uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":O"] = "\uD83D\uDE2E" // 😮
        emoticonToEmojiMap[":*"] = "\uD83D\uDE17" // 😗
        emoticonToEmojiMap[":/"] = "\uD83D\uDE15" // 😕
        emoticonToEmojiMap["B)"] = "\uD83D\uDE0E" // 😎
        emoticonToEmojiMap[":')"] = "\uD83D\uDE22" // 😢
        emoticonToEmojiMap["XD"] = "\uD83D\uDE06" // 😆
        emoticonToEmojiMap[">:("] = "\uD83D\uDE21" // 😡
        emoticonToEmojiMap[":|"] = "\uD83D\uDE10" // 😐
        emoticonToEmojiMap[":3"] = "\uD83D\uDE3A" // 😺
        emoticonToEmojiMap["<3"] = "❤" // ❤️
        emoticonToEmojiMap["</3"] = "\uD83D\uDC94" // 💔
        emoticonToEmojiMap[":'-)"] = "\uD83D\uDE02" // 😂
        emoticonToEmojiMap[":-D"] = "\uD83D\uDE03" // 😃
        emoticonToEmojiMap["=D"] = "\uD83D\uDE03" // 😃
        emoticonToEmojiMap[":-)"] = "\uD83D\uDE42" // 😊
        emoticonToEmojiMap["=]"] = "\uD83D\uDE42" // 😊
        emoticonToEmojiMap["=)"] = "\uD83D\uDE42" // 😊
        emoticonToEmojiMap[":]"] = "\uD83D\uDE42" // 😊
        emoticonToEmojiMap["':)"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap["':-)"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap["'=)"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap["':D"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap["':-D"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap["'=D"] = "\uD83D\uDE04" // 😄
        emoticonToEmojiMap[">:)"] = " \uD83D\uDE06" // 😆
        emoticonToEmojiMap[">;)"] = " \uD83D\uDE06" // 😆
        emoticonToEmojiMap[">:-)"] = "\uD83D\uDE06" // 😆
        emoticonToEmojiMap[">=)"] = "\uD83D\uDE06" // 😆
        emoticonToEmojiMap[";-)"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap["*-)"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap["*)"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap[";]"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap[";D"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap[";^)"] = " \uD83D\uDE09" // 😉
        emoticonToEmojiMap["':("] = " \uD83D\uDE13" // 😓
        emoticonToEmojiMap["':-("] = " \uD83D\uDE13" // 😓
        emoticonToEmojiMap["'=("] = " \uD83D\uDE13" // 😓
        emoticonToEmojiMap[":-*"] = " \uD83D\uDE18" // 😘
        emoticonToEmojiMap["=*"] = " \uD83D\uDE18" // 😘
        emoticonToEmojiMap[":^*"] = " \uD83D\uDE18" // 😘
        emoticonToEmojiMap[">:P"] = " \uD83D\uDE1C" // 😜
        emoticonToEmojiMap["X-P"] = " \uD83D\uDE1C" // 😜
        emoticonToEmojiMap["x-p"] = " \uD83D\uDE1C" // 😜
        emoticonToEmojiMap[">:["] = " \uD83D\uDE1E" // 😞
        emoticonToEmojiMap[":-("] = " \uD83D\uDE1E" // 😞
        emoticonToEmojiMap[":-["] = " \uD83D\uDE1E" // 😞
        emoticonToEmojiMap[":["] = " \uD83D\uDE1E" // 😞
        emoticonToEmojiMap["=("] = " \uD83D\uDE1E" // 😞
        emoticonToEmojiMap[">:-("] = " \uD83D\uDE20" // 😠
        emoticonToEmojiMap[":@"] = " \uD83D\uDE21" // 😡
        emoticonToEmojiMap[":'("] = " \uD83D\uDE22" // 😢
        emoticonToEmojiMap[":'-("] = " \uD83D\uDE22" // 😢
        emoticonToEmojiMap[";("] = " \uD83D\uDE22" // 😢
        emoticonToEmojiMap[";-( "] = " \uD83D\uDE22" // 😢
        emoticonToEmojiMap[">.<"] = " \uD83D\uDE23" // 😣
        emoticonToEmojiMap["D:"] = " \uD83D\uDE28" // 😨
        emoticonToEmojiMap[":$"] = " \uD83D\uDE33" // 😳
        emoticonToEmojiMap["=$"] = " \uD83D\uDE33" // 😳
        emoticonToEmojiMap["#-)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["#)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["%-)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["%)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["X)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["X-)"] = " \uD83D\uDE35" // 😵
        emoticonToEmojiMap["*\\0/*"] = " \uD83D\uDE4C" // 🙌
        emoticonToEmojiMap["\\0/"] = " \uD83D\uDE4C" // 🙌
        emoticonToEmojiMap["*\\O/*"] = " \uD83D\uDE4C" // 🙌
        emoticonToEmojiMap["\\O/"] = " \uD83D\uDE4C" // 🙌
        emoticonToEmojiMap["O:-)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0:-3"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0:3"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0:-)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0:)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0;^)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["O:)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["O;-)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["O=)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["0;-)"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["O:-3"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["O:3"] = " \uD83D\uDE07" // 😇
        emoticonToEmojiMap["B-)"] = " \uD83D\uDE0E" // 😎
        emoticonToEmojiMap["8)"] = " \uD83D\uDE0E" // 😎
        emoticonToEmojiMap["8-)"] = " \uD83D\uDE0E" // 😎
        emoticonToEmojiMap["B-D"] = " \uD83D\uDE0E" // 😎
        emoticonToEmojiMap["8-D"] = " \uD83D\uDE0E" // 😎
        emoticonToEmojiMap["-_-"] = " \uD83D\uDE11" // 😑
        emoticonToEmojiMap["-__-"] = " \uD83D\uDE11" // 😑
        emoticonToEmojiMap["-___-"] = " \uD83D\uDE11" // 😑
        emoticonToEmojiMap[">:\\"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[">:/"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[":-/"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[":-."] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[":\\"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap["=/"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap["=\\"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[":L"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap["=L"] = " \uD83D\uDE15" // 😕
        emoticonToEmojiMap[":-P"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap["=P"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":-p"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":p"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap["=p"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":-?"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":?"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":-b"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":b"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap["d:"] = " \uD83D\uDE1B" // 😛
        emoticonToEmojiMap[":-O"] = " \uD83D\uDE2E" // 😮
        emoticonToEmojiMap[":-o"] = " \uD83D\uDE2E" // 😮
        emoticonToEmojiMap[":o"] = " \uD83D\uDE2E" // 😮
        emoticonToEmojiMap["O_O"] = " \uD83D\uDE2E" // 😮
        emoticonToEmojiMap[">:O"] = " \uD83D\uDE2E" // 😮
        emoticonToEmojiMap[":-X"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap[":X"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap[":-#"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap[":#"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap["=X"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap["=x"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap[":x"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap[":-x"] = " \uD83D\uDE36" // 😶
        emoticonToEmojiMap["=#"] = " \uD83D\uDE36" // 😶
        // Add more mappings as needed
    }

    fun replaceEmoticonsWithEmojis(text: String): String {
        val urlPattern = Regex("(https?://\\S+)")
        val urls = mutableListOf<String>()

        // Temporarily replace URLs with placeholders
        var tempText = text.replace(urlPattern) {
            urls.add(it.value)
            "##URL##${urls.size - 1}" // Unique placeholder for each URL
        }

        // Replace emojis only in non-URL text
        emoticonToEmojiMap.forEach { (shortcut, emoji) ->
            tempText = tempText.replace(shortcut, emoji)
        }

        // Restore URLs from placeholders
        urls.forEachIndexed { index, url ->
            tempText = tempText.replace("##URL##$index", url)
        }

        return tempText
    }
}
