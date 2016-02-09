package eu.thog.twistedsouls;

import eu.thog.twistedsouls.item.ItemShard;
import eu.thog.twistedsouls.tileentity.TileEntitySoulContainer;
import eu.thog.twistedsouls.tileentity.TileEntitySoulSpawner;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;

public class CommonProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
        for (Field field : TwistedSouls.Registry.class.getFields())
        {
            try
            {
                Object obj = field.get(null);
                if (obj instanceof Item)
                    registerItem((Item) obj, ((Item) obj).getUnlocalizedName().substring(5));
                else if (obj instanceof Block)
                    registerBlock((Block) obj, ((Block) obj).getUnlocalizedName().substring(5));
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        GameRegistry.registerTileEntity(TileEntitySoulSpawner.class, "TileEntitySoulSpawner");
        GameRegistry.registerTileEntity(TileEntitySoulContainer.class, "TileEntitySoulContainer");
    }

    protected void registerItem(Item item, String name)
    {
        GameRegistry.registerItem(item, name);
    }

    protected void registerBlock(Block block, String name)
    {
        GameRegistry.registerBlock(block, name);
    }

    public void init()
    {

    }


    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        DamageSource source = event.source;
        if (source instanceof EntityDamageSource)
        {
            if (source.getSourceOfDamage() instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
                Entity entity = event.entityLiving;

                if (entity instanceof EntityLiving)
                {
                    int i;
                    ItemStack emptyShard = null;
                    for (i = 0; i < 10; i++)
                    {
                        ItemStack stack = player.inventory.getStackInSlot(i);
                        if (stack != null)
                        {
                            if (stack.getItem() == TwistedSouls.Registry.SHARD && stack.getItemDamage() != 0)
                            {
                                NBTTagCompound compound = stack.getTagCompound();
                                if (compound != null)
                                {
                                    String mobName = compound.getString("mobName");
                                    String mobID = compound.getString("mobID");
                                    if (mobID.equals(EntityList.getEntityString(entity)) && mobName.equals(entity.getName()))
                                    {
                                        stack.setItemDamage(stack.getItemDamage() - 202);
                                        return;
                                    }
                                }
                            } else if (stack.getItem() == TwistedSouls.Registry.EMPTY_SHARD)
                            {
                                emptyShard = stack;
                            }
                        }
                    }
                    if (emptyShard != null)
                    {
                        ItemStack result = ItemShard.initShard(emptyShard, (EntityLiving) entity);
                        if (!player.inventory.addItemStackToInventory(result))
                        {
                            entity.worldObj.spawnEntityInWorld(new EntityItem(entity.worldObj, entity.posX + 0.5F, entity.posY + 0.5F, entity.posZ + 0.5, ItemShard.initShard(emptyShard, (EntityLiving) entity)));
                            System.out.println("LOL");
                        }
                        if (emptyShard.stackSize == 0)
                            player.inventory.removeStackFromSlot(i);
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public void onRepairUpdate(AnvilUpdateEvent event)
    {
        ItemStack right = event.right;
        ItemStack left = event.left;

        if (right.getItem().equals(TwistedSouls.Registry.SHARD) && left.getItem().equals(TwistedSouls.Registry.SHARD) && right.getItemDamage() != 0)
        {
            ItemStack output = right.copy();
            int rightKill = right.getMaxDamage() - right.getItemDamage();
            int leftKill = left.getMaxDamage() - left.getItemDamage();
            output.setItemDamage(output.getMaxDamage() - (rightKill + leftKill));
            event.output = output;
        }
    }

    @SubscribeEvent
    public void onRepairUpdate(AnvilRepairEvent event)
    {
        ItemStack right = event.right;
        ItemStack left = event.left;

        if (right.getItem().equals(TwistedSouls.Registry.SHARD) && left.getItem().equals(TwistedSouls.Registry.SHARD) && right.getItemDamage() != 0)
        {
            int rightKill = right.getMaxDamage() - right.getItemDamage();
            int leftKill = left.getMaxDamage() - left.getItemDamage();
            event.output.setTagCompound(right.getTagCompound());
            event.output.setItem(TwistedSouls.Registry.SHARD);
            event.output.setItemDamage(right.getMaxDamage() - (rightKill + leftKill));
        }
    }
}
