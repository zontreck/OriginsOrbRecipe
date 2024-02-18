package dev.zontreck.otemod.implementation;

public class Messages {
    public static final String OTE_PREFIX;
    public static final String PREFIX_UPDATED;
    public static final String PREFIX_COLOR_UPDATED;
    public static final String NICK_UPDATED;
    public static final String NAME_COLOR_UPDATED;
    public static final String CHAT_COLOR_UPDATED;
    public static final String CONSOLE_ERROR;
    public static final String STARTER_FAILURE_PERMISSIONS;
    public static final String STARTER_KIT_GIVEN;

    public static final String FLIGHT_GIVEN;
    public static final String FLIGHT_REMOVED;


    static{
        OTE_PREFIX = "!Dark_Gray![!Dark_Purple!Thresholds!Dark_Gray!] ";

        PREFIX_UPDATED = OTE_PREFIX + " !Dark_Purple!Your prefix has been updated";
        PREFIX_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple!Your prefix color has been updated";
        NICK_UPDATED = OTE_PREFIX + "!Dark_Purple! Your nickname has been updated";
        NAME_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple!Your name color has been updated";
        CHAT_COLOR_UPDATED = OTE_PREFIX + "!Dark_Purple!Your chat color has been updated";
        CONSOLE_ERROR = OTE_PREFIX + "!Dark_Red!This command can only be executed from within the game, and by a player";
        STARTER_FAILURE_PERMISSIONS = OTE_PREFIX + "!Dark_Red!This command can only be executed by server operators";
        STARTER_KIT_GIVEN = OTE_PREFIX + "!Dark_Purple!You have been given a starter kit. Welcome to the server.";
        FLIGHT_GIVEN = OTE_PREFIX + "!Dark_Green!You start to feel lighter than a feather";
        FLIGHT_REMOVED = OTE_PREFIX + "!Dark_Red!You have a sinking feeling you are no longer lighter than a feather.";
    }
}
