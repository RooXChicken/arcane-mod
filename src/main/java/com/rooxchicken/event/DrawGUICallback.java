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

    private Identifier manaBarTex = Identifier.of("arcane-mana", "textures/gui/manabar.png");
    private Identifier vampiricBar = Identifier.of("arcane-mana", "textures/gui/vampiricBar.png");
    private Identifier manaBarOverlayTex = Identifier.of("arcane-mana", "textures/gui/manabarOverlay.png");

    //private int barSize = 60;

    @Override
    public void onHudRender(DrawContext context, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        int x = _x;
        int y = _y;

        if(HandleData.bloodEnabled)
        {
            context.fill(x+4, y+61, x + 19, y+61 - (int)(52 * (HandleData.blood/20.0)), 0xFFFF2424);
            context.drawTexture(vampiricBar, x-10, y, 0, 0, (int)(29*1.5), (int)(47*1.5), (int)(29*1.5), (int)(47*1.5));
            context.drawCenteredTextWithShadow(textRenderer, Text.of(HandleData.blood + ""), x+12, y+20, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.of("-"), x+12, y+30, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.of("20"), x+12, y+40, 0xFFFFFFFF);
            x += 26;
        }
        
        RenderSystem.enableBlend();

        if(HandleData.displaySkill > 0)
        {
            String cooldown = HandleData.cooldown;
            if(!cooldown.contains("READY") && !cooldown.contains("ACTIVE") && !cooldown.contains("INACTIVE") && !cooldown.contains("CHARGING"))
                cooldown += "s";

            ArrayList<OrderedText> txt = new ArrayList<OrderedText>();
            
            MutableText skillName = MutableText.of(Text.of(HandleData.skillName).getContent());
            skillName.setStyle(skillName.getStyle().withColor(TextColor.parse(HandleData.nameColor.trim())));

            MutableText manaCost = MutableText.of(Text.of("Cost: " + HandleData.manaUse).getContent());
            manaCost.setStyle(manaCost.getStyle().withColor(TextColor.parse(HandleData.manaUseColor.trim())));
            txt.add(manaCost.asOrderedText());

            txt.add(Text.of(" | ").asOrderedText());

            MutableText cooldownTxt = MutableText.of(Text.of(cooldown).getContent());
            cooldownTxt.setStyle(cooldownTxt.getStyle().withColor(TextColor.parse(HandleData.cooldownColor.trim())));
            txt.add(cooldownTxt.asOrderedText());

            if(HandleData.bloodEnabled)
            {
                txt.add(1, Text.of(((HandleData.bloodUsage != -2) ? " -" + HandleData.bloodUsage : " +3") + " ").asOrderedText());
            }

            context.drawCenteredTextWithShadow(textRenderer, skillName.asOrderedText(), x+39+40, y-2, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, OrderedText.concat(txt), x+39+40, y+38, 0xFFFFFFFF);
            //context.drawText(textRenderer, Text.of(HandleData.skillName + " | Cost: " + HandleData.manaUse + " | " + cooldown), x, y, 0xFFFFFFFF, true);
            //y += 10;
        }
        else
        {
            //ItemStack playerHead = new ItemStack()
            //context.drawItem()
        }
        //ArcaneMana.LOGGER.info("" + ((1.0*ArcaneManaClient.maxMana)/ArcaneManaClient.mana));
        int off = 39;
        //context.fill(x, y, x + 100, y+12, 0xFF000000);
        
        context.fill(x+off, y+16, x+off + 87, y+27, 0xFF444444);
        context.fill(x+off, y+16, x+off + (int)(87 * (1.0*ArcaneManaClient.mana/ArcaneManaClient.maxMana)), y+27, 0xFF2244FF);
        context.drawTexture(manaBarTex, x, y, 0, 0, (int)(90*1.5), (int)(29*1.5), (int)(90*1.5), (int)(29*1.5));
        //RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.2f);
        //context.drawTexture(manaBarOverlayTex, x, y, 0, 0, (int)(90*1.5), (int)(29*1.5), (int)(90*1.5), (int)(29*1.5));
        // if(HandleData.bloodEnabled)
        // {
        //     //context.fill(x+off, y+16, x+off + 87, y+27, 0xFF444444);
        //     context.fill(x-18, y+40, x+off + (int)(87 * (HandleData.blood/20.0)), y+27, 0xFFFF2424);
        //     context.drawCenteredTextWithShadow(textRenderer, Text.of(HandleData.blood + "/20"), x+43+off, y+30, 0xFFFFFFFF);
        // }
        context.drawCenteredTextWithShadow(textRenderer, Text.of(ArcaneManaClient.mana + "/" + ArcaneManaClient.maxMana), x+43+off, y+18, 0xFFFFFFFF);

        context.drawTexture(Identifier.of("arcane-mana", "textures/gui/icons/manatoken.png"), x+10, y+10, 0, 0, 24, 23, 24, 23);


        RenderSystem.disableBlend();
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
