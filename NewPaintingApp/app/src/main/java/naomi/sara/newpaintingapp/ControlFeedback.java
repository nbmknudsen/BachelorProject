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
    String[] brushNames = new String[] {"thin", "square", "round"};;
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
                int fastSound = context.getResources().getIdentifier("sound" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_f",
                        "raw", context.getPackageName());
                int mediumSound = context.getResources().getIdentifier("sound" +
                        brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_m",
                        "raw", context.getPackageName());
                int slowSound = context.getResources().getIdentifier("sound" +
                        brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_s_",
                        "raw", context.getPackageName());

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
     * Method used to initialize an start the MediaPlayers. This also mutes the fast and medium
     * sounds, while keeping the slow sound unmuted.
     * @param playHapticsFast     PlayHaptics instance used to play the fast haptics
     * @param playHapticsMedium   PlayHaptics instance used to play the medium haptics
     * @param playHapticsSlow     PlayHaptics instance used to play the slow haptics
     * @param context             Context of the DrawingView class need to retrieve the haptics
     *                            sound files
     * @param chosenBrush         The brush chosen in the brush spinner
     * @param chosenBackground    The canvas chosen in the canvas spinner
     */
    public void initializeHaptics(PlayHaptics playHapticsFast, PlayHaptics playHapticsMedium,
                                PlayHaptics playHapticsSlow, Context context, int chosenBrush,
                                int chosenBackground) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            (new Thread(()->{
                int fastSound = context.getResources().getIdentifier("haptics" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_f",
                        "raw", context.getPackageName());
                int mediumSound = context.getResources().getIdentifier("haptics" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_m",
                        "raw", context.getPackageName());
                int slowSound = context.getResources().getIdentifier("haptics" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_s_",
                        "raw", context.getPackageName());

                playHapticsFast.init(context, fastSound);
                playHapticsFast.setVolume(0, 0);
                playHapticsFast.startPlaying(context);

                playHapticsMedium.init(context, fastSound);
                playHapticsMedium.setVolume(0, 0);
                playHapticsMedium.startPlaying(context);

                playHapticsSlow.init(context, slowSound);
                playHapticsSlow.startPlaying(context);
                playHapticsSlow.setVolume(1,1);

            })).start();
        }
    }

    /**
     * Method used to change the volume of the different MediaPlayers depending on the velocity of
     * the user's movements.
     * @param velocity          The velocity of the user's movements on the screen
     * @param playHapticsFast     PlayHaptics instance used to play the fast haptics
     * @param playHapticsMedium   PlayHaptics instance used to play the medium haptics
     * @param playHapticsSlow     PlayHaptics instance used to play the slow haptics
     */
    public void velocityHaptics(double velocity, PlayHaptics playHapticsFast, PlayHaptics playHapticsMedium,
                              PlayHaptics playHapticsSlow) {
        (new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //Log.d("VELOCITY", "Velocity: " + velocity);

                if (velocity > 0.045) {
                    playHapticsFast.setVolume(1,1);
                    playHapticsMedium.setVolume(0,0);
                    playHapticsSlow.setVolume(0,0);
                } else if (velocity > 0.025) {
                    playHapticsFast.setVolume(0,0);
                    playHapticsMedium.setVolume(1,1);
                    playHapticsSlow.setVolume(0,0);
                } else {
                    playHapticsFast.setVolume(0,0);
                    playHapticsMedium.setVolume(0,0);
                    playHapticsSlow.setVolume(1,1);
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
