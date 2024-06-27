package net.minecraft.world.entity.item;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import util.Mth;

public class Minecart extends Entity implements Container {
   public static final int RIDEABLE = 0;
   public static final int CHEST = 1;
   public static final int FURNACE = 2;
   private ItemInstance[] items;
   public static final long serialVersionUID = 0L;
   public int damage;
   public int hurtTime;
   public int hurtDir;
   private boolean flipped;
   public int type;
   public int fuel;
   public double xPush;
   public double zPush;
   private static final int[][][] EXITS = new int[][][]{{{0, 0, -1}, {0, 0, 1}}, {{-1, 0, 0}, {1, 0, 0}}, {{-1, -1, 0}, {1, 0, 0}}, {{-1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, {-1, 0, 0}}, {{0, 0, -1}, {-1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
   private int lSteps;
   private double lx;
   private double ly;
   private double lz;
   private double lyr;
   private double lxr;
   private double lxd;
   private double lyd;
   private double lzd;

   public Minecart(Level var1) {
      super(var1);
      this.items = new ItemInstance[36];
      this.damage = 0;
      this.hurtTime = 0;
      this.hurtDir = 1;
      this.flipped = false;
      this.blocksBuilding = true;
      this.setSize(0.98F, 0.7F);
      this.heightOffset = this.bbHeight / 2.0F;
      this.makeStepSound = false;
   }

   protected void defineSynchedData() {
   }

   public AABB getCollideAgainstBox(Entity var1) {
      return var1.bb;
   }

   public AABB getCollideBox() {
      return null;
   }

   public boolean isPushable() {
      return true;
   }

   public Minecart(Level var1, double var2, double var4, double var6, int var8) {
      this(var1);
      this.setPos(var2, var4 + (double)this.heightOffset, var6);
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.xo = var2;
      this.yo = var4;
      this.zo = var6;
      this.type = var8;
   }

   public double getRideHeight() {
      return (double)this.bbHeight * 0.0D - 0.30000001192092896D;
   }

   public boolean hurt(Entity var1, int var2) {
      if (!this.level.isOnline && !this.removed) {
         this.hurtDir = -this.hurtDir;
         this.hurtTime = 10;
         this.markHurt();
         this.damage += var2 * 10;
         if (this.damage > 40) {
            this.spawnAtLocation(Item.minecart.id, 1, 0.0F);
            if (this.type == 1) {
               this.spawnAtLocation(Tile.chest.id, 1, 0.0F);
            } else if (this.type == 2) {
               this.spawnAtLocation(Tile.furnace.id, 1, 0.0F);
            }

            this.remove();
         }

         return true;
      } else {
         return true;
      }
   }

   public void animateHurt() {
      System.out.println("Animating hurt");
      this.hurtDir = -this.hurtDir;
      this.hurtTime = 10;
      this.damage += this.damage * 10;
   }

   public boolean isPickable() {
      return !this.removed;
   }

   public void remove() {
      for(int var1 = 0; var1 < this.getContainerSize(); ++var1) {
         ItemInstance var2 = this.getItem(var1);
         if (var2 != null) {
            float var3 = this.random.nextFloat() * 0.8F + 0.1F;
            float var4 = this.random.nextFloat() * 0.8F + 0.1F;
            float var5 = this.random.nextFloat() * 0.8F + 0.1F;

            while(var2.count > 0) {
               int var6 = this.random.nextInt(21) + 10;
               if (var6 > var2.count) {
                  var6 = var2.count;
               }

               var2.count -= var6;
               ItemEntity var7 = new ItemEntity(this.level, this.x + (double)var3, this.y + (double)var4, this.z + (double)var5, new ItemInstance(var2.id, var6, var2.getAuxValue()));
               float var8 = 0.05F;
               var7.xd = (double)((float)this.random.nextGaussian() * var8);
               var7.yd = (double)((float)this.random.nextGaussian() * var8 + 0.2F);
               var7.zd = (double)((float)this.random.nextGaussian() * var8);
               this.level.addEntity(var7);
            }
         }
      }

      super.remove();
   }

   public void tick() {
      if (this.hurtTime > 0) {
         --this.hurtTime;
      }

      if (this.damage > 0) {
         --this.damage;
      }

      double var7;
      if (this.level.isOnline && this.lSteps > 0) {
         if (this.lSteps > 0) {
            double var41 = this.x + (this.lx - this.x) / (double)this.lSteps;
            double var42 = this.y + (this.ly - this.y) / (double)this.lSteps;
            double var5 = this.z + (this.lz - this.z) / (double)this.lSteps;

            for(var7 = this.lyr - (double)this.yRot; var7 < -180.0D; var7 += 360.0D) {
            }

            while(var7 >= 180.0D) {
               var7 -= 360.0D;
            }

            this.yRot = (float)((double)this.yRot + var7 / (double)this.lSteps);
            this.xRot = (float)((double)this.xRot + (this.lxr - (double)this.xRot) / (double)this.lSteps);
            --this.lSteps;
            this.setPos(var41, var42, var5);
            this.setRot(this.yRot, this.xRot);
         } else {
            this.setPos(this.x, this.y, this.z);
            this.setRot(this.yRot, this.xRot);
         }

      } else {
         this.xo = this.x;
         this.yo = this.y;
         this.zo = this.z;
         this.yd -= 0.03999999910593033D;
         int var1 = Mth.floor(this.x);
         int var2 = Mth.floor(this.y);
         int var3 = Mth.floor(this.z);
         if (this.level.getTile(var1, var2 - 1, var3) == Tile.rail.id) {
            --var2;
         }

         double var4 = 0.4D;
         boolean var6 = false;
         var7 = 0.0078125D;
         if (this.level.getTile(var1, var2, var3) == Tile.rail.id) {
            Vec3 var9 = this.getPos(this.x, this.y, this.z);
            int var10 = this.level.getData(var1, var2, var3);
            this.y = (double)var2;
            if (var10 >= 2 && var10 <= 5) {
               this.y = (double)(var2 + 1);
            }

            if (var10 == 2) {
               this.xd -= var7;
            }

            if (var10 == 3) {
               this.xd += var7;
            }

            if (var10 == 4) {
               this.zd += var7;
            }

            if (var10 == 5) {
               this.zd -= var7;
            }

            int[][] var11 = EXITS[var10];
            double var12 = (double)(var11[1][0] - var11[0][0]);
            double var14 = (double)(var11[1][2] - var11[0][2]);
            double var16 = Math.sqrt(var12 * var12 + var14 * var14);
            double var18 = this.xd * var12 + this.zd * var14;
            if (var18 < 0.0D) {
               var12 = -var12;
               var14 = -var14;
            }

            double var20 = Math.sqrt(this.xd * this.xd + this.zd * this.zd);
            this.xd = var20 * var12 / var16;
            this.zd = var20 * var14 / var16;
            double var22 = 0.0D;
            double var24 = (double)var1 + 0.5D + (double)var11[0][0] * 0.5D;
            double var26 = (double)var3 + 0.5D + (double)var11[0][2] * 0.5D;
            double var28 = (double)var1 + 0.5D + (double)var11[1][0] * 0.5D;
            double var30 = (double)var3 + 0.5D + (double)var11[1][2] * 0.5D;
            var12 = var28 - var24;
            var14 = var30 - var26;
            double var32;
            double var34;
            double var36;
            if (var12 == 0.0D) {
               this.x = (double)var1 + 0.5D;
               var22 = this.z - (double)var3;
            } else if (var14 == 0.0D) {
               this.z = (double)var3 + 0.5D;
               var22 = this.x - (double)var1;
            } else {
               var32 = this.x - var24;
               var34 = this.z - var26;
               var36 = (var32 * var12 + var34 * var14) * 2.0D;
               var22 = var36;
            }

            this.x = var24 + var12 * var22;
            this.z = var26 + var14 * var22;
            this.setPos(this.x, this.y + (double)this.heightOffset, this.z);
            var32 = this.xd;
            var34 = this.zd;
            if (this.rider != null) {
               var32 *= 0.75D;
               var34 *= 0.75D;
            }

            if (var32 < -var4) {
               var32 = -var4;
            }

            if (var32 > var4) {
               var32 = var4;
            }

            if (var34 < -var4) {
               var34 = -var4;
            }

            if (var34 > var4) {
               var34 = var4;
            }

            this.move(var32, 0.0D, var34);
            if (var11[0][1] != 0 && Mth.floor(this.x) - var1 == var11[0][0] && Mth.floor(this.z) - var3 == var11[0][2]) {
               this.setPos(this.x, this.y + (double)var11[0][1], this.z);
            } else if (var11[1][1] != 0 && Mth.floor(this.x) - var1 == var11[1][0] && Mth.floor(this.z) - var3 == var11[1][2]) {
               this.setPos(this.x, this.y + (double)var11[1][1], this.z);
            }

            if (this.rider != null) {
               this.xd *= 0.996999979019165D;
               this.yd *= 0.0D;
               this.zd *= 0.996999979019165D;
            } else {
               if (this.type == 2) {
                  var36 = (double)Mth.sqrt(this.xPush * this.xPush + this.zPush * this.zPush);
                  if (var36 > 0.01D) {
                     var6 = true;
                     this.xPush /= var36;
                     this.zPush /= var36;
                     double var38 = 0.04D;
                     this.xd *= 0.800000011920929D;
                     this.yd *= 0.0D;
                     this.zd *= 0.800000011920929D;
                     this.xd += this.xPush * var38;
                     this.zd += this.zPush * var38;
                  } else {
                     this.xd *= 0.8999999761581421D;
                     this.yd *= 0.0D;
                     this.zd *= 0.8999999761581421D;
                  }
               }

               this.xd *= 0.9599999785423279D;
               this.yd *= 0.0D;
               this.zd *= 0.9599999785423279D;
            }

            Vec3 var46 = this.getPos(this.x, this.y, this.z);
            if (var46 != null && var9 != null) {
               double var37 = (var9.y - var46.y) * 0.05D;
               var20 = Math.sqrt(this.xd * this.xd + this.zd * this.zd);
               if (var20 > 0.0D) {
                  this.xd = this.xd / var20 * (var20 + var37);
                  this.zd = this.zd / var20 * (var20 + var37);
               }

               this.setPos(this.x, var46.y, this.z);
            }

            int var47 = Mth.floor(this.x);
            int var48 = Mth.floor(this.z);
            if (var47 != var1 || var48 != var3) {
               var20 = Math.sqrt(this.xd * this.xd + this.zd * this.zd);
               this.xd = var20 * (double)(var47 - var1);
               this.zd = var20 * (double)(var48 - var3);
            }

            if (this.type == 2) {
               double var39 = (double)Mth.sqrt(this.xPush * this.xPush + this.zPush * this.zPush);
               if (var39 > 0.01D && this.xd * this.xd + this.zd * this.zd > 0.001D) {
                  this.xPush /= var39;
                  this.zPush /= var39;
                  if (this.xPush * this.xd + this.zPush * this.zd < 0.0D) {
                     this.xPush = 0.0D;
                     this.zPush = 0.0D;
                  } else {
                     this.xPush = this.xd;
                     this.zPush = this.zd;
                  }
               }
            }
         } else {
            if (this.xd < -var4) {
               this.xd = -var4;
            }

            if (this.xd > var4) {
               this.xd = var4;
            }

            if (this.zd < -var4) {
               this.zd = -var4;
            }

            if (this.zd > var4) {
               this.zd = var4;
            }

            if (this.onGround) {
               this.xd *= 0.5D;
               this.yd *= 0.5D;
               this.zd *= 0.5D;
            }

            this.move(this.xd, this.yd, this.zd);
            if (!this.onGround) {
               this.xd *= 0.949999988079071D;
               this.yd *= 0.949999988079071D;
               this.zd *= 0.949999988079071D;
            }
         }

         this.xRot = 0.0F;
         double var43 = this.xo - this.x;
         double var44 = this.zo - this.z;
         if (var43 * var43 + var44 * var44 > 0.001D) {
            this.yRot = (float)(Math.atan2(var44, var43) * 180.0D / 3.141592653589793D);
            if (this.flipped) {
               this.yRot += 180.0F;
            }
         }

         double var13;
         for(var13 = (double)(this.yRot - this.yRotO); var13 >= 180.0D; var13 -= 360.0D) {
         }

         while(var13 < -180.0D) {
            var13 += 360.0D;
         }

         if (var13 < -170.0D || var13 >= 170.0D) {
            this.yRot += 180.0F;
            this.flipped = !this.flipped;
         }

         this.setRot(this.yRot, this.xRot);
         List var15 = this.level.getEntities(this, this.bb.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));
         if (var15 != null && var15.size() > 0) {
            for(int var45 = 0; var45 < var15.size(); ++var45) {
               Entity var17 = (Entity)var15.get(var45);
               if (var17 != this.rider && var17.isPushable() && var17 instanceof Minecart) {
                  var17.push(this);
               }
            }
         }

         if (this.rider != null && this.rider.removed) {
            this.rider = null;
         }

         if (var6 && this.random.nextInt(4) == 0) {
            --this.fuel;
            if (this.fuel < 0) {
               this.xPush = this.zPush = 0.0D;
            }

            this.level.addParticle("largesmoke", this.x, this.y + 0.8D, this.z, 0.0D, 0.0D, 0.0D);
         }

      }
   }

   public Vec3 getPosOffs(double var1, double var3, double var5, double var7) {
      int var9 = Mth.floor(var1);
      int var10 = Mth.floor(var3);
      int var11 = Mth.floor(var5);
      if (this.level.getTile(var9, var10 - 1, var11) == Tile.rail.id) {
         --var10;
      }

      if (this.level.getTile(var9, var10, var11) == Tile.rail.id) {
         int var12 = this.level.getData(var9, var10, var11);
         var3 = (double)var10;
         if (var12 >= 2 && var12 <= 5) {
            var3 = (double)(var10 + 1);
         }

         int[][] var13 = EXITS[var12];
         double var14 = (double)(var13[1][0] - var13[0][0]);
         double var16 = (double)(var13[1][2] - var13[0][2]);
         double var18 = Math.sqrt(var14 * var14 + var16 * var16);
         var14 /= var18;
         var16 /= var18;
         var1 += var14 * var7;
         var5 += var16 * var7;
         if (var13[0][1] != 0 && Mth.floor(var1) - var9 == var13[0][0] && Mth.floor(var5) - var11 == var13[0][2]) {
            var3 += (double)var13[0][1];
         } else if (var13[1][1] != 0 && Mth.floor(var1) - var9 == var13[1][0] && Mth.floor(var5) - var11 == var13[1][2]) {
            var3 += (double)var13[1][1];
         }

         return this.getPos(var1, var3, var5);
      } else {
         return null;
      }
   }

   public Vec3 getPos(double var1, double var3, double var5) {
      int var7 = Mth.floor(var1);
      int var8 = Mth.floor(var3);
      int var9 = Mth.floor(var5);
      if (this.level.getTile(var7, var8 - 1, var9) == Tile.rail.id) {
         --var8;
      }

      if (this.level.getTile(var7, var8, var9) == Tile.rail.id) {
         int var10 = this.level.getData(var7, var8, var9);
         var3 = (double)var8;
         if (var10 >= 2 && var10 <= 5) {
            var3 = (double)(var8 + 1);
         }

         int[][] var11 = EXITS[var10];
         double var12 = 0.0D;
         double var14 = (double)var7 + 0.5D + (double)var11[0][0] * 0.5D;
         double var16 = (double)var8 + 0.5D + (double)var11[0][1] * 0.5D;
         double var18 = (double)var9 + 0.5D + (double)var11[0][2] * 0.5D;
         double var20 = (double)var7 + 0.5D + (double)var11[1][0] * 0.5D;
         double var22 = (double)var8 + 0.5D + (double)var11[1][1] * 0.5D;
         double var24 = (double)var9 + 0.5D + (double)var11[1][2] * 0.5D;
         double var26 = var20 - var14;
         double var28 = (var22 - var16) * 2.0D;
         double var30 = var24 - var18;
         if (var26 == 0.0D) {
            var1 = (double)var7 + 0.5D;
            var12 = var5 - (double)var9;
         } else if (var30 == 0.0D) {
            var5 = (double)var9 + 0.5D;
            var12 = var1 - (double)var7;
         } else {
            double var32 = var1 - var14;
            double var34 = var5 - var18;
            double var36 = (var32 * var26 + var34 * var30) * 2.0D;
            var12 = var36;
         }

         var1 = var14 + var26 * var12;
         var3 = var16 + var28 * var12;
         var5 = var18 + var30 * var12;
         if (var28 < 0.0D) {
            ++var3;
         }

         if (var28 > 0.0D) {
            var3 += 0.5D;
         }

         return Vec3.newTemp(var1, var3, var5);
      } else {
         return null;
      }
   }

   protected void addAdditonalSaveData(CompoundTag var1) {
      var1.putInt("Type", this.type);
      if (this.type == 2) {
         var1.putDouble("PushX", this.xPush);
         var1.putDouble("PushZ", this.zPush);
         var1.putShort("Fuel", (short)this.fuel);
      } else if (this.type == 1) {
         ListTag var2 = new ListTag();

         for(int var3 = 0; var3 < this.items.length; ++var3) {
            if (this.items[var3] != null) {
               CompoundTag var4 = new CompoundTag();
               var4.putByte("Slot", (byte)var3);
               this.items[var3].save(var4);
               var2.add(var4);
            }
         }

         var1.put("Items", var2);
      }

   }

   protected void readAdditionalSaveData(CompoundTag var1) {
      this.type = var1.getInt("Type");
      if (this.type == 2) {
         this.xPush = var1.getDouble("PushX");
         this.zPush = var1.getDouble("PushZ");
         this.fuel = var1.getShort("Fuel");
      } else if (this.type == 1) {
         ListTag var2 = var1.getList("Items");
         this.items = new ItemInstance[this.getContainerSize()];

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            CompoundTag var4 = (CompoundTag)var2.get(var3);
            int var5 = var4.getByte("Slot") & 255;
            if (var5 >= 0 && var5 < this.items.length) {
               this.items[var5] = new ItemInstance(var4);
            }
         }
      }

   }

   public float getShadowHeightOffs() {
      return 0.0F;
   }

   public void push(Entity var1) {
      if (!this.level.isOnline) {
         if (var1 != this.rider) {
            if (var1 instanceof Mob && !(var1 instanceof Player) && this.type == 0 && this.xd * this.xd + this.zd * this.zd > 0.01D && this.rider == null && var1.riding == null) {
               var1.ride(this);
            }

            double var2 = var1.x - this.x;
            double var4 = var1.z - this.z;
            double var6 = var2 * var2 + var4 * var4;
            if (var6 >= 9.999999747378752E-5D) {
               var6 = (double)Mth.sqrt(var6);
               var2 /= var6;
               var4 /= var6;
               double var8 = 1.0D / var6;
               if (var8 > 1.0D) {
                  var8 = 1.0D;
               }

               var2 *= var8;
               var4 *= var8;
               var2 *= 0.10000000149011612D;
               var4 *= 0.10000000149011612D;
               var2 *= (double)(1.0F - this.pushthrough);
               var4 *= (double)(1.0F - this.pushthrough);
               var2 *= 0.5D;
               var4 *= 0.5D;
               if (var1 instanceof Minecart) {
                  double var10 = var1.xd + this.xd;
                  double var12 = var1.zd + this.zd;
                  if (((Minecart)var1).type == 2 && this.type != 2) {
                     this.xd *= 0.20000000298023224D;
                     this.zd *= 0.20000000298023224D;
                     this.push(var1.xd - var2, 0.0D, var1.zd - var4);
                     var1.xd *= 0.699999988079071D;
                     var1.zd *= 0.699999988079071D;
                  } else if (((Minecart)var1).type != 2 && this.type == 2) {
                     var1.xd *= 0.20000000298023224D;
                     var1.zd *= 0.20000000298023224D;
                     var1.push(this.xd + var2, 0.0D, this.zd + var4);
                     this.xd *= 0.699999988079071D;
                     this.zd *= 0.699999988079071D;
                  } else {
                     var10 /= 2.0D;
                     var12 /= 2.0D;
                     this.xd *= 0.20000000298023224D;
                     this.zd *= 0.20000000298023224D;
                     this.push(var10 - var2, 0.0D, var12 - var4);
                     var1.xd *= 0.20000000298023224D;
                     var1.zd *= 0.20000000298023224D;
                     var1.push(var10 + var2, 0.0D, var12 + var4);
                  }
               } else {
                  this.push(-var2, 0.0D, -var4);
                  var1.push(var2 / 4.0D, 0.0D, var4 / 4.0D);
               }
            }

         }
      }
   }

   public int getContainerSize() {
      return 27;
   }

   public ItemInstance getItem(int var1) {
      return this.items[var1];
   }

   public ItemInstance removeItem(int var1, int var2) {
      if (this.items[var1] != null) {
         ItemInstance var3;
         if (this.items[var1].count <= var2) {
            var3 = this.items[var1];
            this.items[var1] = null;
            return var3;
         } else {
            var3 = this.items[var1].remove(var2);
            if (this.items[var1].count == 0) {
               this.items[var1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   public void setItem(int var1, ItemInstance var2) {
      this.items[var1] = var2;
      if (var2 != null && var2.count > this.getMaxStackSize()) {
         var2.count = this.getMaxStackSize();
      }

   }

   public String getName() {
      return "Minecart";
   }

   public void load(ListTag<CompoundTag> var1) {
   }

   public int getMaxStackSize() {
      return 64;
   }

   public void setChanged() {
   }

   public boolean interact(Player var1) {
      if (this.type == 0) {
         if (this.rider != null && this.rider instanceof Player && this.rider != var1) {
            return true;
         }

         if (!this.level.isOnline) {
            var1.ride(this);
         }
      } else if (this.type == 1) {
         if (!this.level.isOnline) {
            var1.openContainer(this);
         }
      } else if (this.type == 2) {
         ItemInstance var2 = var1.inventory.getSelected();
         if (var2 != null && var2.id == Item.coal.id) {
            if (--var2.count == 0) {
               var1.inventory.setItem(var1.inventory.selected, (ItemInstance)null);
            }

            this.fuel += 1200;
         }

         this.xPush = this.x - var1.x;
         this.zPush = this.z - var1.z;
      }

      return true;
   }

   public float getLootContent() {
      int var1 = 0;

      for(int var2 = 0; var2 < this.items.length; ++var2) {
         if (this.items[var2] != null) {
            ++var1;
         }
      }

      return (float)var1 / (float)this.items.length;
   }

   public void lerpTo(double var1, double var3, double var5, float var7, float var8, int var9) {
      this.lx = var1;
      this.ly = var3;
      this.lz = var5;
      this.lyr = (double)var7;
      this.lxr = (double)var8;
      this.lSteps = var9 + 2;
      this.xd = this.lxd;
      this.yd = this.lyd;
      this.zd = this.lzd;
   }

   public void lerpMotion(double var1, double var3, double var5) {
      this.lxd = this.xd = var1;
      this.lyd = this.yd = var3;
      this.lzd = this.zd = var5;
   }

   public boolean stillValid(Player var1) {
      if (this.removed) {
         return false;
      } else {
         return !(var1.distanceToSqr(this) > 64.0D);
      }
   }
}
