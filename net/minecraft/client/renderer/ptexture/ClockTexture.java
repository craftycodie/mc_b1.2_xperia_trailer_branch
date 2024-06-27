package net.minecraft.client.renderer.ptexture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;

public class ClockTexture extends DynamicTexture {
   private Minecraft mc;
   private int[] raw = new int[256];
   private int[] dialRaw = new int[256];
   private double rot;
   private double rota;

   public ClockTexture(Minecraft mc) {
      super(Item.clock.getIcon((ItemInstance)null));
      this.mc = mc;
      this.textureId = 1;

      try {
         BufferedImage bi = ImageIO.read(Minecraft.class.getResource("/gui/items.png"));
         int xo = this.tex % 16 * 16;
         int yo = this.tex / 16 * 16;
         bi.getRGB(xo, yo, 16, 16, this.raw, 0, 16);
         bi = ImageIO.read(Minecraft.class.getResource("/misc/dial.png"));
         bi.getRGB(0, 0, 16, 16, this.dialRaw, 0, 16);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void tick() {
      double rott = 0.0D;
      if (this.mc.level != null && this.mc.player != null) {
         float time = this.mc.level.getTimeOfDay(1.0F);
         rott = (double)(-time * 3.1415927F * 2.0F);
         if (this.mc.level.dimension.foggy) {
            rott = Math.random() * 3.1415927410125732D * 2.0D;
         }
      }

      double rotd;
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
      double sin = Math.sin(this.rot);
      double cos = Math.cos(this.rot);

      for(int i = 0; i < 256; ++i) {
         int a = this.raw[i] >> 24 & 255;
         int r = this.raw[i] >> 16 & 255;
         int g = this.raw[i] >> 8 & 255;
         int b = this.raw[i] >> 0 & 255;
         if (r == b && g == 0 && b > 0) {
            double xo = -((double)(i % 16) / 15.0D - 0.5D);
            double yo = (double)(i / 16) / 15.0D - 0.5D;
            int br = r;
            int x = (int)((xo * cos + yo * sin + 0.5D) * 16.0D);
            int y = (int)((yo * cos - xo * sin + 0.5D) * 16.0D);
            int j = (x & 15) + (y & 15) * 16;
            a = this.dialRaw[j] >> 24 & 255;
            r = (this.dialRaw[j] >> 16 & 255) * r / 255;
            g = (this.dialRaw[j] >> 8 & 255) * br / 255;
            b = (this.dialRaw[j] >> 0 & 255) * br / 255;
         }

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

   }
}
