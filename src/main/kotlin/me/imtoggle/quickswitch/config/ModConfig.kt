package me.imtoggle.quickswitch.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.core.OneKeyBind
import cc.polyfrost.oneconfig.config.data.*
import cc.polyfrost.oneconfig.internal.config.core.KeyBindHandler
import me.imtoggle.quickswitch.QuickSwitch
import me.imtoggle.quickswitch.mixin.KeyBindHandlerAccessor
import java.lang.reflect.Field

object ModConfig : Config(Mod(QuickSwitch.NAME, ModType.UTIL_QOL), "${QuickSwitch.MODID}.json") {

    var entries = ArrayList<KeyBindEntry>()

    fun addKeyBind(map: MutableMap.MutableEntry<Field?, Any?>, keyBind: OneKeyBind) {
        (KeyBindHandler.INSTANCE as KeyBindHandlerAccessor).keyBinds[map] = keyBind
    }

    fun removeKeyBind(map: MutableMap.MutableEntry<Field?, Any?>) {
        (KeyBindHandler.INSTANCE as KeyBindHandlerAccessor).keyBinds.remove(map)
    }

    init {
        initialize()
        for (entry in entries) {
            entry.onAdd()
        }
    }

}