package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tesselator;
import net.minecraft.world.level.Level;

public class FlameParticle extends Particle {
   private float oSize;

   public FlameParticle(Level level, double x, double y, double z, double xd, double yd, double zd) {
      super(level, x, y, z, xd, yd, zd);
      this.xd = this.xd * 0.009999999776482582D + xd;
      this.yd = this.yd * 0.009999999776482582D + yd;
      this.zd = this.zd * 0.009999999776482582D + zd;
      double var10000 = x + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
      var10000 = y + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
      var10000 = z + (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
      this.oSize = this.size;
      this.rCol = this.gCol = this.bCol = 1.0F;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
      this.noPhysics = true;
      this.tex = 48;
   }

   public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2) {
      float s = ((float)this.age + a) / (float)this.lifetime;
      this.size = this.oSize * (1.0F - s * s * 0.5F);
      super.render(t, a, xa, ya, za, xa2, za2);
   }

   public float getBrightness(float a) {
      float l = ((float)this.age + a) / (float)this.lifetime;
      if (l < 0.0F) {
         l = 0.0F;
      }

      if (l > 1.0F) {
         l = 1.0F;
      }

      float br = super.getBrightness(a);
      return br * l + (1.0F - l);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.9599999785423279D;
      this.yd *= 0.9599999785423279D;
      this.zd *= 0.9599999785423279D;
      if (this.onGround) {
         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
      }

   }
}
