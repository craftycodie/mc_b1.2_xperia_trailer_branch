package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tesselator;
import net.minecraft.world.level.Level;
import util.Mth;

public class NoteParticle extends Particle {
   float oSize;

   public NoteParticle(Level level, double x, double y, double z, double xa, double ya, double za) {
      this(level, x, y, z, xa, ya, za, 2.0F);
   }

   public NoteParticle(Level level, double x, double y, double z, double xa, double ya, double za, float scale) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd *= 0.009999999776482582D;
      this.yd *= 0.009999999776482582D;
      this.zd *= 0.009999999776482582D;
      this.yd += 0.2D;
      this.rCol = Mth.sin(((float)xa + 0.0F) * 3.1415927F * 2.0F) * 0.65F + 0.35F;
      this.gCol = Mth.sin(((float)xa + 0.33333334F) * 3.1415927F * 2.0F) * 0.65F + 0.35F;
      this.bCol = Mth.sin(((float)xa + 0.6666667F) * 3.1415927F * 2.0F) * 0.65F + 0.35F;
      this.size *= 0.75F;
      this.size *= scale;
      this.oSize = this.size;
      this.lifetime = 6;
      this.noPhysics = false;
      this.tex = 64;
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

      this.move(this.xd, this.yd, this.zd);
      if (this.y == this.yo) {
         this.xd *= 1.1D;
         this.zd *= 1.1D;
      }

      this.xd *= 0.6600000262260437D;
      this.yd *= 0.6600000262260437D;
      this.zd *= 0.6600000262260437D;
      if (this.onGround) {
         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
      }

   }
}
