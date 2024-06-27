package net.minecraft.world.level.levelgen.synth;

public class Scale extends Synth {
   private Synth synth;
   private double xScale;
   private double yScale;

   public Scale(Synth var1, double var2, double var4) {
      this.synth = var1;
      this.xScale = 1.0D / var2;
      this.yScale = 1.0D / var4;
   }

   public double getValue(double var1, double var3) {
      return this.synth.getValue(var1 * this.xScale, var3 * this.yScale);
   }
}
