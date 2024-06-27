package net.minecraft.client.renderer.ptexture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;

public class CompassTexture extends DynamicTexture {
   private Minecraft mc;
   private int[] raw = new int[256];
   private double rot;
   private double rota;

   public CompassTexture(Minecraft mc) {
      super(Item.compass.getIcon((ItemInstance)null));
      this.mc = mc;
      this.textureId = 1;

      try {
         BufferedImage bi = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
         int xo = this.tex % 16 * 16;
         int yo = this.tex / 16 * 16;
         bi.getRGB(xo, yo, 16, 16, this.raw, 0, 16);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void tick() {
      for(int i = 0; i < 256; ++i) {
         int a = this.raw[i] >> 24 & 255;
         int r = this.raw[i] >> 16 & 255;
         int g = this.raw[i] >> 8 & 255;
         int b = this.raw[i] >> 0 & 255;
         if (this.anaglyph3d) {
            int rr = (r * 30 + g * 59 + b * 11) / 100;
            int gg = (r * 30 + g * 70) / 100;
            int bb = (r * 30 + b * 70) / 100;
            r = rr;
            g = gg;
            b = bb;
         }

         this.pixels[i * 4 + 0] = (byte)r;
         this.pixels[i * 4 + 1] = (byte)g;
         this.pixels[i * 4 + 2] = (byte)b;
         this.pixels[i * 4 + 3] = (byte)a;
      }

      double rott = 0.0D;
      double rotd;
      double sin;
      if (this.mc.level != null && this.mc.player != null) {
         rotd = (double)this.mc.level.xSpawn - this.mc.player.x;
         sin = (double)this.mc.level.zSpawn - this.mc.player.z;
         rott = (double)(this.mc.player.yRot - 90.0F) * 3.141592653589793D / 180.0D - Math.atan2(sin, rotd);
         if (this.mc.level.dimension.foggy) {
            rott = Math.random() * 3.1415927410125732D * 2.0D;
         }
      }

      for(rotd = rott - this.rot; rotd < -3.141592653589793D; rotd += 6.283185307179586D) {
      }

      while(rotd >= 3.141592653589793D) {
         rotd -= 6.283185307179586D;
      }

      if (rotd < -1.0D) {
         rotd = -1.0D;
      }

      if (rotd > 1.0D) {
         rotd = 1.0D;
      }

      this.rota += rotd * 0.1D;
      this.rota *= 0.8D;
      this.rot += this.rota;
      sin = Math.sin(this.rot);
      double cos = Math.cos(this.rot);

      int d;
      int x;
      int y;
      int i;
      int r;
      int g;
      int b;
      short a;
      int rr;
      int gg;
      int bb;
      for(d = -4; d <= 4; ++d) {
         x = (int)(8.5D + cos * (double)d * 0.3D);
         y = (int)(7.5D - sin * (double)d * 0.3D * 0.5D);
         i = y * 16 + x;
         r = 100;
         g = 100;
         b = 100;
         a = 255;
         if (this.anaglyph3d) {
            rr = (r * 30 + g * 59 + b * 11) / 100;
            gg = (r * 30 + g * 70) / 100;
            bb = (r * 30 + b * 70) / 100;
            r = rr;
            g = gg;
            b = bb;
         }

         this.pixels[i * 4 + 0] = (byte)r;
         this.pixels[i * 4 + 1] = (byte)g;
         this.pixels[i * 4 + 2] = (byte)b;
         this.pixels[i * 4 + 3] = (byte)a;
      }

      for(d = -8; d <= 16; ++d) {
         x = (int)(8.5D + sin * (double)d * 0.3D);
         y = (int)(7.5D + cos * (double)d * 0.3D * 0.5D);
         i = y * 16 + x;
         r = d >= 0 ? 255 : 100;
         g = d >= 0 ? 20 : 100;
         b = d >= 0 ? 20 : 100;
         a = 255;
         if (this.anaglyph3d) {
            rr = (r * 30 + g * 59 + b * 11) / 100;
            gg = (r * 30 + g * 70) / 100;
            bb = (r * 30 + b * 70) / 100;
            r = rr;
            g = gg;
            b = bb;
         }

         this.pixels[i * 4 + 0] = (byte)r;
         this.pixels[i * 4 + 1] = (byte)g;
         this.pixels[i * 4 + 2] = (byte)b;
         this.pixels[i * 4 + 3] = (byte)a;
      }

   }
}
