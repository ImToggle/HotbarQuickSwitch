package me.imtoggle.quickswitch

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.events.event.TickEvent
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.utils.dsl.mc
import me.imtoggle.quickswitch.config.ModConfig
import net.minecraft.item.Item
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = QuickSwitch.MODID, name = QuickSwitch.NAME, version = QuickSwitch.VERSION, modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter")
object QuickSwitch {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ModConfig
        EventManager.INSTANCE.register(this)
    }

    var switchQueue = -1

    @Subscribe
    fun onTickEnd(e: TickEvent) {
        if (e.stage != Stage.END || switchQueue == -1) return
        mc.thePlayer.inventory.currentItem = switchQueue
        switchQueue = -1
    }

    fun switchTo(type: Any): Boolean {
        val hotbar = mc.thePlayer. inventory.mainInventory
            .slice(0..8)
            .withIndex()
        val index = if (type is Int) {
            hotbar.firstOrNull { (_, itemStack) -> itemStack == null }?.index
        } else {
            hotbar.filter { (_, itemStack) ->
                itemStack != null && itemStack.item.isFrom(type)
            }.maxByOrNull { (_, itemStack) ->
                itemStack.stackSize
            }?.index
        }

        index?.let {
            switchQueue = it
            return true
        }
        return false
    }

    fun Item.isFrom(type: Any) : Boolean {
        return if (type is Item) {
            this == type
        } else {
            (type as Class<Item>).isInstance(this)
        }
    }

}