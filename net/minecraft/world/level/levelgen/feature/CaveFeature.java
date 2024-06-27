package net.minecraft.world.level.levelgen.feature;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.TilePos;
import net.minecraft.world.level.tile.Tile;
import util.Mth;

public class CaveFeature extends Feature {
   public boolean place(Level var1, Random var2, int var3, int var4, int var5) {
      float var6 = var2.nextFloat() * 3.1415927F;
      double var7 = 8.0D;
      double var9 = (double)(var3 + 8) + (double)Mth.sin(var6) * var7;
      double var11 = (double)(var3 + 8) - (double)Mth.sin(var6) * var7;
      double var13 = (double)(var5 + 8) + (double)Mth.cos(var6) * var7;
      double var15 = (double)(var5 + 8) - (double)Mth.cos(var6) * var7;
      double var17 = (double)(var4 + var2.nextInt(8) + 2);
      double var19 = (double)(var4 + var2.nextInt(8) + 2);
      double var21 = var2.nextDouble() * 4.0D + 2.0D;
      double var23 = var2.nextDouble() * 0.6D;
      long var25 = var2.nextLong();
      var2.setSeed(var25);
      ArrayList var27 = new ArrayList();

      int var28;
      for(var28 = 0; var28 <= 16; ++var28) {
         double var29 = var9 + (var11 - var9) * (double)var28 / 16.0D;
         double var31 = var17 + (var19 - var17) * (double)var28 / 16.0D;
         double var33 = var13 + (var15 - var13) * (double)var28 / 16.0D;
         double var35 = var2.nextDouble();
         double var37 = ((double)Mth.sin((float)var28 / 16.0F * 3.1415927F) * var21 + 1.0D) * var35 + 1.0D;
         double var39 = ((double)Mth.sin((float)var28 / 16.0F * 3.1415927F) * var21 + 1.0D) * var35 + 1.0D;

         for(int var41 = (int)(var29 - var37 / 2.0D); var41 <= (int)(var29 + var37 / 2.0D); ++var41) {
            for(int var42 = (int)(var31 - var39 / 2.0D); var42 <= (int)(var31 + var39 / 2.0D); ++var42) {
               for(int var43 = (int)(var33 - var37 / 2.0D); var43 <= (int)(var33 + var37 / 2.0D); ++var43) {
                  double var44 = ((double)var41 + 0.5D - var29) / (var37 / 2.0D);
                  double var46 = ((double)var42 + 0.5D - var31) / (var39 / 2.0D);
                  double var48 = ((double)var43 + 0.5D - var33) / (var37 / 2.0D);
                  if (var44 * var44 + var46 * var46 + var48 * var48 < var2.nextDouble() * var23 + (1.0D - var23) && !var1.isEmptyTile(var41, var42, var43)) {
                     for(int var50 = var41 - 2; var50 <= var41 + 1; ++var50) {
                        for(int var51 = var42 - 1; var51 <= var42 + 1; ++var51) {
                           for(int var52 = var43 - 1; var52 <= var43 + 1; ++var52) {
                              if (var50 <= var3 || var52 <= var5 || var50 >= var3 + 16 - 1 || var52 >= var5 + 16 - 1) {
                                 return false;
                              }

                              if (var1.getMaterial(var50, var51, var52).isLiquid()) {
                                 return false;
                              }
                           }
                        }
                     }

                     var27.add(new TilePos(var41, var42, var43));
                  }
               }
            }
         }
      }

      TilePos var53;
      for(var28 = 0; var28 < var27.size(); ++var28) {
         var53 = (TilePos)var27.get(var28);
         var1.setTileNoUpdate(var53.x, var53.y, var53.z, 0);
      }

      for(var28 = 0; var28 < var27.size(); ++var28) {
         var53 = (TilePos)var27.get(var28);
         if (var1.getTile(var53.x, var53.y - 1, var53.z) == Tile.dirt.id && var1.getRawBrightness(var53.x, var53.y, var53.z) > 8) {
            var1.setTileNoUpdate(var53.x, var53.y - 1, var53.z, Tile.grass.id);
         }
      }

      return true;
   }
}
