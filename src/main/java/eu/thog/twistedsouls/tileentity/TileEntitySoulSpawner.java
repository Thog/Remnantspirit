package eu.thog.twistedsouls.tileentity;

import eu.thog.twistedsouls.data.SoulSpawnerPolicy;
import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntitySoulSpawner extends TileEntitySyncable implements ITickable, IShardInteract
{
    private final SoulSpawnerPolicy policy;

    public TileEntitySoulSpawner()
    {
        this.policy = SoulSpawnerPolicy.init(worldObj, this.getPos());
    }

    @Override
    public void setWorldObj(World worldIn)
    {
        this.policy.setWorld(worldIn);
        super.setWorldObj(worldIn);
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        this.policy.setPos(posIn);
        super.setPos(posIn);
    }

    public ItemShard.SoulData getSoulData()
    {
        return this.policy.getSoulData();
    }

    public boolean applyShard(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return false;
        this.policy.setSoulData(ItemShard.SoulData.deserialize(stack));
        stack.stackSize--;
        this.markDirty();
        this.worldObj.markBlockForUpdate(pos);
        return true;
    }

    public ItemStack removeShard()
    {
        if (this.policy.getSoulData() == null)
            return null;
        ItemStack stack = this.policy.toShard();
        this.policy.setSoulData(null);
        this.markDirty();
        this.worldObj.markBlockForUpdate(pos);
        return stack;
    }

    public SoulSpawnerPolicy getPolicy()
    {
        return policy;
    }

    @Override
    public void update()
    {
        this.policy.update();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        this.policy.readFromNBT(compound);
        super.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        this.policy.writeToNBT(compound);
        super.writeToNBT(compound);
    }

    @Override
    public boolean func_183000_F()
    {
        return true;
    }
}