package fr.caranouga.expeditech.common.entities.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import fr.caranouga.expeditech.common.entities.TechLevelExperienceOrb;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

@OnlyIn(Dist.CLIENT)
public class TechLevelExperienceOrbRenderer extends EntityRenderer<TechLevelExperienceOrb> {
    private static final ResourceLocation TEXTURE = modLocation("textures/entity/tech_level_xp_orb.png");
    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(TEXTURE);

    public TechLevelExperienceOrbRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        this.shadowRadius = 0.15F;
        this.shadowStrength = 0.75F;
    }

    protected int getBlockLightLevel(TechLevelExperienceOrb pEntity, BlockPos pPos) {
        return MathHelper.clamp(super.getBlockLightLevel(pEntity, pPos) + 7, 0, 15);
    }

    public void render(TechLevelExperienceOrb pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();

        int iconIdx = pEntity.getIcon();
        float textureU = (float)(iconIdx % 4 * 16 + 0) / 64.0F;
        float textureU2 = (float)(iconIdx % 4 * 16 + 16) / 64.0F;
        float textureV = (float)(iconIdx / 4 * 16 + 0) / 64.0F;
        float textureV2 = (float)(iconIdx / 4 * 16 + 16) / 64.0F;

        float f8 = ((float)pEntity.tickCount + pPartialTicks) / 2.0F;
        float pulse = (MathHelper.sin(f8) + 1.0F) * 0.5F; // [0;1]

        int baseRed = 200;
        int baseGreen = 80;
        int baseBlue = 0;

        int redValue    = (int)(baseRed     + pulse * 55);
        int greenValue  = (int)(baseGreen   + pulse * 40);
        int blueValue   = (int)(baseBlue    + pulse * 20);

        pMatrixStack.translate(0.0D, (double)0.1F, 0.0D);
        pMatrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        pMatrixStack.scale(0.3F, 0.3F, 0.3F);

        IVertexBuilder vertexBuilder = pBuffer.getBuffer(RENDER_TYPE);

        MatrixStack.Entry entry = pMatrixStack.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        vertex(vertexBuilder, matrix4f, matrix3f, -0.5F, -0.25F, redValue, greenValue, blueValue, textureU, textureV2, pPackedLight);
        vertex(vertexBuilder, matrix4f, matrix3f, 0.5F, -0.25F, redValue, greenValue, blueValue, textureU2, textureV2, pPackedLight);
        vertex(vertexBuilder, matrix4f, matrix3f, 0.5F, 0.75F, redValue, greenValue, blueValue, textureU2, textureV, pPackedLight);
        vertex(vertexBuilder, matrix4f, matrix3f, -0.5F, 0.75F, redValue, greenValue, blueValue, textureU, textureV, pPackedLight);

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    private static void vertex(IVertexBuilder pBuffer, Matrix4f pMatrix, Matrix3f pMatrixNormal, float pX, float pY, int pRed, int pGreen, int pBlue, float pTexU, float pTexV, int pPackedLight) {
        pBuffer.vertex(pMatrix, pX, pY, 0.0F)
                .color(pRed, pGreen, pBlue, 128)
                .uv(pTexU, pTexV)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(pPackedLight)
                .normal(pMatrixNormal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    public ResourceLocation getTextureLocation(TechLevelExperienceOrb pEntity) {
        return TEXTURE;
    }
}
