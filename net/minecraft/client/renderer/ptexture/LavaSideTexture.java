package net.minecraft.client.renderer.ptexture;

import net.minecraft.world.level.tile.Tile;
import util.Mth;

public class LavaSideTexture extends DynamicTexture {
   protected float[] current = new float[256];
   protected float[] next = new float[256];
   protected float[] heat = new float[256];
   protected float[] heata = new float[256];
   int tickCount = 0;

   public LavaSideTexture() {
      super(Tile.lava.tex + 1);
      this.replicate = 2;
   }

   public void tick() {
      ++this.tickCount;

      int i;
      float pow;
      int r;
      int g;
      int b;
      int rr;
      int gg;
      for(int x = 0; x < 16; ++x) {
         for(i = 0; i < 16; ++i) {
            pow = 0.0F;
            int xxo = (int)(Mth.sin((float)i * 3.1415927F * 2.0F / 16.0F) * 1.2F);
            r = (int)(Mth.sin((float)x * 3.1415927F * 2.0F / 16.0F) * 1.2F);

            for(g = x - 1; g <= x + 1; ++g) {
               for(b = i - 1; b <= i + 1; ++b) {
                  rr = g + xxo & 15;
                  gg = b + r & 15;
                  pow += this.current[rr + gg * 16];
               }
            }

            this.next[x + i * 16] = pow / 10.0F + (this.heat[(x + 0 & 15) + (i + 0 & 15) * 16] + this.heat[(x + 1 & 15) + (i + 0 & 15) * 16] + this.heat[(x + 1 & 15) + (i + 1 & 15) * 16] + this.heat[(x + 0 & 15) + (i + 1 & 15) * 16]) / 4.0F * 0.8F;
            float[] var10000 = this.heat;
            var10000[x + i * 16] += this.heata[x + i * 16] * 0.01F;
            if (this.heat[x + i * 16] < 0.0F) {
               this.heat[x + i * 16] = 0.0F;
            }

            var10000 = this.heata;
            var10000[x + i * 16] -= 0.06F;
            if (Math.random() < 0.005D) {
               this.heata[x + i * 16] = 1.5F;
            }
         }
      }

      float[] tmp = this.next;
      this.next = this.current;
      this.current = tmp;

      for(i = 0; i < 256; ++i) {
         pow = this.current[i - this.tickCount / 3 * 16 & 255] * 2.0F;
         if (pow > 1.0F) {
            pow = 1.0F;
         }

         if (pow < 0.0F) {
            pow = 0.0F;
         }

         r = (int)(pow * 100.0F + 155.0F);
         g = (int)(pow * pow * 255.0F);
         b = (int)(pow * pow * pow * pow * 128.0F);
         if (this.anaglyph3d) {
            rr = (r * 30 + g * 59 + b * 11) / 100;
            gg = (r * 30 + g * 70) / 100;
            int bb = (r * 30 + b * 70) / 100;
            r = rr;
            g = gg;
            b = bb;
         }

         this.pixels[i * 4 + 0] = (byte)r;
         this.pixels[i * 4 + 1] = (byte)g;
         this.pixels[i * 4 + 2] = (byte)b;
         this.pixels[i * 4 + 3] = -1;
      }

   }
}
