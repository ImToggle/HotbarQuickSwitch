package me.imtoggle.quickswitch

import cc.polyfrost.oneconfig.utils.InputHandler
import me.imtoggle.quickswitch.element.ItemTypeElement

object Selector {

    var elements: Array<ItemTypeElement> = Array(ItemTypes.entries.size) { ItemTypeElement(it, 0) }

    fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var renderX = x
        for (element in elements) {
            element.draw(vg, renderX.toFloat(), y.toFloat(), inputHandler)
            renderX += 48
        }
    }

}