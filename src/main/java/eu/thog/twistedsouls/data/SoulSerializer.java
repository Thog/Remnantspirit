package eu.thog.twistedsouls.data;

import eu.thog.twistedsouls.TwistedSouls;
import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Class that provide entity rendering for {@link ItemShard.SoulData}
 *
 * @author Thog
 */
public class SoulSerializer
{
    private ItemShard.SoulData soulData;

    public ItemShard.SoulData getSoulData()
    {
        return soulData;
    }

    public void setSoulData(ItemShard.SoulData data)
    {
        this.soulData = data;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.soulData = ItemShard.SoulData.deserialize(compound);
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        if (soulData != null)
            soulData.writeNBT(compound);
    }

    public ItemStack toShard()
    {
        if (soulData == null)
            return null;
        ItemStack stack = new ItemStack(TwistedSouls.Registry.SHARD, 1);
        stack.setTagCompound(soulData.serialize());
        stack.setItemDamage(stack.getMaxDamage() - soulData.getKilled());
        return stack;
    }
}
