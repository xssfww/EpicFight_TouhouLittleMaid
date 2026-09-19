package net.EFTLM.EF.Render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.EFTLM.EFTLM;
import net.EFTLM.EF.Item.MaidSkillBookItem;
import net.EFTLM.EF.Skill.MaidSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SkillBookRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ResourceLocation DEFAULT_ICON = ResourceLocation.fromNamespaceAndPath(EFTLM.MODID, "textures/item/skillbook.png");
    private static final float MIN_Z = 7.5f / 16f;
    private static final float MAX_Z = 8.5f / 16f;
    private static SkillBookRenderer INSTANCE;
    private final Map<ResourceLocation, List<float[]>> cache = new ConcurrentHashMap<>();
    public static SkillBookRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SkillBookRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }
        return INSTANCE;
    }
    private SkillBookRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
    }
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext ctx, @NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int light, int overlay) {
        MaidSkill skill = MaidSkillBookItem.getContainSkill(stack);
        ResourceLocation icon = (skill != null && skill.getItemIcon() != null) ? skill.getItemIcon() : DEFAULT_ICON;
        List<float[]> quads = cache.computeIfAbsent(icon, SkillBookRenderer::generateGeometry);
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(icon));
        PoseStack.Pose last = pose.last();
        Matrix4f mat = last.pose();
        Matrix3f nrm = last.normal();
        final int bright = 0xF000F0;
        for (float[] q : quads) {
            float nx = q[20], ny = q[21], nz = q[22];
            for (int i = 0; i < 4; i++) {
                vc.vertex(mat, q[i * 3], q[i * 3 + 1], q[i * 3 + 2])
                        .color(255, 255, 255, 255)
                        .uv(q[12 + i * 2], q[12 + i * 2 + 1])
                        .overlayCoords(overlay)
                        .uv2(bright)
                        .normal(nrm, nx, ny, nz)
                        .endVertex();
            }
        }
    }
    private static List<float[]> generateGeometry(ResourceLocation icon) {
        List<float[]> out = new ArrayList<>();
        NativeImage img = null;
        try {
            Resource res = Minecraft.getInstance()
                    .getResourceManager()
                    .getResourceOrThrow(icon);
            try (InputStream in = res.open()) {
                img = NativeImage.read(in);
            }
        } catch (Exception ignored) {
        }
        if (img == null) {
            addFace(out,
                    0, 0, MAX_Z, 1, 0, MAX_Z, 1, 1, MAX_Z, 0, 1, MAX_Z,
                    0, 1, 1, 1, 1, 0, 0, 0,
                    0, 0, 1);
            addFace(out,
                    1, 0, MIN_Z, 0, 0, MIN_Z, 0, 1, MIN_Z, 1, 1, MIN_Z,
                    0, 1, 1, 1, 1, 0, 0, 0,
                    0, 0, -1);
            return out;
        }
        int w = img.getWidth();
        int h = img.getHeight();
        boolean[] opaque = new boolean[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                opaque[y * w + x] = ((img.getPixelRGBA(x, y) >>> 24) & 0xFF) > 0;
            }
        }
        img.close();
        addFace(out,
                0, 0, MAX_Z, 1, 0, MAX_Z, 1, 1, MAX_Z, 0, 1, MAX_Z,
                0, 1, 1, 1, 1, 0, 0, 0,
                0, 0, 1);
        addFace(out,
                1, 0, MIN_Z, 0, 0, MIN_Z, 0, 1, MIN_Z, 1, 1, MIN_Z,
                0, 1, 1, 1, 1, 0, 0, 0,
                0, 0, -1);
        Map<Integer, int[]> spanMap = new HashMap<>();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (!opaque[y * w + x]) continue;
                if (isTransparent(opaque, w, h, x, y - 1))
                    addSpan(spanMap, 0, y, x);
                if (isTransparent(opaque, w, h, x, y + 1))
                    addSpan(spanMap, 1, y, x);
                if (isTransparent(opaque, w, h, x - 1, y))
                    addSpan(spanMap, 2, x, y);
                if (isTransparent(opaque, w, h, x + 1, y))
                    addSpan(spanMap, 3, x, y);
            }
        }
        for (Map.Entry<Integer, int[]> e : spanMap.entrySet()) {
            int key = e.getKey();
            int facing = key / 10000;
            int anchor = key % 10000;
            int min = e.getValue()[0];
            int max = e.getValue()[1];
            buildSide(out, facing, anchor, min, max, w, h);
        }
        return out;
    }
    private static boolean isTransparent(boolean[] opaque, int w, int h, int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h) return true;
        return !opaque[y * w + x];
    }
    private static void addSpan(Map<Integer, int[]> map, int facing, int anchor, int pos) {
        int key = facing * 10000 + anchor;
        int[] span = map.get(key);
        if (span == null) {
            map.put(key, new int[]{pos, pos});
        } else {
            if (pos < span[0]) span[0] = pos;
            if (pos > span[1]) span[1] = pos;
        }
    }
    private static void buildSide(List<float[]> out, int facing, int anchor, int min, int max, int w, int h) {
        final float pw = 1f / w;
        final float ph = 1f / h;
        final float zMin = 7.5f / 16f;
        final float zMax = 8.5f / 16f;
        switch (facing) {
            case 0: {
                float x0 = min * pw;
                float x1 = (max + 1) * pw;
                float y = 1f - anchor * ph;
                float u0 = min * pw, u1 = (max + 1) * pw;
                float v0 = anchor * ph, v1 = (anchor + 1) * ph;
                addFace(out,
                        x0, y, zMin, x0, y, zMax, x1, y, zMax, x1, y, zMin,
                        u0, v0, u0, v1, u1, v1, u1, v0,
                        0, 1, 0);
                break;
            }
            case 1: {
                float x0 = min * pw;
                float x1 = (max + 1) * pw;
                float y = 1f - (anchor + 1) * ph;
                float u0 = min * pw, u1 = (max + 1) * pw;
                float v0 = anchor * ph, v1 = (anchor + 1) * ph;
                addFace(out,
                        x0, y, zMax, x0, y, zMin, x1, y, zMin, x1, y, zMax,
                        u0, v0, u0, v1, u1, v1, u1, v0,
                        0, -1, 0);
                break;
            }
            case 2: {
                float x = anchor * pw;
                float yTop = 1f - min * ph;
                float yBot = 1f - (max + 1) * ph;
                float u0 = anchor * pw, u1 = (anchor + 1) * pw;
                float v0 = min * ph, v1 = (max + 1) * ph;
                addFace(out,
                        x, yTop, zMax, x, yBot, zMax, x, yBot, zMin, x, yTop, zMin,
                        u0, v1, u0, v0, u1, v0, u1, v1,
                        1, 0, 0);
                break;
            }
            case 3: {
                float x = (anchor + 1) * pw;
                float yTop = 1f - min * ph;
                float yBot = 1f - (max + 1) * ph;
                float u0 = anchor * pw, u1 = (anchor + 1) * pw;
                float v0 = min * ph, v1 = (max + 1) * ph;
                addFace(out,
                        x, yTop, zMin, x, yBot, zMin, x, yBot, zMax, x, yTop, zMax,
                        u0, v1, u0, v0, u1, v0, u1, v1,
                        -1, 0, 0);
                break;
            }
        }
    }
    private static void addFace(List<float[]> out, float x0, float y0, float z0, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float u0, float v0, float u1, float v1, float u2, float v2, float u3, float v3, float nx, float ny, float nz) {
        out.add(new float[]{
                x0, y0, z0, x1, y1, z1, x2, y2, z2, x3, y3, z3,
                u0, v0, u1, v1, u2, v2, u3, v3,
                nx, ny, nz
        });
    }
}