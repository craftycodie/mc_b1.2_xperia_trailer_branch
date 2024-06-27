package net.minecraft.world.entity.item;

import com.mojang.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import util.Mth;

public class PrimedTnt extends Entity {
   public static final long serialVersionUID = 0L;
   public int life;

   public PrimedTnt(Level var1) {
      super(var1);
      this.life = 0;
      this.blocksBuilding = true;
      this.setSize(0.98F, 0.98F);
      this.heightOffset = this.bbHeight / 2.0F;
   }

   public PrimedTnt(Level var1, double var2, double var4, double var6) {
      this(var1);
      this.setPos(var2, var4, var6);
      float var8 = (float)(Math.random() * 3.1415927410125732D * 2.0D);
      this.xd = (double)(-Mth.sin(var8 * 3.1415927F / 180.0F) * 0.02F);
      this.yd = 0.20000000298023224D;
      this.zd = (double)(-Mth.cos(var8 * 3.1415927F / 180.0F) * 0.02F);
      this.makeStepSound = false;
      this.life = 80;
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
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      this.yd -= 0.03999999910593033D;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.9800000190734863D;
      this.yd *= 0.9800000190734863D;
      this.zd *= 0.9800000190734863D;
      if (this.onGround) {
         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
         this.yd *= -0.5D;
      }

      if (this.life-- <= 0) {
         this.remove();
         this.explode();
      } else {
         this.level.addParticle("smoke", this.x, this.y + 0.5D, this.z, 0.0D, 0.0D, 0.0D);
      }

   }

   private void explode() {
      float var1 = 4.0F;
      this.level.explode((Entity)null, this.x, this.y, this.z, var1);
   }

   protected void addAdditonalSaveData(CompoundTag var1) {
      var1.putByte("Fuse", (byte)this.life);
   }

   protected void readAdditionalSaveData(CompoundTag var1) {
      this.life = var1.getByte("Fuse");
   }

   public float getShadowHeightOffs() {
      return 0.0F;
   }
}
