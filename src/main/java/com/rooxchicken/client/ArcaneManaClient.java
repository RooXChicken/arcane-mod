package com.rooxchicken.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.ArcaneMana;
import com.rooxchicken.event.DrawGUICallback;

public class ArcaneManaClient implements ClientModInitializer
{
	public static int maxMana = -1;
	public static int mana = -1;

	@Override
	public void onInitializeClient()
	{
		HudRenderCallback.EVENT.register(new DrawGUICallback());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
		{
			ArcaneManaClient.maxMana = -1;
		});

		//load();
	}

	public static void sendChatCommand(String msg)
	{
		if(!msg.equals("hdn_verifymod") && maxMana == -1)
			return;
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
		if(handler == null)
			return;
    	handler.sendChatCommand(msg);
	}

	// public static void load()
	// {
	// 	File file = new File("infinity-keys.cfg");
	// 	if(!file.exists())
	// 	{
	// 		save();
	// 		return;
	// 	}
	// 	try
	// 	{
	// 		Scanner scan = new Scanner(file);
	// 		scan.close();
	// 	}
	// 	catch (FileNotFoundException e)
	// 	{
	// 		ArcaneMana.LOGGER.error("Failed to open config file.", e);
	// 	}
	// }

	// public static void save()
	// {
	// 	File file = new File("infinity-keys.cfg");
	// 	try
	// 	{
	// 		FileWriter write = new FileWriter(file);

	// 		write.close();

	// 	}
	// 	catch (IOException e)
	// 	{
	// 		ArcaneMana.LOGGER.error("Failed to save config file.", e);
	// 	}
	// }
}