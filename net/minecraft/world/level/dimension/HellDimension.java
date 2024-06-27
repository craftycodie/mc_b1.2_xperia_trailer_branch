package net.minecraft.world.level.dimension;

import java.io.File;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.chunk.storage.OldChunkStorage;
import net.minecraft.world.level.levelgen.HellRandomLevelSource;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.phys.Vec3;

public class HellDimension extends Dimension {
   public void init() {
      this.biomeSource = new FixedBiomeSource(Biome.hell, 1.0D, 0.0D);
      this.foggy = true;
      this.ultraWarm = true;
      this.hasCeiling = true;
      this.id = -1;
   }

   public Vec3 getFogColor(float var1, float var2) {
      return Vec3.newTemp(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
   }

   protected void updateLightRamp() {
      float var1 = 0.1F;

      for(int var2 = 0; var2 <= 15; ++var2) {
         float var3 = 1.0F - (float)var2 / 15.0F;
         this.brightnessRamp[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
      }

   }

   public ChunkSource createRandomLevelSource() {
      return new HellRandomLevelSource(this.level, this.level.seed);
   }

   public ChunkStorage createStorage(File var1) {
      File var2 = new File(var1, "DIM-1");
      var2.mkdirs();
      return new OldChunkStorage(var2, true);
   }

   public boolean isValidSpawn(int var1, int var2) {
      int var3 = this.level.getTopTile(var1, var2);
      if (var3 == Tile.unbreakable.id) {
         return false;
      } else if (var3 == 0) {
         return false;
      } else {
         return Tile.solid[var3];
      }
   }

   public float getTimeOfDay(long var1, float var3) {
      return 0.5F;
   }

   public boolean mayRespawn() {
      return false;
   }
}
