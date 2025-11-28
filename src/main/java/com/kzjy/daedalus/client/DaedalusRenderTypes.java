package com.kzjy.daedalus.client;

import com.kzjy.daedalus.Daedalus;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Kzjy<br>
 * 自定义渲染类型与着色器注册<br>
 * 处理星空特效 Shader 的加载与 RenderType 定义
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Daedalus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DaedalusRenderTypes extends RenderType {

    public DaedalusRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    // 保留星空 Shader 变量
    public static ShaderInstance cosmicShader;

    /**
     * 注册着色器事件<br>
     * 加载 daedalus_cosmic 着色器
     */
    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        try {
            // 保留星空 Shader 注册
            event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(Daedalus.MODID, "daedalus_cosmic"), DefaultVertexFormat.NEW_ENTITY),
                    shaderInstance -> cosmicShader = shaderInstance);
        } catch (Exception e) {
            Daedalus.LOGGER.error("Failed to register Daedalus Cosmic shader", e);
        }

        // [删除] 移除 daedalus_tooltip 的注册代码块
    }

    // 保留星空 RenderType
    public static final RenderType DAEDALUS_COSMIC = create("daedalus_cosmic",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> cosmicShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("textures/atlas/blocks.png"), false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true));

    // [删除] 移除 TOOLTIP_BACKGROUND RenderType
}
