package naomi.sara.newpaintingapp;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.io.IOException;

/**
 * Class used to play haptic feedback.
 */
public class PlayHaptics {
    private MediaPlayer mediaPlayer;
    private AudioManager manager;
    private boolean isPlaying = false;
    private float volume = 0.0f;

    /**
     * Creates a MediaPlayer instance and initialize it with different parameters. It also
     * initializes an AudioManager.
     * @param context Context - used to create a Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void init(Context context, int soundFileName){
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + soundFileName);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA) //only in headphones
                .build());

        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * Plays the MediaPlayer instance and sets the variable isPLaying to true, so we know if the
     * MediaPlayer is playing. Sets the aux line as the preferred device to send sound.
     * @param context Context - used to check if device is connected to bluetooth
     */
    public void startPlaying(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            AudioDeviceInfo aux_line = findAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADPHONES, context);
            if (aux_line != null) {
                mediaPlayer.setPreferredDevice(aux_line);
                mediaPlayer.setOnPreparedListener(mp -> {
                    if (!mp.isPlaying()) {
                        System.out.println("Went into if statement in starPlaying");
                        mp.setLooping(true);
                        //long startTime = System.currentTimeMillis();
                        mp.start();
                        //long endTime = System.currentTimeMillis();
                        //System.out.println("Time taken to read and play the .wav file: " + (endTime - startTime) + " milliseconds");
                        isPlaying = true;
                        volume = 1.0f;
                    }
                });
            }
        }
    }

    /**
     * Sets the left and right volume of the MediaPLayer. We use this to mute the players we don't
     * want to hear and unmute when we want to hear the players again. 0 is mute and 1 is unmute.
     * @param leftVolume   Value of the wanted left volume. This should be the same as rightVolume.
     * @param rightVolume  Value of the wanted right volume This should be the same as leftVolume.
     */
    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
            volume = leftVolume;
        }
    }

    /**
     * Pauses the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing.
     */
    public void pausePlaying(){
        if (isPlaying && mediaPlayer != null) {
            isPlaying = false;
            mediaPlayer.pause();
        }
    }

    /**
     * Stops the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing.
     * It also releases the MediaPlayer resources so it has to be initialized again.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            // Stop playing the audio data and release the resources
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public float getVolume() {
        return volume;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /** Function used to find out if it is possible to connect to specified device. Skeleton code
     * is originally taken from Daniel Beltrami's answer
     * <a href="https://stackoverflow.com/questions/70489941/how-do-i-get-android-media-setpreferreddevice-to-work">here</a>.
     * @param deviceType The AudioDeviceInfo of the device you want to connect to
     * @param context Context - there has to be some context or the AudioManager can't get devices
     */
    public AudioDeviceInfo findAudioDevice(int deviceType, Context context) {
        if (context != null) {
            AudioDeviceInfo[] adis = manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo adi : adis) {
                Log.d("CONNECTION TYPES", String.valueOf(adi.getType()));
                if (adi.getType() == deviceType) {
                    Log.d("AUX CONNECTION", String.valueOf(adi));
                    return adi;
                }
            }
        }
        return null;
    }
}