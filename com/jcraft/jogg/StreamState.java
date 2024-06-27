package com.jcraft.jogg;

public class StreamState {
   byte[] body_data;
   int body_storage;
   int body_fill;
   private int body_returned;
   int[] lacing_vals;
   long[] granule_vals;
   int lacing_storage;
   int lacing_fill;
   int lacing_packet;
   int lacing_returned;
   byte[] header;
   int header_fill;
   public int e_o_s;
   int b_o_s;
   int serialno;
   int pageno;
   long packetno;
   long granulepos;

   public StreamState() {
      this.header = new byte[282];
      this.init();
   }

   StreamState(int serialno) {
      this();
      this.init(serialno);
   }

   void init() {
      this.body_storage = 16384;
      this.body_data = new byte[this.body_storage];
      this.lacing_storage = 1024;
      this.lacing_vals = new int[this.lacing_storage];
      this.granule_vals = new long[this.lacing_storage];
   }

   public void init(int serialno) {
      if (this.body_data == null) {
         this.init();
      } else {
         int i;
         for(i = 0; i < this.body_data.length; ++i) {
            this.body_data[i] = 0;
         }

         for(i = 0; i < this.lacing_vals.length; ++i) {
            this.lacing_vals[i] = 0;
         }

         for(i = 0; i < this.granule_vals.length; ++i) {
            this.granule_vals[i] = 0L;
         }
      }

      this.serialno = serialno;
   }

   public void clear() {
      this.body_data = null;
      this.lacing_vals = null;
      this.granule_vals = null;
   }

   void destroy() {
      this.clear();
   }

   void body_expand(int needed) {
      if (this.body_storage <= this.body_fill + needed) {
         this.body_storage += needed + 1024;
         byte[] foo = new byte[this.body_storage];
         System.arraycopy(this.body_data, 0, foo, 0, this.body_data.length);
         this.body_data = foo;
      }

   }

   void lacing_expand(int needed) {
      if (this.lacing_storage <= this.lacing_fill + needed) {
         this.lacing_storage += needed + 32;
         int[] foo = new int[this.lacing_storage];
         System.arraycopy(this.lacing_vals, 0, foo, 0, this.lacing_vals.length);
         this.lacing_vals = foo;
         long[] bar = new long[this.lacing_storage];
         System.arraycopy(this.granule_vals, 0, bar, 0, this.granule_vals.length);
         this.granule_vals = bar;
      }

   }

   public int packetin(Packet op) {
      int lacing_val = op.bytes / 255 + 1;
      if (this.body_returned != 0) {
         this.body_fill -= this.body_returned;
         if (this.body_fill != 0) {
            System.arraycopy(this.body_data, this.body_returned, this.body_data, 0, this.body_fill);
         }

         this.body_returned = 0;
      }

      this.body_expand(op.bytes);
      this.lacing_expand(lacing_val);
      System.arraycopy(op.packet_base, op.packet, this.body_data, this.body_fill, op.bytes);
      this.body_fill += op.bytes;

      int j;
      for(j = 0; j < lacing_val - 1; ++j) {
         this.lacing_vals[this.lacing_fill + j] = 255;
         this.granule_vals[this.lacing_fill + j] = this.granulepos;
      }

      this.lacing_vals[this.lacing_fill + j] = op.bytes % 255;
      this.granulepos = this.granule_vals[this.lacing_fill + j] = op.granulepos;
      int[] var10000 = this.lacing_vals;
      int var10001 = this.lacing_fill;
      var10000[var10001] |= 256;
      this.lacing_fill += lacing_val;
      ++this.packetno;
      if (op.e_o_s != 0) {
         this.e_o_s = 1;
      }

      return 0;
   }

   public int packetout(Packet op) {
      int ptr = this.lacing_returned;
      if (this.lacing_packet <= ptr) {
         return 0;
      } else if ((this.lacing_vals[ptr] & 1024) != 0) {
         ++this.lacing_returned;
         ++this.packetno;
         return -1;
      } else {
         int size = this.lacing_vals[ptr] & 255;
         int bytes = 0;
         op.packet_base = this.body_data;
         op.packet = this.body_returned;
         op.e_o_s = this.lacing_vals[ptr] & 512;
         op.b_o_s = this.lacing_vals[ptr] & 256;

         int bytes;
         for(bytes = bytes + size; size == 255; bytes += size) {
            ++ptr;
            int val = this.lacing_vals[ptr];
            size = val & 255;
            if ((val & 512) != 0) {
               op.e_o_s = 512;
            }
         }

         op.packetno = this.packetno;
         op.granulepos = this.granule_vals[ptr];
         op.bytes = bytes;
         this.body_returned += bytes;
         this.lacing_returned = ptr + 1;
         ++this.packetno;
         return 1;
      }
   }

   public int pagein(Page og) {
      byte[] header_base = og.header_base;
      int header = og.header;
      byte[] body_base = og.body_base;
      int body = og.body;
      int bodysize = og.body_len;
      int segptr = 0;
      int version = og.version();
      int continued = og.continued();
      int bos = og.bos();
      int eos = og.eos();
      long granulepos = og.granulepos();
      int _serialno = og.serialno();
      int _pageno = og.pageno();
      int segments = header_base[header + 26] & 255;
      int saved = this.lacing_returned;
      int val = this.body_returned;
      if (val != 0) {
         this.body_fill -= val;
         if (this.body_fill != 0) {
            System.arraycopy(this.body_data, val, this.body_data, 0, this.body_fill);
         }

         this.body_returned = 0;
      }

      if (saved != 0) {
         if (this.lacing_fill - saved != 0) {
            System.arraycopy(this.lacing_vals, saved, this.lacing_vals, 0, this.lacing_fill - saved);
            System.arraycopy(this.granule_vals, saved, this.granule_vals, 0, this.lacing_fill - saved);
         }

         this.lacing_fill -= saved;
         this.lacing_packet -= saved;
         this.lacing_returned = 0;
      }

      if (_serialno != this.serialno) {
         return -1;
      } else if (version > 0) {
         return -1;
      } else {
         this.lacing_expand(segments + 1);
         if (_pageno != this.pageno) {
            for(saved = this.lacing_packet; saved < this.lacing_fill; ++saved) {
               this.body_fill -= this.lacing_vals[saved] & 255;
            }

            this.lacing_fill = this.lacing_packet;
            if (this.pageno != -1) {
               this.lacing_vals[this.lacing_fill++] = 1024;
               ++this.lacing_packet;
            }

            if (continued != 0) {
               for(bos = 0; segptr < segments; ++segptr) {
                  val = header_base[header + 27 + segptr] & 255;
                  body += val;
                  bodysize -= val;
                  if (val < 255) {
                     ++segptr;
                     break;
                  }
               }
            }
         }

         if (bodysize != 0) {
            this.body_expand(bodysize);
            System.arraycopy(body_base, body, this.body_data, this.body_fill, bodysize);
            this.body_fill += bodysize;
         }

         saved = -1;

         int[] var10000;
         int var10001;
         while(segptr < segments) {
            val = header_base[header + 27 + segptr] & 255;
            this.lacing_vals[this.lacing_fill] = val;
            this.granule_vals[this.lacing_fill] = -1L;
            if (bos != 0) {
               var10000 = this.lacing_vals;
               var10001 = this.lacing_fill;
               var10000[var10001] |= 256;
               bos = 0;
            }

            if (val < 255) {
               saved = this.lacing_fill;
            }

            ++this.lacing_fill;
            ++segptr;
            if (val < 255) {
               this.lacing_packet = this.lacing_fill;
            }
         }

         if (saved != -1) {
            this.granule_vals[saved] = granulepos;
         }

         if (eos != 0) {
            this.e_o_s = 1;
            if (this.lacing_fill > 0) {
               var10000 = this.lacing_vals;
               var10001 = this.lacing_fill - 1;
               var10000[var10001] |= 512;
            }
         }

         this.pageno = _pageno + 1;
         return 0;
      }
   }

   public int flush(Page og) {
      int vals = false;
      int maxvals = this.lacing_fill > 255 ? 255 : this.lacing_fill;
      int bytes = 0;
      int acc = 0;
      long granule_pos = this.granule_vals[0];
      if (maxvals == 0) {
         return 0;
      } else {
         int vals;
         if (this.b_o_s == 0) {
            granule_pos = 0L;

            for(vals = 0; vals < maxvals; ++vals) {
               if ((this.lacing_vals[vals] & 255) < 255) {
                  ++vals;
                  break;
               }
            }
         } else {
            for(vals = 0; vals < maxvals && acc <= 4096; ++vals) {
               acc += this.lacing_vals[vals] & 255;
               granule_pos = this.granule_vals[vals];
            }
         }

         System.arraycopy("OggS".getBytes(), 0, this.header, 0, 4);
         this.header[4] = 0;
         this.header[5] = 0;
         byte[] var10000;
         if ((this.lacing_vals[0] & 256) == 0) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 1);
         }

         if (this.b_o_s == 0) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 2);
         }

         if (this.e_o_s != 0 && this.lacing_fill == vals) {
            var10000 = this.header;
            var10000[5] = (byte)(var10000[5] | 4);
         }

         this.b_o_s = 1;

         int i;
         for(i = 6; i < 14; ++i) {
            this.header[i] = (byte)((int)granule_pos);
            granule_pos >>>= 8;
         }

         int _pageno = this.serialno;

         for(i = 14; i < 18; ++i) {
            this.header[i] = (byte)_pageno;
            _pageno >>>= 8;
         }

         if (this.pageno == -1) {
            this.pageno = 0;
         }

         _pageno = this.pageno++;

         for(i = 18; i < 22; ++i) {
            this.header[i] = (byte)_pageno;
            _pageno >>>= 8;
         }

         this.header[22] = 0;
         this.header[23] = 0;
         this.header[24] = 0;
         this.header[25] = 0;
         this.header[26] = (byte)vals;

         for(i = 0; i < vals; ++i) {
            this.header[i + 27] = (byte)this.lacing_vals[i];
            bytes += this.header[i + 27] & 255;
         }

         og.header_base = this.header;
         og.header = 0;
         og.header_len = this.header_fill = vals + 27;
         og.body_base = this.body_data;
         og.body = this.body_returned;
         og.body_len = bytes;
         this.lacing_fill -= vals;
         System.arraycopy(this.lacing_vals, vals, this.lacing_vals, 0, this.lacing_fill * 4);
         System.arraycopy(this.granule_vals, vals, this.granule_vals, 0, this.lacing_fill * 8);
         this.body_returned += bytes;
         og.checksum();
         return 1;
      }
   }

   public int pageout(Page og) {
      return (this.e_o_s == 0 || this.lacing_fill == 0) && this.body_fill - this.body_returned <= 4096 && this.lacing_fill < 255 && (this.lacing_fill == 0 || this.b_o_s != 0) ? 0 : this.flush(og);
   }

   public int eof() {
      return this.e_o_s;
   }

   public int reset() {
      this.body_fill = 0;
      this.body_returned = 0;
      this.lacing_fill = 0;
      this.lacing_packet = 0;
      this.lacing_returned = 0;
      this.header_fill = 0;
      this.e_o_s = 0;
      this.b_o_s = 0;
      this.pageno = -1;
      this.packetno = 0L;
      this.granulepos = 0L;
      return 0;
   }
}
