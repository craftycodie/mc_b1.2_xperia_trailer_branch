package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tesselator;
import net.minecraft.world.level.Level;

public class SmokeParticle extends Particle {
   float oSize;

   public SmokeParticle(Level level, double x, double y, double z, double xa, double ya, double za) {
      this(level, x, y, z, xa, ya, za, 1.0F);
   }

   public SmokeParticle(Level level, double x, double y, double z, double xa, double ya, double za, float scale) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd *= 0.10000000149011612D;
      this.yd *= 0.10000000149011612D;
      this.zd *= 0.10000000149011612D;
      this.xd += xa;
      this.yd += ya;
      this.zd += za;
      this.rCol = this.gCol = this.bCol = (float)(Math.random() * 0.30000001192092896D);
      this.size *= 0.75F;
      this.size *= scale;
      this.oSize = this.size;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
      this.lifetime = (int)((float)this.lifetime * scale);
      this.noPhysics = false;
   }

   public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2) {
      float l = ((float)this.age + a) / (float)this.lifetime * 32.0F;
      if (l < 0.0F) {
         l = 0.0F;
      }

      if (l > 1.0F) {
         l = 1.0F;
      }

      this.size = this.oSize * l;
      super.render(t, a, xa, ya, za, xa2, za2);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      this.tex = 7 - this.age * 8 / this.lifetime;
      this.yd += 0.004D;
      this.move(this.xd, this.yd, this.zd);
      if (this.y == this.yo) {
         this.xd *= 1.1D;
         this.zd *= 1.1D;
      }

      this.xd *= 0.9599999785423279D;
      this.yd *= 0.9599999785423279D;
      this.zd *= 0.9599999785423279D;
      if (this.onGround) {
         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
      }

   }
}
