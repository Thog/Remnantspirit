package eu.thog.twistedsouls.data;

import eu.thog.twistedsouls.item.ItemShard;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class that provide entity rendering for {@link ItemShard.SoulData}
 *
 * @author Thog
 */
public class SoulVisualizer extends SoulSerializer
{
    protected Entity entity;
    @SideOnly(value = Side.CLIENT)
    protected double prevMobRotation;
    @SideOnly(value = Side.CLIENT)
    protected double mobRotation;

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

    public Entity getEntity(World world)
    {
        ItemShard.SoulData soulData = getSoulData();
        if (entity == null && soulData != null)
        {
            this.entity = EntityList.createEntityByName(soulData.getMobID(), world);
            if (entity != null)
                this.entity.readFromNBT(soulData.getCompound());
        }
        return entity;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (getSoulData() == null)
            this.entity = null;
    }
}
