package net.minecraft.world.level;

import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.tile.Tile;
import util.Mth;

public class PortalForcer {
   private Random random = new Random();

   public void force(Level var1, Entity var2) {
      if (!this.findPortal(var1, var2)) {
         this.createPortal(var1, var2);
         this.findPortal(var1, var2);
      }
   }

   public boolean findPortal(Level var1, Entity var2) {
      short var3 = 128;
      double var4 = -1.0D;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      int var9 = Mth.floor(var2.x);
      int var10 = Mth.floor(var2.z);

      double var18;
      for(int var11 = var9 - var3; var11 <= var9 + var3; ++var11) {
         double var12 = (double)var11 + 0.5D - var2.x;

         for(int var14 = var10 - var3; var14 <= var10 + var3; ++var14) {
            double var15 = (double)var14 + 0.5D - var2.z;

            for(int var17 = 127; var17 >= 0; --var17) {
               if (var1.getTile(var11, var17, var14) == Tile.portalTile.id) {
                  while(var1.getTile(var11, var17 - 1, var14) == Tile.portalTile.id) {
                     --var17;
                  }

                  var18 = (double)var17 + 0.5D - var2.y;
                  double var20 = var12 * var12 + var18 * var18 + var15 * var15;
                  if (var4 < 0.0D || var20 < var4) {
                     var4 = var20;
                     var6 = var11;
                     var7 = var17;
                     var8 = var14;
                  }
               }
            }
         }
      }

      if (var4 >= 0.0D) {
         double var22 = (double)var6 + 0.5D;
         double var16 = (double)var7 + 0.5D;
         var18 = (double)var8 + 0.5D;
         if (var1.getTile(var6 - 1, var7, var8) == Tile.portalTile.id) {
            var22 -= 0.5D;
         }

         if (var1.getTile(var6 + 1, var7, var8) == Tile.portalTile.id) {
            var22 += 0.5D;
         }

         if (var1.getTile(var6, var7, var8 - 1) == Tile.portalTile.id) {
            var18 -= 0.5D;
         }

         if (var1.getTile(var6, var7, var8 + 1) == Tile.portalTile.id) {
            var18 += 0.5D;
         }

         System.out.println("Teleporting to " + var22 + ", " + var16 + ", " + var18);
         var2.moveTo(var22, var16, var18, var2.yRot, 0.0F);
         var2.xd = var2.yd = var2.zd = 0.0D;
         return true;
      } else {
         return false;
      }
   }

   public boolean createPortal(Level var1, Entity var2) {
      byte var3 = 16;
      double var4 = -1.0D;
      int var6 = Mth.floor(var2.x);
      int var7 = Mth.floor(var2.y);
      int var8 = Mth.floor(var2.z);
      int var9 = var6;
      int var10 = var7;
      int var11 = var8;
      int var12 = 0;
      int var13 = this.random.nextInt(4);

      int var14;
      double var15;
      int var17;
      double var18;
      int var20;
      int var21;
      int var22;
      int var23;
      int var24;
      int var25;
      int var26;
      int var27;
      int var28;
      double var32;
      double var33;
      for(var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
         var15 = (double)var14 + 0.5D - var2.x;

         for(var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
            var18 = (double)var17 + 0.5D - var2.z;

            label293:
            for(var20 = 127; var20 >= 0; --var20) {
               if (var1.isEmptyTile(var14, var20, var17)) {
                  while(var20 > 0 && var1.isEmptyTile(var14, var20 - 1, var17)) {
                     --var20;
                  }

                  for(var21 = var13; var21 < var13 + 4; ++var21) {
                     var22 = var21 % 2;
                     var23 = 1 - var22;
                     if (var21 % 4 >= 2) {
                        var22 = -var22;
                        var23 = -var23;
                     }

                     for(var24 = 0; var24 < 3; ++var24) {
                        for(var25 = 0; var25 < 4; ++var25) {
                           for(var26 = -1; var26 < 4; ++var26) {
                              var27 = var14 + (var25 - 1) * var22 + var24 * var23;
                              var28 = var20 + var26;
                              int var29 = var17 + (var25 - 1) * var23 - var24 * var22;
                              if (var26 < 0 && !var1.getMaterial(var27, var28, var29).isSolid() || var26 >= 0 && !var1.isEmptyTile(var27, var28, var29)) {
                                 continue label293;
                              }
                           }
                        }
                     }

                     var32 = (double)var20 + 0.5D - var2.y;
                     var33 = var15 * var15 + var32 * var32 + var18 * var18;
                     if (var4 < 0.0D || var33 < var4) {
                        var4 = var33;
                        var9 = var14;
                        var10 = var20;
                        var11 = var17;
                        var12 = var21 % 4;
                     }
                  }
               }
            }
         }
      }

      if (var4 < 0.0D) {
         for(var14 = var6 - var3; var14 <= var6 + var3; ++var14) {
            var15 = (double)var14 + 0.5D - var2.x;

            for(var17 = var8 - var3; var17 <= var8 + var3; ++var17) {
               var18 = (double)var17 + 0.5D - var2.z;

               label231:
               for(var20 = 127; var20 >= 0; --var20) {
                  if (var1.isEmptyTile(var14, var20, var17)) {
                     while(var1.isEmptyTile(var14, var20 - 1, var17)) {
                        --var20;
                     }

                     for(var21 = var13; var21 < var13 + 2; ++var21) {
                        var22 = var21 % 2;
                        var23 = 1 - var22;

                        for(var24 = 0; var24 < 4; ++var24) {
                           for(var25 = -1; var25 < 4; ++var25) {
                              var26 = var14 + (var24 - 1) * var22;
                              var27 = var20 + var25;
                              var28 = var17 + (var24 - 1) * var23;
                              if (var25 < 0 && !var1.getMaterial(var26, var27, var28).isSolid() || var25 >= 0 && !var1.isEmptyTile(var26, var27, var28)) {
                                 continue label231;
                              }
                           }
                        }

                        var32 = (double)var20 + 0.5D - var2.y;
                        var33 = var15 * var15 + var32 * var32 + var18 * var18;
                        if (var4 < 0.0D || var33 < var4) {
                           var4 = var33;
                           var9 = var14;
                           var10 = var20;
                           var11 = var17;
                           var12 = var21 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      int var30 = var9;
      int var16 = var10;
      var17 = var11;
      int var31 = var12 % 2;
      int var19 = 1 - var31;
      if (var12 % 4 >= 2) {
         var31 = -var31;
         var19 = -var19;
      }

      boolean var34;
      if (var4 < 0.0D) {
         if (var10 < 70) {
            var10 = 70;
         }

         if (var10 > 118) {
            var10 = 118;
         }

         var16 = var10;

         for(var20 = -1; var20 <= 1; ++var20) {
            for(var21 = 1; var21 < 3; ++var21) {
               for(var22 = -1; var22 < 3; ++var22) {
                  var23 = var30 + (var21 - 1) * var31 + var20 * var19;
                  var24 = var16 + var22;
                  var25 = var17 + (var21 - 1) * var19 - var20 * var31;
                  var34 = var22 < 0;
                  var1.setTile(var23, var24, var25, var34 ? Tile.obsidian.id : 0);
               }
            }
         }
      }

      for(var20 = 0; var20 < 4; ++var20) {
         var1.noNeighborUpdate = true;

         for(var21 = 0; var21 < 4; ++var21) {
            for(var22 = -1; var22 < 4; ++var22) {
               var23 = var30 + (var21 - 1) * var31;
               var24 = var16 + var22;
               var25 = var17 + (var21 - 1) * var19;
               var34 = var21 == 0 || var21 == 3 || var22 == -1 || var22 == 3;
               var1.setTile(var23, var24, var25, var34 ? Tile.obsidian.id : Tile.portalTile.id);
            }
         }

         var1.noNeighborUpdate = false;

         for(var21 = 0; var21 < 4; ++var21) {
            for(var22 = -1; var22 < 4; ++var22) {
               var23 = var30 + (var21 - 1) * var31;
               var24 = var16 + var22;
               var25 = var17 + (var21 - 1) * var19;
               var1.updateNeighborsAt(var23, var24, var25, var1.getTile(var23, var24, var25));
            }
         }
      }

      return true;
   }
}
