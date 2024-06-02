package me.imtoggle.quickswitch

import net.minecraft.init.*
import net.minecraft.item.*

enum class ItemTypes(val displayName: String, val type: Any, val renderItem: Item) {
    Sword("Sword", ItemSword::class.java, Items.diamond_sword),
    Block("Block", ItemBlock::class.java, Item.getItemFromBlock(Blocks.wool)),
    FishingRod("Fishing Rod", ItemFishingRod::class.java, Items.fishing_rod),
    Bow("Bow", ItemBow::class.java, Items.bow),
    Shears("Shears", ItemShears::class.java, Items.shears),
    Axe("Axe", ItemAxe::class.java, Items.diamond_axe),
    Pickaxe("Pickaxe", ItemPickaxe::class.java, Items.diamond_pickaxe),
    Shovel("Shovel", ItemSpade::class.java, Items.diamond_shovel),
    Hoe("Hoe", ItemHoe::class.java, Items.diamond_hoe),
    Egg("Egg", ItemEgg::class.java, Items.egg),
    Snowball("Snowball", ItemSnowball::class.java, Items.snowball),
    EnderPearl("Ender Pearl", ItemEnderPearl::class.java, Items.ender_pearl),
    Bucket("Empty Bucket", Items.bucket, Items.bucket),
    WaterBucket("Water Bucket", Items.water_bucket, Items.water_bucket),
    LavaBucket("Lava Bucket", Items.lava_bucket, Items.lava_bucket);

    fun getRenderItem(index: Int): Item {
        for (i in entries) {
            if (i.ordinal == index) {
                return i.renderItem
            }
        }
        return Item.getItemFromBlock(Blocks.air)
    }

    fun getType(name: String): Any {
        for (i in entries) {
            if (i.displayName == name) {
                return i.type
            }
        }
        return ItemBlock::class.java
    }

}