package me.timeswitcher.lupin.utility;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import me.timeswitcher.lupin.main.Lupin;
import me.timeswitcher.lupin.main.LupinUser;
import me.timeswitcher.lupin.mods.Nametags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class RenderUtil {

	private static final Color blueCyan = Color.CYAN;
	private static final Color pinkPurple = new Color(255, 140, 245);

	private static boolean whiteEffectUp = true;
	private static boolean whiteEffectDown = false;

	private static int whiteEffectCount = 0;

	public static int getWhiteFadeEffect() {
		if (whiteEffectCount <= 20) {

			whiteEffectCount = 20;

			whiteEffectDown = false;
			whiteEffectUp = true;
		}
		if (whiteEffectCount >= 765) {

			whiteEffectCount = 765;

			whiteEffectUp = false;
			whiteEffectDown = true;
		}
		if (whiteEffectUp) {

			whiteEffectCount++;

		} else if (whiteEffectDown) {

			whiteEffectCount--;
		}
		return new Color(whiteEffectCount / 3, whiteEffectCount / 3, whiteEffectCount / 3, whiteEffectCount / 3).getRGB();
	}

	public static int getFade(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return new Color(hue, 0.4f, 1.0f).getRGB();
	}

	public static Color mixColors(Color color1, Color color2, double percent){
		double inverse_percent = 1.0 - percent;
		int redPart = (int) (color1.getRed()*percent + color2.getRed()*inverse_percent);
		int greenPart = (int) (color1.getGreen()*percent + color2.getGreen()*inverse_percent);
		int bluePart = (int) (color1.getBlue()*percent + color2.getBlue()*inverse_percent);
		return new Color(redPart, greenPart, bluePart);
	}

	public static int getRainbowLight(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 0.4f, 1.0f).getRGB();
	}

	public static void drawRect(double left, double top, double right, double bottom, float red, float green, float blue, float alpha) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.color4f(red, green, blue, alpha);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(left, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, bottom, 0.0D).endVertex();
		vertexbuffer.pos(right, top, 0.0D).endVertex();
		vertexbuffer.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		RenderSystem.popMatrix();
	}

	public static void drawRect(double left, double top, double right, double bottom, int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.color4f(f, f1, f2, f3);
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
		vertexbuffer.pos((double) left, (double) bottom, 0.0D).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, 0.0D).endVertex();
		vertexbuffer.pos((double) right, (double) top, 0.0D).endVertex();
		vertexbuffer.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
		RenderSystem.popMatrix();
	}

	public static void drawShape(float cx, float cy, float r, int num_segments, int color) { 
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;

		float theta = (float) (2 * Math.PI / (float)(num_segments)); 
		float c = (float) Math.cos(theta);
		float s = (float) Math.sin(theta);
		float t;

		float x = r;
		float y = 0; 

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_LINE_LOOP); 
		GL11.glColor4f(f, f1, f2, f3);
		for(int i = 0; i < num_segments; i++) 
		{ 
			GL11.glVertex2f(x + cx, y + cy);

			t = x;
			x = c * x - s * y;
			y = s * t + c * y;
		} 
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	public static void drawEntityOnScreen(LivingEntity entity, int x, int y, int width, int height, boolean adjustRotation) {
		float f = 0;
		float f1 = 0;
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) (width) + x, (float) (height) + y, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float) 30, (float) 30, (float) 30);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		if (adjustRotation) {
			entity.renderYawOffset = 180.0F + f * 20.0F;
			entity.rotationYaw = 180.0F + f * 40.0F;
			entity.rotationPitch = -f1 * 20.0F;
			entity.rotationYawHead = entity.rotationYaw;
			entity.prevRotationYawHead = entity.rotationYaw;
		}
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		quaternion1.conjugate();
		entityrenderermanager.setCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		if (adjustRotation) {
			entity.renderYawOffset = f2;
			entity.rotationYaw = f3;
			entity.rotationPitch = f4;
			entity.prevRotationYawHead = f5;
			entity.rotationYawHead = f6;
		}
		RenderSystem.popMatrix();
	}

	public static void drawBox(BlockPos b, float red, float green, float blue, float alpha) {

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.disableLighting();
		RenderSystem.disableAlphaTest();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		RenderSystem.color4f(red, green, blue, alpha);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		// bottom
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ() + 1).endVertex();

		// top
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ() + 1).endVertex();

		// left
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ()).endVertex();
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ() + 1).endVertex();

		// right
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ() + 1).endVertex();

		// front
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ()).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ()).endVertex();
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ()).endVertex();

		// back
		bufferBuilder.pos(b.getX(), b.getY(), b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY(), b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX() + 1, b.getY() + 1, b.getZ() + 1).endVertex();
		bufferBuilder.pos(b.getX(), b.getY() + 1, b.getZ() + 1).endVertex();

		tessellator.draw();
		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}

	public static void drawOutlinedBox(AxisAlignedBB bb, float red, float green, float blue, float alpha) {

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.disableLighting();
		RenderSystem.disableAlphaTest();

		RenderSystem.color4f(red, green, blue, alpha);

		GL11.glBegin(GL11.GL_LINES);
		{
			GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
			GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

			GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
			GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);

			GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
			GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);

			GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
			GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);

			GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
			GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

			GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
			GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

			GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
			GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

			GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
			GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

			GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
			GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

			GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
			GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

			GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
			GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);

			GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
			GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		}
		GL11.glEnd();

		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}

	public static void drawESP(AxisAlignedBB bb, LivingEntity e, float red, float green, float blue, float alpha) {

		bb = new AxisAlignedBB(bb.minX - 0.15d, bb.minY - 0.15d, bb.minZ - 0.15d, bb.maxX + 0.15d, bb.maxY + 0.15d, bb.maxZ + 0.15d);

		RenderSystem.pushMatrix();
		RenderSystem.enableBlend();
		RenderSystem.disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.disableLighting();
		RenderSystem.disableAlphaTest();

		RenderSystem.color4f(red, green, blue, alpha);

		GL11.glLineWidth(1);

		GL11.glBegin(GL11.GL_LINES);

		//box

		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);

		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);

		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);

		//health

		float entityHealth = e.getHealth();
		float maxHealth = e.getMaxHealth();

		float hRed = 0;
		float hGreen = 0;
		float length = (float) (bb.maxY - bb.minY);
		float maxLength = (float) (bb.maxY - bb.minY);

		if (entityHealth == maxHealth) {
			hRed = 0;
			hGreen = 255;
			length = maxLength;

		} else {
			hRed = 255 - (255 * (entityHealth / maxHealth));
			hGreen = 255 * (entityHealth / maxHealth);
			length = maxLength * (entityHealth / maxHealth);
		}
		hRed= hRed / 255.0F;
		hGreen= hGreen / 255.0F;

		RenderSystem.color4f(hRed, hGreen, 0, alpha);

		GL11.glVertex3d(bb.maxX + 0.025d, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX + 0.025d, bb.minY + length, bb.minZ);

		GL11.glVertex3d(bb.maxX + 0.026d, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX + 0.026d, bb.minY + length, bb.minZ);

		GL11.glVertex3d(bb.maxX + 0.027d, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX + 0.027d, bb.minY + length, bb.minZ);

		GL11.glEnd();

		RenderSystem.enableCull();
		RenderSystem.enableDepthTest();
		RenderSystem.enableTexture();
		RenderSystem.enableLighting();
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.popMatrix();
	}

	public static void renderEntityNametag(Entity entityIn, String text, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		double d0 = Lupin.mc.getRenderManager().squareDistanceTo(entityIn);
		if (!(d0 > 4096.0D)) {
			boolean flag = true;
			float f = entityIn.getHeight() + 0.5F;
			int i = (text.contains("deadmau5") || text.contains(LupinUser.SPECIALIGN)) ? -10 : 0;
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, f, 0.0D);
			matrixStackIn.rotate(Lupin.mc.getRenderManager().getCameraOrientation());
			matrixStackIn.scale(-0.025F - (Nametags.SIZE.getReturnValue() / 100), -0.025F - (Nametags.SIZE.getReturnValue() / 100), 0.025F + (Nametags.SIZE.getReturnValue() / 100));
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int)(f1 * 255.0F) << 24;
			FontRenderer fontrenderer = Lupin.mc.fontRenderer;
			float f2 = (float)(-fontrenderer.getStringWidth(text) / 2);
			fontrenderer.renderString(text, f2, (float)i, -1, false, matrix4f, bufferIn, flag, j, packedLightIn);
            fontrenderer.renderString(text, f2, (float) i, -1, false, matrix4f, bufferIn, false, 0, packedLightIn);
            matrixStackIn.pop();
		}
	}

	public static void renderItemEntityNametag(Entity entityIn, String text, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		double d0 = Lupin.mc.getRenderManager().squareDistanceTo(entityIn);
		if (!(d0 > 4096.0D)) {
			boolean flag = true;
			float f = entityIn.getHeight() + 0.5F;
			int i = 0;
			matrixStackIn.push();
			matrixStackIn.translate(0.0D, (double)f, 0.0D);
			matrixStackIn.rotate(Lupin.mc.getRenderManager().getCameraOrientation());
			matrixStackIn.scale(-0.025F - (Nametags.SIZE.getReturnValue() / 100), -0.025F - (Nametags.SIZE.getReturnValue() / 100), 0.025F + (Nametags.SIZE.getReturnValue() / 100));
			Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
			float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
			int j = (int)(f1 * 255.0F) << 24;
			FontRenderer fontrenderer = Lupin.mc.fontRenderer;
			float f2 = (float)(-fontrenderer.getStringWidth(text) / 2);
			fontrenderer.renderString(text, f2, (float)i, -1, false, matrix4f, bufferIn, flag, j, packedLightIn);
			fontrenderer.renderString(text, f2, (float)i, -1, false, matrix4f, bufferIn, false, 0, packedLightIn);
			matrixStackIn.pop();
		}
	}

	public static void drawImage(ResourceLocation location, int x, int y, int width, int height, float alpha) {

		if (location != null) {

			RenderSystem.pushMatrix();
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

			Lupin.mc.getTextureManager().bindTexture(location);
			AbstractGui.blit(x, y, 0, 0, width, height, width, height);

			RenderSystem.popMatrix();
		}
	}

	public static Color getBlueCyan() {
		return blueCyan;
	}

	public static Color getPinkPurple() {
		return pinkPurple;
	}

}