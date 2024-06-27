package net.minecraft.world.level.levelgen;

import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.CactusFeature;
import net.minecraft.world.level.levelgen.feature.ClayFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FlowerFeature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.PumpkinFeature;
import net.minecraft.world.level.levelgen.feature.ReedsFeature;
import net.minecraft.world.level.levelgen.feature.SpringFeature;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.SandTile;
import net.minecraft.world.level.tile.Tile;
import util.ProgressListener;

public class RandomLevelSource implements ChunkSource {
   private static final double SNOW_CUTOFF = 0.5D;
   private static final double SNOW_SCALE = 0.3D;
   private static final boolean FLOATING_ISLANDS = false;
   public static final int CHUNK_HEIGHT = 8;
   public static final int CHUNK_WIDTH = 4;
   private Random random;
   private PerlinNoise lperlinNoise1;
   private PerlinNoise lperlinNoise2;
   private PerlinNoise perlinNoise1;
   private PerlinNoise perlinNoise2;
   private PerlinNoise perlinNoise3;
   public PerlinNoise scaleNoise;
   public PerlinNoise depthNoise;
   private PerlinNoise floatingIslandScale;
   private PerlinNoise floatingIslandNoise;
   public PerlinNoise forestNoise;
   private Level level;
   private double[] buffer;
   private double[] sandBuffer = new double[256];
   private double[] gravelBuffer = new double[256];
   private double[] depthBuffer = new double[256];
   private LargeFeature caveFeature = new LargeCaveFeature();
   private Biome[] biomes;
   double[] pnr;
   double[] ar;
   double[] br;
   double[] sr;
   double[] dr;
   double[] fi;
   double[] fis;
   int[][] waterDepths = new int[32][32];
   private double[] temperatures;

   public RandomLevelSource(Level var1, long var2) {
      this.level = var1;
      this.random = new Random(var2);
      this.lperlinNoise1 = new PerlinNoise(this.random, 16);
      this.lperlinNoise2 = new PerlinNoise(this.random, 16);
      this.perlinNoise1 = new PerlinNoise(this.random, 8);
      this.perlinNoise2 = new PerlinNoise(this.random, 4);
      this.perlinNoise3 = new PerlinNoise(this.random, 4);
      this.scaleNoise = new PerlinNoise(this.random, 10);
      this.depthNoise = new PerlinNoise(this.random, 16);
      this.forestNoise = new PerlinNoise(this.random, 8);
   }

   public void prepareHeights(int var1, int var2, byte[] var3, Biome[] var4, double[] var5) {
      byte var6 = 4;
      byte var7 = 64;
      int var8 = var6 + 1;
      byte var9 = 17;
      int var10 = var6 + 1;
      this.buffer = this.getHeights(this.buffer, var1 * var6, 0, var2 * var6, var8, var9, var10);

      for(int var11 = 0; var11 < var6; ++var11) {
         for(int var12 = 0; var12 < var6; ++var12) {
            for(int var13 = 0; var13 < 16; ++var13) {
               double var14 = 0.125D;
               double var16 = this.buffer[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 0];
               double var18 = this.buffer[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 0];
               double var20 = this.buffer[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 0];
               double var22 = this.buffer[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 0];
               double var24 = (this.buffer[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 1] - var16) * var14;
               double var26 = (this.buffer[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 1] - var18) * var14;
               double var28 = (this.buffer[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 1] - var20) * var14;
               double var30 = (this.buffer[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 1] - var22) * var14;

               for(int var32 = 0; var32 < 8; ++var32) {
                  double var33 = 0.25D;
                  double var35 = var16;
                  double var37 = var18;
                  double var39 = (var20 - var16) * var33;
                  double var41 = (var22 - var18) * var33;

                  for(int var43 = 0; var43 < 4; ++var43) {
                     int var44 = var43 + var11 * 4 << 11 | 0 + var12 * 4 << 7 | var13 * 8 + var32;
                     short var45 = 128;
                     double var46 = 0.25D;
                     double var48 = var35;
                     double var50 = (var37 - var35) * var46;

                     for(int var52 = 0; var52 < 4; ++var52) {
                        double var53 = var5[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                        int var55 = 0;
                        if (var13 * 8 + var32 < var7) {
                           if (var53 < 0.5D && var13 * 8 + var32 >= var7 - 1) {
                              var55 = Tile.ice.id;
                           } else {
                              var55 = Tile.calmWater.id;
                           }
                        }

                        if (var48 > 0.0D) {
                           var55 = Tile.rock.id;
                        }

                        var3[var44] = (byte)var55;
                        var44 += var45;
                        var48 += var50;
                     }

                     var35 += var39;
                     var37 += var41;
                  }

                  var16 += var24;
                  var18 += var26;
                  var20 += var28;
                  var22 += var30;
               }
            }
         }
      }

   }

   public void buildSurfaces(int var1, int var2, byte[] var3, Biome[] var4) {
      byte var5 = 64;
      double var6 = 0.03125D;
      this.sandBuffer = this.perlinNoise2.getRegion(this.sandBuffer, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, var6, var6, 1.0D);
      this.gravelBuffer = this.perlinNoise2.getRegion(this.gravelBuffer, (double)(var2 * 16), 109.0134D, (double)(var1 * 16), 16, 1, 16, var6, 1.0D, var6);
      this.depthBuffer = this.perlinNoise3.getRegion(this.depthBuffer, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, var6 * 2.0D, var6 * 2.0D, var6 * 2.0D);

      for(int var8 = 0; var8 < 16; ++var8) {
         for(int var9 = 0; var9 < 16; ++var9) {
            Biome var10 = var4[var8 + var9 * 16];
            boolean var11 = this.sandBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2D > 0.0D;
            boolean var12 = this.gravelBuffer[var8 + var9 * 16] + this.random.nextDouble() * 0.2D > 3.0D;
            int var13 = (int)(this.depthBuffer[var8 + var9 * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
            int var14 = -1;
            byte var15 = var10.topMaterial;
            byte var16 = var10.material;

            for(int var17 = 127; var17 >= 0; --var17) {
               int var18 = (var8 * 16 + var9) * 128 + var17;
               if (var17 <= 0 + this.random.nextInt(5)) {
                  var3[var18] = (byte)Tile.unbreakable.id;
               } else {
                  byte var19 = var3[var18];
                  if (var19 == 0) {
                     var14 = -1;
                  } else if (var19 == Tile.rock.id) {
                     if (var14 == -1) {
                        if (var13 <= 0) {
                           var15 = 0;
                           var16 = (byte)Tile.rock.id;
                        } else if (var17 >= var5 - 4 && var17 <= var5 + 1) {
                           var15 = var10.topMaterial;
                           var16 = var10.material;
                           if (var12) {
                              var15 = 0;
                           }

                           if (var12) {
                              var16 = (byte)Tile.gravel.id;
                           }

                           if (var11) {
                              var15 = (byte)Tile.sand.id;
                           }

                           if (var11) {
                              var16 = (byte)Tile.sand.id;
                           }
                        }

                        if (var17 < var5 && var15 == 0) {
                           var15 = (byte)Tile.calmWater.id;
                        }

                        var14 = var13;
                        if (var17 >= var5 - 1) {
                           var3[var18] = var15;
                        } else {
                           var3[var18] = var16;
                        }
                     } else if (var14 > 0) {
                        --var14;
                        var3[var18] = var16;
                     }
                  }
               }
            }
         }
      }

   }

   public LevelChunk getChunk(int var1, int var2) {
      this.random.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
      byte[] var3 = new byte['耀'];
      LevelChunk var4 = new LevelChunk(this.level, var3, var1, var2);
      this.biomes = this.level.getBiomeSource().getBiomeBlock(this.biomes, var1 * 16, var2 * 16, 16, 16);
      double[] var5 = this.level.getBiomeSource().temperatures;
      this.prepareHeights(var1, var2, var3, this.biomes, var5);
      this.buildSurfaces(var1, var2, var3, this.biomes);
      this.caveFeature.apply(this, this.level, var1, var2, var3);
      var4.recalcHeightmap();
      return var4;
   }

   private double[] getHeights(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412D;
      double var10 = 684.412D;
      double[] var12 = this.level.getBiomeSource().temperatures;
      double[] var13 = this.level.getBiomeSource().downfalls;
      this.sr = this.scaleNoise.getRegion(this.sr, var2, var4, var5, var7, 1.121D, 1.121D, 0.5D);
      this.dr = this.depthNoise.getRegion(this.dr, var2, var4, var5, var7, 200.0D, 200.0D, 0.5D);
      this.pnr = this.perlinNoise1.getRegion(this.pnr, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8 / 80.0D, var10 / 160.0D, var8 / 80.0D);
      this.ar = this.lperlinNoise1.getRegion(this.ar, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      this.br = this.lperlinNoise2.getRegion(this.br, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      int var14 = 0;
      int var15 = 0;
      int var16 = 16 / var5;

      for(int var17 = 0; var17 < var5; ++var17) {
         int var18 = var17 * var16 + var16 / 2;

         for(int var19 = 0; var19 < var7; ++var19) {
            int var20 = var19 * var16 + var16 / 2;
            double var21 = var12[var18 * 16 + var20];
            double var23 = var13[var18 * 16 + var20] * var21;
            double var25 = 1.0D - var23;
            var25 *= var25;
            var25 *= var25;
            var25 = 1.0D - var25;
            double var27 = (this.sr[var15] + 256.0D) / 512.0D;
            var27 *= var25;
            if (var27 > 1.0D) {
               var27 = 1.0D;
            }

            double var29 = this.dr[var15] / 8000.0D;
            if (var29 < 0.0D) {
               var29 = -var29 * 0.3D;
            }

            var29 = var29 * 3.0D - 2.0D;
            if (var29 < 0.0D) {
               var29 /= 2.0D;
               if (var29 < -1.0D) {
                  var29 = -1.0D;
               }

               var29 /= 1.4D;
               var29 /= 2.0D;
               var27 = 0.0D;
            } else {
               if (var29 > 1.0D) {
                  var29 = 1.0D;
               }

               var29 /= 8.0D;
            }

            if (var27 < 0.0D) {
               var27 = 0.0D;
            }

            var27 += 0.5D;
            var29 = var29 * (double)var6 / 16.0D;
            double var31 = (double)var6 / 2.0D + var29 * 4.0D;
            ++var15;

            for(int var33 = 0; var33 < var6; ++var33) {
               double var34 = 0.0D;
               double var36 = ((double)var33 - var31) * 12.0D / var27;
               if (var36 < 0.0D) {
                  var36 *= 4.0D;
               }

               double var38 = this.ar[var14] / 512.0D;
               double var40 = this.br[var14] / 512.0D;
               double var42 = (this.pnr[var14] / 10.0D + 1.0D) / 2.0D;
               if (var42 < 0.0D) {
                  var34 = var38;
               } else if (var42 > 1.0D) {
                  var34 = var40;
               } else {
                  var34 = var38 + (var40 - var38) * var42;
               }

               var34 -= var36;
               if (var33 > var6 - 4) {
                  double var44 = (double)((float)(var33 - (var6 - 4)) / 3.0F);
                  var34 = var34 * (1.0D - var44) + -10.0D * var44;
               }

               var1[var14] = var34;
               ++var14;
            }
         }
      }

      return var1;
   }

   public boolean hasChunk(int var1, int var2) {
      return true;
   }

   private void calcWaterDepths(ChunkSource var1, int var2, int var3) {
      int var4 = var2 * 16;
      int var5 = var3 * 16;

      for(int var6 = 0; var6 < 16; ++var6) {
         int var7 = this.level.getSeaLevel();

         for(int var8 = 0; var8 < 16; ++var8) {
            int var9 = var4 + var6 + 7;
            int var10 = var5 + var8 + 7;
            int var11 = this.level.getHeightmap(var9, var10);
            if (var11 <= 0 && (this.level.getHeightmap(var9 - 1, var10) > 0 || this.level.getHeightmap(var9 + 1, var10) > 0 || this.level.getHeightmap(var9, var10 - 1) > 0 || this.level.getHeightmap(var9, var10 + 1) > 0)) {
               boolean var12 = false;
               if (var12 || this.level.getTile(var9 - 1, var7, var10) == Tile.calmWater.id && this.level.getData(var9 - 1, var7, var10) < 7) {
                  var12 = true;
               }

               if (var12 || this.level.getTile(var9 + 1, var7, var10) == Tile.calmWater.id && this.level.getData(var9 + 1, var7, var10) < 7) {
                  var12 = true;
               }

               if (var12 || this.level.getTile(var9, var7, var10 - 1) == Tile.calmWater.id && this.level.getData(var9, var7, var10 - 1) < 7) {
                  var12 = true;
               }

               if (var12 || this.level.getTile(var9, var7, var10 + 1) == Tile.calmWater.id && this.level.getData(var9, var7, var10 + 1) < 7) {
                  var12 = true;
               }

               if (var12) {
                  int var13;
                  for(var13 = -5; var13 <= 5; ++var13) {
                     for(int var14 = -5; var14 <= 5; ++var14) {
                        int var15 = (var13 > 0 ? var13 : -var13) + (var14 > 0 ? var14 : -var14);
                        if (var15 <= 5) {
                           var15 = 6 - var15;
                           if (this.level.getTile(var9 + var13, var7, var10 + var14) == Tile.calmWater.id) {
                              int var16 = this.level.getData(var9 + var13, var7, var10 + var14);
                              if (var16 < 7 && var16 < var15) {
                                 this.level.setData(var9 + var13, var7, var10 + var14, var15);
                              }
                           }
                        }
                     }
                  }

                  if (var12) {
                     this.level.setTileAndDataNoUpdate(var9, var7, var10, Tile.calmWater.id, 7);

                     for(var13 = 0; var13 < var7; ++var13) {
                        this.level.setTileAndDataNoUpdate(var9, var13, var10, Tile.calmWater.id, 8);
                     }
                  }
               }
            }
         }
      }

   }

   public void postProcess(ChunkSource var1, int var2, int var3) {
      SandTile.instaFall = true;
      int var4 = var2 * 16;
      int var5 = var3 * 16;
      Biome var6 = this.level.getBiomeSource().getBiome(var4 + 16, var5 + 16);
      this.random.setSeed(this.level.seed);
      long var7 = this.random.nextLong() / 2L * 2L + 1L;
      long var9 = this.random.nextLong() / 2L * 2L + 1L;
      this.random.setSeed((long)var2 * var7 + (long)var3 * var9 ^ this.level.seed);
      double var11 = 0.25D;
      int var13;
      int var14;
      int var15;
      if (this.random.nextInt(4) == 0) {
         var13 = var4 + this.random.nextInt(16) + 8;
         var14 = this.random.nextInt(128);
         var15 = var5 + this.random.nextInt(16) + 8;
         (new LakeFeature(Tile.calmWater.id)).place(this.level, this.random, var13, var14, var15);
      }

      if (this.random.nextInt(8) == 0) {
         var13 = var4 + this.random.nextInt(16) + 8;
         var14 = this.random.nextInt(this.random.nextInt(120) + 8);
         var15 = var5 + this.random.nextInt(16) + 8;
         if (var14 < 64 || this.random.nextInt(10) == 0) {
            (new LakeFeature(Tile.calmLava.id)).place(this.level, this.random, var13, var14, var15);
         }
      }

      int var16;
      for(var13 = 0; var13 < 8; ++var13) {
         var14 = var4 + this.random.nextInt(16) + 8;
         var15 = this.random.nextInt(128);
         var16 = var5 + this.random.nextInt(16) + 8;
         (new MonsterRoomFeature()).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 10; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(128);
         var16 = var5 + this.random.nextInt(16);
         (new ClayFeature(32)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 20; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(128);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.dirt.id, 32)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 10; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(128);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.gravel.id, 32)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 20; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(128);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.coalOre.id, 16)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 20; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(64);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.ironOre.id, 8)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 2; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(32);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.goldOre.id, 8)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 8; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(16);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.redStoneOre.id, 7)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 1; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(16);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.emeraldOre.id, 7)).place(this.level, this.random, var14, var15, var16);
      }

      for(var13 = 0; var13 < 1; ++var13) {
         var14 = var4 + this.random.nextInt(16);
         var15 = this.random.nextInt(16) + this.random.nextInt(16);
         var16 = var5 + this.random.nextInt(16);
         (new OreFeature(Tile.lapisOre.id, 6)).place(this.level, this.random, var14, var15, var16);
      }

      var11 = 0.5D;
      var13 = (int)((this.forestNoise.getValue((double)var4 * var11, (double)var5 * var11) / 8.0D + this.random.nextDouble() * 4.0D + 4.0D) / 3.0D);
      var14 = 0;
      if (this.random.nextInt(10) == 0) {
         ++var14;
      }

      if (var6 == Biome.forest) {
         var14 += var13 + 5;
      }

      if (var6 == Biome.rainForest) {
         var14 += var13 + 5;
      }

      if (var6 == Biome.seasonalForest) {
         var14 += var13 + 2;
      }

      if (var6 == Biome.taiga) {
         var14 += var13 + 5;
      }

      if (var6 == Biome.desert) {
         var14 -= 20;
      }

      if (var6 == Biome.tundra) {
         var14 -= 20;
      }

      if (var6 == Biome.plains) {
         var14 -= 20;
      }

      int var17;
      for(var15 = 0; var15 < var14; ++var15) {
         var16 = var4 + this.random.nextInt(16) + 8;
         var17 = var5 + this.random.nextInt(16) + 8;
         Feature var18 = var6.getTreeFeature(this.random);
         var18.init(1.0D, 1.0D, 1.0D);
         var18.place(this.level, this.random, var16, this.level.getHeightmap(var16, var17), var17);
      }

      int var23;
      for(var15 = 0; var15 < 2; ++var15) {
         var16 = var4 + this.random.nextInt(16) + 8;
         var17 = this.random.nextInt(128);
         var23 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.flower.id)).place(this.level, this.random, var16, var17, var23);
      }

      if (this.random.nextInt(2) == 0) {
         var15 = var4 + this.random.nextInt(16) + 8;
         var16 = this.random.nextInt(128);
         var17 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.rose.id)).place(this.level, this.random, var15, var16, var17);
      }

      if (this.random.nextInt(4) == 0) {
         var15 = var4 + this.random.nextInt(16) + 8;
         var16 = this.random.nextInt(128);
         var17 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.mushroom1.id)).place(this.level, this.random, var15, var16, var17);
      }

      if (this.random.nextInt(8) == 0) {
         var15 = var4 + this.random.nextInt(16) + 8;
         var16 = this.random.nextInt(128);
         var17 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.mushroom2.id)).place(this.level, this.random, var15, var16, var17);
      }

      for(var15 = 0; var15 < 10; ++var15) {
         var16 = var4 + this.random.nextInt(16) + 8;
         var17 = this.random.nextInt(128);
         var23 = var5 + this.random.nextInt(16) + 8;
         (new ReedsFeature()).place(this.level, this.random, var16, var17, var23);
      }

      if (this.random.nextInt(32) == 0) {
         var15 = var4 + this.random.nextInt(16) + 8;
         var16 = this.random.nextInt(128);
         var17 = var5 + this.random.nextInt(16) + 8;
         (new PumpkinFeature()).place(this.level, this.random, var15, var16, var17);
      }

      var15 = 0;
      if (var6 == Biome.desert) {
         var15 += 10;
      }

      int var19;
      for(var16 = 0; var16 < var15; ++var16) {
         var17 = var4 + this.random.nextInt(16) + 8;
         var23 = this.random.nextInt(128);
         var19 = var5 + this.random.nextInt(16) + 8;
         (new CactusFeature()).place(this.level, this.random, var17, var23, var19);
      }

      for(var16 = 0; var16 < 50; ++var16) {
         var17 = var4 + this.random.nextInt(16) + 8;
         var23 = this.random.nextInt(this.random.nextInt(120) + 8);
         var19 = var5 + this.random.nextInt(16) + 8;
         (new SpringFeature(Tile.water.id)).place(this.level, this.random, var17, var23, var19);
      }

      for(var16 = 0; var16 < 20; ++var16) {
         var17 = var4 + this.random.nextInt(16) + 8;
         var23 = this.random.nextInt(this.random.nextInt(this.random.nextInt(112) + 8) + 8);
         var19 = var5 + this.random.nextInt(16) + 8;
         (new SpringFeature(Tile.lava.id)).place(this.level, this.random, var17, var23, var19);
      }

      this.temperatures = this.level.getBiomeSource().getTemperatureBlock(this.temperatures, var4 + 8, var5 + 8, 16, 16);

      for(var16 = var4 + 8; var16 < var4 + 8 + 16; ++var16) {
         for(var17 = var5 + 8; var17 < var5 + 8 + 16; ++var17) {
            var23 = var16 - (var4 + 8);
            var19 = var17 - (var5 + 8);
            int var20 = this.level.getTopSolidBlock(var16, var17);
            double var21 = this.temperatures[var23 * 16 + var19] - (double)(var20 - 64) / 64.0D * 0.3D;
            if (var21 < 0.5D && var20 > 0 && var20 < 128 && this.level.isEmptyTile(var16, var20, var17) && this.level.getMaterial(var16, var20 - 1, var17).blocksMotion() && this.level.getMaterial(var16, var20 - 1, var17) != Material.ice) {
               this.level.setTile(var16, var20, var17, Tile.topSnow.id);
            }
         }
      }

      SandTile.instaFall = false;
   }

   public boolean save(boolean var1, ProgressListener var2) {
      return true;
   }

   public boolean tick() {
      return false;
   }

   public boolean shouldSave() {
      return true;
   }

   public String gatherStats() {
      return "RandomLevelSource";
   }
}
