package net.minecraft.world.entity.item;

import com.mojang.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import util.Mth;

public class FallingTile extends Entity {
   public static final long serialVersionUID = 0L;
   public int tile;
   public int time = 0;

   public FallingTile(Level var1) {
      super(var1);
   }

   public FallingTile(Level var1, double var2, double var4, double var6, int var8) {
      super(var1);
      this.tile = var8;
      this.blocksBuilding = true;
      this.setSize(0.98F, 0.98F);
      this.heightOffset = this.bbHeight / 2.0F;
      this.setPos(var2, var4, var6);
      this.xd = 0.0D;
      this.yd = 0.0D;
      this.zd = 0.0D;
      this.makeStepSound = false;
      this.xo = var2;
      this.yo = var4;
      this.zo = var6;
   }

   protected void defineSynchedData() {
   }

   public boolean isPickable() {
      return !this.removed;
   }

   public void tick() {
      if (this.tile == 0) {
         this.remove();
      } else {
         this.xo = this.x;
         this.yo = this.y;
         this.zo = this.z;
         ++this.time;
         this.yd -= 0.03999999910593033D;
         this.move(this.xd, this.yd, this.zd);
         this.xd *= 0.9800000190734863D;
         this.yd *= 0.9800000190734863D;
         this.zd *= 0.9800000190734863D;
         int var1 = Mth.floor(this.x);
         int var2 = Mth.floor(this.y);
         int var3 = Mth.floor(this.z);
         if (this.level.getTile(var1, var2, var3) == this.tile) {
            this.level.setTile(var1, var2, var3, 0);
         }

         if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
            this.yd *= -0.5D;
            this.remove();
            if ((!this.level.mayPlace(this.tile, var1, var2, var3, true) || !this.level.setTile(var1, var2, var3, this.tile)) && !this.level.isOnline) {
               this.spawnAtLocation(this.tile, 1);
            }
         } else if (this.time > 100 && !this.level.isOnline) {
            this.spawnAtLocation(this.tile, 1);
            this.remove();
         }

      }
   }

   protected void addAdditonalSaveData(CompoundTag var1) {
      var1.putByte("Tile", (byte)this.tile);
   }

   protected void readAdditionalSaveData(CompoundTag var1) {
      this.tile = var1.getByte("Tile") & 255;
   }

   public float getShadowHeightOffs() {
      return 0.0F;
   }

   public Level getLevel() {
      return this.level;
   }
}
