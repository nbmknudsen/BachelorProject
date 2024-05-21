package naomi.sara.newpaintingapp;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Build;

/**
 * Class with methods used to control auditory and haptic feedback
 */
public class ControlFeedback {

    //  Array of brush names and array of canvas names
    String[] brushNames = new String[] {"thin", "flat", "round"};;
    String[] canvasNames = new String[] {"wood", "canvas", "silk", "glass"};;

    // Variables needed for the PlaySound class - parametric mode
    private int sampleRate;
    private int buffLength;
    private int amplitude;
    private int frequency;

    /**
     * Method used to initialize an start the MediaPlayers. This also mutes the fast and medium
     * sounds, while keeping the slow sound unmuted.
     * @param playMusicFast     PlayMusic instance used to play the fast sounds
     * @param playMusicMedium   PlayMusic instance used to play the medium sounds
     * @param playMusicSlow     PlayMusic instance used to play the slow sounds
     * @param context           Context of the DrawingView class need to retrieve the sound files
     * @param chosenBrush       The brush chosen in the brush spinner
     * @param chosenBackground  The canvas chosen in the canvas spinner
     */
    public void initializeMusic(PlayMusic playMusicFast, PlayMusic playMusicMedium,
                                 PlayMusic playMusicSlow, Context context, int chosenBrush,
                                 int chosenBackground) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            (new Thread(()->{
                int fastSound = context.getResources().getIdentifier(
                        "g4_"+"wood"+"_type_silver_oak_sound", "raw",
                        context.getPackageName());
                int mediumSound = context.getResources().getIdentifier(
                        brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] +
                                "_m_haptics", "raw", context.getPackageName());
                int slowSound = context.getResources().getIdentifier(
                        brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] +
                                "_s_4_haptics", "raw", context.getPackageName());

                playMusicFast.init(context, fastSound);
                playMusicFast.setVolume(0, 0);
                playMusicFast.startPlaying(context);

                playMusicMedium.init(context, fastSound);
                playMusicMedium.setVolume(0, 0);
                playMusicMedium.startPlaying(context);

                playMusicSlow.init(context, slowSound);
                playMusicSlow.startPlaying(context);
                playMusicSlow.setVolume(1,1);

            })).start();
        }
    }

    /**
     * Method used to change the volume of the different MediaPlayers depending on the velocity of
     * the user's movements.
     * @param velocity          The velocity of the user's movements on the screen
     * @param playMusicFast     PlayMusic instance used to play the fast sounds
     * @param playMusicMedium   PlayMusic instance used to play the medium sounds
     * @param playMusicSlow     PlayMusic instance used to play the slow sounds
     */
    public void velocityMusic(double velocity, PlayMusic playMusicFast, PlayMusic playMusicMedium,
                               PlayMusic playMusicSlow) {
        (new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //Log.d("VELOCITY", "Velocity: " + velocity);

                if (velocity > 0.045) {
                    playMusicFast.setVolume(1,1);
                    playMusicMedium.setVolume(0,0);
                    playMusicSlow.setVolume(0,0);
                } else if (velocity > 0.025) {
                    playMusicFast.setVolume(0,0);
                    playMusicMedium.setVolume(1,1);
                    playMusicSlow.setVolume(0,0);
                } else {
                    playMusicFast.setVolume(0,0);
                    playMusicMedium.setVolume(0,0);
                    playMusicSlow.setVolume(1,1);
                }
            }
        })).start();
    }

    /**
     * Plays the correct frequency and amplitude and sample rate of canvas. This is only used with
     * the PlaySound class.
     */
    private void playParametricHaptics(PlaySound playSound, int chosenBackground) {
        if (!playSound.isPlaying) {
            // Create a new thread to play the audio.
            (new Thread(() -> {
                switch (chosenBackground) {
                    case 0:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 140;
                        break;
                    case 1:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 90;
                        break;
                    case 2:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 300;
                        break;
                    case 3:
                        sampleRate = 41000;
                        amplitude = 3000;
                        frequency = 30;
                        break;
                    default:
                }
                buffLength = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
                playSound.initTrack(sampleRate, buffLength);
                playSound.startPlaying();
                playSound.playback(amplitude, frequency, sampleRate, buffLength);
            })).start();
        }
    }
}
