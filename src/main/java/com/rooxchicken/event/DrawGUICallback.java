package com.rooxchicken.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rooxchicken.ArcaneMana;
import com.rooxchicken.client.ArcaneManaClient;
import com.rooxchicken.data.HandleData;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DrawGUICallback implements HudRenderCallback
{
    private MatrixStack matrixStack;

    private int _x = 10;
    private int _y = 10;

    //private int barSize = 60;

    @Override
    public void onHudRender(DrawContext context, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int x = _x;
        int y = _y;

        if(HandleData.displaySkill > 0)
        {
            String cooldown = HandleData.cooldown;
            if(!cooldown.contains("READY") && !cooldown.contains("ACTIVE") && !cooldown.contains("INACTIVE") && !cooldown.contains("CHARGING"))
                cooldown += "s";
            context.drawText(textRenderer, Text.of(HandleData.skillName + " | Cost: " + HandleData.manaUse + " | " + cooldown), x, y, 0xFFFFFFFF, true);
            y += 10;
        }
        //ArcaneMana.LOGGER.info("" + ((1.0*ArcaneManaClient.maxMana)/ArcaneManaClient.mana));
        context.fill(x, y, x + 100, y+12, 0xFF000000);
        context.fill(x, y, x + (int)(100 * (1.0*ArcaneManaClient.mana/ArcaneManaClient.maxMana)), y+12, 0xFF2244FF);
        context.drawCenteredTextWithShadow(textRenderer, Text.of(ArcaneManaClient.mana + "/" + ArcaneManaClient.maxMana), x+50, y+2, 0xFFFFFFFF);
    }
    
    private void startScaling(DrawContext context, double scale)
    {
        matrixStack = context.getMatrices();
		matrixStack.push();
		matrixStack.scale((float)scale, (float)scale, (float)scale);
    }

    private void stopScaling(DrawContext context)
    {
        matrixStack.pop();
    }
}
