package eu.thog.twistedsouls;

import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoulSpawnerPolicy
{
    private BlockPos spawnerPos;
    private World world;
    private ItemShard.SoulData data;
    private int delay;
    private int mobCount;
    private int spawnDelay;
    private int spawnRange;
    private int maxNearbyEntities;
    @SideOnly(value = Side.CLIENT)
    private Entity renderEntity;
    @SideOnly(value = Side.CLIENT)
    private double prevMobRotation;
    @SideOnly(value = Side.CLIENT)
    private double mobRotation;


    protected SoulSpawnerPolicy(World world, BlockPos pos)
    {
        this.world = world;
        this.spawnerPos = pos;
        this.spawnRange = 4; // Vanilla value
        this.maxNearbyEntities = 6;
    }

    public static SoulSpawnerPolicy init(World world, BlockPos pos)
    {
        return new SoulSpawnerPolicy(world, pos);
    }

    public BlockPos getSpawnerPosition()
    {
        return spawnerPos;
    }

    public boolean isActivated()
    {
        if (data == null)
            return false;
        int tier = data.getTier();
        if (tier < 5)
        {
            BlockPos blockpos = this.getSpawnerPosition();
            return this.world.isAnyPlayerWithinRangeAt((double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D, 16);
        }
        return true;
    }

    public void setDelay(int delay)
    {
        this.delay = (delay * 20);
    }


    private void updateData()
    {
        int t = this.data.getTier();
        if (t == 1)
        {
            this.mobCount = 2;
            setDelay(20);
        } else if (t < 4)
        {
            this.mobCount = 4;
            setDelay(t == 2 ? 10 : 5);
        } else
        {
            this.mobCount = 6;
            setDelay(t == 4 ? 5 : 2);
        }
    }

    public void update()
    {
        if (this.isActivated())
        {
            BlockPos blockpos = this.getSpawnerPosition();

            if (world.isRemote)
            {
                double d3 = (double) ((float) blockpos.getX() + world.rand.nextFloat());
                double d4 = (double) ((float) blockpos.getY() + world.rand.nextFloat());
                double d5 = (double) ((float) blockpos.getZ() + world.rand.nextFloat());
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;
            } else
            {
                if (this.spawnDelay == -1)
                {
                    this.spawnDelay = delay;
                }

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.mobCount; ++i)
                {
                    Entity entity = EntityList.createEntityByName(data.getMobID(), this.world);

                    if (entity == null)
                    {
                        return;
                    }

                    int j = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(), (double) (blockpos.getX() + 1), (double) (blockpos.getY() + 1), (double) (blockpos.getZ() + 1))).expand((double) this.spawnRange, (double) this.spawnRange, (double) this.spawnRange)).size();

                    if (this.data.getTier() < 5 && j >= this.maxNearbyEntities)
                    {
                        this.spawnDelay = delay;
                        return;
                    }

                    double d0 = (double) blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) this.spawnRange + 0.5D;
                    double d1 = (double) (blockpos.getY() + world.rand.nextInt(3) - 1);
                    double d2 = (double) blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) this.spawnRange + 0.5D;
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
                    entity.setLocationAndAngles(d0, d1, d2, world.rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.isNotColliding())
                    {
                        if (entity instanceof EntityLiving)
                        {
                            try
                            {
                                entityliving.readEntityFromNBT(this.data.getCompound());
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        entity.worldObj.spawnEntityInWorld(entity);
                        world.playAuxSFX(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
                {
                    this.spawnDelay = delay;
                }
            }
        }
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.data = ItemShard.SoulData.deserialize(compound);
        if (this.data == null && FMLCommonHandler.instance().getSide().isClient())
            this.renderEntity = null;
        if (data != null)
            updateData();
        this.spawnDelay = compound.getInteger("spawnDelay");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        if (data != null)
            data.writeNBT(compound);
        compound.setInteger("spawnDelay", spawnDelay);
    }

    public ItemShard.SoulData getSoulData()
    {
        return data;
    }

    public void setSoulData(ItemShard.SoulData data)
    {
        this.data = data;
        if (data != null)
            updateData();
        else
        {
            this.spawnDelay = -1;
        }
    }

    public ItemStack toShard()
    {
        if (data == null)
            return null;
        ItemStack stack = new ItemStack(TwistedSouls.Registry.SHARD, 1);
        stack.setTagCompound(data.serialize());
        stack.setItemDamage(stack.getMaxDamage() - data.getKilled());
        return stack;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public void setPos(BlockPos pos)
    {
        this.spawnerPos = pos;
    }

    @SideOnly(value = Side.CLIENT)
    public double getPrevMobRotation()
    {
        return prevMobRotation;
    }

    @SideOnly(value = Side.CLIENT)
    public double getMobRotation()
    {
        return mobRotation;
    }

    @SideOnly(value = Side.CLIENT)
    public Entity getRenderEntity()
    {
        if (renderEntity == null)
        {
            this.renderEntity = EntityList.createEntityByName(data.getMobID(), this.world);
            if (renderEntity != null)
                this.renderEntity.readFromNBT(data.getCompound());
        }
        return renderEntity;
    }
}
