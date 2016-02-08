package eu.thog.twistedsouls;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TwistedSouls.MODID, version = TwistedSouls.VERSION)
public class TwistedSouls
{
    @SidedProxy(clientSide = "eu.thog.twistedsouls.client.ClientProxy", serverSide = "eu.thog.twistedsouls.CommonProxy")
    public static CommonProxy proxy;
    public static final String MODID = "twistedsouls";
    public static final String VERSION = "0.1";

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
}
