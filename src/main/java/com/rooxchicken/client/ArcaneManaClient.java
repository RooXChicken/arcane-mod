package com.rooxchicken.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.ArcaneMana;
import com.rooxchicken.data.HandleData;
import com.rooxchicken.event.DrawGUICallback;
import com.rooxchicken.screen.AbilityElement;
import com.rooxchicken.screen.ConfigScreen;

public class ArcaneManaClient implements ClientModInitializer
{
	public static int maxMana = -1;
	public static int mana = -1;

	public static AbilityElement manaBar;
	public static AbilityElement bloodBar;

	public static boolean mainRender = false;

	private KeyBinding configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.arcane.config", GLFW.GLFW_KEY_C, "key.category.arcane"));

	@Override
	public void onInitializeClient()
	{
		manaBar = new AbilityElement(0);
		bloodBar = new AbilityElement(1);
		HudRenderCallback.EVENT.register(new DrawGUICallback());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
		{
			ArcaneManaClient.maxMana = -1;
			mainRender = false;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{	
			if(mana == -1)
            	return;

			if(configKey.wasPressed())
			{
				client.setScreen(new ConfigScreen(Text.of("Config Screen")));
			}
		});

		load();
	}

	public static void sendChatCommand(String msg)
	{
		if(!msg.equals("hdn_verifymod") && mainRender)
			return;
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
		if(handler == null)
			return;
    	handler.sendChatCommand(msg);
	}

	public static void load()
	{
		File file = new File("arcane-mana.cfg");
		if(!file.exists())
		{
			save();
			return;
		}
		try
		{
			Scanner scan = new Scanner(file);
			manaBar.load(scan);
			bloodBar.load(scan);
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			ArcaneMana.LOGGER.error("Failed to open config file.", e);
		}
	}

	public static void save()
	{
		File file = new File("arcane-mana.cfg");
		try
		{
			FileWriter write = new FileWriter(file);
			write.write(manaBar.save() + bloodBar.save());

			write.close();

		}
		catch (IOException e)
		{
			ArcaneMana.LOGGER.error("Failed to save config file.", e);
		}
	}
}