package net.minecraft.world.level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import util.Mth;

public class Explosion {
   public boolean fire = false;
   private Random random = new Random();
   private Level level;
   public double x;
   public double y;
   public double z;
   public Entity source;
   public float r;
   public Set<TilePos> toBlow = new HashSet();

   public Explosion(Level var1, Entity var2, double var3, double var5, double var7, float var9) {
      this.level = var1;
      this.source = var2;
      this.r = var9;
      this.x = var3;
      this.y = var5;
      this.z = var7;
   }

   public void explode() {
      float var1 = this.r;
      byte var2 = 16;

      int var3;
      int var4;
      int var5;
      double var15;
      double var17;
      double var19;
      for(var3 = 0; var3 < var2; ++var3) {
         for(var4 = 0; var4 < var2; ++var4) {
            for(var5 = 0; var5 < var2; ++var5) {
               if (var3 == 0 || var3 == var2 - 1 || var4 == 0 || var4 == var2 - 1 || var5 == 0 || var5 == var2 - 1) {
                  double var6 = (double)((float)var3 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var8 = (double)((float)var4 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var10 = (double)((float)var5 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
                  double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
                  var6 /= var12;
                  var8 /= var12;
                  var10 /= var12;
                  float var14 = this.r * (0.7F + this.level.random.nextFloat() * 0.6F);
                  var15 = this.x;
                  var17 = this.y;
                  var19 = this.z;

                  for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * 0.75F) {
                     int var22 = Mth.floor(var15);
                     int var23 = Mth.floor(var17);
                     int var24 = Mth.floor(var19);
                     int var25 = this.level.getTile(var22, var23, var24);
                     if (var25 > 0) {
                        var14 -= (Tile.tiles[var25].getExplosionResistance(this.source) + 0.3F) * var21;
                     }

                     if (var14 > 0.0F) {
                        this.toBlow.add(new TilePos(var22, var23, var24));
                     }

                     var15 += var6 * (double)var21;
                     var17 += var8 * (double)var21;
                     var19 += var10 * (double)var21;
                  }
               }
            }
         }
      }

      this.r *= 2.0F;
      var3 = Mth.floor(this.x - (double)this.r - 1.0D);
      var4 = Mth.floor(this.x + (double)this.r + 1.0D);
      var5 = Mth.floor(this.y - (double)this.r - 1.0D);
      int var29 = Mth.floor(this.y + (double)this.r + 1.0D);
      int var7 = Mth.floor(this.z - (double)this.r - 1.0D);
      int var30 = Mth.floor(this.z + (double)this.r + 1.0D);
      List var9 = this.level.getEntities(this.source, AABB.newTemp((double)var3, (double)var5, (double)var7, (double)var4, (double)var29, (double)var30));
      Vec3 var31 = Vec3.newTemp(this.x, this.y, this.z);

      for(int var11 = 0; var11 < var9.size(); ++var11) {
         Entity var33 = (Entity)var9.get(var11);
         double var13 = var33.distanceTo(this.x, this.y, this.z) / (double)this.r;
         if (var13 <= 1.0D) {
            var15 = var33.x - this.x;
            var17 = var33.y - this.y;
            var19 = var33.z - this.z;
            double var39 = (double)Mth.sqrt(var15 * var15 + var17 * var17 + var19 * var19);
            var15 /= var39;
            var17 /= var39;
            var19 /= var39;
            double var40 = (double)this.level.getSeenPercent(var31, var33.bb);
            double var41 = (1.0D - var13) * var40;
            var33.hurt(this.source, (int)((var41 * var41 + var41) / 2.0D * 8.0D * (double)this.r + 1.0D));
            var33.xd += var15 * var41;
            var33.yd += var17 * var41;
            var33.zd += var19 * var41;
         }
      }

      this.r = var1;
      ArrayList var32 = new ArrayList();
      var32.addAll(this.toBlow);
      if (this.fire) {
         for(int var34 = var32.size() - 1; var34 >= 0; --var34) {
            TilePos var35 = (TilePos)var32.get(var34);
            int var36 = var35.x;
            int var37 = var35.y;
            int var16 = var35.z;
            int var38 = this.level.getTile(var36, var37, var16);
            int var18 = this.level.getTile(var36, var37 - 1, var16);
            if (var38 == 0 && Tile.solid[var18] && this.random.nextInt(3) == 0) {
               this.level.setTile(var36, var37, var16, Tile.fire.id);
            }
         }
      }

   }

   public void addParticles() {
      this.level.playSound(this.x, this.y, this.z, "random.explode", 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
      ArrayList var1 = new ArrayList();
      var1.addAll(this.toBlow);

      for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
         TilePos var3 = (TilePos)var1.get(var2);
         int var4 = var3.x;
         int var5 = var3.y;
         int var6 = var3.z;
         int var7 = this.level.getTile(var4, var5, var6);

         for(int var8 = 0; var8 < 1; ++var8) {
            double var9 = (double)((float)var4 + this.level.random.nextFloat());
            double var11 = (double)((float)var5 + this.level.random.nextFloat());
            double var13 = (double)((float)var6 + this.level.random.nextFloat());
            double var15 = var9 - this.x;
            double var17 = var11 - this.y;
            double var19 = var13 - this.z;
            double var21 = (double)Mth.sqrt(var15 * var15 + var17 * var17 + var19 * var19);
            var15 /= var21;
            var17 /= var21;
            var19 /= var21;
            double var23 = 0.5D / (var21 / (double)this.r + 0.1D);
            var23 *= (double)(this.level.random.nextFloat() * this.level.random.nextFloat() + 0.3F);
            var15 *= var23;
            var17 *= var23;
            var19 *= var23;
            this.level.addParticle("explode", (var9 + this.x * 1.0D) / 2.0D, (var11 + this.y * 1.0D) / 2.0D, (var13 + this.z * 1.0D) / 2.0D, var15, var17, var19);
            this.level.addParticle("smoke", var9, var11, var13, var15, var17, var19);
         }

         if (var7 > 0) {
            Tile.tiles[var7].spawnResources(this.level, var4, var5, var6, this.level.getData(var4, var5, var6), 0.3F);
            this.level.setTile(var4, var5, var6, 0);
            Tile.tiles[var7].wasExploded(this.level, var4, var5, var6);
         }
      }

   }
}
