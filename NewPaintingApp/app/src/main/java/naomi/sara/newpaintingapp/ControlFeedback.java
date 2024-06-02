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
    String[] brushNames = new String[] {"thin", "square", "round"};
    String[] canvasNames = new String[] {"wood", "canvas", "silk", "glass"};

    // Variables needed for the PlaySound class - parametric mode
    private int sampleRate;
    private int buffLength;
    private int amplitude;
    private int frequency;


    public void initializeClip(PlayClip clip, Context context, int chosenBrush, int chosenBackground, String soundType) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            (new Thread(()-> {
            int fastSound = context.getResources().getIdentifier(soundType + "_" +
                            brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_f",
                    "raw", context.getPackageName());
            int mediumSound = context.getResources().getIdentifier(soundType + "_" +
                            brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_m",
                    "raw", context.getPackageName());
            int slowSound = context.getResources().getIdentifier(soundType + "_" +
                            brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_s",
                    "raw", context.getPackageName());

            clip.init(context, fastSound, mediumSound, slowSound);

            })).start();
        }
    }

    /**
     * Method used to initialize and start the MediaPlayer in the different PlayHaptics instances.
     * This also mutes the fast and medium haptics, while keeping the slow haptics unmuted.
     * @param playFast            Instance of a class that implements PlayInterface. Plays the fast
     *                            haptics/sounds
     * @param playMedium          Instance of a class that implements PlayInterface. Plays the medium
     *                            haptics/sounds
     * @param playSlow            Instance of a class that implements PlayInterface. Plays the slow
     *                            haptics/sounds
     * @param context             Context of the DrawingView class need to retrieve the haptics
     *                            sound files
     * @param chosenBrush         The brush chosen in the brush spinner
     * @param chosenBackground    The canvas chosen in the canvas spinner
     */
    public void initializeFeedback(PlayInterface playFast, PlayInterface playMedium, PlayInterface playSlow,
                           Context context, int chosenBrush, int chosenBackground, String soundType) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.S){
            (new Thread(()-> {
                int fastSound = context.getResources().getIdentifier(soundType + "_" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_f",
                        "raw", context.getPackageName());
                int mediumSound = context.getResources().getIdentifier(soundType + "_" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_m",
                        "raw", context.getPackageName());
                int slowSound = context.getResources().getIdentifier(soundType + "_" +
                                brushNames[chosenBrush] + "_" + canvasNames[chosenBackground] + "_s",
                        "raw", context.getPackageName());

                playFast.init(context, fastSound);
                playFast.startPlaying(context);
                playFast.setVolume(0.0f, 0.0f);

                playMedium.init(context, mediumSound);
                playMedium.startPlaying(context);
                playMedium.setVolume(0.0f, 0.0f);

                playSlow.init(context, slowSound);
                playSlow.startPlaying(context);
                playSlow.setVolume(1.0f,1.0f);

            })).start();
        }
    }

    /**
     * Method used to change the volume of the MediaPlayer in the different PlayHaptics instances
     * depending on the velocity of the user's movements.
     * @param velocity     The velocity of the user's movements on the screen
     * @param playFast     Instance of a class that implements PlayInterface. Plays the fast
     *                     haptics/sounds
     * @param playMedium   Instance of a class that implements PlayInterface. Plays the medium
     *                     haptics/sounds
     * @param playSlow     Instance of a class that implements PlayInterface. Plays the fast
     *                     haptics/sounds
     */
    public void velocityFeedback(double velocity, PlayInterface playFast, PlayInterface playMedium,
                                 PlayInterface playSlow) {
        //(new Thread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //Log.d("VELOCITY", "Velocity: " + velocity);

                if (velocity > 0.045) {
                    playFast.setVolume(1.0f,1.0f);
                    playMedium.setVolume(0.0f,0.0f);
                    playSlow.setVolume(0.0f,0.0f);
                } else if (velocity > 0.025) {
                    playFast.setVolume(0.0f,0.0f);
                    playMedium.setVolume(1.0f,1.0f);
                    playSlow.setVolume(0.0f,0.0f);
                } else {
                    playFast.setVolume(0.0f,0.0f);
                    playMedium.setVolume(0.0f,0.0f);
                    playSlow.setVolume(1.0f,1.0f);
                }
            }
        //})).start();
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
