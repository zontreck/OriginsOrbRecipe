package dev.zontreck.otemod.implementation;

public class Messages {
    public static final String OTE_PREFIX;
    public static final String PREFIX_UPDATED;
    public static final String PREFIX_COLOR_UPDATED;
    public static final String NICK_UPDATED;
    public static final String NAME_COLOR_UPDATED;
    public static final String CHAT_COLOR_UPDATED;


    static{
        OTE_PREFIX = "!Dark_Gray![!Dark_Purple!OTE!Dark_Gray!] ";

        PREFIX_UPDATED = OTE_PREFIX + " !Dark_Purple!Your prefix has been updated";
        PREFIX_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple! Your prefix color has been updated";
        NICK_UPDATED = OTE_PREFIX + "!Dark_Purple! Your nickname has been updated";
        NAME_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple!  Your name color has been updated";
        CHAT_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple! Your chat color has been updated";
    }
}
