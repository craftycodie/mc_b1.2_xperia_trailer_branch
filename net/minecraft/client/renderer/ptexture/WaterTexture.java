package net.minecraft.client.renderer.ptexture;

import net.minecraft.world.level.tile.Tile;

public class WaterTexture extends DynamicTexture {
   protected float[] current = new float[256];
   protected float[] next = new float[256];
   protected float[] heat = new float[256];
   protected float[] heata = new float[256];
   private int tickCount = 0;

   public WaterTexture() {
      super(Tile.water.tex);
   }

   public void tick() {
      ++this.tickCount;

      int x;
      int i;
      float pow;
      int r;
      int g;
      for(x = 0; x < 16; ++x) {
         for(i = 0; i < 16; ++i) {
            pow = 0.0F;

            for(int xx = x - 1; xx <= x + 1; ++xx) {
               r = xx & 15;
               g = i & 15;
               pow += this.current[r + g * 16];
            }

            this.next[x + i * 16] = pow / 3.3F + this.heat[x + i * 16] * 0.8F;
         }
      }

      for(x = 0; x < 16; ++x) {
         for(i = 0; i < 16; ++i) {
            float[] var10000 = this.heat;
            var10000[x + i * 16] += this.heata[x + i * 16] * 0.05F;
            if (this.heat[x + i * 16] < 0.0F) {
               this.heat[x + i * 16] = 0.0F;
            }

            var10000 = this.heata;
            var10000[x + i * 16] -= 0.1F;
            if (Math.random() < 0.05D) {
               this.heata[x + i * 16] = 0.5F;
            }
         }
      }

      float[] tmp = this.next;
      this.next = this.current;
      this.current = tmp;

      for(i = 0; i < 256; ++i) {
         pow = this.current[i];
         if (pow > 1.0F) {
            pow = 1.0F;
         }

         if (pow < 0.0F) {
            pow = 0.0F;
         }

         float pp = pow * pow;
         r = (int)(32.0F + pp * 32.0F);
         g = (int)(50.0F + pp * 64.0F);
         int b = 255;
         int a = (int)(146.0F + pp * 50.0F);
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
