package net.minecraft.client;

public class Timer {
   private static final int MAX_TICKS_PER_UPDATE = 10;
   float ticksPerSecond;
   private double lastTime;
   public int ticks;
   public float a;
   public float timeScale = 1.0F;
   public float passedTime = 0.0F;
   private long lastMs;
   private long lastMsSysTime;
   private double adjustTime = 1.0D;

   public Timer(float ticksPerSecond) {
      this.ticksPerSecond = ticksPerSecond;
      this.lastMs = System.currentTimeMillis();
      this.lastMsSysTime = System.nanoTime() / 1000000L;
   }

   public void advanceTime() {
      long nowMs = System.currentTimeMillis();
      long passedMs = nowMs - this.lastMs;
      long msSysTime = System.nanoTime() / 1000000L;
      double passedSeconds;
      if (passedMs > 1000L) {
         long passedMsSysTime = msSysTime - this.lastMsSysTime;
         passedSeconds = (double)passedMs / (double)passedMsSysTime;
         this.adjustTime += (passedSeconds - this.adjustTime) * 0.20000000298023224D;
         this.lastMs = nowMs;
         this.lastMsSysTime = msSysTime;
      }

      if (passedMs < 0L) {
         this.lastMs = nowMs;
         this.lastMsSysTime = msSysTime;
      }

      double now = (double)msSysTime / 1000.0D;
      passedSeconds = (now - this.lastTime) * this.adjustTime;
      this.lastTime = now;
      if (passedSeconds < 0.0D) {
         passedSeconds = 0.0D;
      }

      if (passedSeconds > 1.0D) {
         passedSeconds = 1.0D;
      }

      this.passedTime = (float)((double)this.passedTime + passedSeconds * (double)this.timeScale * (double)this.ticksPerSecond);
      this.ticks = (int)this.passedTime;
      this.passedTime -= (float)this.ticks;
      if (this.ticks > 10) {
         this.ticks = 10;
      }

      this.a = this.passedTime;
   }

   public void skipTime() {
      long nowMs = System.currentTimeMillis();
      long passedMs = nowMs - this.lastMs;
      long msSysTime = System.nanoTime() / 1000000L;
      double passedSeconds;
      if (passedMs > 1000L) {
         long passedMsSysTime = msSysTime - this.lastMsSysTime;
         passedSeconds = (double)passedMs / (double)passedMsSysTime;
         this.adjustTime += (passedSeconds - this.adjustTime) * 0.20000000298023224D;
         this.lastMs = nowMs;
         this.lastMsSysTime = msSysTime;
      }

      if (passedMs < 0L) {
         this.lastMs = nowMs;
         this.lastMsSysTime = msSysTime;
      }

      double now = (double)msSysTime / 1000.0D;
      passedSeconds = (now - this.lastTime) * this.adjustTime;
      this.lastTime = now;
      if (passedSeconds < 0.0D) {
         passedSeconds = 0.0D;
      }

      if (passedSeconds > 1.0D) {
         passedSeconds = 1.0D;
      }

      this.passedTime = (float)((double)this.passedTime + passedSeconds * (double)this.timeScale * (double)this.ticksPerSecond);
      this.ticks = 0;
      if (this.ticks > 10) {
         this.ticks = 10;
      }

      this.passedTime -= (float)this.ticks;
   }
}
