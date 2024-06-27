package net.minecraft.world.level.tile.entity;

import com.mojang.nbt.CompoundTag;
import net.minecraft.world.entity.EntityIO;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

public class MobSpawnerTileEntity extends TileEntity {
   private static final int MAX_DIST = 16;
   public int spawnDelay = -1;
   private String entityId = "Pig";
   public double spin;
   public double oSpin = 0.0D;

   public MobSpawnerTileEntity() {
      this.spawnDelay = 20;
   }

   public String getEntityId() {
      return this.entityId;
   }

   public void setEntityId(String var1) {
      this.entityId = var1;
   }

   public boolean isNearPlayer() {
      return this.level.getNearestPlayer((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, 16.0D) != null;
   }

   public void tick() {
      this.oSpin = this.spin;
      if (this.isNearPlayer()) {
         double var1 = (double)((float)this.x + this.level.random.nextFloat());
         double var3 = (double)((float)this.y + this.level.random.nextFloat());
         double var5 = (double)((float)this.z + this.level.random.nextFloat());
         this.level.addParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
         this.level.addParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);

         for(this.spin += (double)(1000.0F / ((float)this.spawnDelay + 200.0F)); this.spin > 360.0D; this.oSpin -= 360.0D) {
            this.spin -= 360.0D;
         }

         if (this.spawnDelay == -1) {
            this.delay();
         }

         if (this.spawnDelay > 0) {
            --this.spawnDelay;
         } else {
            byte var7 = 4;

            for(int var8 = 0; var8 < var7; ++var8) {
               Mob var9 = (Mob)((Mob)EntityIO.newEntity(this.entityId, this.level));
               if (var9 == null) {
                  return;
               }

               int var10 = this.level.getEntitiesOfClass(var9.getClass(), AABB.newTemp((double)this.x, (double)this.y, (double)this.z, (double)(this.x + 1), (double)(this.y + 1), (double)(this.z + 1)).grow(8.0D, 4.0D, 8.0D)).size();
               if (var10 >= 6) {
                  this.delay();
                  return;
               }

               if (var9 != null) {
                  double var11 = (double)this.x + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 4.0D;
                  double var13 = (double)(this.y + this.level.random.nextInt(3) - 1);
                  double var15 = (double)this.z + (this.level.random.nextDouble() - this.level.random.nextDouble()) * 4.0D;
                  var9.moveTo(var11, var13, var15, this.level.random.nextFloat() * 360.0F, 0.0F);
                  if (var9.canSpawn()) {
                     this.level.addEntity(var9);

                     for(int var17 = 0; var17 < 20; ++var17) {
                        var1 = (double)this.x + 0.5D + ((double)this.level.random.nextFloat() - 0.5D) * 2.0D;
                        var3 = (double)this.y + 0.5D + ((double)this.level.random.nextFloat() - 0.5D) * 2.0D;
                        var5 = (double)this.z + 0.5D + ((double)this.level.random.nextFloat() - 0.5D) * 2.0D;
                        this.level.addParticle("smoke", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                        this.level.addParticle("flame", var1, var3, var5, 0.0D, 0.0D, 0.0D);
                     }

                     var9.spawnAnim();
                     this.delay();
                  }
               }
            }

            super.tick();
         }
      }
   }

   private void delay() {
      this.spawnDelay = 200 + this.level.random.nextInt(600);
   }

   public void load(CompoundTag var1) {
      super.load(var1);
      this.entityId = var1.getString("EntityId");
      this.spawnDelay = var1.getShort("Delay");
   }

   public void save(CompoundTag var1) {
      super.save(var1);
      var1.putString("EntityId", this.entityId);
      var1.putShort("Delay", (short)this.spawnDelay);
   }
}
