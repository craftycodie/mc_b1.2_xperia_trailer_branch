package paulscode.sound.libraries;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import paulscode.sound.Channel;

public class ChannelLWJGLOpenAL extends Channel {
   public IntBuffer ALSource;
   public int ALformat;
   public int sampleRate;
   ByteBuffer bufferBuffer = BufferUtils.createByteBuffer(5242880);

   public ChannelLWJGLOpenAL(int type, IntBuffer src) {
      super(type);
      this.libraryType = LibraryLWJGLOpenAL.class;
      this.ALSource = src;
   }

   public void cleanup() {
      if (this.ALSource != null) {
         try {
            AL10.alSourceStop(this.ALSource);
            AL10.alGetError();
         } catch (Exception var3) {
         }

         try {
            AL10.alDeleteSources(this.ALSource);
            AL10.alGetError();
         } catch (Exception var2) {
         }

         this.ALSource.clear();
      }

      this.ALSource = null;
      super.cleanup();
   }

   public boolean attachBuffer(IntBuffer buf) {
      if (this.errorCheck(this.channelType != 0, "Sound buffers may only be attached to normal sources.")) {
         return false;
      } else {
         AL10.alSourcei(this.ALSource.get(0), 4105, buf.get(0));
         return this.checkALError();
      }
   }

   public void setAudioFormat(AudioFormat audioFormat) {
      int soundFormat = false;
      short soundFormat;
      if (audioFormat.getChannels() == 1) {
         if (audioFormat.getSampleSizeInBits() == 8) {
            soundFormat = 4352;
         } else {
            if (audioFormat.getSampleSizeInBits() != 16) {
               this.errorMessage("Illegal sample size in method 'setAudioFormat'");
               return;
            }

            soundFormat = 4353;
         }
      } else {
         if (audioFormat.getChannels() != 2) {
            this.errorMessage("Audio data neither mono nor stereo in method 'setAudioFormat'");
            return;
         }

         if (audioFormat.getSampleSizeInBits() == 8) {
            soundFormat = 4354;
         } else {
            if (audioFormat.getSampleSizeInBits() != 16) {
               this.errorMessage("Illegal sample size in method 'setAudioFormat'");
               return;
            }

            soundFormat = 4355;
         }
      }

      this.ALformat = soundFormat;
      this.sampleRate = (int)audioFormat.getSampleRate();
   }

   public void setFormat(int format, int rate) {
      this.ALformat = format;
      this.sampleRate = rate;
   }

   public boolean preLoadBuffers(LinkedList<byte[]> bufferList) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else if (this.errorCheck(bufferList == null, "Buffer List null in method 'preLoadBuffers'")) {
         return false;
      } else {
         boolean playing = this.playing();
         if (playing) {
            AL10.alSourceStop(this.ALSource.get(0));
            this.checkALError();
         }

         int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         IntBuffer streamBuffers;
         if (processed > 0) {
            streamBuffers = BufferUtils.createIntBuffer(processed);
            AL10.alGenBuffers(streamBuffers);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'preLoadBuffers'")) {
               return false;
            }

            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), streamBuffers);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'preLoadBuffers'")) {
               return false;
            }
         }

         if (playing) {
            AL10.alSourcePlay(this.ALSource.get(0));
            this.checkALError();
         }

         streamBuffers = BufferUtils.createIntBuffer(bufferList.size());
         AL10.alGenBuffers(streamBuffers);
         if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
            return false;
         } else {
            for(int i = 0; i < bufferList.size(); ++i) {
               this.bufferBuffer.clear();
               this.bufferBuffer.put((byte[])bufferList.get(i), 0, ((byte[])bufferList.get(i)).length);
               this.bufferBuffer.flip();

               try {
                  AL10.alBufferData(streamBuffers.get(i), this.ALformat, this.bufferBuffer, this.sampleRate);
               } catch (Exception var8) {
                  this.errorMessage("Error creating buffers in method 'preLoadBuffers'");
                  this.printStackTrace(var8);
                  return false;
               }

               if (this.errorCheck(this.checkALError(), "Error creating buffers in method 'preLoadBuffers'")) {
                  return false;
               }
            }

            try {
               AL10.alSourceQueueBuffers(this.ALSource.get(0), streamBuffers);
            } catch (Exception var7) {
               this.errorMessage("Error queuing buffers in method 'preLoadBuffers'");
               this.printStackTrace(var7);
               return false;
            }

            if (this.errorCheck(this.checkALError(), "Error queuing buffers in method 'preLoadBuffers'")) {
               return false;
            } else {
               AL10.alSourcePlay(this.ALSource.get(0));
               return !this.errorCheck(this.checkALError(), "Error playing source in method 'preLoadBuffers'");
            }
         }
      }
   }

   public boolean queueBuffer(byte[] buffer) {
      if (this.errorCheck(this.channelType != 1, "Buffers may only be queued for streaming sources.")) {
         return false;
      } else {
         this.bufferBuffer.clear();
         this.bufferBuffer.put(buffer, 0, buffer.length);
         this.bufferBuffer.flip();
         IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
         AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
         if (this.checkALError()) {
            return false;
         } else {
            AL10.alBufferData(intBuffer.get(0), this.ALformat, this.bufferBuffer, this.sampleRate);
            if (this.checkALError()) {
               return false;
            } else {
               AL10.alSourceQueueBuffers(this.ALSource.get(0), intBuffer);
               return !this.checkALError();
            }
         }
      }
   }

   public int feedRawAudioData(byte[] buffer) {
      if (this.errorCheck(this.channelType != 1, "Raw audio data can only be fed to streaming sources.")) {
         return -1;
      } else {
         ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, buffer.length);
         int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         IntBuffer intBuffer;
         if (processed > 0) {
            intBuffer = BufferUtils.createIntBuffer(processed);
            AL10.alGenBuffers(intBuffer);
            if (this.errorCheck(this.checkALError(), "Error clearing stream buffers in method 'feedRawAudioData'")) {
               return -1;
            }

            AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
            if (this.errorCheck(this.checkALError(), "Error unqueuing stream buffers in method 'feedRawAudioData'")) {
               return -1;
            }
         } else {
            intBuffer = BufferUtils.createIntBuffer(1);
            AL10.alGenBuffers(intBuffer);
            if (this.errorCheck(this.checkALError(), "Error generating stream buffers in method 'preLoadBuffers'")) {
               return -1;
            }
         }

         AL10.alBufferData(intBuffer.get(0), this.ALformat, byteBuffer, this.sampleRate);
         if (this.checkALError()) {
            return -1;
         } else {
            AL10.alSourceQueueBuffers(this.ALSource.get(0), intBuffer);
            if (this.checkALError()) {
               return -1;
            } else {
               if (this.attachedSource != null && this.attachedSource.channel == this && this.attachedSource.active() && !this.playing()) {
                  AL10.alSourcePlay(this.ALSource.get(0));
                  this.checkALError();
               }

               return processed;
            }
         }
      }
   }

   public int buffersProcessed() {
      if (this.channelType != 1) {
         return 0;
      } else {
         int processed = AL10.alGetSourcei(this.ALSource.get(0), 4118);
         return this.checkALError() ? 0 : processed;
      }
   }

   public void flush() {
      if (this.channelType == 1) {
         int queued = AL10.alGetSourcei(this.ALSource.get(0), 4117);
         if (!this.checkALError()) {
            for(IntBuffer intBuffer = BufferUtils.createIntBuffer(1); queued > 0; --queued) {
               try {
                  AL10.alSourceUnqueueBuffers(this.ALSource.get(0), intBuffer);
               } catch (Exception var4) {
                  return;
               }

               if (this.checkALError()) {
                  return;
               }
            }

         }
      }
   }

   public void close() {
      try {
         AL10.alSourceStop(this.ALSource.get(0));
         AL10.alGetError();
      } catch (Exception var2) {
      }

      if (this.channelType == 1) {
         this.flush();
      }

   }

   public void play() {
      AL10.alSourcePlay(this.ALSource.get(0));
      this.checkALError();
   }

   public void pause() {
      AL10.alSourcePause(this.ALSource.get(0));
      this.checkALError();
   }

   public void stop() {
      AL10.alSourceStop(this.ALSource.get(0));
      this.checkALError();
   }

   public void rewind() {
      if (this.channelType != 1) {
         AL10.alSourceRewind(this.ALSource.get(0));
         this.checkALError();
      }
   }

   public boolean playing() {
      int state = AL10.alGetSourcei(this.ALSource.get(0), 4112);
      if (this.checkALError()) {
         return false;
      } else {
         return state == 4114;
      }
   }

   private boolean checkALError() {
      switch(AL10.alGetError()) {
      case 0:
         return false;
      case 40961:
         this.errorMessage("Invalid name parameter.");
         return true;
      case 40962:
         this.errorMessage("Invalid parameter.");
         return true;
      case 40963:
         this.errorMessage("Invalid enumerated parameter value.");
         return true;
      case 40964:
         this.errorMessage("Illegal call.");
         return true;
      case 40965:
         this.errorMessage("Unable to allocate memory.");
         return true;
      default:
         this.errorMessage("An unrecognized error occurred.");
         return true;
      }
   }
}
