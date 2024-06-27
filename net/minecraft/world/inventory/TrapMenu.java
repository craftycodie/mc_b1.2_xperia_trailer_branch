package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.tile.entity.DispenserTileEntity;

public class TrapMenu extends AbstractContainerMenu {
   private DispenserTileEntity trap;

   public TrapMenu(Container var1, DispenserTileEntity var2) {
      this.trap = var2;

      int var3;
      int var4;
      for(var3 = 0; var3 < 3; ++var3) {
         for(var4 = 0; var4 < 3; ++var4) {
            this.addSlot(new Slot(var2, var4 + var3 * 3, 61 + var4 * 18, 17 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 3; ++var3) {
         for(var4 = 0; var4 < 9; ++var4) {
            this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 9; ++var3) {
         this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
      }

   }

   public boolean stillValid(Player var1) {
      return this.trap.stillValid(var1);
   }
}
