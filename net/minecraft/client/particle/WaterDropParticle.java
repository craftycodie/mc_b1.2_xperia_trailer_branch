package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tesselator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.LiquidTile;
import util.Mth;

public class WaterDropParticle extends Particle {
   public WaterDropParticle(Level level, double x, double y, double z) {
      super(level, x, y, z, 0.0D, 0.0D, 0.0D);
      this.xd *= 0.30000001192092896D;
      this.yd = (double)((float)Math.random() * 0.2F + 0.1F);
      this.zd *= 0.30000001192092896D;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.tex = 19 + this.random.nextInt(4);
      this.setSize(0.01F, 0.01F);
      this.gravity = 0.06F;
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
   }

   public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2) {
      super.render(t, a, xa, ya, za, xa2, za2);
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd -= (double)this.gravity;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.9800000190734863D;
      this.yd *= 0.9800000190734863D;
      this.zd *= 0.9800000190734863D;
      if (this.lifetime-- <= 0) {
         this.remove();
      }

      if (this.onGround) {
         if (Math.random() < 0.5D) {
            this.remove();
         }

         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
      }

      Material m = this.level.getMaterial(Mth.floor(this.x), Mth.floor(this.y), Mth.floor(this.z));
      if (m.isLiquid() || m.isSolid()) {
         double y0 = (double)((float)(Mth.floor(this.y) + 1) - LiquidTile.getHeight(this.level.getData(Mth.floor(this.x), Mth.floor(this.y), Mth.floor(this.z))));
         if (this.y < y0) {
            this.remove();
         }
      }

   }
}
