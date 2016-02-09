package eu.thog.twistedsouls.client.renderer;

import eu.thog.twistedsouls.tileentity.TileEntitySoulContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by Thog
 * 09/02/2016
 */
public class TileEntitySoulContainerRenderer extends TileEntitySpecialRenderer<TileEntitySoulContainer>
{
    @Override
    public void renderTileEntityAt(TileEntitySoulContainer te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        renderMob(te, x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }

    private void renderMob(TileEntitySoulContainer te, double x, double y, double z, float partialTicks)
    {
        if (te.getVisualizer().getSoulData() != null)
        {
            this.enableGhostView();
            Entity entity = te.getVisualizer().getEntity(te.getWorld());
            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            GlStateManager.scale(0.75, 0.75, 0.75);
            Minecraft.getMinecraft().getRenderManager().doRenderEntity(entity, 0.0D, 0.0D, entity.rotationYaw, entity.rotationPitch, partialTicks, true);
            this.disableGhostView();
        }
    }

    private void disableGhostView()
    {
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
    }

    private void enableGhostView()
    {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.10F);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569F);
    }
}
