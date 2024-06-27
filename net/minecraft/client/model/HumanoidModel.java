package net.minecraft.client.model;

import util.Mth;

public class HumanoidModel extends Model {
   public Cube head;
   public Cube hair;
   public Cube body;
   public Cube arm0;
   public Cube arm1;
   public Cube leg0;
   public Cube leg1;
   public Cube ear;
   public Cube cloak;
   public boolean holdingLeftHand;
   public boolean holdingRightHand;
   public boolean sneaking;

   public HumanoidModel() {
      this(0.0F);
   }

   public HumanoidModel(float g) {
      this(g, 0.0F);
   }

   public HumanoidModel(float g, float yOffset) {
      this.holdingLeftHand = false;
      this.holdingRightHand = false;
      this.sneaking = false;
      this.cloak = new Cube(0, 0);
      this.cloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, g);
      this.ear = new Cube(24, 0);
      this.ear.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, g);
      this.head = new Cube(0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, g);
      this.head.setPos(0.0F, 0.0F + yOffset, 0.0F);
      this.hair = new Cube(32, 0);
      this.hair.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, g + 0.5F);
      this.hair.setPos(0.0F, 0.0F + yOffset, 0.0F);
      this.body = new Cube(16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, g);
      this.body.setPos(0.0F, 0.0F + yOffset, 0.0F);
      this.arm0 = new Cube(40, 16);
      this.arm0.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, g);
      this.arm0.setPos(-5.0F, 2.0F + yOffset, 0.0F);
      this.arm1 = new Cube(40, 16);
      this.arm1.mirror = true;
      this.arm1.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, g);
      this.arm1.setPos(5.0F, 2.0F + yOffset, 0.0F);
      this.leg0 = new Cube(0, 16);
      this.leg0.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, g);
      this.leg0.setPos(-2.0F, 12.0F + yOffset, 0.0F);
      this.leg1 = new Cube(0, 16);
      this.leg1.mirror = true;
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, g);
      this.leg1.setPos(2.0F, 12.0F + yOffset, 0.0F);
   }

   public void render(float time, float r, float bob, float yRot, float xRot, float scale) {
      this.setupAnim(time, r, bob, yRot, xRot, scale);
      this.head.render(scale);
      this.body.render(scale);
      this.arm0.render(scale);
      this.arm1.render(scale);
      this.leg0.render(scale);
      this.leg1.render(scale);
      this.hair.render(scale);
   }

   public void setupAnim(float time, float r, float bob, float yRot, float xRot, float scale) {
      this.head.yRot = yRot / 57.295776F;
      this.head.xRot = xRot / 57.295776F;
      this.hair.yRot = this.head.yRot;
      this.hair.xRot = this.head.xRot;
      this.arm0.xRot = Mth.cos(time * 0.6662F + 3.1415927F) * 2.0F * r * 0.5F;
      this.arm1.xRot = Mth.cos(time * 0.6662F) * 2.0F * r * 0.5F;
      this.arm0.zRot = 0.0F;
      this.arm1.zRot = 0.0F;
      this.leg0.xRot = Mth.cos(time * 0.6662F) * 1.4F * r;
      this.leg1.xRot = Mth.cos(time * 0.6662F + 3.1415927F) * 1.4F * r;
      this.leg0.yRot = 0.0F;
      this.leg1.yRot = 0.0F;
      Cube var10000;
      if (this.riding) {
         var10000 = this.arm0;
         var10000.xRot += -0.62831855F;
         var10000 = this.arm1;
         var10000.xRot += -0.62831855F;
         this.leg0.xRot = -1.2566371F;
         this.leg1.xRot = -1.2566371F;
         this.leg0.yRot = 0.31415927F;
         this.leg1.yRot = -0.31415927F;
      }

      if (this.holdingLeftHand) {
         this.arm1.xRot = this.arm1.xRot * 0.5F - 0.31415927F;
      }

      if (this.holdingRightHand) {
         this.arm0.xRot = this.arm0.xRot * 0.5F - 0.31415927F;
      }

      this.arm0.yRot = 0.0F;
      this.arm1.yRot = 0.0F;
      if (this.attackTime > -9990.0F) {
         float swing = this.attackTime;
         this.body.yRot = Mth.sin(Mth.sqrt(swing) * 3.1415927F * 2.0F) * 0.2F;
         this.arm0.z = Mth.sin(this.body.yRot) * 5.0F;
         this.arm0.x = -Mth.cos(this.body.yRot) * 5.0F;
         this.arm1.z = -Mth.sin(this.body.yRot) * 5.0F;
         this.arm1.x = Mth.cos(this.body.yRot) * 5.0F;
         var10000 = this.arm0;
         var10000.yRot += this.body.yRot;
         var10000 = this.arm1;
         var10000.yRot += this.body.yRot;
         var10000 = this.arm1;
         var10000.xRot += this.body.yRot;
         swing = 1.0F - this.attackTime;
         swing *= swing;
         swing *= swing;
         swing = 1.0F - swing;
         float aa = Mth.sin(swing * 3.1415927F);
         float bb = Mth.sin(this.attackTime * 3.1415927F) * -(this.head.xRot - 0.7F) * 0.75F;
         var10000 = this.arm0;
         var10000.xRot = (float)((double)var10000.xRot - ((double)aa * 1.2D + (double)bb));
         var10000 = this.arm0;
         var10000.yRot += this.body.yRot * 2.0F;
         this.arm0.zRot = Mth.sin(this.attackTime * 3.1415927F) * -0.4F;
      }

      if (this.sneaking) {
         this.body.xRot = 0.5F;
         var10000 = this.leg0;
         var10000.xRot -= 0.0F;
         var10000 = this.leg1;
         var10000.xRot -= 0.0F;
         var10000 = this.arm0;
         var10000.xRot += 0.4F;
         var10000 = this.arm1;
         var10000.xRot += 0.4F;
         this.leg0.z = 4.0F;
         this.leg1.z = 4.0F;
         this.leg0.y = 9.0F;
         this.leg1.y = 9.0F;
         this.head.y = 1.0F;
      } else {
         this.body.xRot = 0.0F;
         this.leg0.z = 0.0F;
         this.leg1.z = 0.0F;
         this.leg0.y = 12.0F;
         this.leg1.y = 12.0F;
         this.head.y = 0.0F;
      }

      var10000 = this.arm0;
      var10000.zRot += Mth.cos(bob * 0.09F) * 0.05F + 0.05F;
      var10000 = this.arm1;
      var10000.zRot -= Mth.cos(bob * 0.09F) * 0.05F + 0.05F;
      var10000 = this.arm0;
      var10000.xRot += Mth.sin(bob * 0.067F) * 0.05F;
      var10000 = this.arm1;
      var10000.xRot -= Mth.sin(bob * 0.067F) * 0.05F;
   }

   public void renderHair(float scale) {
      this.hair.yRot = this.head.yRot;
      this.hair.xRot = this.head.xRot;
      this.hair.render(scale);
   }

   public void renderEars(float scale) {
      this.ear.yRot = this.head.yRot;
      this.ear.xRot = this.head.xRot;
      this.ear.x = 0.0F;
      this.ear.y = 0.0F;
      this.ear.render(scale);
   }

   public void renderCloak(float scale) {
      this.cloak.render(scale);
   }

   public void render(HumanoidModel model, float scale) {
      this.head.yRot = model.head.yRot;
      this.head.y = model.head.y;
      this.head.xRot = model.head.xRot;
      this.hair.yRot = this.head.yRot;
      this.hair.xRot = this.head.xRot;
      this.arm0.xRot = model.arm0.xRot;
      this.arm0.zRot = model.arm0.zRot;
      this.arm1.xRot = model.arm1.xRot;
      this.arm1.zRot = model.arm1.zRot;
      this.leg0.xRot = model.leg0.xRot;
      this.leg1.xRot = model.leg1.xRot;
      this.head.render(scale);
      this.body.render(scale);
      this.arm0.render(scale);
      this.arm1.render(scale);
      this.leg0.render(scale);
      this.leg1.render(scale);
      this.hair.render(scale);
   }
}
