package paulscode.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StreamThread extends SimpleThread {
   private SoundSystemLogger logger = SoundSystemConfig.getLogger();
   private List<Source> streamingSources = new LinkedList();
   private final Object listLock = new Object();

   protected void cleanup() {
      this.kill();
      super.cleanup();
   }

   public void run() {
      try {
         this.snooze(3600000L);

         while(!this.dying()) {
            while(!this.dying() && !this.streamingSources.isEmpty()) {
               synchronized(this.listLock) {
                  ListIterator iter = this.streamingSources.listIterator();

                  while(true) {
                     if (this.dying() || !iter.hasNext()) {
                        break;
                     }

                     Source src = (Source)iter.next();
                     if (src == null) {
                        iter.remove();
                     } else if (src.stopped()) {
                        if (!src.rawDataStream) {
                           iter.remove();
                        }
                     } else if (src.active()) {
                        if (!src.paused()) {
                           src.checkFadeOut();
                           if (!src.stream() && !src.rawDataStream && (src.channel == null || !src.channel.processBuffer())) {
                              if (src.toLoop) {
                                 if (!src.playing()) {
                                    if (src.checkFadeOut()) {
                                       src.preLoad = true;
                                    } else {
                                       src.incrementSoundSequence();
                                       src.preLoad = true;
                                    }
                                 }
                              } else if (!src.playing() && !src.checkFadeOut()) {
                                 if (src.incrementSoundSequence()) {
                                    src.preLoad = true;
                                 } else {
                                    iter.remove();
                                 }
                              }
                           }
                        }
                     } else {
                        if (src.toLoop || src.rawDataStream) {
                           src.toPlay = true;
                        }

                        iter.remove();
                     }
                  }
               }

               if (!this.dying() && !this.streamingSources.isEmpty()) {
                  this.snooze(20L);
               }
            }

            if (!this.dying() && this.streamingSources.isEmpty()) {
               this.snooze(3600000L);
            }
         }
      } finally {
         this.cleanup();
      }

   }

   public void watch(Source source) {
      if (source != null) {
         if (!this.streamingSources.contains(source)) {
            synchronized(this.listLock) {
               ListIterator iter = this.streamingSources.listIterator();

               while(iter.hasNext()) {
                  Source src = (Source)iter.next();
                  if (src == null) {
                     iter.remove();
                  } else if (source.channel == src.channel) {
                     src.stop();
                     iter.remove();
                  }
               }

               this.streamingSources.add(source);
            }
         }
      }
   }

   private void message(String message) {
      this.logger.message(message, 0);
   }

   private void importantMessage(String message) {
      this.logger.importantMessage(message, 0);
   }

   private boolean errorCheck(boolean error, String message) {
      return this.logger.errorCheck(error, "StreamThread", message, 0);
   }

   private void errorMessage(String message) {
      this.logger.errorMessage("StreamThread", message, 0);
   }
}
