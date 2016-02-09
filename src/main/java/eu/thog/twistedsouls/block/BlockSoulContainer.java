package eu.thog.twistedsouls.block;

import eu.thog.twistedsouls.tileentity.TileEntitySoulContainer;
import eu.thog.twistedsouls.util.BoxUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSoulContainer extends BlockSoulInteract implements ITileEntityProvider
{
    public BlockSoulContainer()
    {
        super(Material.rock);
        this.setLightOpacity(0);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySoulContainer();
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }


    @Override
    @SideOnly(value = Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.getBox(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return this.getBox(worldIn, pos);
    }

    public AxisAlignedBB getBox(World worldIn, BlockPos pos)
    {
        AxisAlignedBB axisAlignedBB = super.getCollisionBoundingBox(worldIn, pos, null);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntitySoulContainer)
        {
            Entity entity = ((TileEntitySoulContainer) tileEntity).getVisualizer().getEntity(worldIn);
            if (entity != null)
            {
                AxisAlignedBB box = entity.getEntityBoundingBox();
                if (box != null && !BoxUtil.equals(box, BoxUtil.AABB_ZERO))
                {
                    return box;
                }
            }

        }
        return axisAlignedBB;
    }
}
