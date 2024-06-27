package net.minecraft.client.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.Level;
import util.Mth;

public class RemotePlayer extends Player {
   public Input input;
   private int lSteps;
   private double lx;
   private double ly;
   private double lz;
   private double lyr;
   private double lxr;
   float fallTime = 0.0F;

   public RemotePlayer(Level level, String name) {
      super(level);
      this.name = name;
      this.heightOffset = 0.0F;
      this.footSize = 0.0F;
      if (name != null && name.length() > 0) {
         this.customTextureUrl = "http://s3.amazonaws.com/MinecraftSkins/" + name + ".png";
         System.out.println("Loading texture " + this.customTextureUrl);
      }

      this.noPhysics = true;
      this.viewScale = 10.0D;
   }

   public boolean hurt(Entity source, int dmg) {
      return true;
   }

   public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
      this.heightOffset = 0.0F;
      this.lx = x;
      this.ly = y;
      this.lz = z;
      this.lyr = (double)yRot;
      this.lxr = (double)xRot;
      this.lSteps = steps;
   }

   public void tick() {
      super.tick();
      this.walkAnimSpeedO = this.walkAnimSpeed;
      double xxd = this.x - this.xo;
      double zzd = this.z - this.zo;
      float wst = Mth.sqrt(xxd * xxd + zzd * zzd) * 4.0F;
      if (wst > 1.0F) {
         wst = 1.0F;
      }

      this.walkAnimSpeed += (wst - this.walkAnimSpeed) * 0.4F;
      this.walkAnimPos += this.walkAnimSpeed;
   }

   public float getShadowHeightOffs() {
      return 0.0F;
   }

   public void aiStep() {
      super.updateAi();
      if (this.lSteps > 0) {
         double xt = this.x + (this.lx - this.x) / (double)this.lSteps;
         double yt = this.y + (this.ly - this.y) / (double)this.lSteps;
         double zt = this.z + (this.lz - this.z) / (double)this.lSteps;

         double yrd;
         for(yrd = this.lyr - (double)this.yRot; yrd < -180.0D; yrd += 360.0D) {
         }

         while(yrd >= 180.0D) {
            yrd -= 360.0D;
         }

         this.yRot = (float)((double)this.yRot + yrd / (double)this.lSteps);
         this.xRot = (float)((double)this.xRot + (this.lxr - (double)this.xRot) / (double)this.lSteps);
         --this.lSteps;
         this.setPos(xt, yt, zt);
         this.setRot(this.yRot, this.xRot);
      }

      this.oBob = this.bob;
      float tBob = Mth.sqrt(this.xd * this.xd + this.zd * this.zd);
      float tTilt = (float)Math.atan(-this.yd * 0.20000000298023224D) * 15.0F;
      if (tBob > 0.1F) {
         tBob = 0.1F;
      }

      if (!this.onGround || this.health <= 0) {
         tBob = 0.0F;
      }

      if (this.onGround || this.health <= 0) {
         tTilt = 0.0F;
      }

      this.bob += (tBob - this.bob) * 0.4F;
      this.tilt += (tTilt - this.tilt) * 0.8F;
   }

   public void setEquippedSlot(int slot, int itemId, int auxValue) {
      ItemInstance item = null;
      if (itemId >= 0) {
         item = new ItemInstance(itemId, 1, auxValue);
      }

      if (slot == 0) {
         this.inventory.items[this.inventory.selected] = item;
      } else {
         this.inventory.armor[slot - 1] = item;
      }

   }
}
