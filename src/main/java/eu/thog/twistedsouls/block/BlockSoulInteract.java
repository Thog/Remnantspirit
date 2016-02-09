package eu.thog.twistedsouls.block;

import eu.thog.twistedsouls.TwistedSouls;
import eu.thog.twistedsouls.tileentity.IShardInteract;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Thog
 * 09/02/2016
 */
public class BlockSoulInteract extends Block
{
    public BlockSoulInteract(Material material)
    {
        super(material);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            ItemStack stack = playerIn.getHeldItem();
            TileEntity tile = worldIn.getTileEntity(pos);
            if (stack != null && tile != null && tile instanceof IShardInteract && stack.getItem() == TwistedSouls.Registry.SHARD && ((IShardInteract) tile).getSoulData() == null)
                return ((IShardInteract) tile).applyShard(stack);
            if (stack == null && tile != null && tile instanceof IShardInteract && ((IShardInteract) tile).getSoulData() != null)
            {
                ItemStack result = ((IShardInteract) tile).removeShard();
                if (result != null)
                    playerIn.inventory.addItemStackToInventory(result);
                return true;
            }
            return false;
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!worldIn.isRemote && tile != null && tile instanceof IShardInteract && ((IShardInteract) tile).getSoulData() != null)
        {
            ItemStack result = ((IShardInteract) tile).removeShard();
            if (result != null)
            {
                EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), result);
                worldIn.spawnEntityInWorld(item);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
}
