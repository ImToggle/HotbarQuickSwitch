package me.imtoggle.quickswitch.element

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import cc.polyfrost.oneconfig.utils.dsl.nanoVGHelper
import me.imtoggle.quickswitch.MainRenderer
import me.imtoggle.quickswitch.config.KeyBindEntry
import java.awt.Color

@Suppress("UnstableAPIUsage")
class KeyBindElement(val entry: KeyBindEntry) {
    private val MINUS = SVG("/assets/minus.svg")
    val keyBindButton = KeyBindButton(entry.keyBind)
    private val removeButton = BasicButton(32, 32, MINUS, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY_DESTRUCTIVE)
    var hovered = false
    var types = ArrayList<ItemTypeElement>()
    var removeQueue = ArrayList<ItemTypeElement>()

    init {
        removeButton.setClickAction {
            MainRenderer.removeQueue.add(this)
        }
    }

    fun addItem(type: Int, index: Int, bypass: Boolean) {
        if (entry.itemTypes.contains(type)) {
            if (!bypass) return
        } else {
            if (index == -1) {
                entry.itemTypes.add(type)
            } else{
                entry.itemTypes.add(index, type)
            }
        }
        if (index == -1) {
            types.add(ItemTypeElement(type, 1))
        } else{
            types.add(index, ItemTypeElement(type, 1))
        }
    }

    fun removeItem(type: ItemTypeElement) {
        types.remove(type)
        entry.itemTypes.remove(type.id)
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        keyBindButton.draw(vg, x, y, inputHandler)
        removeButton.draw(vg, x + 256 + 16, y, inputHandler)
        val yHovered = inputHandler.mouseY() in y + 40..y + 88
        hovered = inputHandler.mouseX() in x..x + 1024 && yHovered
        var renderX = x
        val flag = MainRenderer.dragging != null && yHovered
        MainRenderer.dragging?.let {
            if (yHovered && entry.itemTypes.contains(it.id)) {
                it.cantPut = true
            }
        }
        var drawn = false
        for ((index, type) in types.withIndex()) {
            if (flag && inputHandler.mouseX() in renderX - 8..renderX + 40) {
                MainRenderer.target = index
                nanoVGHelper.drawRoundedRect(vg, renderX, y + 48, 32f, 32f, Color(255, 255, 255, 100).rgb, 10f)
                renderX += 48
                drawn = true
            }

            type.draw(vg, renderX, y + 48, inputHandler, flag)
            if (type.shouldRemove) removeQueue.add(type)
            renderX += 48
        }
        if (flag && !drawn) {
            nanoVGHelper.drawRoundedRect(vg, renderX, y + 48, 32f, 32f, Color(255, 255, 255, 100).rgb, 10f)
            MainRenderer.target = -1
        }
        for (type in removeQueue) {
            removeItem(type)
        }
        removeQueue.clear()
    }

}