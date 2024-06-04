package me.imtoggle.quickswitch.util

import net.minecraft.init.*
import net.minecraft.item.*

enum class ItemTypes(val type: Any, val renderItem: Item) {
    Sword(ItemSword::class.java, Items.diamond_sword),
    Block(ItemBlock::class.java, Item.getItemFromBlock(Blocks.wool)),
    FishingRod(ItemFishingRod::class.java, Items.fishing_rod),
    Bow(ItemBow::class.java, Items.bow),
    Shears(ItemShears::class.java, Items.shears),
    Axe(ItemAxe::class.java, Items.diamond_axe),
    Pickaxe(ItemPickaxe::class.java, Items.diamond_pickaxe),
    Shovel(ItemSpade::class.java, Items.diamond_shovel),
    Hoe(ItemHoe::class.java, Items.diamond_hoe),
    Egg(ItemEgg::class.java, Items.egg),
    Snowball(ItemSnowball::class.java, Items.snowball),
    EnderPearl(ItemEnderPearl::class.java, Items.ender_pearl),
    Fireball(ItemFireball::class.java, Items.fire_charge),
    Food(ItemFood::class.java, Items.golden_apple),
    Bucket(Items.bucket, Items.bucket),
    WaterBucket(Items.water_bucket, Items.water_bucket),
    LavaBucket(Items.lava_bucket, Items.lava_bucket),
    NONE(0, Item.getItemFromBlock(Blocks.barrier));

    companion object {
        fun getRenderItem(index: Int): Item {
            for (i in entries) {
                if (i.ordinal == index) {
                    return i.renderItem
                }
            }
            return Item.getItemFromBlock(Blocks.air)
        }

    }

}