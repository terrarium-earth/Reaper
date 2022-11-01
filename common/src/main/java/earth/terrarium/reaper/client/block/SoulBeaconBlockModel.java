package earth.terrarium.reaper.client.block;

import earth.terrarium.reaper.Reaper;
import earth.terrarium.reaper.common.blockentity.SoulBeaconBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SoulBeaconBlockModel extends AnimatedGeoModel<SoulBeaconBlockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reaper.MODID, "textures/block/soul_beacon.png");
    public static final ResourceLocation MODEL = new ResourceLocation(Reaper.MODID, "geo/soul_beacon.geo.json");
    public static final ResourceLocation ANIMATION = new ResourceLocation(Reaper.MODID, "animations/soul_beacon.animation.json");

    @Override
    public ResourceLocation getModelResource(SoulBeaconBlockEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SoulBeaconBlockEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SoulBeaconBlockEntity animatable) {
        return ANIMATION;
    }
}
