package me.imtoggle.quickswitch.element

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import me.imtoggle.quickswitch.KeyBindsRenderer
import me.imtoggle.quickswitch.config.KeyBindEntry

@Suppress("UnstableAPIUsage")
class KeyBindElement(val entry: KeyBindEntry) {

    val keyBindButton = KeyBindButton(entry.keyBind)
    private val removeButton = BasicButton(32, 32, "", BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY_DESTRUCTIVE)

    init {
        removeButton.setClickAction {
            KeyBindsRenderer.removeQueue.add(this)
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        keyBindButton.draw(vg, x, y, inputHandler)
        removeButton.draw(vg, x + 256 + 16, y, inputHandler)
    }

}