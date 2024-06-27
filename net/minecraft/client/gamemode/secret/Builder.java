package net.minecraft.client.gamemode.secret;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.phys.Vec3;
import util.Mth;

public class Builder extends PathfinderMob {
   private static final int MAX_TURN = 30;
   private static final int BUILD_TIME = 10;
   private static final float BUILD_RANGE = 3.0F;
   private static final int MAX_STUCK_COUNT = 20;
   private static final int PUSH_COOLDOWN = 50;
   public static final int SWING_DURATION = 8;
   public Minecraft minecraft;
   public boolean swinging = false;
   public boolean shouldSwing = false;
   public int swingTime = 0;
   public Builder.State state;
   public Waypoint buildTarget;
   public static final Schematic schema = new Schematic();
   public static final int xSchemaOrigin = 113;
   public static final int ySchemaOrigin = 72;
   public static final int zSchemaOrigin = 139;
   public int xold;
   public int zold;
   public int xlaststuck;
   public int zlaststuck;
   public int stuckCount;
   public int pushStep;
   public int age;
   public static boolean initialized = false;
   private int buildStep;
   private boolean verbose;
   public BuilderInventory inventory;
   private Path path;

   public Builder(Level var1) {
      super(var1);
      this.state = Builder.State.ROAMING;
      this.zlaststuck = 0;
      this.stuckCount = 0;
      this.pushStep = 50;
      this.age = 0;
      this.buildStep = 0;
      this.verbose = false;
      this.inventory = new BuilderInventory();
      this.textureName = "/mob/char.png";
      this.modelName = "humanoid";
      this.setSize(0.6F, 1.8F);
   }

   public void addAdditonalSaveData(CompoundTag var1) {
      super.addAdditonalSaveData(var1);
   }

   public void readAdditionalSaveData(CompoundTag var1) {
      super.readAdditionalSaveData(var1);
   }

   public void swing() {
      this.swingTime = -1;
      this.swinging = true;
   }

   protected Entity findAttackTarget() {
      return this.state == Builder.State.MOVING && this.buildTarget != null && !(this.buildTarget.distanceTo(this) < 2.0F) ? this.buildTarget : null;
   }

   public void setBuildTarget(int var1, int var2, int var3, int var4) {
      if (this.buildTarget != null && this.buildTarget instanceof Waypoint) {
         this.level.removeEntity(this.buildTarget);
         this.buildTarget = null;
      }

      Waypoint var5 = new Waypoint(this.level);
      var5.targetTile = var4;
      var5.setPos((double)var1, (double)var2, (double)var3);
      this.buildTarget = var5;
      this.attackTarget = this.buildTarget;
      if (this.verbose) {
         System.out.println("Build target set to " + var4 + " at " + var1 + ", " + var2 + ", " + var3);
      }

      int var6 = (int)Math.floor(this.bb.y0);
      if (this.buildTarget.y < (double)(var6 - 1)) {
         Vec3 var7 = Vec3.newTemp(this.x, this.y, this.z).vectorTo(Vec3.newTemp(this.buildTarget.x, this.buildTarget.y, this.buildTarget.z)).normalize();
         this.push(var7.x * 0.6000000238418579D, 0.4000000059604645D, var7.z * 0.6000000238418579D);
      }

   }

   private int getStackHeight(int var1, int var2) {
      int var3;
      for(var3 = 0; this.level.getTile(var1, var3 + 1, var2) != 0 || this.level.getTile(var1, var3 + 2, var2) != 0 || this.level.getTile(var1, var3 + 3, var2) != 0; ++var3) {
      }

      return var3;
   }

   private int getHeighestNeighborStack() {
      int var1 = (int)Math.floor(this.x);
      int var2 = (int)Math.floor(this.z);
      int var3 = 0;
      if (this.getStackHeight(var1 + 1, var2) > var3) {
         var3 = this.getStackHeight(var1 + 1, var2);
      }

      if (this.getStackHeight(var1, var2 + 1) > var3) {
         var3 = this.getStackHeight(var1, var2 + 1);
      }

      if (this.getStackHeight(var1 - 1, var2 + 1) > var3) {
         var3 = this.getStackHeight(var1 - 1, var2 + 1);
      }

      if (this.getStackHeight(var1 - 1, var2) > var3) {
         var3 = this.getStackHeight(var1 - 1, var2);
      }

      if (this.getStackHeight(var1 - 1, var2 - 1) > var3) {
         var3 = this.getStackHeight(var1 - 1, var2 - 1);
      }

      if (this.getStackHeight(var1 + 1, var2 + 1) > var3) {
         var3 = this.getStackHeight(var1 + 1, var2 + 1);
      }

      if (this.getStackHeight(var1, var2 - 1) > var3) {
         var3 = this.getStackHeight(var1, var2 - 1);
      }

      if (this.getStackHeight(var1 + 1, var2 - 1) > var3) {
         var3 = this.getStackHeight(var1 + 1, var2 - 1);
      }

      return var3;
   }

   protected void updateAi() {
      ++this.age;
      int var2;
      int var3;
      int var4;
      if (!initialized) {
         initialized = true;

         for(int var1 = schema.start[0].length - 1; var1 > 0; --var1) {
            for(var2 = 0; var2 < schema.start.length; ++var2) {
               for(var3 = 0; var3 < schema.start[var2][var1].length; ++var3) {
                  if (schema.start[var2][var1][var3] != 0) {
                     this.level.setTile(113 + var2, 72 + var1, 139 + var3, schema.start[var2][var1][var3]);
                     var4 = 0;

                     while(this.level.getTile(113 + var2, 72 + var1 - var4 - 1, 139 + var3) == 0) {
                        ++var4;
                        this.level.setTile(113 + var2, 72 + var1 - var4, 139 + var3, 5);
                     }
                  }
               }
            }
         }
      }

      this.health = 20;
      if (this.pushStep < 50) {
         --this.pushStep;
         if (this.pushStep <= 0) {
            this.pushStep = 50;
         }
      }

      float var24 = 40.0F;
      this.jumping = false;
      var2 = (int)Math.floor(this.x);
      var3 = (int)Math.floor(this.bb.y0);
      var4 = (int)Math.floor(this.z);
      if (this.age > 3000 && this.random.nextInt(500) == 0) {
         SecretMode var5 = (SecretMode)this.minecraft.gameMode;
         this.level.explode(this, (double)var2, (double)var3, (double)var4, 1.0F);
         this.level.removeEntity(this);
         var5.spawnBuilder();
      }

      if (this.level.getTile(var2, var3, var4) != 0 || this.buildTarget != null && this.buildTarget.x == (double)var2 && this.buildTarget.z == (double)var4) {
         this.jumping = true;
      }

      if (this.pushStep == 50 && this.buildTarget != null && this.random.nextInt(300) == 0) {
         Vec3 var25 = Vec3.newTemp(this.x, this.bb.y0, this.z).vectorTo(Vec3.newTemp(this.buildTarget.x, this.buildTarget.y, this.buildTarget.z)).normalize();
         this.push(var25.x * 0.5D, 0.4000000059604645D, var25.z * 0.5D);
         --this.pushStep;
      }

      float var26;
      if (this.state == Builder.State.MOVING) {
         if (this.attackTarget == null) {
            this.attackTarget = this.findAttackTarget();
         } else if (!this.attackTarget.isAlive()) {
            this.attackTarget = null;
         } else {
            var26 = this.attackTarget.distanceTo(this);
            if (this.canSee(this.attackTarget)) {
               this.checkHurtTarget(this.attackTarget, var26);
            }
         }

         if (this.attackTarget != null) {
            this.path = this.level.findPath(this, this.attackTarget, var24);
         }
      }

      if (this.state == Builder.State.MOVING || this.state == Builder.State.BUILDING) {
         if (Mth.floor(this.x) == this.xold && Mth.floor(this.z) == this.zold) {
            ++this.stuckCount;
         } else {
            this.stuckCount = 0;
         }

         this.xold = Mth.floor(this.x);
         this.zold = Mth.floor(this.z);
         if (this.state == Builder.State.MOVING && this.path == null || this.stuckCount >= 20) {
            this.state = Builder.State.STUCK;
            return;
         }
      }

      if (this.state == Builder.State.STUCK) {
         if (this.verbose) {
            System.out.println(System.currentTimeMillis() + " - I think I'm stuck... thinking about solution.");
         }

         if (this.verbose) {
            System.out.println("Checking if heighest neighobur stack (" + this.getHeighestNeighborStack() + ") is > " + var3);
         }

         if (this.getHeighestNeighborStack() > var3) {
            if (this.verbose) {
               System.out.println("Building ourselves up.");
            }

            if (this.level.getTile(var2, var3 + 1, var4) != 0) {
               this.level.setTile(var2, var3 + 1, var4, 0);
            }

            if (this.level.getTile(var2, var3 + 2, var4) != 0) {
               this.level.setTile(var2, var3 + 2, var4, 0);
            }

            if (this.level.getTile(var2, var3 + 3, var4) != 0) {
               this.level.setTile(var2, var3 + 3, var4, 0);
            }

            if (this.pushStep == 50) {
               --this.pushStep;
               this.push(Math.random() * 0.6D - 0.3D, 0.4D, Math.random() * 0.6D - 0.3D);
            }

            this.jumping = true;
            this.level.setTile(var2, var3, var4, 5);
            this.stuckCount = 0;
            this.state = Builder.State.MOVING;
            return;
         }

         if (this.pushStep == 50) {
            if (this.verbose) {
               System.out.println("Nudging ourselves");
            }

            --this.pushStep;
            this.push(Math.random() * 0.6D - 0.3D, 0.5D, Math.random() * 0.6D - 0.3D);
            this.jumping = true;
            this.stuckCount = 0;
            this.state = Builder.State.MOVING;
            return;
         }
      }

      int var6;
      int var7;
      int var8;
      int var10;
      int var27;
      if (this.state == Builder.State.MOVING) {
         if (this.attackTarget == null || this.buildTarget == null) {
            this.attackTarget = null;
            this.buildTarget = null;
            this.state = Builder.State.ROAMING;
            return;
         }

         if (this.attackTarget.distanceTo(this) <= 3.0F) {
            this.state = Builder.State.BUILDING;
            this.lookAt(this.buildTarget, 30.0F);
            this.attackTarget = null;
            return;
         }
      } else if (this.state == Builder.State.ROAMING) {
         if (this.buildTarget != null && this.buildTarget.distanceTo(this) > 8.0F) {
            this.state = Builder.State.MOVING;
            return;
         }

         int var9;
         for(var27 = 0; var27 < schema.target[0].length; ++var27) {
            for(var6 = 0; var6 < schema.target.length; ++var6) {
               for(var7 = 0; var7 < schema.target[var6][var27].length; ++var7) {
                  var8 = this.level.getTile(113 + var6, 72 + var27, 139 + var7);
                  if (schema.target[var6][var27][var7] != 0 && var8 != schema.target[var6][var27][var7]) {
                     for(var9 = 0; this.level.getTile(113 + var6, 72 + var27 - var9 - 1, 139 + var7) == 0; ++var9) {
                     }

                     if (var9 > 0) {
                        if (this.verbose) {
                           System.out.println("I should build wood foundation " + var9 + " steps under target block, at " + (139 + var7) + " -- Foundation: " + (113 + var6) + ", " + (72 + var27 - var9) + ", " + (139 + var7) + ".");
                        }

                        this.setBuildTarget(113 + var6, 72 + var27 - var9, 139 + var7, 5);
                     } else {
                        if (this.verbose) {
                           System.out.println("I should build " + schema.target[var6][var27][var7] + " at " + (113 + var6) + ", " + (72 + var27) + ", " + (139 + var7));
                        }

                        this.setBuildTarget(113 + var6, 72 + var27, 139 + var7, schema.target[var6][var27][var7]);
                     }

                     this.state = Builder.State.BUILDING;
                     if (this.buildTarget != null) {
                        this.lookAt(this.buildTarget, 30.0F);
                     }

                     return;
                  }
               }
            }
         }

         for(var27 = schema.target[0].length - 1; var27 > 0; --var27) {
            for(var6 = 0; var6 < schema.target.length; ++var6) {
               for(var7 = 0; var7 < schema.target[var6][var27].length; ++var7) {
                  var8 = this.level.getTile(113 + var6, 72 + var27, 139 + var7);
                  if (schema.target[var6][var27][var7] == 0 && var8 != schema.target[var6][var27][var7]) {
                     if (this.verbose) {
                        System.out.println("I should build " + schema.target[var6][var27][var7] + " at " + (113 + var6) + ", " + (72 + var27) + ", " + (139 + var7));
                     }

                     this.setBuildTarget(113 + var6, 72 + var27, 139 + var7, schema.target[var6][var27][var7]);
                     this.state = Builder.State.BUILDING;
                     if (this.buildTarget != null) {
                        this.lookAt(this.buildTarget, 30.0F);
                     }

                     return;
                  }
               }
            }
         }

         if (this.verbose) {
            System.out.println("Looks like schematic is complete, let's see if there's any excess wood to destroy");
         }

         byte var28 = 15;

         for(var6 = schema.target[0].length + var28; var6 >= -(schema.target[0].length + var28); --var6) {
            for(var7 = -var28; var7 < schema.target.length + var28; ++var7) {
               for(var8 = -var28; var8 < schema.target[0][0].length + var28; ++var8) {
                  var9 = this.level.getTile(113 + var7, 72 + var6, 139 + var8);
                  if (var9 == 5) {
                     for(var10 = 0; this.level.getTile(113 + var7, 72 + var6 - var10 - 1, 139 + var8) == 0; ++var10) {
                     }

                     if (var10 > 0) {
                        this.setBuildTarget(113 + var7, 72 + var6 - var10, 139 + var8, 5);
                     } else {
                        this.setBuildTarget(113 + var7, 72 + var6, 139 + var8, 0);
                     }

                     this.state = Builder.State.BUILDING;
                     if (this.buildTarget != null) {
                        this.lookAt(this.buildTarget, 30.0F);
                     }

                     return;
                  }
               }
            }
         }

         this.attackTarget = null;
         this.buildTarget = null;
         if (this.verbose) {
            System.out.println("Forever roaming");
         }
      } else if (this.state == Builder.State.BUILDING) {
         if (this.buildTarget == null) {
            this.state = Builder.State.ROAMING;
            return;
         }

         if (this.buildTarget.distanceTo(this) > 3.0F) {
            this.shouldSwing = false;
            this.holdGround = false;
            this.state = Builder.State.MOVING;
            return;
         }

         this.holdGround = true;
         this.shouldSwing = true;
         this.jumping = false;
         if (this.buildStep < 10) {
            ++this.buildStep;
         } else {
            this.buildStep = 0;
            if (this.buildStep == 0 && this.verbose) {
               System.out.println("Building at " + (int)Math.floor(this.buildTarget.x) + ", " + (int)Math.floor(this.buildTarget.y) + ", " + (int)Math.floor(this.buildTarget.z));
            }

            var27 = (int)Math.floor(this.buildTarget.x);
            var6 = (int)Math.floor(this.buildTarget.y);
            var7 = (int)Math.floor(this.buildTarget.z);
            var8 = this.level.getTile(var27, var6, var7);
            if (var8 != 0 && this.buildTarget.targetTile != var8) {
               if (this.verbose) {
                  System.out.println("This tile is something it shouldn't be, destroying it...");
               }

               if (this.buildTarget.targetTile != 0) {
                  Tile.tiles[var8].playerDestroy(this.level, var27, var6, var7, this.level.getData(var27, var6, var7));
               }

               this.level.setTile(var27, var6, var7, 0);
               if (this.buildTarget.targetTile == 0) {
                  this.stuckCount = 0;
                  this.shouldSwing = false;
                  this.holdGround = false;
                  this.buildTarget = null;
                  this.state = Builder.State.ROAMING;
               }

               return;
            }

            if (this.buildTarget.targetTile != 0 && !this.level.mayPlace(this.buildTarget.targetTile, var27, var6, var7, false)) {
               if (this.verbose) {
                  System.out.println("May not place here, jumping");
               }

               if (this.pushStep == 50) {
                  --this.pushStep;
                  this.push(0.0D, 0.800000011920929D, 0.0D);
               }

               this.jumping = true;
            } else {
               if (this.verbose) {
                  System.out.println("Placing block");
               }

               this.level.setTile(var27, var6, var7, this.buildTarget.targetTile);
            }

            var8 = this.level.getTile(var27, var6, var7);
            if (this.buildTarget.targetTile == var8) {
               if (this.verbose) {
                  System.out.println("Done building, going to roaming.");
               }

               this.stuckCount = 0;
               this.shouldSwing = false;
               this.holdGround = false;
               this.buildTarget = null;
               this.state = Builder.State.ROAMING;
               return;
            }
         }
      }

      if (this.swinging) {
         ++this.swingTime;
         if (this.swingTime == 8) {
            this.swingTime = 0;
            this.swinging = false;
         }
      } else {
         this.swingTime = 0;
      }

      this.attackAnim = (float)this.swingTime / 8.0F;
      if (this.attackTarget == null) {
         this.attackTarget = this.findAttackTarget();
         if (this.attackTarget != null) {
            this.path = this.level.findPath(this, this.attackTarget, var24);
         }
      } else if (!this.attackTarget.isAlive()) {
         this.attackTarget = null;
      } else {
         var26 = this.attackTarget.distanceTo(this);
         if (this.canSee(this.attackTarget)) {
            this.checkHurtTarget(this.attackTarget, var26);
         }
      }

      if (this.holdGround) {
         this.xxa = 0.0F;
         this.yya = 0.0F;
         this.jumping = false;
      } else {
         if (!this.holdGround && this.attackTarget != null && (this.path == null || this.random.nextInt(20) == 0)) {
            this.path = this.level.findPath(this, this.attackTarget, var24);
         } else if (this.path == null && this.random.nextInt(80) == 0 || this.random.nextInt(80) == 0) {
            boolean var29 = false;
            var6 = -1;
            var7 = -1;
            var8 = -1;
            float var32 = -99999.0F;

            for(var10 = 0; var10 < 10; ++var10) {
               int var11 = Mth.floor(this.x + (double)this.random.nextInt(13) - 6.0D);
               int var12 = Mth.floor(this.y + (double)this.random.nextInt(7) - 3.0D);
               int var13 = Mth.floor(this.z + (double)this.random.nextInt(13) - 6.0D);
               float var14 = this.getWalkTargetValue(var11, var12, var13);
               if (var14 > var32) {
                  var32 = var14;
                  var6 = var11;
                  var7 = var12;
                  var8 = var13;
                  var29 = true;
               }
            }

            if (var29) {
               this.path = this.level.findPath(this, var6, var7, var8, 10.0F);
            }
         }

         var27 = Mth.floor(this.bb.y0);
         boolean var30 = this.isInWater();
         boolean var31 = this.isInLava();
         this.xRot = 0.0F;
         if (this.path != null && this.random.nextInt(100) != 0) {
            Vec3 var33 = this.path.current(this);
            double var34 = (double)(this.bbWidth * 2.0F);

            while(var33 != null && var33.distanceToSqr(this.x, var33.y, this.z) < var34 * var34) {
               this.path.next();
               if (this.path.isDone()) {
                  var33 = null;
                  this.path = null;
               } else {
                  var33 = this.path.current(this);
               }
            }

            this.jumping = false;
            if (var33 != null) {
               double var35 = var33.x - this.x;
               double var36 = var33.z - this.z;
               double var15 = var33.y - (double)var27;
               float var17 = (float)(Math.atan2(var36, var35) * 180.0D / 3.1415927410125732D) - 90.0F;
               float var18 = var17 - this.yRot;

               for(this.yya = this.runSpeed; var18 < -180.0F; var18 += 360.0F) {
               }

               while(var18 >= 180.0F) {
                  var18 -= 360.0F;
               }

               if (var18 > 30.0F) {
                  var18 = 30.0F;
               }

               if (var18 < -30.0F) {
                  var18 = -30.0F;
               }

               this.yRot += var18;
               if (this.holdGround && this.attackTarget != null) {
                  double var19 = this.attackTarget.x - this.x;
                  double var21 = this.attackTarget.z - this.z;
                  float var23 = this.yRot;
                  this.yRot = (float)(Math.atan2(var21, var19) * 180.0D / 3.1415927410125732D) - 90.0F;
                  var18 = (var23 - this.yRot + 90.0F) * 3.1415927F / 180.0F;
                  this.xxa = -Mth.sin(var18) * this.yya * 1.0F;
                  this.yya = Mth.cos(var18) * this.yya * 1.0F;
               }

               if (var15 > 0.0D) {
                  this.jumping = true;
               }
            }

            if (this.attackTarget != null) {
               this.lookAt(this.attackTarget, 30.0F);
            }

            if (this.horizontalCollision) {
               this.jumping = true;
            }

            if (this.random.nextFloat() < 0.8F && (var30 || var31)) {
               this.jumping = true;
            }

         } else {
            super.updateAi();
            this.path = null;
         }
      }
   }

   public ItemInstance getCarriedItem() {
      return this.inventory.getSelected();
   }

   public void aiStep() {
      if (!this.swinging && this.shouldSwing) {
         this.swing();
      }

      super.aiStep();
   }

   protected float getWalkTargetValue(int var1, int var2, int var3) {
      return 10.0F;
   }

   public static enum State {
      BUILDING,
      MOVING,
      ROAMING,
      STUCK;
   }
}
