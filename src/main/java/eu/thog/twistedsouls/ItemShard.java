package eu.thog.twistedsouls;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

/**
 * Desc...
 * Created by Thog the 22/01/2016
 */
public class ItemShard extends Item
{
    public ItemShard()
    {
        this.setMaxDamage(1024);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        NBTTagCompound compound = initCompound(stack);
        String mobName = compound.getString("mobName");
        if (mobName.isEmpty() || compound.getString("mobID").isEmpty())
        {
            tooltip.add("ITEM CORRUPTED!");
            mobName = "???";
        }
        tooltip.add("Mob: " + mobName);
        int killed = (stack.getMaxDamage() - stack.getItemDamage());
        tooltip.add("Tier: " + ((killed / 204) + 1));
        tooltip.add("Souls: " + killed);
    }

    private NBTTagCompound initCompound(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
        {
            compound = new NBTTagCompound();
            stack.setTagCompound(compound);
        }
        return compound;
    }

    public static ItemStack initShard(ItemStack stack, EntityLiving entity)
    {
        ItemStack newStack = new ItemStack(RemnantSpiritRegistry.SHARD, 1);
        newStack.setItemDamage(RemnantSpiritRegistry.SHARD.getMaxDamage() - 1);
        newStack.setTagCompound(SoulData.init(entity).serialize());
        if (stack != null)
            --stack.stackSize;
        return newStack;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return super.getIsRepairable(toRepair, repair);
    }

    public static class SoulData
    {
        private String mobID;
        private String mobName;
        private int tier;
        private int killed;
        private NBTTagCompound compound;
        private final static List<String> FORBIDDEN_NBT = Arrays.asList("X", "Y", "Z", "UUIDMost", "UUIDLeast", "Health", "HealF", "ActiveEffects", "HurtTime", "DeathTime", "HurtByTimestamp");

        private SoulData()
        {

        }

        public NBTTagCompound serialize()
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.writeNBT(compound);
            return compound;
        }

        public void readNBT(NBTTagCompound compound)
        {
            this.mobID = compound.getString("mobID");
            this.mobName = compound.getString("mobName");
            this.tier = compound.getInteger("tier");
            this.killed = compound.getByte("killed");
            this.compound = compound; // Contain Entity extra data
        }

        public void writeNBT(NBTTagCompound compound)
        {
            for (String entry : this.compound.getKeySet())
            {
                compound.setTag(entry, this.compound.getTag(entry));
            }
            compound.setString("mobID", mobID);
            compound.setString("mobName", mobName);
            compound.setInteger("tier", killed / 204);
            compound.setInteger("killed", killed);
        }

        public static SoulData deserialize(NBTTagCompound compound)
        {
            if (compound.getString("mobID").isEmpty())
                return null;

            SoulData data = new SoulData();
            data.readNBT(compound);
            return data;
        }

        public static SoulData deserialize(ItemStack stack)
        {
            SoulData data = deserialize(stack.getTagCompound());
            if (data == null)
                return null;
            data.killed = (stack.getMaxDamage() - stack.getItemDamage());
            data.tier = (data.killed / 202) + 1;
            return data;
        }

        public static SoulData init(EntityLiving entity)
        {
            SoulData data = new SoulData();
            data.mobID = EntityList.getEntityString(entity);
            data.mobName = entity.getName();
            NBTTagCompound compound = new NBTTagCompound();
            entity.writeEntityToNBT(compound);
            for (String key : FORBIDDEN_NBT)
                compound.removeTag(key);
            compound.setBoolean("PersistenceRequired", true); // Force persistence
            data.compound = compound;
            return data;
        }

        public String getMobName()
        {
            return mobName;
        }

        public String getMobID()
        {
            return mobID;
        }

        public int getTier()
        {
            return tier;
        }

        public int getKilled()
        {
            return killed;
        }

        public NBTTagCompound getCompound()
        {
            return compound;
        }
    }
}
