package dev.zontreck.otemod.util;

public class ExperienceUtils
{

    public static int getXpNeededForNextLevel(int xpLvl) {
        if (xpLvl >= 30) {
            return 112 + (xpLvl - 30) * 9;
        } else {
            return xpLvl >= 15 ? 37 + (xpLvl - 15) * 5 : 7 + xpLvl * 2;
        }
    }
}
