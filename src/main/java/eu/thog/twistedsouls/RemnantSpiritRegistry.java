package eu.thog.twistedsouls;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Desc...
 * Created by Thog the 22/01/2016
 */
public class RemnantSpiritRegistry
{
    public static final Item EMPTY_SHARD = new Item().setUnlocalizedName("empty_shard").setCreativeTab(CreativeTabs.tabMisc);
    public static final Item SHARD = new ItemShard().setUnlocalizedName("shard");
    public static final Block SOUL_SPAWNER = new BlockSoulSpawner().setUnlocalizedName("soul_spawner").setCreativeTab(CreativeTabs.tabMisc);
}
