package net.minecraft.world.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.tile.Tile;

public class TilePlanterItem extends Item {
   private int tileId;

   public TilePlanterItem(int var1, Tile var2) {
      super(var1);
      this.tileId = var2.id;
   }

   public boolean useOn(ItemInstance var1, Player var2, Level var3, int var4, int var5, int var6, int var7) {
      if (var3.getTile(var4, var5, var6) == Tile.topSnow.id) {
         var7 = 0;
      } else {
         if (var7 == 0) {
            --var5;
         }

         if (var7 == 1) {
            ++var5;
         }

         if (var7 == 2) {
            --var6;
         }

         if (var7 == 3) {
            ++var6;
         }

         if (var7 == 4) {
            --var4;
         }

         if (var7 == 5) {
            ++var4;
         }
      }

      if (var1.count == 0) {
         return false;
      } else {
         if (var3.mayPlace(this.tileId, var4, var5, var6, false)) {
            Tile var8 = Tile.tiles[this.tileId];
            if (var3.setTile(var4, var5, var6, this.tileId)) {
               Tile.tiles[this.tileId].setPlacedOnFace(var3, var4, var5, var6, var7);
               var3.playSound((double)((float)var4 + 0.5F), (double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), var8.soundType.getStepSound(), (var8.soundType.getVolume() + 1.0F) / 2.0F, var8.soundType.getPitch() * 0.8F);
               --var1.count;
            }
         }

         return true;
      }
   }
}
