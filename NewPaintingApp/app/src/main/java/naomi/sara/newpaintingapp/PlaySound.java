package naomi.sara.newpaintingapp;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

/**
 * Class that simulates a pure sine wave. Skeleton code is originally taken from
 * <a href="https://rajat-r-bapuri.github.io/DSP-Lab-Android-Demos/Android_Demos/java_implementations/Sine_Wave_Demo1/">here</a>.
 * The class inherits from the Activity class.
 */
public class PlaySound extends Activity {
    private AudioTrack Track;
    public boolean isPlaying = false;

    /**
     * Creates an AudioTrack instance and initialize it with different parameters
     * @param sampleRate    The sample rate in hz of the track to be created
     * @param buffLength    The length of the buffer
     */
    public void initTrack(int sampleRate, int buffLength) {
        // Very similar to opening a stream in PyAudio

        Track = new AudioTrack.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM) //USAGE_ALARM
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)//CONTENT_TYPE_SONIFICATION
                        .build())
                .setAudioFormat(new AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build())
                .setBufferSizeInBytes(buffLength)
                .build();
    }

    /**
     * Plays the AudioTrack instance and sets the variable isPLaying to true, so we know if an
     * AudioTrack is playing.
     */
    public void startPlaying() {
        Track.play();
        isPlaying = true;
    }

    /**
     * Specifies the sine wave to be played
     * @param amplitude     Amplitude of the sine wave
     * @param frequency     Frequency of the sine wave
     * @param sampleRate    The sample rate of the track (is the same as in initTrack())
     * @param buffLength    The length of the buffer (is the same as in initTrack())
     */
    public void playback(int amplitude, int frequency, int sampleRate, int buffLength) {
        // simple sine wave generator
        short[] frame_out = new short[buffLength];
        double twopi = 8. * Math.atan(1.); // pi can be described as 4. * Math.atan(1.)
        double phase = 0.0;
        while (isPlaying) {
            for (int i = 0; i < buffLength; i++) {
                frame_out[i] = (short) (amplitude * Math.sin(phase));
                phase += twopi * frequency / sampleRate;
                if (phase > twopi) {
                    phase -= twopi;
                }
            }
            Track.write(frame_out, 0, buffLength);
        }
    }

    /**
     * Stops the AudioTrack instance and sets the variable isPLaying to false, so we know that the
     * AudioTrack isn't playing.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            // Stop playing the audio data and release the resources
            Track.stop();
            Track.release();
        }
    }
}