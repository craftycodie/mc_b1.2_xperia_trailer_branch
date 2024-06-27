package net.minecraft.client.skins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class TexturePackRepository {
   private List<TexturePack> texturePacks = new ArrayList();
   private TexturePack defaultTexturePack = new DefaultTexturePack();
   public TexturePack selected;
   private Map<String, TexturePack> skinCache = new HashMap();
   private Minecraft minecraft;
   private File workDir;
   private String chosenSkinName;

   public TexturePackRepository(Minecraft minecraft, File workingDirectory) {
      this.minecraft = minecraft;
      this.workDir = new File(workingDirectory, "texturepacks");
      if (!this.workDir.exists()) {
         this.workDir.mkdirs();
      }

      this.chosenSkinName = minecraft.options.skin;
      this.updateList();
      this.selected.select();
   }

   public boolean selectSkin(TexturePack skin) {
      if (skin == this.selected) {
         return false;
      } else {
         this.selected.deselect();
         this.chosenSkinName = skin.name;
         this.selected = skin;
         this.minecraft.options.skin = this.chosenSkinName;
         this.minecraft.options.save();
         this.selected.select();
         return true;
      }
   }

   public void updateList() {
      List<TexturePack> newSkins = new ArrayList();
      this.selected = null;
      newSkins.add(this.defaultTexturePack);
      if (this.workDir.exists() && this.workDir.isDirectory()) {
         File[] files = this.workDir.listFiles();
         File[] var6 = files;
         int var5 = files.length;

         for(int var4 = 0; var4 < var5; ++var4) {
            File file = var6[var4];
            if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
               String id = file.getName() + ":" + file.length() + ":" + file.lastModified();

               try {
                  if (!this.skinCache.containsKey(id)) {
                     TexturePack skin = new FileTexturePack(file);
                     skin.id = id;
                     this.skinCache.put(id, skin);
                     skin.load(this.minecraft);
                  }

                  TexturePack skin = (TexturePack)this.skinCache.get(id);
                  if (skin.name.equals(this.chosenSkinName)) {
                     this.selected = skin;
                  }

                  newSkins.add(skin);
               } catch (IOException var9) {
                  var9.printStackTrace();
               }
            }
         }
      }

      if (this.selected == null) {
         this.selected = this.defaultTexturePack;
      }

      this.texturePacks.removeAll(newSkins);
      Iterator var11 = this.texturePacks.iterator();

      while(var11.hasNext()) {
         TexturePack skin = (TexturePack)var11.next();
         skin.unload(this.minecraft);
         this.skinCache.remove(skin.id);
      }

      this.texturePacks = newSkins;
   }

   public List<TexturePack> getAll() {
      return new ArrayList(this.texturePacks);
   }
}
