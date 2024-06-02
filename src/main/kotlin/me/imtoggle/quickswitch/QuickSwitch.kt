package me.imtoggle.quickswitch

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
    }

    fun switchTo(type: Any): Boolean {
        val index = mc.thePlayer. inventory.mainInventory
            .slice(0..8)
            .withIndex()
            .filter { (_, itemStack) ->
                itemStack != null && itemStack.item.isFrom(type)
            }.maxByOrNull { (_, itemStack) ->
                itemStack.stackSize
            }?.index
        index?.let {
            mc.thePlayer.inventory.currentItem = it
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