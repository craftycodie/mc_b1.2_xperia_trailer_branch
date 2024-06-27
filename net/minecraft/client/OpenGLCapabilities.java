package net.minecraft.client;

import org.lwjgl.opengl.GLContext;

public class OpenGLCapabilities {
   private static boolean USE_OCCLUSION_QUERY = false;

   public boolean hasOcclusionChecks() {
      return USE_OCCLUSION_QUERY && GLContext.getCapabilities().GL_ARB_occlusion_query;
   }
}
