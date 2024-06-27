package net.minecraft.world.level.material;

public class DecorationMaterial extends Material {
   public boolean isSolid() {
      return false;
   }

   public boolean blocksLight() {
      return false;
   }

   public boolean blocksMotion() {
      return false;
   }
}
