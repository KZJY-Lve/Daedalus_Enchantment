package com.kzjy.daedalus.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.Collections;
import java.util.function.Function;

public class DaedalusModelLoader implements IGeometryLoader<DaedalusModelLoader.DaedalusCosmicGeometry> {

    @Override
    public DaedalusCosmicGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
        // 1. 读取我们的自定义配置 (mask 路径)
        JsonObject cosmicObj = jsonObject.getAsJsonObject("daedalus_cosmic");
        if (cosmicObj == null) throw new IllegalStateException("Missing 'daedalus_cosmic' object.");

        String maskPath = cosmicObj.get("mask").getAsString();

        // 2. 复制并清洗 JSON，交给 BlockModel 处理基础部分
        JsonObject clean = jsonObject.deepCopy();
        clean.remove("daedalus_cosmic");
        clean.remove("loader");

        BlockModel baseModel = context.deserialize(clean, BlockModel.class);

        return new DaedalusCosmicGeometry(baseModel, new ResourceLocation(maskPath));
    }

    public static class DaedalusCosmicGeometry implements IUnbakedGeometry<DaedalusCosmicGeometry> {
        private final BlockModel baseModel;
        private final ResourceLocation maskLocation;

        public DaedalusCosmicGeometry(BlockModel baseModel, ResourceLocation maskLocation) {
            this.baseModel = baseModel;
            this.maskLocation = maskLocation;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            // 4. 调用 baseModel.bake 生成基础物品模型 (处理 layer0 等)
            BakedModel bakedBaseModel = this.baseModel.bake(baker, this.baseModel, spriteGetter, modelState, modelLocation, true);

            // 5. 包裹为星空模型
            return new DaedalusCosmicBakedModel(bakedBaseModel, Collections.singletonList(maskLocation));
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            this.baseModel.resolveParents(modelGetter);
        }
    }
}
