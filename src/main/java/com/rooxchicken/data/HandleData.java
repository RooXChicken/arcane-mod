package com.rooxchicken.data;

import com.rooxchicken.ArcaneMana;
import com.rooxchicken.client.ArcaneManaClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class HandleData
{
    public static String manaUse;
    public static String skillName;
    public static String cooldown;

    public static int displaySkill = 0;

    public static void parseData(String msg)
    {
        String[] data = msg.split("_");
        int mode = Integer.parseInt(data[1]);

        switch(mode)
        {
            case 0: //veriy mod
                ArcaneManaClient.sendChatCommand("hdn_verifymod");
            break;
            case 1: //wand data
                manaUse = data[2];
                skillName = data[3];
                cooldown = data[4];
                displaySkill = 2;
            break;
            case 2: //mana data
                //ArcaneMana.LOGGER.info("Received: " + msg);
                ArcaneManaClient.mana = Integer.parseInt(data[2]);
                ArcaneManaClient.maxMana = Integer.parseInt(data[3]);
            break;
            case 3:
                displaySkill--;
            break;
        }
    }
}
