package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
   int dim;
   int entries;
   StaticCodeBook c = new StaticCodeBook();
   float[] valuelist;
   int[] codelist;
   CodeBook.DecodeAux decode_tree;
   private int[] t = new int[15];

   int encode(int a, Buffer b) {
      b.write(this.codelist[a], this.c.lengthlist[a]);
      return this.c.lengthlist[a];
   }

   int errorv(float[] a) {
      int best = this.best(a, 1);

      for(int k = 0; k < this.dim; ++k) {
         a[k] = this.valuelist[best * this.dim + k];
      }

      return best;
   }

   int encodev(int best, float[] a, Buffer b) {
      for(int k = 0; k < this.dim; ++k) {
         a[k] = this.valuelist[best * this.dim + k];
      }

      return this.encode(best, b);
   }

   int encodevs(float[] a, Buffer b, int step, int addmul) {
      int best = this.besterror(a, step, addmul);
      return this.encode(best, b);
   }

   synchronized int decodevs_add(float[] a, int offset, Buffer b, int n) {
      int step = n / this.dim;
      if (this.t.length < step) {
         this.t = new int[step];
      }

      int i;
      for(i = 0; i < step; ++i) {
         int entry = this.decode(b);
         if (entry == -1) {
            return -1;
         }

         this.t[i] = entry * this.dim;
      }

      i = 0;

      for(int o = 0; i < this.dim; o += step) {
         for(int j = 0; j < step; ++j) {
            a[offset + o + j] += this.valuelist[this.t[j] + i];
         }

         ++i;
      }

      return 0;
   }

   int decodev_add(float[] a, int offset, Buffer b, int n) {
      int var10002;
      int i;
      int j;
      int entry;
      int t;
      if (this.dim > 8) {
         i = 0;

         while(i < n) {
            entry = this.decode(b);
            if (entry == -1) {
               return -1;
            }

            t = entry * this.dim;

            for(j = 0; j < this.dim; a[offset + var10002] += this.valuelist[t + j++]) {
               var10002 = i++;
            }
         }
      } else {
         i = 0;

         while(i < n) {
            entry = this.decode(b);
            if (entry == -1) {
               return -1;
            }

            t = entry * this.dim;
            j = 0;
            switch(this.dim) {
            case 0:
            default:
               break;
            case 8:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 7:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 6:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 5:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 4:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 3:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 2:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            case 1:
               var10002 = i++;
               a[offset + var10002] += this.valuelist[t + j++];
            }
         }
      }

      return 0;
   }

   int decodev_set(float[] a, int offset, Buffer b, int n) {
      int i = 0;

      while(i < n) {
         int entry = this.decode(b);
         if (entry == -1) {
            return -1;
         }

         int t = entry * this.dim;

         for(int j = 0; j < this.dim; a[offset + i++] = this.valuelist[t + j++]) {
         }
      }

      return 0;
   }

   int decodevv_add(float[][] a, int offset, int ch, Buffer b, int n) {
      int chptr = 0;
      int i = offset / ch;

      while(i < (offset + n) / ch) {
         int entry = this.decode(b);
         if (entry == -1) {
            return -1;
         }

         int t = entry * this.dim;

         for(int j = 0; j < this.dim; ++j) {
            int var10001 = chptr++;
            a[var10001][i] += this.valuelist[t + j];
            if (chptr == ch) {
               chptr = 0;
               ++i;
            }
         }
      }

      return 0;
   }

   int decode(Buffer b) {
      int ptr = 0;
      CodeBook.DecodeAux t = this.decode_tree;
      int lok = b.look(t.tabn);
      if (lok >= 0) {
         ptr = t.tab[lok];
         b.adv(t.tabl[lok]);
         if (ptr <= 0) {
            return -ptr;
         }
      }

      do {
         switch(b.read1()) {
         case -1:
         default:
            return -1;
         case 0:
            ptr = t.ptr0[ptr];
            break;
         case 1:
            ptr = t.ptr1[ptr];
         }
      } while(ptr > 0);

      return -ptr;
   }

   int decodevs(float[] a, int index, Buffer b, int step, int addmul) {
      int entry = this.decode(b);
      if (entry == -1) {
         return -1;
      } else {
         int i;
         int o;
         switch(addmul) {
         case -1:
            i = 0;

            for(o = 0; i < this.dim; o += step) {
               a[index + o] = this.valuelist[entry * this.dim + i];
               ++i;
            }

            return entry;
         case 0:
            i = 0;

            for(o = 0; i < this.dim; o += step) {
               a[index + o] += this.valuelist[entry * this.dim + i];
               ++i;
            }

            return entry;
         case 1:
            i = 0;

            for(o = 0; i < this.dim; o += step) {
               a[index + o] *= this.valuelist[entry * this.dim + i];
               ++i;
            }
         }

         return entry;
      }
   }

   int best(float[] a, int step) {
      int besti = -1;
      float best = 0.0F;
      int e = 0;

      for(int i = 0; i < this.entries; ++i) {
         if (this.c.lengthlist[i] > 0) {
            float _this = dist(this.dim, this.valuelist, e, a, step);
            if (besti == -1 || _this < best) {
               best = _this;
               besti = i;
            }
         }

         e += this.dim;
      }

      return besti;
   }

   int besterror(float[] a, int step, int addmul) {
      int best = this.best(a, step);
      int i;
      int o;
      switch(addmul) {
      case 0:
         i = 0;

         for(o = 0; i < this.dim; o += step) {
            a[o] -= this.valuelist[best * this.dim + i];
            ++i;
         }

         return best;
      case 1:
         i = 0;

         for(o = 0; i < this.dim; o += step) {
            float val = this.valuelist[best * this.dim + i];
            if (val == 0.0F) {
               a[o] = 0.0F;
            } else {
               a[o] /= val;
            }

            ++i;
         }
      }

      return best;
   }

   void clear() {
   }

   private static float dist(int el, float[] ref, int index, float[] b, int step) {
      float acc = 0.0F;

      for(int i = 0; i < el; ++i) {
         float val = ref[index + i] - b[i * step];
         acc += val * val;
      }

      return acc;
   }

   int init_decode(StaticCodeBook s) {
      this.c = s;
      this.entries = s.entries;
      this.dim = s.dim;
      this.valuelist = s.unquantize();
      this.decode_tree = this.make_decode_tree();
      if (this.decode_tree == null) {
         this.clear();
         return -1;
      } else {
         return 0;
      }
   }

   static int[] make_words(int[] l, int n) {
      int[] marker = new int[33];
      int[] r = new int[n];

      int i;
      int length;
      int entry;
      for(i = 0; i < n; ++i) {
         length = l[i];
         if (length > 0) {
            entry = marker[length];
            if (length < 32 && entry >>> length != 0) {
               return null;
            }

            r[i] = entry;

            int j;
            for(j = length; j > 0; --j) {
               int var10002;
               if ((marker[j] & 1) != 0) {
                  if (j == 1) {
                     var10002 = marker[1]++;
                  } else {
                     marker[j] = marker[j - 1] << 1;
                  }
                  break;
               }

               var10002 = marker[j]++;
            }

            for(j = length + 1; j < 33 && marker[j] >>> 1 == entry; ++j) {
               entry = marker[j];
               marker[j] = marker[j - 1] << 1;
            }
         }
      }

      for(i = 0; i < n; ++i) {
         length = 0;

         for(entry = 0; entry < l[i]; ++entry) {
            length <<= 1;
            length |= r[i] >>> entry & 1;
         }

         r[i] = length;
      }

      return r;
   }

   CodeBook.DecodeAux make_decode_tree() {
      int top = 0;
      CodeBook.DecodeAux t = new CodeBook.DecodeAux();
      int[] ptr0 = t.ptr0 = new int[this.entries * 2];
      int[] ptr1 = t.ptr1 = new int[this.entries * 2];
      int[] codelist = make_words(this.c.lengthlist, this.c.entries);
      if (codelist == null) {
         return null;
      } else {
         t.aux = this.entries * 2;

         int i;
         int i;
         int p;
         int j;
         for(i = 0; i < this.entries; ++i) {
            if (this.c.lengthlist[i] > 0) {
               i = 0;

               for(p = 0; p < this.c.lengthlist[i] - 1; ++p) {
                  j = codelist[i] >>> p & 1;
                  if (j == 0) {
                     if (ptr0[i] == 0) {
                        ++top;
                        ptr0[i] = top;
                     }

                     i = ptr0[i];
                  } else {
                     if (ptr1[i] == 0) {
                        ++top;
                        ptr1[i] = top;
                     }

                     i = ptr1[i];
                  }
               }

               if ((codelist[i] >>> p & 1) == 0) {
                  ptr0[i] = -i;
               } else {
                  ptr1[i] = -i;
               }
            }
         }

         t.tabn = Util.ilog(this.entries) - 4;
         if (t.tabn < 5) {
            t.tabn = 5;
         }

         i = 1 << t.tabn;
         t.tab = new int[i];
         t.tabl = new int[i];

         for(i = 0; i < i; ++i) {
            p = 0;
            int j = false;

            for(j = 0; j < t.tabn && (p > 0 || j == 0); ++j) {
               if ((i & 1 << j) != 0) {
                  p = ptr1[p];
               } else {
                  p = ptr0[p];
               }
            }

            t.tab[i] = p;
            t.tabl[i] = j;
         }

         return t;
      }
   }

   class DecodeAux {
      int[] tab;
      int[] tabl;
      int tabn;
      int[] ptr0;
      int[] ptr1;
      int aux;
   }
}
