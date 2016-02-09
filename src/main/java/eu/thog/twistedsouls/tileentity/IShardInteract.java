package eu.thog.twistedsouls.tileentity;

import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.item.ItemStack;

/**
 * Provide a common implementation for {@link net.minecraft.tileentity.TileEntity} based Shard container
 */
public interface IShardInteract
{

    /**
     * Provide data about the soul
     *
     * @return contained SoulData or null if empty
     */
    ItemShard.SoulData getSoulData();

    /**
     * Remove Shard from the {@link net.minecraft.tileentity.TileEntity}
     *
     * @return The shard to release {@link ItemStack}
     */
    ItemStack removeShard();

    /**
     * Insert Shard into the {@link net.minecraft.tileentity.TileEntity}
     *
     * @param stack The shard {@link ItemStack}
     * @return If it success
     */
    boolean applyShard(ItemStack stack);
}
