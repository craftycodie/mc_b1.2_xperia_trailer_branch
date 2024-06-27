package net.minecraft.client.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public class Map extends JComponent {
   private static final long serialVersionUID = 1L;
   private BufferedImage img;
   private int[] pixels;
   private int w;
   private int h;
   private int xMouse;
   private int yMouse;
   public int xCam;
   public int yCam;
   Level level = new Level("TEST", new Dimension(), (new Random()).nextLong());
   String hovered = null;
   public PerlinSimplexNoise scaleNoise = new PerlinSimplexNoise(new Random(), 10);
   public PerlinSimplexNoise depthNoise = new PerlinSimplexNoise(new Random(), 10);
   double[] pnr;
   double[] ar;
   double[] br;
   double[] sr;
   double[] dr;
   double[] fi;
   double[] fis;

   public Map(int w, int h) {
      this.w = w;
      this.h = h;
      this.img = new BufferedImage(w, h, 1);
      this.setPreferredSize(new java.awt.Dimension(w * 2, h * 2));
      this.setMinimumSize(new java.awt.Dimension(w * 2, h * 2));
      this.setMaximumSize(new java.awt.Dimension(w * 2, h * 2));
      this.pixels = ((DataBufferInt)this.img.getRaster().getDataBuffer()).getData();
      this.addMouseListener(new MouseListener() {
         public void mouseClicked(MouseEvent me) {
         }

         public void mouseEntered(MouseEvent me) {
         }

         public void mouseExited(MouseEvent me) {
         }

         public void mousePressed(MouseEvent me) {
            Map.this.xMouse = me.getX() / 2;
            Map.this.yMouse = me.getY() / 2;
         }

         public void mouseReleased(MouseEvent me) {
         }
      });
      this.addMouseMotionListener(new MouseMotionListener() {
         public void mouseDragged(MouseEvent me) {
            Map var10000 = Map.this;
            var10000.xCam += me.getX() / 2 - Map.this.xMouse;
            var10000 = Map.this;
            var10000.yCam += me.getY() / 2 - Map.this.yMouse;
            Map.this.xMouse = me.getX() / 2;
            Map.this.yMouse = me.getY() / 2;
         }

         public void mouseMoved(MouseEvent me) {
            Map.this.xMouse = me.getX() / 2;
            Map.this.yMouse = me.getY() / 2;
         }
      });
   }

   private void buildMapImage(int x, int z, double ss) {
      Biome.recalc();
      Biome[] biomes = this.level.getBiomeSource().getBiomeBlock(x, z, this.w, this.h);
      double[] temperatures = this.level.getBiomeSource().temperatures;
      double[] downfalls = this.level.getBiomeSource().downfalls;
      this.sr = this.scaleNoise.getRegion(this.sr, (double)z, (double)x, this.w, this.h, 1.0D, 1.0D, 0.45454545454545453D);
      this.dr = this.depthNoise.getRegion(this.dr, (double)z, (double)x, this.w, this.h, 1.0D, 1.0D, 0.49751243781094534D, 0.5D);

      for(int i = 0; i < this.pixels.length; ++i) {
         int xp = i / this.w;
         int zp = i % this.w;
         int pp = xp + zp * this.w;
         double scale = (this.sr[pp] + 256.0D) / 512.0D;
         if (scale > 1.0D) {
            scale = 1.0D;
         }

         double temperature = temperatures[i];
         double downfall = downfalls[i] * temperature;
         double dd = 1.0D - downfall;
         dd *= dd;
         dd *= dd;
         dd = 1.0D - dd;
         scale *= dd;
         double depth = this.dr[pp] / 2000.0D;
         if (depth < 0.0D) {
            depth = -depth - 0.1D;
         }

         depth = (depth - 0.01D) * 100.0D;
         if (depth < 0.0D) {
            depth /= 2.0D;
            if (depth < -1.0D) {
               depth = -1.0D;
            }

            depth /= 1.4D;
            scale = 0.0D;
         } else {
            depth /= 6.0D;
            if (depth > 1.0D) {
               depth = 1.0D;
            }

            depth *= dd * 0.8D + 0.2D;
         }

         scale += 0.5D;
         depth = depth * 8.0D / 16.0D;
         double yCenter = 4.0D + depth * 4.0D;
         Biome biome = biomes[xp * this.h + zp];
         if (xp == this.xMouse && zp == this.yMouse) {
            double temp = temperatures[xp * this.h + zp];
            double down = downfalls[xp * this.h + zp];
            this.hovered = biome.name + " (" + (int)(temp * 100.0D) + "/" + (int)(down * 100.0D) + ")";
         }

         if (!(yCenter < 4.0D)) {
            int br = (int)((yCenter - 4.0D) * 64.0D);
            this.pixels[pp] = br << 16 | br << 8 | br;
         }
      }

   }

   public void paint(Graphics g) {
      double s = 0.03125D;
      this.buildMapImage(-this.xCam, -this.yCam, s * 4.0D * 128.0D);
      g.drawImage(this.img, 0, 0, this.w * 2, this.h * 2, 0, 0, this.w, this.h, (ImageObserver)null);
      if (this.hovered != null) {
         g.setColor(Color.black);
         Rectangle r = g.getFontMetrics().getStringBounds(this.hovered, (Graphics)null).getBounds();
         g.fillRect(this.xMouse * 2 + r.x - 2, this.yMouse * 2 + r.y, r.width + 4, r.height);
         g.setColor(Color.white);
         g.drawString(this.hovered, this.xMouse * 2, this.yMouse * 2);
      }

      this.repaint();
   }

   public static void main(String[] args) {
      Map mapComponent = new Map(480, 480);
      JFrame frame = new JFrame("Map test");
      frame.add(mapComponent);
      frame.pack();
      frame.setDefaultCloseOperation(3);
      frame.setLocationRelativeTo((Component)null);
      frame.setVisible(true);
   }
}
