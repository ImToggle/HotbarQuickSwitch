package me.imtoggle.quickswitch.config

import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import me.imtoggle.quickswitch.util.ItemTypes
import me.imtoggle.quickswitch.MainRenderer
import me.imtoggle.quickswitch.QuickSwitch
import me.imtoggle.quickswitch.element.KeyBindElement
import java.lang.reflect.Field

class KeyBindEntry {

    var keyBind = OneKeyBind(UKeyboard.KEY_NONE)

    var itemTypes = ArrayList<Int>()

    @Exclude
    var map: MutableMap.MutableEntry<Field?, Any?> = object : MutableMap.MutableEntry<Field?, Any?> {
        override val key: Field = this.javaClass.declaredFields[0]

        override val value: Any = this

        override fun setValue(newValue: Any?): Any? {
            return null
        }
    }

    fun onAdd() {
        keyBind.setRunnable { run() }
        ModConfig.addKeyBind(map, keyBind)
        val element = KeyBindElement(this)
        itemTypes.forEach { element.addItem(it, -1, true) }
        MainRenderer.keyElements.add(element)
    }

    fun onRemove() {
        ModConfig.entries.remove(this)
        ModConfig.removeKeyBind(map)
    }

    fun run() {
        if (itemTypes.isEmpty()) return
        for (type in itemTypes) {
            if (QuickSwitch.switchTo(ItemTypes.entries[type].type)) break
        }
    }

}