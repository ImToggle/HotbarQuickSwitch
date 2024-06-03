package me.imtoggle.quickswitch

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.RawMouseEvent
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import me.imtoggle.quickswitch.config.KeyBindEntry
import me.imtoggle.quickswitch.config.ModConfig
import me.imtoggle.quickswitch.element.ItemTypeElement
import me.imtoggle.quickswitch.element.KeyBindElement

@Suppress("UnstableAPIUsage")
object MainRenderer : BasicOption(null, null, "", "", "", "", 2) {
    private val PLUS = SVG("/assets/plus.svg")
    private val addButton = BasicButton(32, 32, PLUS, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)

    var keyElements = ArrayList<KeyBindElement>()

    var removeQueue = ArrayList<KeyBindElement>()

    var dragging: ItemTypeElement? = null
    var currentHeight = 0
    var target = -1
    var xOffset = 0f
    var yOffset = 0f
    var checkChange = false

    init {
        addButton.setClickAction {
            val entry = KeyBindEntry()
            ModConfig.entries.add(entry)
            entry.onAdd()
        }
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    private fun onMouse(event: RawMouseEvent) {
        if (event.button == 0 && event.state == 0) {
            checkChange = true
        }
    }

    override fun getHeight(): Int = currentHeight

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var renderY = y
        for (element in keyElements) {
            element.draw(vg, x.toFloat(), renderY.toFloat(), inputHandler)
            renderY += 96
        }
        if (inputHandler.isMouseDown) {
            tryDraw(dragging, vg, inputHandler.mouseX() - xOffset, inputHandler.mouseY() - yOffset, inputHandler)
        }
        if (checkChange) {
            checkChange = false
            dragging?.let {
                for (element in keyElements) {
                    if (element.hovered) {
                        element.addItem(it.id, target, false)
                    }
                }
                dragging = null
            }
            inputHandler.stopBlockingInput()
        }
        for (element in removeQueue) {
            keyElements.remove(element)
            element.entry.onRemove()
        }
        removeQueue.clear()
        addButton.draw(vg, x.toFloat(), renderY.toFloat(), inputHandler)

        currentHeight = renderY - y + 32
    }

    fun tryDraw(element: ItemTypeElement?, vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        element?.let {
            inputHandler.blockAllInput()
            it.draw(vg, inputHandler.mouseX() - xOffset, inputHandler.mouseY() - yOffset, inputHandler)
        }
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        for (element in keyElements) {
            element.keyBindButton.isKeyTyped(keyCode)
        }
    }
}