package net.minecraft.world.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.tile.Tile;
import util.Mth;

public class FlyingMob extends Mob {
   public FlyingMob(Level var1) {
      super(var1);
   }

   protected void causeFallDamage(float var1) {
   }

   public void travel(float var1, float var2) {
      if (this.isInWater()) {
         this.moveRelative(var1, var2, 0.02F);
         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.800000011920929D;
         this.yd *= 0.800000011920929D;
         this.zd *= 0.800000011920929D;
      } else if (this.isInLava()) {
         this.moveRelative(var1, var2, 0.02F);
         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.5D;
         this.yd *= 0.5D;
         this.zd *= 0.5D;
      } else {
         float var3 = 0.91F;
         if (this.onGround) {
            var3 = 0.54600006F;
            int var4 = this.level.getTile(Mth.floor(this.x), Mth.floor(this.bb.y0) - 1, Mth.floor(this.z));
            if (var4 > 0) {
               var3 = Tile.tiles[var4].friction * 0.91F;
            }
         }

         float var8 = 0.16277136F / (var3 * var3 * var3);
         this.moveRelative(var1, var2, this.onGround ? 0.1F * var8 : 0.02F);
         var3 = 0.91F;
         if (this.onGround) {
            var3 = 0.54600006F;
            int var5 = this.level.getTile(Mth.floor(this.x), Mth.floor(this.bb.y0) - 1, Mth.floor(this.z));
            if (var5 > 0) {
               var3 = Tile.tiles[var5].friction * 0.91F;
            }
         }

         this.move(this.xd, this.yd, this.zd);
         this.xd *= (double)var3;
         this.yd *= (double)var3;
         this.zd *= (double)var3;
      }

      this.walkAnimSpeedO = this.walkAnimSpeed;
      double var10 = this.x - this.xo;
      double var9 = this.z - this.zo;
      float var7 = Mth.sqrt(var10 * var10 + var9 * var9) * 4.0F;
      if (var7 > 1.0F) {
         var7 = 1.0F;
      }

      this.walkAnimSpeed += (var7 - this.walkAnimSpeed) * 0.4F;
      this.walkAnimPos += this.walkAnimSpeed;
   }

   public boolean onLadder() {
      return false;
   }
}
