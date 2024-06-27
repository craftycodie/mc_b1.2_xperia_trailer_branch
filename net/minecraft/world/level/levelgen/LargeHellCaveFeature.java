package net.minecraft.world.level.levelgen;

import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.tile.Tile;
import util.Mth;

public class LargeHellCaveFeature extends LargeFeature {
   protected void addRoom(int var1, int var2, byte[] var3, double var4, double var6, double var8) {
      this.addTunnel(var1, var2, var3, var4, var6, var8, 1.0F + this.random.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
   }

   protected void addTunnel(int var1, int var2, byte[] var3, double var4, double var6, double var8, float var10, float var11, float var12, int var13, int var14, double var15) {
      double var17 = (double)(var1 * 16 + 8);
      double var19 = (double)(var2 * 16 + 8);
      float var21 = 0.0F;
      float var22 = 0.0F;
      Random var23 = new Random(this.random.nextLong());
      if (var14 <= 0) {
         int var24 = this.radius * 16 - 16;
         var14 = var24 - var23.nextInt(var24 / 4);
      }

      boolean var51 = false;
      if (var13 == -1) {
         var13 = var14 / 2;
         var51 = true;
      }

      int var25 = var23.nextInt(var14 / 2) + var14 / 4;

      for(boolean var26 = var23.nextInt(6) == 0; var13 < var14; ++var13) {
         double var27 = 1.5D + (double)(Mth.sin((float)var13 * 3.1415927F / (float)var14) * var10 * 1.0F);
         double var29 = var27 * var15;
         float var31 = Mth.cos(var12);
         float var32 = Mth.sin(var12);
         var4 += (double)(Mth.cos(var11) * var31);
         var6 += (double)var32;
         var8 += (double)(Mth.sin(var11) * var31);
         if (var26) {
            var12 *= 0.92F;
         } else {
            var12 *= 0.7F;
         }

         var12 += var22 * 0.1F;
         var11 += var21 * 0.1F;
         var22 *= 0.9F;
         var21 *= 0.75F;
         var22 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 2.0F;
         var21 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 4.0F;
         if (!var51 && var13 == var25 && var10 > 1.0F) {
            this.addTunnel(var1, var2, var3, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 - 1.5707964F, var12 / 3.0F, var13, var14, 1.0D);
            this.addTunnel(var1, var2, var3, var4, var6, var8, var23.nextFloat() * 0.5F + 0.5F, var11 + 1.5707964F, var12 / 3.0F, var13, var14, 1.0D);
            return;
         }

         if (var51 || var23.nextInt(4) != 0) {
            double var33 = var4 - var17;
            double var35 = var8 - var19;
            double var37 = (double)(var14 - var13);
            double var39 = (double)(var10 + 2.0F + 16.0F);
            if (var33 * var33 + var35 * var35 - var37 * var37 > var39 * var39) {
               return;
            }

            if (!(var4 < var17 - 16.0D - var27 * 2.0D) && !(var8 < var19 - 16.0D - var27 * 2.0D) && !(var4 > var17 + 16.0D + var27 * 2.0D) && !(var8 > var19 + 16.0D + var27 * 2.0D)) {
               int var52 = Mth.floor(var4 - var27) - var1 * 16 - 1;
               int var34 = Mth.floor(var4 + var27) - var1 * 16 + 1;
               int var53 = Mth.floor(var6 - var29) - 1;
               int var36 = Mth.floor(var6 + var29) + 1;
               int var54 = Mth.floor(var8 - var27) - var2 * 16 - 1;
               int var38 = Mth.floor(var8 + var27) - var2 * 16 + 1;
               if (var52 < 0) {
                  var52 = 0;
               }

               if (var34 > 16) {
                  var34 = 16;
               }

               if (var53 < 1) {
                  var53 = 1;
               }

               if (var36 > 120) {
                  var36 = 120;
               }

               if (var54 < 0) {
                  var54 = 0;
               }

               if (var38 > 16) {
                  var38 = 16;
               }

               boolean var55 = false;

               int var40;
               int var43;
               for(var40 = var52; !var55 && var40 < var34; ++var40) {
                  for(int var41 = var54; !var55 && var41 < var38; ++var41) {
                     for(int var42 = var36 + 1; !var55 && var42 >= var53 - 1; --var42) {
                        var43 = (var40 * 16 + var41) * 128 + var42;
                        if (var42 >= 0 && var42 < 128) {
                           if (var3[var43] == Tile.lava.id || var3[var43] == Tile.calmLava.id) {
                              var55 = true;
                           }

                           if (var42 != var53 - 1 && var40 != var52 && var40 != var34 - 1 && var41 != var54 && var41 != var38 - 1) {
                              var42 = var53;
                           }
                        }
                     }
                  }
               }

               if (!var55) {
                  for(var40 = var52; var40 < var34; ++var40) {
                     double var56 = ((double)(var40 + var1 * 16) + 0.5D - var4) / var27;

                     for(var43 = var54; var43 < var38; ++var43) {
                        double var44 = ((double)(var43 + var2 * 16) + 0.5D - var8) / var27;
                        int var46 = (var40 * 16 + var43) * 128 + var36;

                        for(int var47 = var36 - 1; var47 >= var53; --var47) {
                           double var48 = ((double)var47 + 0.5D - var6) / var29;
                           if (var48 > -0.7D && var56 * var56 + var48 * var48 + var44 * var44 < 1.0D) {
                              byte var50 = var3[var46];
                              if (var50 == Tile.hellRock.id || var50 == Tile.dirt.id || var50 == Tile.grass.id) {
                                 var3[var46] = 0;
                              }
                           }

                           --var46;
                        }
                     }
                  }

                  if (var51) {
                     break;
                  }
               }
            }
         }
      }

   }

   protected void addFeature(Level var1, int var2, int var3, int var4, int var5, byte[] var6) {
      int var7 = this.random.nextInt(this.random.nextInt(this.random.nextInt(10) + 1) + 1);
      if (this.random.nextInt(5) != 0) {
         var7 = 0;
      }

      for(int var8 = 0; var8 < var7; ++var8) {
         double var9 = (double)(var2 * 16 + this.random.nextInt(16));
         double var11 = (double)this.random.nextInt(128);
         double var13 = (double)(var3 * 16 + this.random.nextInt(16));
         int var15 = 1;
         if (this.random.nextInt(4) == 0) {
            this.addRoom(var4, var5, var6, var9, var11, var13);
            var15 += this.random.nextInt(4);
         }

         for(int var16 = 0; var16 < var15; ++var16) {
            float var17 = this.random.nextFloat() * 3.1415927F * 2.0F;
            float var18 = (this.random.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float var19 = this.random.nextFloat() * 2.0F + this.random.nextFloat();
            this.addTunnel(var4, var5, var6, var9, var11, var13, var19 * 2.0F, var17, var18, 0, 0, 0.5D);
         }
      }

   }
}
