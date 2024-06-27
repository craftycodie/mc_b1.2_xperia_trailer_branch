package net.minecraft.world.level;

import java.util.Random;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

public class BiomeMap {
   private PerlinNoise temperature;
   private PerlinNoise downfall;

   public BiomeMap(Level var1) {
      Random var2 = new Random(var1.seed);
      this.temperature = new PerlinNoise(var2, 4);
      this.downfall = new PerlinNoise(var2, 4);
   }

   public double getDownfall(double var1, double var3) {
      float var5 = 0.125F;
      return this.downfall.getValue(var1 * (double)var5, var3 * (double)var5) * 5.0D;
   }

   public double getTemperature(double var1, double var3, double var5) {
      float var7 = 0.125F;
      double var8 = (this.temperature.getValue(var1 * (double)var7, var5 * (double)var7) - (var3 - 64.0D) / 16.0D) * 5.0D;
      return var8;
   }
}
