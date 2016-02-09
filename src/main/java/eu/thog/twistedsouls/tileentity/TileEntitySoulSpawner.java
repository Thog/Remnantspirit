package eu.thog.twistedsouls.tileentity;

import eu.thog.twistedsouls.SoulSpawnerPolicy;
import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class TileEntitySoulSpawner extends TileEntity implements ITickable
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
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.getNbtCompound());
        this.worldObj.markBlockForUpdate(pos);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.policy.readFromNBT(compound);
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        this.policy.writeToNBT(compound);
    }

    @Override
    public boolean func_183000_F()
    {
        return true;
    }
}