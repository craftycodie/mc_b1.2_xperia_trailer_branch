package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.crafting.Recipes;
import net.minecraft.world.level.tile.Tile;

public class InventoryMenu extends AbstractContainerMenu {
   public CraftingContainer craftSlots;
   public Container resultSlots;
   public boolean active;

   public InventoryMenu(Inventory var1) {
      this(var1, true);
   }

   public InventoryMenu(Inventory var1, boolean var2) {
      this.craftSlots = new CraftingContainer(this, 2, 2);
      this.resultSlots = new ResultContainer();
      this.active = false;
      this.active = var2;
      this.addSlot(new ResultSlot(this.craftSlots, this.resultSlots, 0, 144, 36));

      final int var3;
      int var4;
      for(var3 = 0; var3 < 2; ++var3) {
         for(var4 = 0; var4 < 2; ++var4) {
            this.addSlot(new Slot(this.craftSlots, var4 + var3 * 2, 88 + var4 * 18, 26 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 4; ++var3) {
         this.addSlot(new Slot(var1, var1.getContainerSize() - 1 - var3, 8, 8 + var3 * 18) {
            public int getMaxStackSize() {
               return 1;
            }

            public boolean mayPlace(ItemInstance var1) {
               if (var1.getItem() instanceof ArmorItem) {
                  return ((ArmorItem)var1.getItem()).slot == var3;
               } else if (var1.getItem().id == Tile.pumpkin.id) {
                  return var3 == 0;
               } else {
                  return false;
               }
            }
         });
      }

      for(var3 = 0; var3 < 3; ++var3) {
         for(var4 = 0; var4 < 9; ++var4) {
            this.addSlot(new Slot(var1, var4 + (var3 + 1) * 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for(var3 = 0; var3 < 9; ++var3) {
         this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
      }

      this.slotsChanged(this.craftSlots);
   }

   public void slotsChanged(Container var1) {
      this.resultSlots.setItem(0, Recipes.getInstance().getItemFor(this.craftSlots));
   }

   public void removed(Player var1) {
      super.removed(var1);

      for(int var2 = 0; var2 < 4; ++var2) {
         ItemInstance var3 = this.craftSlots.getItem(var2);
         if (var3 != null) {
            var1.drop(var3);
            this.craftSlots.setItem(var2, (ItemInstance)null);
         }
      }

   }

   public boolean stillValid(Player var1) {
      return true;
   }
}
