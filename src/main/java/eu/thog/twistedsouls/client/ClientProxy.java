package eu.thog.twistedsouls.client;

import eu.thog.twistedsouls.CommonProxy;
import eu.thog.twistedsouls.TwistedSouls;
import eu.thog.twistedsouls.client.renderer.IModelLoader;
import eu.thog.twistedsouls.client.renderer.TileEntitySoulContainerRenderer;
import eu.thog.twistedsouls.client.renderer.TileEntitySoulSpawnerRenderer;
import eu.thog.twistedsouls.tileentity.TileEntitySoulContainer;
import eu.thog.twistedsouls.tileentity.TileEntitySoulSpawner;
import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        super.preInit();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulSpawner.class, new TileEntitySoulSpawnerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySoulContainer.class, new TileEntitySoulContainerRenderer());
    }

    @Override
    protected void registerItem(Item item, String name)
    {
        super.registerItem(item, name);
        if (item instanceof IModelLoader)
            ((IModelLoader) item).registerRender();
        else
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(TwistedSouls.MODID + ":" + name, "inventory"));
    }

    @Override
    protected void registerBlock(Block block, String name)
    {
        super.registerBlock(block, name);
        if (block instanceof IModelLoader)
            ((IModelLoader) block).registerRender();
        else
            ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock(block), 0, new ModelResourceLocation(TwistedSouls.MODID + ":" + name, "inventory"));
    }
}
