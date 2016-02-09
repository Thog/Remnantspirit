package eu.thog.twistedsouls;

import eu.thog.twistedsouls.block.BlockSoulContainer;
import eu.thog.twistedsouls.block.BlockSoulSpawner;
import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TwistedSouls.MODID, version = TwistedSouls.VERSION)
public class TwistedSouls
{
    public static final String MODID = "twistedsouls";
    public static final String VERSION = "0.1";
    @SidedProxy(clientSide = "eu.thog.twistedsouls.client.ClientProxy", serverSide = "eu.thog.twistedsouls.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
    }

    public static class Registry
    {
        public static final Item EMPTY_SHARD = new Item().setUnlocalizedName("empty_shard").setCreativeTab(CreativeTabs.tabMisc);
        public static final Item SHARD = new ItemShard().setUnlocalizedName("shard");
        public static final Block SOUL_SPAWNER = new BlockSoulSpawner().setUnlocalizedName("soul_spawner").setCreativeTab(CreativeTabs.tabMisc);
        public static final Block SOUL_CONTAINER = new BlockSoulContainer().setUnlocalizedName("soul_container").setCreativeTab(CreativeTabs.tabMisc);
    }
}
