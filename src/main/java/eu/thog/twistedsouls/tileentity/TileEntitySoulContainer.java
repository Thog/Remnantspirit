package eu.thog.twistedsouls.tileentity;

import eu.thog.twistedsouls.data.SoulVisualizer;
import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntitySoulContainer extends TileEntitySyncable implements IShardInteract
{
    private SoulVisualizer visualizer;

    public TileEntitySoulContainer()
    {
        this.visualizer = new SoulVisualizer();
    }

    public SoulVisualizer getVisualizer()
    {
        return visualizer;
    }

    @Override
    public ItemShard.SoulData getSoulData()
    {
        return visualizer.getSoulData();
    }

    @Override
    public ItemStack removeShard()
    {
        if (this.visualizer.getSoulData() == null)
            return null;
        this.visualizer.setSoulData(null);
        this.markDirty();
        this.worldObj.markBlockForUpdate(pos);
        return null;
    }

    @Override
    public boolean applyShard(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return false;
        this.visualizer.setSoulData(ItemShard.SoulData.deserialize(stack));
        if (this.getSoulData() != null)
            this.getSoulData().setKilled(1);
        stack.setItemDamage(stack.getItemDamage() + 1);
        if (stack.getItemDamage() == stack.getMaxDamage())
            --stack.stackSize;
        this.markDirty();
        this.worldObj.markBlockForUpdate(pos);
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.visualizer.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        this.visualizer.writeToNBT(compound);
        super.writeToNBT(compound);
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public net.minecraft.util.AxisAlignedBB getRenderBoundingBox()
    {
        int offset = 256;
        return AxisAlignedBB.fromBounds(-offset, -offset, -offset, offset, offset, offset);
    }
}
