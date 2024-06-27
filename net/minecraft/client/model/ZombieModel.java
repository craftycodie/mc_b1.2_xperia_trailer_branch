package net.minecraft.client.model;

import util.Mth;

public class ZombieModel extends HumanoidModel {
   public void setupAnim(float time, float r, float bob, float yRot, float xRot, float scale) {
      super.setupAnim(time, r, bob, yRot, xRot, scale);
      float attack2 = Mth.sin(this.attackTime * 3.1415927F);
      float attack = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * 3.1415927F);
      this.arm0.zRot = 0.0F;
      this.arm1.zRot = 0.0F;
      this.arm0.yRot = -(0.1F - attack2 * 0.6F);
      this.arm1.yRot = 0.1F - attack2 * 0.6F;
      this.arm0.xRot = -1.5707964F;
      this.arm1.xRot = -1.5707964F;
      Cube var10000 = this.arm0;
      var10000.xRot -= attack2 * 1.2F - attack * 0.4F;
      var10000 = this.arm1;
      var10000.xRot -= attack2 * 1.2F - attack * 0.4F;
      var10000 = this.arm0;
      var10000.zRot += Mth.cos(bob * 0.09F) * 0.05F + 0.05F;
      var10000 = this.arm1;
      var10000.zRot -= Mth.cos(bob * 0.09F) * 0.05F + 0.05F;
      var10000 = this.arm0;
      var10000.xRot += Mth.sin(bob * 0.067F) * 0.05F;
      var10000 = this.arm1;
      var10000.xRot -= Mth.sin(bob * 0.067F) * 0.05F;
   }
}
