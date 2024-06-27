package paulscode.sound.libraries;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import paulscode.sound.Channel;
import paulscode.sound.FilenameURL;
import paulscode.sound.ICodec;
import paulscode.sound.Library;
import paulscode.sound.SoundBuffer;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.Source;

public class LibraryJavaSound extends Library {
   private static final boolean GET = false;
   private static final boolean SET = true;
   private final int maxClipSize = 1048576;
   private static Mixer myMixer = null;
   private static LibraryJavaSound instance = null;

   public LibraryJavaSound() throws SoundSystemException {
      instance = this;
   }

   public void init() throws SoundSystemException {
      if (myMixer == null) {
         myMixer = AudioSystem.getMixer((Info)null);
      }

      if (myMixer == null) {
         this.importantMessage("\"Java Sound Audio Engine\" was not found!");
         super.init();
         throw new SoundSystemException("\"Java Sound Audio Engine\" was not found in the list of available mixers.", 14);
      } else {
         this.setMasterVolume(1.0F);
         this.message("JavaSound initialized.");
         super.init();
      }
   }

   public static boolean libraryCompatible() {
      Info[] var3;
      int var2 = (var3 = AudioSystem.getMixerInfo()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         Info mixerInfo = var3[var1];
         if (mixerInfo.getName().equals("Java Sound Audio Engine")) {
            return true;
         }
      }

      return false;
   }

   protected Channel createChannel(int type) {
      return new ChannelJavaSound(type, myMixer);
   }

   public void cleanup() {
      super.cleanup();
      instance = null;
      myMixer = null;
   }

   public boolean loadSound(FilenameURL filenameURL) {
      if (this.bufferMap == null) {
         this.bufferMap = new HashMap();
         this.importantMessage("Buffer Map was null in method 'loadSound'");
      }

      if (this.errorCheck(filenameURL == null, "Filename/URL not specified in method 'loadSound'")) {
         return false;
      } else if (this.bufferMap.get(filenameURL.getFilename()) != null) {
         return true;
      } else {
         ICodec codec = SoundSystemConfig.getCodec(filenameURL.getFilename());
         if (this.errorCheck(codec == null, "No codec found for file '" + filenameURL.getFilename() + "' in method 'loadSound'")) {
            return false;
         } else {
            URL url = filenameURL.getURL();
            if (this.errorCheck(url == null, "Unable to open file '" + filenameURL.getFilename() + "' in method 'loadSound'")) {
               return false;
            } else {
               codec.initialize(url);
               SoundBuffer buffer = codec.readAll();
               codec.cleanup();
               codec = null;
               if (buffer != null) {
                  this.bufferMap.put(filenameURL.getFilename(), buffer);
               } else {
                  this.errorMessage("Sound buffer null in method 'loadSound'");
               }

               return true;
            }
         }
      }
   }

   public void setMasterVolume(float value) {
      super.setMasterVolume(value);
      Set<String> keys = this.sourceMap.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String sourcename = (String)iter.next();
         Source source = (Source)this.sourceMap.get(sourcename);
         if (source != null) {
            source.positionChanged();
         }
      }

   }

   public void newSource(boolean priority, boolean toStream, boolean toLoop, String sourcename, FilenameURL filenameURL, float x, float y, float z, int attModel, float distOrRoll) {
      SoundBuffer buffer = null;
      if (!toStream) {
         buffer = (SoundBuffer)this.bufferMap.get(filenameURL.getFilename());
         if (buffer == null && !this.loadSound(filenameURL)) {
            this.errorMessage("Source '" + sourcename + "' was not created " + "because an error occurred while loading " + filenameURL.getFilename());
            return;
         }

         buffer = (SoundBuffer)this.bufferMap.get(filenameURL.getFilename());
         if (buffer == null) {
            this.errorMessage("Source '" + sourcename + "' was not created " + "because audio data was not found for " + filenameURL.getFilename());
            return;
         }
      }

      if (!toStream && buffer != null) {
         buffer.trimData(1048576);
      }

      this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, false));
   }

   public void rawDataStream(AudioFormat audioFormat, boolean priority, String sourcename, float x, float y, float z, int attModel, float distOrRoll) {
      this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll));
   }

   public void quickPlay(boolean priority, boolean toStream, boolean toLoop, String sourcename, FilenameURL filenameURL, float x, float y, float z, int attModel, float distOrRoll, boolean temporary) {
      SoundBuffer buffer = null;
      if (!toStream) {
         buffer = (SoundBuffer)this.bufferMap.get(filenameURL.getFilename());
         if (buffer == null && !this.loadSound(filenameURL)) {
            this.errorMessage("Source '" + sourcename + "' was not created " + "because an error occurred while loading " + filenameURL.getFilename());
            return;
         }

         buffer = (SoundBuffer)this.bufferMap.get(filenameURL.getFilename());
         if (buffer == null) {
            this.errorMessage("Source '" + sourcename + "' was not created " + "because audio data was not found for " + filenameURL.getFilename());
            return;
         }
      }

      if (!toStream && buffer != null) {
         buffer.trimData(1048576);
      }

      this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, temporary));
   }

   public void copySources(HashMap<String, Source> srcMap) {
      if (srcMap != null) {
         Set<String> keys = srcMap.keySet();
         Iterator<String> iter = keys.iterator();
         if (this.bufferMap == null) {
            this.bufferMap = new HashMap();
            this.importantMessage("Buffer Map was null in method 'copySources'");
         }

         this.sourceMap.clear();

         while(true) {
            String sourcename;
            Source source;
            SoundBuffer buffer;
            do {
               do {
                  if (!iter.hasNext()) {
                     return;
                  }

                  sourcename = (String)iter.next();
                  source = (Source)srcMap.get(sourcename);
               } while(source == null);

               buffer = null;
               if (!source.toStream) {
                  this.loadSound(source.filenameURL);
                  buffer = (SoundBuffer)this.bufferMap.get(source.filenameURL.getFilename());
               }

               if (!source.toStream && buffer != null) {
                  buffer.trimData(1048576);
               }
            } while(!source.toStream && buffer == null);

            this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, source, buffer));
         }
      }
   }

   public static Mixer getMixer() {
      return mixer(false, (Mixer)null);
   }

   public static void setMixer(Mixer m) throws SoundSystemException {
      mixer(true, m);
      SoundSystemException e = SoundSystem.getLastException();
      SoundSystem.setException((SoundSystemException)null);
      if (e != null) {
         throw e;
      }
   }

   private static synchronized Mixer mixer(boolean action, Mixer m) {
      if (action) {
         myMixer = m;
         if (instance != null) {
            ListIterator<Channel> itr = instance.normalChannels.listIterator();
            SoundSystem.setException((SoundSystemException)null);

            ChannelJavaSound c;
            while(itr.hasNext()) {
               c = (ChannelJavaSound)itr.next();
               c.newMixer(m);
            }

            itr = instance.streamingChannels.listIterator();

            while(itr.hasNext()) {
               c = (ChannelJavaSound)itr.next();
               c.newMixer(m);
            }
         }
      }

      return myMixer;
   }

   public static String getTitle() {
      return "Java Sound";
   }

   public static String getDescription() {
      return "The Java Sound API.  For more information, see http://java.sun.com/products/java-media/sound/";
   }

   public String getClassName() {
      return "LibraryJavaSound";
   }
}
