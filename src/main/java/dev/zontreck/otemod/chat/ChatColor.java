package dev.zontreck.otemod.chat;

public class ChatColor {
    public enum ColorOptions{
        Black,
        Dark_Blue,
        Dark_Green,
        Dark_Aqua,
        Dark_Red,
        Dark_Purple,
        Gold,
        Gray,
        Dark_Gray,
        Blue,
        Green,
        Aqua,
        Red,
        Light_Purple,
        Yellow,
        White,
        MinecoinGold,
        Underline,
        Bold,
        Italic,
        Strikethrough,
        Crazy
    }
    public static char CODE = '§';
    public static String BLACK = build("0");
    public static String DARK_BLUE = build("1");
    public static String DARK_GREEN = build("2");
    public static String DARK_AQUA = build("3");
    public static String DARK_RED = build("4");
    public static String DARK_PURPLE = build("5");
    public static String GOLD = build("6");
    public static String GRAY = build("7");
    public static String DARK_GRAY = build("8");
    public static String BLUE = build("9");
    public static String GREEN = build("a");
    public static String AQUA = build("b");
    public static String RED = build("c");
    public static String LIGHT_PURPLE = build("d");
    public static String YELLOW = build("e");
    public static String WHITE = build("f");
    public static String MINECOIN_GOLD = build("g");

    public static String UNDERLINE = build("u");
    public static String BOLD = build("l");
    public static String ITALIC = build("o");
    public static String STRIKETHROUGH = build("m");
    public static String CRAZY = build("k");
    public static String RESET = build("r");


    public static String build(String c)
    {
        return CODE+c;
    }

    public static String resetChat()
    {
        return RESET+WHITE;
    }

    public static String from(String nick){
        switch(nick.toLowerCase()){
            case "black":
            {
                return BLACK;
            }
            case "crazy":
            {
                return CRAZY;
            }
            default:
            {
                return RESET+CRAZY;
            }
        }
    }
}
