package net.minecraft.world.entity.item;

import com.mojang.nbt.CompoundTag;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.phys.AABB;

public class Boat extends Entity {
   public static final long serialVersionUID = 0L;
   public int damage;
   public int hurtTime;
   public int hurtDir;
   private int lSteps;
   private double lx;
   private double ly;
   private double lz;
   private double lyr;
   private double lxr;
   private double lxd;
   private double lyd;
   private double lzd;

   public Boat(Level var1) {
      super(var1);
      this.damage = 0;
      this.hurtTime = 0;
      this.hurtDir = 1;
      this.blocksBuilding = true;
      this.setSize(1.5F, 0.6F);
      this.heightOffset = this.bbHeight / 2.0F;
      this.makeStepSound = false;
   }

   protected void defineSynchedData() {
   }

   public AABB getCollideAgainstBox(Entity var1) {
      return var1.bb;
   }

   public AABB getCollideBox() {
      return this.bb;
   }

   public boolean isPushable() {
      return true;
   }

   public Boat(Level var1, double var2, double var4, double var6) {
      this(var1);
      this.setPos(var2, var4 + (double)this.heightOffset, var6);
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.xo = var2;
      this.yo = var4;
      this.zo = var6;
   }

   public double getRideHeight() {
      return (double)this.bbHeight * 0.0D - 0.30000001192092896D;
   }

   public boolean hurt(Entity var1, int var2) {
      if (!this.level.isOnline && !this.removed) {
         this.hurtDir = -this.hurtDir;
         this.hurtTime = 10;
         this.damage += var2 * 10;
         this.markHurt();
         if (this.damage > 40) {
            int var3;
            for(var3 = 0; var3 < 3; ++var3) {
               this.spawnAtLocation(Tile.wood.id, 1, 0.0F);
            }

            for(var3 = 0; var3 < 2; ++var3) {
               this.spawnAtLocation(Item.stick.id, 1, 0.0F);
            }

            this.remove();
         }

         return true;
      } else {
         return true;
      }
   }

   public void animateHurt() {
      this.hurtDir = -this.hurtDir;
      this.hurtTime = 10;
      this.damage += this.damage * 10;
   }

   public boolean isPickable() {
      return !this.removed;
   }

   public void lerpTo(double var1, double var3, double var5, float var7, float var8, int var9) {
      this.lx = var1;
      this.ly = var3;
      this.lz = var5;
      this.lyr = (double)var7;
      this.lxr = (double)var8;
      this.lSteps = var9 + 4;
      this.xd = this.lxd;
      this.yd = this.lyd;
      this.zd = this.lzd;
   }

   public void lerpMotion(double var1, double var3, double var5) {
      this.lxd = this.xd = var1;
      this.lyd = this.yd = var3;
      this.lzd = this.zd = var5;
   }

   public void tick() {
      super.tick();
      if (this.hurtTime > 0) {
         --this.hurtTime;
      }

      if (this.damage > 0) {
         --this.damage;
      }

      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      byte var1 = 5;
      double var2 = 0.0D;

      for(int var4 = 0; var4 < var1; ++var4) {
         double var5 = this.bb.y0 + (this.bb.y1 - this.bb.y0) * (double)(var4 + 0) / (double)var1 - 0.125D;
         double var7 = this.bb.y0 + (this.bb.y1 - this.bb.y0) * (double)(var4 + 1) / (double)var1 - 0.125D;
         AABB var9 = AABB.newTemp(this.bb.x0, var5, this.bb.z0, this.bb.x1, var7, this.bb.z1);
         if (this.level.containsLiquid(var9, Material.water)) {
            var2 += 1.0D / (double)var1;
         }
      }

      double var6;
      double var8;
      double var10;
      double var23;
      if (this.level.isOnline) {
         if (this.lSteps > 0) {
            var23 = this.x + (this.lx - this.x) / (double)this.lSteps;
            var6 = this.y + (this.ly - this.y) / (double)this.lSteps;
            var8 = this.z + (this.lz - this.z) / (double)this.lSteps;

            for(var10 = this.lyr - (double)this.yRot; var10 < -180.0D; var10 += 360.0D) {
            }

            while(var10 >= 180.0D) {
               var10 -= 360.0D;
            }

            this.yRot = (float)((double)this.yRot + var10 / (double)this.lSteps);
            this.xRot = (float)((double)this.xRot + (this.lxr - (double)this.xRot) / (double)this.lSteps);
            --this.lSteps;
            this.setPos(var23, var6, var8);
            this.setRot(this.yRot, this.xRot);
         } else {
            var23 = this.x + this.xd;
            var6 = this.y + this.yd;
            var8 = this.z + this.zd;
            this.setPos(var23, var6, var8);
            if (this.onGround) {
               this.xd *= 0.5D;
               this.yd *= 0.5D;
               this.zd *= 0.5D;
            }

            this.xd *= 0.9900000095367432D;
            this.yd *= 0.949999988079071D;
            this.zd *= 0.9900000095367432D;
         }

      } else {
         var23 = var2 * 2.0D - 1.0D;
         this.yd += 0.03999999910593033D * var23;
         if (this.rider != null) {
            this.xd += this.rider.xd * 0.2D;
            this.zd += this.rider.zd * 0.2D;
         }

         var6 = 0.4D;
         if (this.xd < -var6) {
            this.xd = -var6;
         }

         if (this.xd > var6) {
            this.xd = var6;
         }

         if (this.zd < -var6) {
            this.zd = -var6;
         }

         if (this.zd > var6) {
            this.zd = var6;
         }

         if (this.onGround) {
            this.xd *= 0.5D;
            this.yd *= 0.5D;
            this.zd *= 0.5D;
         }

         this.move(this.xd, this.yd, this.zd);
         var8 = Math.sqrt(this.xd * this.xd + this.zd * this.zd);
         double var12;
         if (var8 > 0.15D) {
            var10 = Math.cos((double)this.yRot * 3.141592653589793D / 180.0D);
            var12 = Math.sin((double)this.yRot * 3.141592653589793D / 180.0D);

            for(int var14 = 0; (double)var14 < 1.0D + var8 * 60.0D; ++var14) {
               double var15 = (double)(this.random.nextFloat() * 2.0F - 1.0F);
               double var17 = (double)(this.random.nextInt(2) * 2 - 1) * 0.7D;
               double var19;
               double var21;
               if (this.random.nextBoolean()) {
                  var19 = this.x - var10 * var15 * 0.8D + var12 * var17;
                  var21 = this.z - var12 * var15 * 0.8D - var10 * var17;
                  this.level.addParticle("splash", var19, this.y - 0.125D, var21, this.xd, this.yd, this.zd);
               } else {
                  var19 = this.x + var10 + var12 * var15 * 0.7D;
                  var21 = this.z + var12 - var10 * var15 * 0.7D;
                  this.level.addParticle("splash", var19, this.y - 0.125D, var21, this.xd, this.yd, this.zd);
               }
            }
         }

         if (this.horizontalCollision && var8 > 0.15D) {
            if (!this.level.isOnline) {
               this.remove();

               int var24;
               for(var24 = 0; var24 < 3; ++var24) {
                  this.spawnAtLocation(Tile.wood.id, 1, 0.0F);
               }

               for(var24 = 0; var24 < 2; ++var24) {
                  this.spawnAtLocation(Item.stick.id, 1, 0.0F);
               }
            }
         } else {
            this.xd *= 0.9900000095367432D;
            this.yd *= 0.949999988079071D;
            this.zd *= 0.9900000095367432D;
         }

         this.xRot = 0.0F;
         var10 = (double)this.yRot;
         var12 = this.xo - this.x;
         double var25 = this.zo - this.z;
         if (var12 * var12 + var25 * var25 > 0.001D) {
            var10 = (double)((float)(Math.atan2(var25, var12) * 180.0D / 3.141592653589793D));
         }

         double var16;
         for(var16 = var10 - (double)this.yRot; var16 >= 180.0D; var16 -= 360.0D) {
         }

         while(var16 < -180.0D) {
            var16 += 360.0D;
         }

         if (var16 > 20.0D) {
            var16 = 20.0D;
         }

         if (var16 < -20.0D) {
            var16 = -20.0D;
         }

         this.yRot = (float)((double)this.yRot + var16);
         this.setRot(this.yRot, this.xRot);
         List var18 = this.level.getEntities(this, this.bb.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));
         if (var18 != null && var18.size() > 0) {
            for(int var26 = 0; var26 < var18.size(); ++var26) {
               Entity var20 = (Entity)var18.get(var26);
               if (var20 != this.rider && var20.isPushable() && var20 instanceof Boat) {
                  var20.push(this);
               }
            }
         }

         if (this.rider != null && this.rider.removed) {
            this.rider = null;
         }

      }
   }

   public void positionRider() {
      if (this.rider != null) {
         double var1 = Math.cos((double)this.yRot * 3.141592653589793D / 180.0D) * 0.4D;
         double var3 = Math.sin((double)this.yRot * 3.141592653589793D / 180.0D) * 0.4D;
         this.rider.setPos(this.x + var1, this.y + this.getRideHeight() + this.rider.getRidingHeight(), this.z + var3);
      }
   }

   protected void addAdditonalSaveData(CompoundTag var1) {
   }

   protected void readAdditionalSaveData(CompoundTag var1) {
   }

   public float getShadowHeightOffs() {
      return 0.0F;
   }

   public String getName() {
      return "Boat";
   }

   public void setChanged() {
   }

   public boolean interact(Player var1) {
      if (this.rider != null && this.rider instanceof Player && this.rider != var1) {
         return true;
      } else {
         if (!this.level.isOnline) {
            var1.ride(this);
         }

         return true;
      }
   }
}
