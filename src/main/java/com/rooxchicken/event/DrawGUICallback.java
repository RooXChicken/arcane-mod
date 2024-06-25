package com.rooxchicken.event;

import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rooxchicken.ArcaneMana;
import com.rooxchicken.client.ArcaneManaClient;
import com.rooxchicken.data.HandleData;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class DrawGUICallback implements HudRenderCallback
{
    private MatrixStack matrixStack;

    private int _x = 10;
    private int _y = 10;

    private Identifier manaBarTex = Identifier.of("arcane-mana", "textures/gui/manaBar.png");
    private Identifier manaBarInTex = Identifier.of("arcane-mana", "textures/gui/manaBarIn.png");
    private Identifier manaBarInBlackTex = Identifier.of("arcane-mana", "textures/gui/manaBarInBlack.png");
    private Identifier manaBarCrystalTex = Identifier.of("arcane-mana", "textures/gui/manaBarWithCrystal.png");
    private Identifier manaBarOverlayTex = Identifier.of("arcane-mana", "textures/gui/manabarOverlay.png");

    private Identifier vampiricBar = Identifier.of("arcane-mana", "textures/gui/vampiricBar.png");
    private Identifier vampiricBar90 = Identifier.of("arcane-mana", "textures/gui/vampiricBar90.png");


    //private int barSize = 60;

    @Override
    public void onHudRender(DrawContext context, float tickDelta)
    {
        if(!ArcaneManaClient.mainRender)
            return;
            
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        double scale1 = ArcaneManaClient.manaBar.Scale;
        int pos1X = (int)(ArcaneManaClient.manaBar.PositionX * (1/scale1));
        int pos1Y = (int)(ArcaneManaClient.manaBar.PositionY * (1/scale1));

        double scale2 = ArcaneManaClient.bloodBar.Scale;
        int pos2X = (int)(ArcaneManaClient.bloodBar.PositionX * (1/scale2));
        int pos2Y = (int)(ArcaneManaClient.bloodBar.PositionY * (1/scale2));

        if(HandleData.bloodEnabled)
        {
            startScaling(context, scale2);

            if(!ArcaneManaClient.bloodBar.rotation)
            {
                context.fill(pos2X+4, pos2Y+61, pos2X + 19, pos2Y+61 - (int)(52 * (HandleData.blood/20.0)), 0xFFFF2424);
                context.drawTexture(vampiricBar, pos2X-10, pos2Y, 0, 0, (int)(29*1.5), (int)(47*1.5), (int)(29*1.5), (int)(47*1.5));
                context.drawCenteredTextWithShadow(textRenderer, Text.of(HandleData.blood + ""), pos2X+12, pos2Y+20, 0xFFFFFFFF);
                context.drawCenteredTextWithShadow(textRenderer, Text.of("-"), pos2X+12, pos2Y+30, 0xFFFFFFFF);
                context.drawCenteredTextWithShadow(textRenderer, Text.of("20"), pos2X+12, pos2Y+40, 0xFFFFFFFF);
            }
            else
            {
                context.fill(pos2X-1, pos2Y+8, pos2X - 1 + (int)(52 * (HandleData.blood/20.0)), pos2Y+23, 0xFFFF2424);
                context.drawTexture(vampiricBar90, pos2X-10, pos2Y, 0, 0, (int)(47*1.5), (int)(21*1.5), (int)(47*1.5), (int)(21*1.5));
                context.drawCenteredTextWithShadow(textRenderer, Text.of(" | 20"), pos2X+32, pos2Y+12, 0xFFFFFFFF);
                context.drawCenteredTextWithShadow(textRenderer, Text.of(HandleData.blood + ""), pos2X+16, pos2Y+12, 0xFFFFFFFF);
            }

            stopScaling(context);
        }
    
        startScaling(context, scale1);

        if(HandleData.displaySkill > 0)
        {
            ArrayList<OrderedText> txt = new ArrayList<OrderedText>();
            
            MutableText skillName = MutableText.of(Text.of(HandleData.skillName).getContent());
            skillName.setStyle(skillName.getStyle().withColor(TextColor.parse(HandleData.nameColor.trim())));

            MutableText manaCost = MutableText.of(Text.of("Cost: " + HandleData.manaUse).getContent());
            manaCost.setStyle(manaCost.getStyle().withColor(TextColor.parse(HandleData.manaUseColor.trim())));
            txt.add(manaCost.asOrderedText());

            txt.add(Text.of(" | ").asOrderedText());

            MutableText cooldownTxt = MutableText.of(Text.of(HandleData.cooldown).getContent());
            cooldownTxt.setStyle(cooldownTxt.getStyle().withColor(TextColor.parse(HandleData.cooldownColor.trim())));
            txt.add(cooldownTxt.asOrderedText());

            if(HandleData.bloodEnabled)
            {
                MutableText bloodTxt = MutableText.of(Text.of(((HandleData.bloodUsage != -2) ? " -" + HandleData.bloodUsage : " +3")).getContent());
                bloodTxt.setStyle(bloodTxt.getStyle().withColor(0xAA2222));
                txt.add(1, bloodTxt.asOrderedText());
                
            }

            context.drawCenteredTextWithShadow(textRenderer, skillName.asOrderedText(), pos1X+39+40, pos1Y-2, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, OrderedText.concat(txt), pos1X+39+40, pos1Y+38, 0xFFFFFFFF);
        }

        context.fill(pos1X+39, pos1Y+16, pos1X+39 + 87, pos1Y+27, 0xFF444444);
        context.fill(pos1X+39, pos1Y+16, pos1X+39 + (int)(87 * (1.0*ArcaneManaClient.mana/ArcaneManaClient.maxMana)), pos1Y+27, 0xFF4466FF);

        RenderSystem.enableBlend();
        context.drawTexture(manaBarOverlayTex, pos1X, pos1Y, 0, 0, (int)(90*1.5), (int)(29*1.5), (int)(90*1.5), (int)(29*1.5));

        if(HandleData.displaySkill < 0)
        {
            context.drawTexture(manaBarInTex, pos1X, pos1Y, 0, 0, (int)(110*1.5), (int)(29*1.5), (int)(110*1.5), (int)(29*1.5));
            context.drawTexture(Identifier.of("arcane-mana", "textures/gui/icons/manatoken.png"), pos1X+10, pos1Y+10, 0, 0, 24, 23, 24, 23);
        }
        else
        {
            context.drawTexture(manaBarInBlackTex, pos1X, pos1Y, 0, 0, (int)(110*1.5), (int)(29*1.5), (int)(110*1.5), (int)(29*1.5));
            context.drawTexture(Identifier.of("arcane-mana", "textures/gui/icons/" + HandleData.skillNameFile + ".png"), pos1X+9, pos1Y+9, 0, 0, 26, 26, 26, 26);
        }

        if(HandleData.hasCrystal < 0 || HandleData.crystalName.equals("empty"))
            context.drawTexture(manaBarTex, pos1X, pos1Y, 0, 0, (int)(110*1.5), (int)(29*1.5), (int)(110*1.5), (int)(29*1.5));
        else
        {
            context.drawTexture(manaBarCrystalTex, pos1X, pos1Y, 0, 0, (int)(110*1.5), (int)(29*1.5), (int)(110*1.5), (int)(29*1.5));
            context.drawTexture(Identifier.of("arcane-mana", "textures/gui/crystals/" + HandleData.crystalName + ".png"), pos1X+131, pos1Y+10, 0, 0, 24, 23, 24, 23);
        }

        if(HandleData.upgraded && HandleData.hasCrystal >= 0)
            context.drawTexture(Identifier.of("arcane-mana", "textures/gui/star.png"), pos1X+128, pos1Y, 0, 0, 16, 16, 16, 16);

        context.drawCenteredTextWithShadow(textRenderer, Text.of(ArcaneManaClient.mana + "/" + ArcaneManaClient.maxMana), pos1X+82, pos1Y+18, 0xFFFFFFFF);

        RenderSystem.disableBlend();
        
        stopScaling(context);
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
