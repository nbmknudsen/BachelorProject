package naomi.sara.newpaintingapp;

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
 * Class used to play auditory feedback. The class implements the PlayInterface interface.
 */
public class PlayMusicOld implements PlayInterface { //extends Activity The class inherits from the Activity class.
    private MediaPlayer mediaPlayer;
    private AudioManager manager;
    private boolean isPlaying = false;
    private float volume = 0f;

    /**
     * Creates a MediaPlayer instance and initialize it with different parameters
     *
     * @param context Context - used to create a Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void init(Context context, int fileName) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build());

        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + fileName);
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
     * MediaPlayer is playing. Sets the bluetooth headset as the preferred device to send sound.
     *
     * @param context Context - used to check if device is connected to bluetooth
     */
    public void startPlaying(Context context) {
        AudioDeviceInfo bt_headset = findAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP, context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (bt_headset == null) {
                //AudioDeviceInfo speaker = findAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, context);
                manager.setSpeakerphoneOn(true);
                //mediaPlayer.setPreferredDevice(speaker);
            } else {
                Log.d("BLUETOOTH CONNECTION", String.valueOf(bt_headset.getType()));

                    mediaPlayer.setPreferredDevice(bt_headset);
                }
        }
        mediaPlayer.setOnPreparedListener(mp -> {
            if (!mp.isPlaying()) {
                mp.setLooping(true);
                mp.start();
                isPlaying = true;
                volume = 1.0f;
            }
        });
    }

    /**
     * Sets the left and right volume of the MediaPLayer. We use this to mute the players we don't
     * want to hear and unmute when we want to hear the players again. 0 is mute and 1 is unmute.
     *
     * @param leftVolume  Value of the wanted left volume. This should be the same as rightVolume.
     * @param rightVolume Value of the wanted right volume This should be the same as leftVolume.
     */
    public void setVolume(float leftVolume, float rightVolume) {
        if (mediaPlayer != null && leftVolume == rightVolume && (leftVolume == 0f || leftVolume == 1f)) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
            volume = leftVolume;
        }
    }


    /**
     * Pauses the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing.
     */
    public void pausePlaying(){
        if (isPlaying) {
            isPlaying = false;
            mediaPlayer.pause();
        }
    }

    /**
     * Stops the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing. It also releases the MediaPlayer resources so it have to be
     * initialized again.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            // Stop playing the audio data and release the resources
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
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
        //Log.d("DEVICE INFO", String.valueOf(deviceType));
        if (context != null) {
            AudioDeviceInfo[] adis = manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo adi : adis) {
                Log.d("CONNECTION TYPES", String.valueOf(adi.getType()));
                if (adi.getType() == deviceType) {
                    Log.d("CONNECTED TO", String.valueOf(adi.getType()));
                    return adi;
                }
            }
        }
        return null;
    }

}
