package net.minecraft.client.particle;

import net.minecraft.world.level.Level;

public class SplashParticle extends WaterDropParticle {
   public SplashParticle(Level level, double x, double y, double z, double xa, double ya, double za) {
      super(level, x, y, z);
      this.gravity = 0.04F;
      ++this.tex;
      if (ya == 0.0D && (xa != 0.0D || za != 0.0D)) {
         this.xd = xa;
         this.yd = ya + 0.1D;
         this.zd = za;
      }

   }
}
