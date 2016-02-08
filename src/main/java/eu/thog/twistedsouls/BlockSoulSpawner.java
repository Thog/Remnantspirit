package eu.thog.twistedsouls;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoulSpawner extends Block implements ITileEntityProvider
{
    protected BlockSoulSpawner()
    {
        super(Material.rock);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            ItemStack stack = playerIn.getHeldItem();
            TileEntity tile = worldIn.getTileEntity(pos);
            if (stack != null && tile != null && tile instanceof TileEntitySoulSpawner && stack.getItem() == RemnantSpiritRegistry.SHARD && ((TileEntitySoulSpawner) tile).getSoulData() == null)
                return ((TileEntitySoulSpawner) tile).applyShard(stack);
            if (stack == null && tile != null && tile instanceof TileEntitySoulSpawner && ((TileEntitySoulSpawner) tile).getSoulData() != null)
            {
                playerIn.inventory.addItemStackToInventory(((TileEntitySoulSpawner) tile).removeShard());
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
        if (!worldIn.isRemote && tile != null && tile instanceof TileEntitySoulSpawner && ((TileEntitySoulSpawner) tile).getSoulData() != null)
        {
            EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileEntitySoulSpawner) tile).removeShard());
            worldIn.spawnEntityInWorld(item);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySoulSpawner();
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }
}
