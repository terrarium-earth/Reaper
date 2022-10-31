package earth.terrarium.reaper.client.block;

import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.blockentity.ReaperGeneratorBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ReaperGeneratorBlockModel extends AnimatedGeoModel<ReaperGeneratorBlockEntity> {
    public static final ResourceLocation TEXTURE_ACTIVE = new ResourceLocation(Reaper.MOD_ID, "textures/block/active_reaper_generator.png");
    public static final ResourceLocation TEXTURE_INACTIVE = new ResourceLocation(Reaper.MOD_ID, "textures/block/inactive_reaper_generator.png");
    public static final ResourceLocation MODEL = new ResourceLocation(Reaper.MOD_ID, "geo/reaper_generator.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(Reaper.MOD_ID, "animations/reaper_generator.animation.json");

    @Override
    public ResourceLocation getModelResource(ReaperGeneratorBlockEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ReaperGeneratorBlockEntity object) {
        return TEXTURE_ACTIVE;
    }

    @Override
    public ResourceLocation getAnimationResource(ReaperGeneratorBlockEntity animatable) {
        return ANIMATION;
    }
}