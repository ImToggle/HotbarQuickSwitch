package me.imtoggle.quickswitch

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import me.imtoggle.quickswitch.config.KeyBindEntry
import me.imtoggle.quickswitch.config.ModConfig
import me.imtoggle.quickswitch.element.KeyBindElement

@Suppress("UnstableAPIUsage")
object KeyBindsRenderer : BasicOption(null, null, "", "", "", "", 2) {

    private val addButton = BasicButton(32, 32, "", BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)

    var keyElements = ArrayList<KeyBindElement>()

    var removeQueue = ArrayList<KeyBindElement>()

    var currentHeight = 0

    init {
        addButton.setClickAction {
            val entry = KeyBindEntry()
            ModConfig.entries.add(entry)
            entry.onAdd()
        }
    }

    override fun getHeight(): Int = currentHeight

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var renderY = y
        for (element in keyElements) {
            element.draw(vg, x.toFloat(), renderY.toFloat(), inputHandler)
            renderY += 96
        }
        for (element in removeQueue) {
            keyElements.remove(element)
            element.entry.onRemove()
        }
        addButton.draw(vg, x.toFloat(), renderY.toFloat(), inputHandler)

        currentHeight = renderY - y + 32
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        for (element in keyElements) {
            element.keyBindButton.isKeyTyped(keyCode)
        }
    }
}