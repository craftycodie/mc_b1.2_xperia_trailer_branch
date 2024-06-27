package net.minecraft.client.particle;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import util.Mth;

public class BubbleParticle extends Particle {
   public BubbleParticle(Level level, double x, double y, double z, double xa, double ya, double za) {
      super(level, x, y, z, xa, ya, za);
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.tex = 32;
      this.setSize(0.02F, 0.02F);
      this.size *= this.random.nextFloat() * 0.6F + 0.2F;
      this.xd = xa * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
      this.yd = ya * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
      this.zd = za * 0.20000000298023224D + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.02F);
      this.lifetime = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd += 0.002D;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.8500000238418579D;
      this.yd *= 0.8500000238418579D;
      this.zd *= 0.8500000238418579D;
      if (this.level.getMaterial(Mth.floor(this.x), Mth.floor(this.y), Mth.floor(this.z)) != Material.water) {
         this.remove();
      }

      if (this.lifetime-- <= 0) {
         this.remove();
      }

   }
}
