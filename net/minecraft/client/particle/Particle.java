package net.minecraft.client.particle;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.renderer.Tesselator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import util.Mth;

public class Particle extends Entity {
   protected int tex;
   protected float uo;
   protected float vo;
   protected int age = 0;
   protected int lifetime = 0;
   protected float size;
   protected float gravity;
   protected float rCol;
   protected float gCol;
   protected float bCol;
   public static double xOff;
   public static double yOff;
   public static double zOff;

   public Particle(Level level, double x, double y, double z, double xa, double ya, double za) {
      super(level);
      this.setSize(0.2F, 0.2F);
      this.heightOffset = this.bbHeight / 2.0F;
      this.setPos(x, y, z);
      this.rCol = this.gCol = this.bCol = 1.0F;
      this.xd = xa + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
      this.yd = ya + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
      this.zd = za + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
      float speed = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
      float dd = Mth.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
      this.xd = this.xd / (double)dd * (double)speed * 0.4000000059604645D;
      this.yd = this.yd / (double)dd * (double)speed * 0.4000000059604645D + 0.10000000149011612D;
      this.zd = this.zd / (double)dd * (double)speed * 0.4000000059604645D;
      this.uo = this.random.nextFloat() * 3.0F;
      this.vo = this.random.nextFloat() * 3.0F;
      this.size = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;
      this.lifetime = (int)(4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
      this.age = 0;
      this.makeStepSound = false;
   }

   public Particle setPower(float power) {
      this.xd *= (double)power;
      this.yd = (this.yd - 0.10000000149011612D) * (double)power + 0.10000000149011612D;
      this.zd *= (double)power;
      return this;
   }

   public Particle scale(float scale) {
      this.setSize(0.2F * scale, 0.2F * scale);
      this.size *= scale;
      return this;
   }

   protected void defineSynchedData() {
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      this.yd -= 0.04D * (double)this.gravity;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.9800000190734863D;
      this.yd *= 0.9800000190734863D;
      this.zd *= 0.9800000190734863D;
      if (this.onGround) {
         this.xd *= 0.699999988079071D;
         this.zd *= 0.699999988079071D;
      }

   }

   public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2) {
      float u0 = (float)(this.tex % 16) / 16.0F;
      float u1 = u0 + 0.0624375F;
      float v0 = (float)(this.tex / 16) / 16.0F;
      float v1 = v0 + 0.0624375F;
      float r = 0.1F * this.size;
      float x = (float)(this.xo + (this.x - this.xo) * (double)a - xOff);
      float y = (float)(this.yo + (this.y - this.yo) * (double)a - yOff);
      float z = (float)(this.zo + (this.z - this.zo) * (double)a - zOff);
      float br = this.getBrightness(a);
      t.color(this.rCol * br, this.gCol * br, this.bCol * br);
      t.vertexUV((double)(x - xa * r - xa2 * r), (double)(y - ya * r), (double)(z - za * r - za2 * r), (double)u1, (double)v1);
      t.vertexUV((double)(x - xa * r + xa2 * r), (double)(y + ya * r), (double)(z - za * r + za2 * r), (double)u1, (double)v0);
      t.vertexUV((double)(x + xa * r + xa2 * r), (double)(y + ya * r), (double)(z + za * r + za2 * r), (double)u0, (double)v0);
      t.vertexUV((double)(x + xa * r - xa2 * r), (double)(y - ya * r), (double)(z + za * r - za2 * r), (double)u0, (double)v1);
   }

   public int getParticleTexture() {
      return 0;
   }

   public void addAdditonalSaveData(CompoundTag entityTag) {
   }

   public void readAdditionalSaveData(CompoundTag tag) {
   }
}
