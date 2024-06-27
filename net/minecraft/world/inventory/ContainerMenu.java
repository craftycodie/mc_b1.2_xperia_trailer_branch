package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class ContainerMenu extends AbstractContainerMenu {
   private Container container;

   public ContainerMenu(Container var1, Container var2) {
      this.container = var2;
      int var3 = var2.getContainerSize() / 9;
      int var4 = (var3 - 4) * 18;

      int var5;
      int var6;
      for(var5 = 0; var5 < var3; ++var5) {
         for(var6 = 0; var6 < 9; ++var6) {
            this.addSlot(new Slot(var2, var6 + var5 * 9, 8 + var6 * 18, 18 + var5 * 18));
         }
      }

      for(var5 = 0; var5 < 3; ++var5) {
         for(var6 = 0; var6 < 9; ++var6) {
            this.addSlot(new Slot(var1, var6 + var5 * 9 + 9, 8 + var6 * 18, 103 + var5 * 18 + var4));
         }
      }

      for(var5 = 0; var5 < 9; ++var5) {
         this.addSlot(new Slot(var1, var5, 8 + var5 * 18, 161 + var4));
      }

   }

   public boolean stillValid(Player var1) {
      return this.container.stillValid(var1);
   }
}
