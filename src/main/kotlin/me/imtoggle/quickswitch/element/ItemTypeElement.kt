package me.imtoggle.quickswitch.element

import cc.polyfrost.oneconfig.gui.OneConfigGui
import cc.polyfrost.oneconfig.gui.elements.BasicElement
import cc.polyfrost.oneconfig.libs.universal.UResolution
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.imtoggle.quickswitch.ItemTypes
import me.imtoggle.quickswitch.Selector
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

@Suppress("UnstableAPIUsage")
class ItemTypeElement(val id: Int, val action: Boolean): BasicElement(32, 32, ColorPalette.SECONDARY, true, 10f) {

    data class RenderInfo(val x: Float, val y: Float)

    private val item = ItemStack(ItemTypes.getRenderItem(id))
    private var renderInfo: RenderInfo? = null
    private var lastPressed = false

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun update(x: Float, y: Float, inputHandler: InputHandler) {
        super.update(x, y, inputHandler)
        if (lastPressed != pressed) {
            lastPressed = pressed
            if (pressed && action) {
                Selector.xOffset = inputHandler.mouseX() - x
                Selector.yOffset = inputHandler.mouseY() - y
                Selector.adding = ItemTypeElement(id, false)
            }
        }
    }

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        super.draw(vg, x, y, inputHandler)
        renderInfo = RenderInfo(x + 4, y + 4)
    }

    @SubscribeEvent
    fun draw(e: GuiScreenEvent.DrawScreenEvent.Post) {
        val (x, y) = renderInfo ?: return
        renderInfo = null
        val oneConfigGui = mc.currentScreen as? OneConfigGui ?: return
        val unscaleMC = 1 / UResolution.scaleFactor
        val oneUIScale = OneConfigGui.getScaleFactor() * oneConfigGui.animationScaleFactor
        val rawX = ((UResolution.windowWidth - 800 * oneUIScale) / 2f).toInt()
        val rawY = ((UResolution.windowHeight - 768 * oneUIScale) / 2f).toInt()
        GlStateManager.pushMatrix()
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(rawX, rawY, (1024 * oneUIScale).toInt(), (696 * oneUIScale).toInt())
        GlStateManager.scale(unscaleMC * oneUIScale, unscaleMC * oneUIScale, 1.0)
        GlStateManager.pushMatrix()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glColor4f(1f, 1f, 1f, 1f)
        RenderHelper.enableGUIStandardItemLighting()
        val itemRenderer = mc.renderItem
        GlStateManager.translate(x, y, 0f)
        GlStateManager.scale(24 / 16f, 24 / 16f, 1f)
        itemRenderer.renderItemAndEffectIntoGUI(item, 0, 0)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableBlend()
        GlStateManager.disableRescaleNormal()
        GlStateManager.enableAlpha()
        GlStateManager.popMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

}