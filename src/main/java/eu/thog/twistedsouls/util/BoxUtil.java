package eu.thog.twistedsouls.util;

import net.minecraft.util.AxisAlignedBB;

/**
 * Created by Thog
 * 09/02/2016
 */
public class BoxUtil
{
    public static final AxisAlignedBB AABB_ZERO = AxisAlignedBB.fromBounds(0, 0, 0, 0, 0, 0);

    public static boolean equals(AxisAlignedBB a, AxisAlignedBB b)
    {
        return a.maxX == b.maxX && a.maxY == b.maxY && a.maxZ == b.maxZ && a.minX == b.minX && a.minY == b.minY && a.minZ == a.minZ;
    }
}
