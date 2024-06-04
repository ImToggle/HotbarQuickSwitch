package me.imtoggle.quickswitch.element

import cc.polyfrost.oneconfig.gui.OneConfigGui
import cc.polyfrost.oneconfig.gui.animations.Animation
import cc.polyfrost.oneconfig.gui.animations.DummyAnimation
import cc.polyfrost.oneconfig.gui.elements.BasicElement
import cc.polyfrost.oneconfig.libs.universal.UResolution
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.imtoggle.quickswitch.util.ItemTypes
import me.imtoggle.quickswitch.MainRenderer
import me.imtoggle.quickswitch.util.EaseOutQuart
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

@Suppress("UnstableAPIUsage")
class ItemTypeElement(val id: Int, val actionType: Int) : BasicElement(32, 32, ColorPalette.SECONDARY, true, 10f) {

    data class RenderInfo(val x: Float, val y: Float)

    private val notAllowed = ItemStack(Item.getItemFromBlock(Blocks.barrier))
    private val item = ItemStack(ItemTypes.getRenderItem(id))
    private var renderInfo: RenderInfo? = null
    private var lastPressed = false
    var shouldRemove = false
    var cantPut = false
    private var xAnimation: Animation = DummyAnimation(0f)
    private var yAnimation: Animation = DummyAnimation(0f)

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun update(x: Float, y: Float, inputHandler: InputHandler) {
        shouldRemove = false
        super.update(x, y, inputHandler)
        if (lastPressed != pressed) {
            lastPressed = pressed
            if (pressed && actionType != -1) {
                MainRenderer.xOffset = inputHandler.mouseX() - x
                MainRenderer.yOffset = inputHandler.mouseY() - y
                MainRenderer.dragging = ItemTypeElement(id, -1)
                if (actionType == 1) {
                    shouldRemove = true
                }
            }
        }
    }

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        super.draw(vg, x, y, inputHandler)
        renderInfo = RenderInfo(x + 4, y + 4)
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler, doAnimation: Boolean) {
        val duration = if (doAnimation) 400f else 0f
        if (x != xAnimation.end) {
            xAnimation = EaseOutQuart(duration, xAnimation.end, x, false)
        }
        if (y != yAnimation.end) {
            yAnimation = EaseOutQuart(duration, yAnimation.end, y, false)
        }
        draw(vg, xAnimation.get(), yAnimation.get(), inputHandler)
    }

    @SubscribeEvent
    fun drawIcon(e: GuiScreenEvent.DrawScreenEvent.Post) {
        val (x, y) = renderInfo ?: return
        renderInfo = null
        val oneConfigGui = mc.currentScreen as? OneConfigGui ?: return
        val unscaleMC = 1 / UResolution.scaleFactor
        val oneUIScale = OneConfigGui.getScaleFactor() * oneConfigGui.animationScaleFactor
        val rawX = ((UResolution.windowWidth - 800 * oneUIScale) / 2f).toInt()
        val rawY = ((UResolution.windowHeight - 768 * oneUIScale) / 2f).toInt()
        GlStateManager.pushMatrix()
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(rawX, rawY, (1024 * oneUIScale).toInt(), ((696 - if (actionType == 1) 48 else 0) * oneUIScale).toInt())
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
        itemRenderer.renderItemAndEffectIntoGUI(if (cantPut) notAllowed else item, 0, 0)
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableBlend()
        GlStateManager.disableRescaleNormal()
        GlStateManager.enableAlpha()
        GlStateManager.popMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
        cantPut = false
    }

}