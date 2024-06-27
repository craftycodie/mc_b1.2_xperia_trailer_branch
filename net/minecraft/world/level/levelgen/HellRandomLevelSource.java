package net.minecraft.world.level.levelgen;

import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.feature.FlowerFeature;
import net.minecraft.world.level.levelgen.feature.HellFireFeature;
import net.minecraft.world.level.levelgen.feature.HellPortalFeature;
import net.minecraft.world.level.levelgen.feature.HellSpringFeature;
import net.minecraft.world.level.levelgen.feature.LightGemFeature;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.tile.SandTile;
import net.minecraft.world.level.tile.Tile;
import util.ProgressListener;

public class HellRandomLevelSource implements ChunkSource {
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
   private Level level;
   private double[] buffer;
   double[] caveBuffer1;
   double[] caveBuffer2;
   double[] caveBuffer3;
   private double[] sandBuffer = new double[256];
   private double[] gravelBuffer = new double[256];
   private double[] depthBuffer = new double[256];
   private LargeFeature caveFeature = new LargeHellCaveFeature();
   double[] pnr;
   double[] ar;
   double[] br;
   double[] sr;
   double[] dr;
   double[] fi;
   double[] fis;

   public HellRandomLevelSource(Level var1, long var2) {
      this.level = var1;
      this.random = new Random(var2);
      this.lperlinNoise1 = new PerlinNoise(this.random, 16);
      this.lperlinNoise2 = new PerlinNoise(this.random, 16);
      this.perlinNoise1 = new PerlinNoise(this.random, 8);
      this.perlinNoise2 = new PerlinNoise(this.random, 4);
      this.perlinNoise3 = new PerlinNoise(this.random, 4);
      this.scaleNoise = new PerlinNoise(this.random, 10);
      this.depthNoise = new PerlinNoise(this.random, 16);
   }

   public void prepareHeights(int var1, int var2, byte[] var3) {
      byte var4 = 4;
      byte var5 = 32;
      int var6 = var4 + 1;
      byte var7 = 17;
      int var8 = var4 + 1;
      this.buffer = this.getHeights(this.buffer, var1 * var4, 0, var2 * var4, var6, var7, var8);

      for(int var9 = 0; var9 < var4; ++var9) {
         for(int var10 = 0; var10 < var4; ++var10) {
            for(int var11 = 0; var11 < 16; ++var11) {
               double var12 = 0.125D;
               double var14 = this.buffer[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 0];
               double var16 = this.buffer[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 0];
               double var18 = this.buffer[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 0];
               double var20 = this.buffer[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 0];
               double var22 = (this.buffer[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 1] - var14) * var12;
               double var24 = (this.buffer[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
               double var26 = (this.buffer[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 1] - var18) * var12;
               double var28 = (this.buffer[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;

               for(int var30 = 0; var30 < 8; ++var30) {
                  double var31 = 0.25D;
                  double var33 = var14;
                  double var35 = var16;
                  double var37 = (var18 - var14) * var31;
                  double var39 = (var20 - var16) * var31;

                  for(int var41 = 0; var41 < 4; ++var41) {
                     int var42 = var41 + var9 * 4 << 11 | 0 + var10 * 4 << 7 | var11 * 8 + var30;
                     short var43 = 128;
                     double var44 = 0.25D;
                     double var46 = var33;
                     double var48 = (var35 - var33) * var44;

                     for(int var50 = 0; var50 < 4; ++var50) {
                        int var51 = 0;
                        if (var11 * 8 + var30 < var5) {
                           var51 = Tile.calmLava.id;
                        }

                        if (var46 > 0.0D) {
                           var51 = Tile.hellRock.id;
                        }

                        var3[var42] = (byte)var51;
                        var42 += var43;
                        var46 += var48;
                     }

                     var33 += var37;
                     var35 += var39;
                  }

                  var14 += var22;
                  var16 += var24;
                  var18 += var26;
                  var20 += var28;
               }
            }
         }
      }

   }

   public void buildSurfaces(int var1, int var2, byte[] var3) {
      byte var4 = 64;
      double var5 = 0.03125D;
      this.sandBuffer = this.perlinNoise2.getRegion(this.sandBuffer, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, var5, var5, 1.0D);
      this.gravelBuffer = this.perlinNoise2.getRegion(this.gravelBuffer, (double)(var2 * 16), 109.0134D, (double)(var1 * 16), 16, 1, 16, var5, 1.0D, var5);
      this.depthBuffer = this.perlinNoise3.getRegion(this.depthBuffer, (double)(var1 * 16), (double)(var2 * 16), 0.0D, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

      for(int var7 = 0; var7 < 16; ++var7) {
         for(int var8 = 0; var8 < 16; ++var8) {
            boolean var9 = this.sandBuffer[var7 + var8 * 16] + this.random.nextDouble() * 0.2D > 0.0D;
            boolean var10 = this.gravelBuffer[var7 + var8 * 16] + this.random.nextDouble() * 0.2D > 0.0D;
            int var11 = (int)(this.depthBuffer[var7 + var8 * 16] / 3.0D + 3.0D + this.random.nextDouble() * 0.25D);
            int var12 = -1;
            byte var13 = (byte)Tile.hellRock.id;
            byte var14 = (byte)Tile.hellRock.id;

            for(int var15 = 127; var15 >= 0; --var15) {
               int var16 = (var7 * 16 + var8) * 128 + var15;
               if (var15 >= 127 - this.random.nextInt(5)) {
                  var3[var16] = (byte)Tile.unbreakable.id;
               } else if (var15 <= 0 + this.random.nextInt(5)) {
                  var3[var16] = (byte)Tile.unbreakable.id;
               } else {
                  byte var17 = var3[var16];
                  if (var17 == 0) {
                     var12 = -1;
                  } else if (var17 == Tile.hellRock.id) {
                     if (var12 == -1) {
                        if (var11 <= 0) {
                           var13 = 0;
                           var14 = (byte)Tile.hellRock.id;
                        } else if (var15 >= var4 - 4 && var15 <= var4 + 1) {
                           var13 = (byte)Tile.hellRock.id;
                           var14 = (byte)Tile.hellRock.id;
                           if (var10) {
                              var13 = (byte)Tile.gravel.id;
                           }

                           if (var10) {
                              var14 = (byte)Tile.hellRock.id;
                           }

                           if (var9) {
                              var13 = (byte)Tile.hellSand.id;
                           }

                           if (var9) {
                              var14 = (byte)Tile.hellSand.id;
                           }
                        }

                        if (var15 < var4 && var13 == 0) {
                           var13 = (byte)Tile.calmLava.id;
                        }

                        var12 = var11;
                        if (var15 >= var4 - 1) {
                           var3[var16] = var13;
                        } else {
                           var3[var16] = var14;
                        }
                     } else if (var12 > 0) {
                        --var12;
                        var3[var16] = var14;
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
      this.prepareHeights(var1, var2, var3);
      this.buildSurfaces(var1, var2, var3);
      this.caveFeature.apply(this, this.level, var1, var2, var3);
      LevelChunk var4 = new LevelChunk(this.level, var3, var1, var2);
      return var4;
   }

   private double[] getHeights(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412D;
      double var10 = 2053.236D;
      this.sr = this.scaleNoise.getRegion(this.sr, (double)var2, (double)var3, (double)var4, var5, 1, var7, 1.0D, 0.0D, 1.0D);
      this.dr = this.depthNoise.getRegion(this.dr, (double)var2, (double)var3, (double)var4, var5, 1, var7, 100.0D, 0.0D, 100.0D);
      this.pnr = this.perlinNoise1.getRegion(this.pnr, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8 / 80.0D, var10 / 60.0D, var8 / 80.0D);
      this.ar = this.lperlinNoise1.getRegion(this.ar, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      this.br = this.lperlinNoise2.getRegion(this.br, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      int var12 = 0;
      int var13 = 0;
      double[] var14 = new double[var6];

      int var15;
      for(var15 = 0; var15 < var6; ++var15) {
         var14[var15] = Math.cos((double)var15 * 3.141592653589793D * 6.0D / (double)var6) * 2.0D;
         double var16 = (double)var15;
         if (var15 > var6 / 2) {
            var16 = (double)(var6 - 1 - var15);
         }

         if (var16 < 4.0D) {
            var16 = 4.0D - var16;
            var14[var15] -= var16 * var16 * var16 * 10.0D;
         }
      }

      for(var15 = 0; var15 < var5; ++var15) {
         for(int var36 = 0; var36 < var7; ++var36) {
            double var17 = (this.sr[var13] + 256.0D) / 512.0D;
            if (var17 > 1.0D) {
               var17 = 1.0D;
            }

            double var19 = 0.0D;
            double var21 = this.dr[var13] / 8000.0D;
            if (var21 < 0.0D) {
               var21 = -var21;
            }

            var21 = var21 * 3.0D - 3.0D;
            if (var21 < 0.0D) {
               var21 /= 2.0D;
               if (var21 < -1.0D) {
                  var21 = -1.0D;
               }

               var21 /= 1.4D;
               var21 /= 2.0D;
               var17 = 0.0D;
            } else {
               if (var21 > 1.0D) {
                  var21 = 1.0D;
               }

               var21 /= 6.0D;
            }

            var17 += 0.5D;
            var21 = var21 * (double)var6 / 16.0D;
            ++var13;

            for(int var23 = 0; var23 < var6; ++var23) {
               double var24 = 0.0D;
               double var26 = var14[var23];
               double var28 = this.ar[var12] / 512.0D;
               double var30 = this.br[var12] / 512.0D;
               double var32 = (this.pnr[var12] / 10.0D + 1.0D) / 2.0D;
               if (var32 < 0.0D) {
                  var24 = var28;
               } else if (var32 > 1.0D) {
                  var24 = var30;
               } else {
                  var24 = var28 + (var30 - var28) * var32;
               }

               var24 -= var26;
               double var34;
               if (var23 > var6 - 4) {
                  var34 = (double)((float)(var23 - (var6 - 4)) / 3.0F);
                  var24 = var24 * (1.0D - var34) + -10.0D * var34;
               }

               if ((double)var23 < var19) {
                  var34 = (var19 - (double)var23) / 4.0D;
                  if (var34 < 0.0D) {
                     var34 = 0.0D;
                  }

                  if (var34 > 1.0D) {
                     var34 = 1.0D;
                  }

                  var24 = var24 * (1.0D - var34) + -10.0D * var34;
               }

               var1[var12] = var24;
               ++var12;
            }
         }
      }

      return var1;
   }

   public boolean hasChunk(int var1, int var2) {
      return true;
   }

   public void postProcess(ChunkSource var1, int var2, int var3) {
      SandTile.instaFall = true;
      int var4 = var2 * 16;
      int var5 = var3 * 16;

      int var6;
      int var7;
      int var8;
      int var9;
      for(var6 = 0; var6 < 8; ++var6) {
         var7 = var4 + this.random.nextInt(16) + 8;
         var8 = this.random.nextInt(120) + 4;
         var9 = var5 + this.random.nextInt(16) + 8;
         (new HellSpringFeature(Tile.lava.id)).place(this.level, this.random, var7, var8, var9);
      }

      var6 = this.random.nextInt(this.random.nextInt(10) + 1) + 1;

      int var10;
      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var4 + this.random.nextInt(16) + 8;
         var9 = this.random.nextInt(120) + 4;
         var10 = var5 + this.random.nextInt(16) + 8;
         (new HellFireFeature()).place(this.level, this.random, var8, var9, var10);
      }

      var6 = this.random.nextInt(this.random.nextInt(10) + 1);

      for(var7 = 0; var7 < var6; ++var7) {
         var8 = var4 + this.random.nextInt(16) + 8;
         var9 = this.random.nextInt(120) + 4;
         var10 = var5 + this.random.nextInt(16) + 8;
         (new LightGemFeature()).place(this.level, this.random, var8, var9, var10);
      }

      for(var7 = 0; var7 < 10; ++var7) {
         var8 = var4 + this.random.nextInt(16) + 8;
         var9 = this.random.nextInt(128);
         var10 = var5 + this.random.nextInt(16) + 8;
         (new HellPortalFeature()).place(this.level, this.random, var8, var9, var10);
      }

      if (this.random.nextInt(1) == 0) {
         var7 = var4 + this.random.nextInt(16) + 8;
         var8 = this.random.nextInt(128);
         var9 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.mushroom1.id)).place(this.level, this.random, var7, var8, var9);
      }

      if (this.random.nextInt(1) == 0) {
         var7 = var4 + this.random.nextInt(16) + 8;
         var8 = this.random.nextInt(128);
         var9 = var5 + this.random.nextInt(16) + 8;
         (new FlowerFeature(Tile.mushroom2.id)).place(this.level, this.random, var7, var8, var9);
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
      return "HellRandomLevelSource";
   }
}
