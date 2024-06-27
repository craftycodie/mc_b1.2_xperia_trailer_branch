package net.minecraft.world.level.levelgen.feature;

import java.util.Random;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Tile;
import net.minecraft.world.level.tile.entity.ChestTileEntity;
import net.minecraft.world.level.tile.entity.MobSpawnerTileEntity;

public class MonsterRoomFeature extends Feature {
   public boolean place(Level var1, Random var2, int var3, int var4, int var5) {
      byte var6 = 3;
      int var7 = var2.nextInt(2) + 2;
      int var8 = var2.nextInt(2) + 2;
      int var9 = 0;

      int var10;
      int var11;
      int var12;
      for(var10 = var3 - var7 - 1; var10 <= var3 + var7 + 1; ++var10) {
         for(var11 = var4 - 1; var11 <= var4 + var6 + 1; ++var11) {
            for(var12 = var5 - var8 - 1; var12 <= var5 + var8 + 1; ++var12) {
               Material var13 = var1.getMaterial(var10, var11, var12);
               if (var11 == var4 - 1 && !var13.isSolid()) {
                  return false;
               }

               if (var11 == var4 + var6 + 1 && !var13.isSolid()) {
                  return false;
               }

               if ((var10 == var3 - var7 - 1 || var10 == var3 + var7 + 1 || var12 == var5 - var8 - 1 || var12 == var5 + var8 + 1) && var11 == var4 && var1.isEmptyTile(var10, var11, var12) && var1.isEmptyTile(var10, var11 + 1, var12)) {
                  ++var9;
               }
            }
         }
      }

      if (var9 >= 1 && var9 <= 5) {
         for(var10 = var3 - var7 - 1; var10 <= var3 + var7 + 1; ++var10) {
            for(var11 = var4 + var6; var11 >= var4 - 1; --var11) {
               for(var12 = var5 - var8 - 1; var12 <= var5 + var8 + 1; ++var12) {
                  if (var10 != var3 - var7 - 1 && var11 != var4 - 1 && var12 != var5 - var8 - 1 && var10 != var3 + var7 + 1 && var11 != var4 + var6 + 1 && var12 != var5 + var8 + 1) {
                     var1.setTile(var10, var11, var12, 0);
                  } else if (var11 >= 0 && !var1.getMaterial(var10, var11 - 1, var12).isSolid()) {
                     var1.setTile(var10, var11, var12, 0);
                  } else if (var1.getMaterial(var10, var11, var12).isSolid()) {
                     if (var11 == var4 - 1 && var2.nextInt(4) != 0) {
                        var1.setTile(var10, var11, var12, Tile.mossStone.id);
                     } else {
                        var1.setTile(var10, var11, var12, Tile.stoneBrick.id);
                     }
                  }
               }
            }
         }

         label110:
         for(var10 = 0; var10 < 2; ++var10) {
            for(var11 = 0; var11 < 3; ++var11) {
               var12 = var3 + var2.nextInt(var7 * 2 + 1) - var7;
               int var14 = var5 + var2.nextInt(var8 * 2 + 1) - var8;
               if (var1.isEmptyTile(var12, var4, var14)) {
                  int var15 = 0;
                  if (var1.getMaterial(var12 - 1, var4, var14).isSolid()) {
                     ++var15;
                  }

                  if (var1.getMaterial(var12 + 1, var4, var14).isSolid()) {
                     ++var15;
                  }

                  if (var1.getMaterial(var12, var4, var14 - 1).isSolid()) {
                     ++var15;
                  }

                  if (var1.getMaterial(var12, var4, var14 + 1).isSolid()) {
                     ++var15;
                  }

                  if (var15 == 1) {
                     var1.setTile(var12, var4, var14, Tile.chest.id);
                     ChestTileEntity var16 = (ChestTileEntity)var1.getTileEntity(var12, var4, var14);
                     int var17 = 0;

                     while(true) {
                        if (var17 >= 8) {
                           continue label110;
                        }

                        ItemInstance var18 = this.randomItem(var2);
                        if (var18 != null) {
                           var16.setItem(var2.nextInt(var16.getContainerSize()), var18);
                        }

                        ++var17;
                     }
                  }
               }
            }
         }

         var1.setTile(var3, var4, var5, Tile.mobSpawner.id);
         MobSpawnerTileEntity var19 = (MobSpawnerTileEntity)var1.getTileEntity(var3, var4, var5);
         var19.setEntityId(this.randomEntityId(var2));
         return true;
      } else {
         return false;
      }
   }

   private ItemInstance randomItem(Random var1) {
      int var2 = var1.nextInt(11);
      if (var2 == 0) {
         return new ItemInstance(Item.saddle);
      } else if (var2 == 1) {
         return new ItemInstance(Item.ironIngot, var1.nextInt(4) + 1);
      } else if (var2 == 2) {
         return new ItemInstance(Item.bread);
      } else if (var2 == 3) {
         return new ItemInstance(Item.wheat, var1.nextInt(4) + 1);
      } else if (var2 == 4) {
         return new ItemInstance(Item.sulphur, var1.nextInt(4) + 1);
      } else if (var2 == 5) {
         return new ItemInstance(Item.string, var1.nextInt(4) + 1);
      } else if (var2 == 6) {
         return new ItemInstance(Item.bucket_empty);
      } else if (var2 == 7 && var1.nextInt(100) == 0) {
         return new ItemInstance(Item.apple_gold);
      } else if (var2 == 8 && var1.nextInt(2) == 0) {
         return new ItemInstance(Item.redStone, var1.nextInt(4) + 1);
      } else {
         return var2 == 9 && var1.nextInt(10) == 0 ? new ItemInstance(Item.items[Item.record_01.id + var1.nextInt(2)]) : null;
      }
   }

   private String randomEntityId(Random var1) {
      int var2 = var1.nextInt(4);
      if (var2 == 0) {
         return "Skeleton";
      } else if (var2 == 1) {
         return "Zombie";
      } else if (var2 == 2) {
         return "Zombie";
      } else {
         return var2 == 3 ? "Spider" : "";
      }
   }
}
