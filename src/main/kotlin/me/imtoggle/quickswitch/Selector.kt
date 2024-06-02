package me.imtoggle.quickswitch

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.RawMouseEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.InputHandler
import me.imtoggle.quickswitch.element.ItemTypeElement

object Selector : BasicOption(null, null, "", "", "", "", 2) {

    var elements: Array<ItemTypeElement> = Array(ItemTypes.entries.size) { ItemTypeElement(it, true) }

    var adding: ItemTypeElement? = null

    var xOffset = 0f
    var yOffset = 0f
    var stop = false

    init {
        EventManager.INSTANCE.register(this)
    }

    @Subscribe
    private fun onMouse(event: RawMouseEvent) {
        if (event.button == 0 && event.state == 0) {
            adding = null
            stop = true
        }
    }

    override fun getHeight(): Int = 48

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var renderX = x
        for (element in elements) {
            element.draw(vg, renderX.toFloat(), y.toFloat(), inputHandler)
            renderX += 48
        }
        if (inputHandler.isMouseDown) {
            adding?.let {
                inputHandler.blockAllInput()
                it.draw(vg, inputHandler.mouseX() - xOffset, inputHandler.mouseY() - yOffset, inputHandler)
            }
        }
        if (stop) {
            inputHandler.stopBlockingInput()
            stop = false
        }
    }

}