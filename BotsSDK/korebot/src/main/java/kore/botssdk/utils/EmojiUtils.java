package kore.botssdk.utils;

import java.util.HashMap;
import java.util.Map;

public class EmojiUtils {
    private static final Map<String, String> emoticonToEmojiMap;

    static {
        emoticonToEmojiMap = new HashMap<>();
        emoticonToEmojiMap.put(":)", "\uD83D\uDE0A"); // 😊
        emoticonToEmojiMap.put(":(", "\uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put(":D", "\uD83D\uDE03"); // 😃
        emoticonToEmojiMap.put(";)", "\uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put(":P", "\uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":O", "\uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put(":*", "\uD83D\uDE17"); // 😗
        emoticonToEmojiMap.put(":/", "\uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put("B)", "\uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put(":')", "\uD83D\uDE22"); // 😢
        emoticonToEmojiMap.put("XD", "\uD83D\uDE06"); // 😆
        emoticonToEmojiMap.put(">:(", "\uD83D\uDE21"); // 😡
        emoticonToEmojiMap.put(":|", "\uD83D\uDE10"); // 😐
        emoticonToEmojiMap.put(":3", "\uD83D\uDE3A"); // 😺
        emoticonToEmojiMap.put("<3", "❤"); // ❤️
        emoticonToEmojiMap.put("</3", "\uD83D\uDC94"); // 💔
        emoticonToEmojiMap.put(":'-)", "\uD83D\uDE02"); // 😂
        emoticonToEmojiMap.put(":-D", "\uD83D\uDE03"); // 😃
        emoticonToEmojiMap.put("=D", "\uD83D\uDE03"); // 😃
        emoticonToEmojiMap.put(":-)", "\uD83D\uDE42"); // 😊
        emoticonToEmojiMap.put("=]", "\uD83D\uDE42"); // 😊
        emoticonToEmojiMap.put("=)", "\uD83D\uDE42"); // 😊
        emoticonToEmojiMap.put(":]", "\uD83D\uDE42"); // 😊
        emoticonToEmojiMap.put("':)", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put("':-)", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put("'=)", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put("':D", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put("':-D", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put("'=D", "\uD83D\uDE04"); // 😄
        emoticonToEmojiMap.put(">:)"," \uD83D\uDE06"); // 😆
        emoticonToEmojiMap.put(">;)"," \uD83D\uDE06"); // 😆
        emoticonToEmojiMap.put(">:-)", "\uD83D\uDE06"); // 😆
        emoticonToEmojiMap.put(">=)", "\uD83D\uDE06"); // 😆
        emoticonToEmojiMap.put(";-)"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put("*-)"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put("*)"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put(";]"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put(";D"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put(";^)"," \uD83D\uDE09"); // 😉
        emoticonToEmojiMap.put("':("," \uD83D\uDE13"); // 😓
        emoticonToEmojiMap.put("':-("," \uD83D\uDE13"); // 😓
        emoticonToEmojiMap.put("'=("," \uD83D\uDE13"); // 😓
        emoticonToEmojiMap.put(":-*"," \uD83D\uDE18"); // 😘
        emoticonToEmojiMap.put("=*"," \uD83D\uDE18"); // 😘
        emoticonToEmojiMap.put(":^*"," \uD83D\uDE18"); // 😘
        emoticonToEmojiMap.put(">:P"," \uD83D\uDE1C"); // 😜
        emoticonToEmojiMap.put("X-P"," \uD83D\uDE1C"); // 😜
        emoticonToEmojiMap.put("x-p"," \uD83D\uDE1C"); // 😜
        emoticonToEmojiMap.put(">:["," \uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put(":-("," \uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put(":-["," \uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put(":["," \uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put("=("," \uD83D\uDE1E"); // 😞
        emoticonToEmojiMap.put(">:-("," \uD83D\uDE20"); // 😠
        emoticonToEmojiMap.put(":@"," \uD83D\uDE21"); // 😡
        emoticonToEmojiMap.put(":'("," \uD83D\uDE22"); // 😢
        emoticonToEmojiMap.put(":'-("," \uD83D\uDE22"); // 😢
        emoticonToEmojiMap.put(";("," \uD83D\uDE22"); // 😢
        emoticonToEmojiMap.put(";-( "," \uD83D\uDE22"); // 😢
        emoticonToEmojiMap.put(">.<"," \uD83D\uDE23"); // 😣
        emoticonToEmojiMap.put("D:"," \uD83D\uDE28"); // 😨
        emoticonToEmojiMap.put(":$"," \uD83D\uDE33"); // 😳
        emoticonToEmojiMap.put("=$"," \uD83D\uDE33"); // 😳
        emoticonToEmojiMap.put("#-)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("#)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("%-)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("%)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("X)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("X-)"," \uD83D\uDE35"); // 😵
        emoticonToEmojiMap.put("*\\0/*"," \uD83D\uDE4C"); // 🙌
        emoticonToEmojiMap.put("\\0/"," \uD83D\uDE4C"); // 🙌
        emoticonToEmojiMap.put("*\\O/*"," \uD83D\uDE4C"); // 🙌
        emoticonToEmojiMap.put("\\O/"," \uD83D\uDE4C"); // 🙌
        emoticonToEmojiMap.put("O:-)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0:-3"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0:3"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0:-)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0:)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0;^)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("O:)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("O;-)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("O=)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("0;-)"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("O:-3"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("O:3"," \uD83D\uDE07"); // 😇
        emoticonToEmojiMap.put("B-)"," \uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put("8)"," \uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put("8-)"," \uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put("B-D"," \uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put("8-D"," \uD83D\uDE0E"); // 😎
        emoticonToEmojiMap.put("-_-"," \uD83D\uDE11"); // 😑
        emoticonToEmojiMap.put("-__-"," \uD83D\uDE11"); // 😑
        emoticonToEmojiMap.put("-___-"," \uD83D\uDE11"); // 😑
        emoticonToEmojiMap.put(">:\\"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(">:/"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(":-/"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(":-."," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(":\\"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put("=/"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put("=\\"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(":L"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put("=L"," \uD83D\uDE15"); // 😕
        emoticonToEmojiMap.put(":-P"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put("=P"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":-p"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":p"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put("=p"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":-?"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":?"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":-b"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":b"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put("d:"," \uD83D\uDE1B"); // 😛
        emoticonToEmojiMap.put(":-O"," \uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put(":-o"," \uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put(":o"," \uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put("O_O"," \uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put(">:O"," \uD83D\uDE2E"); // 😮
        emoticonToEmojiMap.put(":-X"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put(":X"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put(":-#"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put(":#"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put("=X"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put("=x"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put(":x"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put(":-x"," \uD83D\uDE36"); // 😶
        emoticonToEmojiMap.put("=#"," \uD83D\uDE36"); // 😶
        // Add more mappings as needed
    }

    public static String replaceEmoticonsWithEmojis(String text) {
        for (Map.Entry<String, String> entry : emoticonToEmojiMap.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }
}
