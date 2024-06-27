package com.jcraft.jorbis;

class Lpc {
   Drft fft = new Drft();
   int ln;
   int m;

   static float lpc_from_data(float[] data, float[] lpc, int n, int m) {
      float[] aut = new float[m + 1];

      int i;
      int j;
      float r;
      for(j = m + 1; j-- != 0; aut[j] = r) {
         r = 0.0F;

         for(i = j; i < n; ++i) {
            r += data[i] * data[i - j];
         }
      }

      float error = aut[0];

      for(i = 0; i < m; ++i) {
         r = -aut[i + 1];
         if (error == 0.0F) {
            for(int k = 0; k < m; ++k) {
               lpc[k] = 0.0F;
            }

            return 0.0F;
         }

         for(j = 0; j < i; ++j) {
            r -= lpc[j] * aut[i - j];
         }

         r /= error;
         lpc[i] = r;

         for(j = 0; j < i / 2; ++j) {
            float tmp = lpc[j];
            lpc[j] += r * lpc[i - 1 - j];
            lpc[i - 1 - j] += r * tmp;
         }

         if (i % 2 != 0) {
            lpc[j] += lpc[j] * r;
         }

         error = (float)((double)error * (1.0D - (double)(r * r)));
      }

      return error;
   }

   float lpc_from_curve(float[] curve, float[] lpc) {
      int n = this.ln;
      float[] work = new float[n + n];
      float fscale = (float)(0.5D / (double)n);

      int i;
      for(i = 0; i < n; ++i) {
         work[i * 2] = curve[i] * fscale;
         work[i * 2 + 1] = 0.0F;
      }

      work[n * 2 - 1] = curve[n - 1] * fscale;
      n *= 2;
      this.fft.backward(work);
      i = 0;

      float temp;
      for(int j = n / 2; i < n / 2; work[j++] = temp) {
         temp = work[i];
         work[i++] = work[j];
      }

      return lpc_from_data(work, lpc, n, this.m);
   }

   void init(int mapped, int m) {
      this.ln = mapped;
      this.m = m;
      this.fft.init(mapped * 2);
   }

   void clear() {
      this.fft.clear();
   }

   static float FAST_HYPOT(float a, float b) {
      return (float)Math.sqrt((double)(a * a + b * b));
   }

   void lpc_to_curve(float[] curve, float[] lpc, float amp) {
      int l2;
      for(l2 = 0; l2 < this.ln * 2; ++l2) {
         curve[l2] = 0.0F;
      }

      if (amp != 0.0F) {
         for(l2 = 0; l2 < this.m; ++l2) {
            curve[l2 * 2 + 1] = lpc[l2] / (4.0F * amp);
            curve[l2 * 2 + 2] = -lpc[l2] / (4.0F * amp);
         }

         this.fft.backward(curve);
         l2 = this.ln * 2;
         float unit = (float)(1.0D / (double)amp);
         curve[0] = (float)(1.0D / (double)(curve[0] * 2.0F + unit));

         for(int i = 1; i < this.ln; ++i) {
            float real = curve[i] + curve[l2 - i];
            float imag = curve[i] - curve[l2 - i];
            float a = real + unit;
            curve[i] = (float)(1.0D / (double)FAST_HYPOT(a, imag));
         }

      }
   }
}
