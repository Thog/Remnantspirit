package eu.thog.twistedsouls.client.renderer;

import eu.thog.twistedsouls.SoulSpawnerPolicy;
import eu.thog.twistedsouls.tileentity.TileEntitySoulSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;

public class TileEntitySoulSpawnerRenderer extends TileEntitySpecialRenderer<TileEntitySoulSpawner>
{
    @Override
    public void renderTileEntityAt(TileEntitySoulSpawner te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        renderMob(te.getPolicy(), x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderMob(SoulSpawnerPolicy policy, double x, double y, double z, float partialTicks)
    {
        if (policy.getSoulData() != null)
        {
            Entity entity = policy.getRenderEntity();
            float f = 0.4375F;
            GlStateManager.translate(0.0F, 0.4F, 0.0F);
            GlStateManager.rotate((float) (policy.getPrevMobRotation() + (policy.getMobRotation() - policy.getPrevMobRotation()) * (double) partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.4F, 0.0F);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks);
        }
    }
}
