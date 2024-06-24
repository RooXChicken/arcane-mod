package com.rooxchicken.data;

import com.rooxchicken.ArcaneMana;
import com.rooxchicken.client.ArcaneManaClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class HandleData
{
    public static String manaUse;
    public static String skillName = "";
    public static String skillNameFile;
    public static String cooldown;
    public static String nameColor;
    public static String manaUseColor;
    public static String cooldownColor;
    public static int blood;
    public static int bloodUsage;
    public static boolean bloodEnabled = false;
    public static int hasCrystal = 0;
    public static String crystalName = "empty";
    public static boolean upgraded = false;

    public static int displaySkill = 0;

    public static void parseData(String msg)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        String[] data = msg.split("_");
        int mode = Integer.parseInt(data[1]);

        switch(mode)
        {
            case 0: //veriy mod
                ArcaneManaClient.sendChatCommand("hdn_verifymod");
            break;
            case 1: //wand data
                manaUse = data[2];
                if(!skillName.equals(data[3]))
                    client.world.playSound(client.player, client.player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.6f, 1f);
                skillName = data[3];
                skillNameFile = skillName.toLowerCase().replace("'", "").replace(" ", "_");
                //ArcaneMana.LOGGER.info(skillNameFile);
                cooldown = data[4];
                nameColor = data[5].replace("-", "_");
                manaUseColor = data[6].replace("-", "_");
                cooldownColor = data[7].replace("-", "_");

                bloodEnabled = Boolean.parseBoolean(data[9]);

                if(bloodEnabled)
                {
                    if(!data[8].equals(" ") && !data[8].equals(""))
                        bloodUsage = Integer.parseInt(data[8].substring(1).trim());
                    else
                        bloodUsage = -2;
                }
                else
                    bloodUsage = -1;

                displaySkill = 2;
            break;
            case 2: //mana data
                //ArcaneMana.LOGGER.info("Received: " + msg);
                ArcaneManaClient.mana = Integer.parseInt(data[2]);
                ArcaneManaClient.maxMana = Integer.parseInt(data[3]);
                if(!data[4].equals(" ") && !data[4].equals(""))
                {
                    blood = Integer.parseInt(data[4].trim());
                }
            break;
            case 3:
                displaySkill--;
                hasCrystal--;
                if(displaySkill <= 0)
                    bloodEnabled = false;

            break;

            case 4:
                hasCrystal = 2;
                crystalName = data[2].toLowerCase().replace(" ", "_");
                upgraded = Boolean.parseBoolean(data[3]);
            break;
        }
    }
}
