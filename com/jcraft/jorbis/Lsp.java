package com.jcraft.jorbis;

class Lsp {
   static final float M_PI = 3.1415927F;

   static void lsp_to_curve(float[] curve, int[] map, int n, int ln, float[] lsp, int m, float amp, float ampoffset) {
      float wdel = 3.1415927F / (float)ln;

      int i;
      for(i = 0; i < m; ++i) {
         lsp[i] = Lookup.coslook(lsp[i]);
      }

      int m2 = m / 2 * 2;
      i = 0;

      while(i < n) {
         int k = map[i];
         float p = 0.70710677F;
         float q = 0.70710677F;
         float w = Lookup.coslook(wdel * (float)k);

         int hx;
         for(hx = 0; hx < m2; hx += 2) {
            q *= lsp[hx] - w;
            p *= lsp[hx + 1] - w;
         }

         if ((m & 1) != 0) {
            q *= lsp[m - 1] - w;
            q *= q;
            p *= p * (1.0F - w * w);
         } else {
            q *= q * (1.0F + w);
            p *= p * (1.0F - w);
         }

         q += p;
         hx = Float.floatToIntBits(q);
         int ix = Integer.MAX_VALUE & hx;
         int qexp = 0;
         if (ix < 2139095040 && ix != 0) {
            if (ix < 8388608) {
               q = (float)((double)q * 3.3554432E7D);
               hx = Float.floatToIntBits(q);
               ix = Integer.MAX_VALUE & hx;
               qexp = -25;
            }

            qexp += (ix >>> 23) - 126;
            hx = hx & -2139095041 | 1056964608;
            q = Float.intBitsToFloat(hx);
         }

         q = Lookup.fromdBlook(amp * Lookup.invsqlook(q) * Lookup.invsq2explook(qexp + m) - ampoffset);

         while(true) {
            int var10001 = i++;
            curve[var10001] *= q;
            if (i >= n || map[i] != k) {
               break;
            }
         }
      }

   }
}
