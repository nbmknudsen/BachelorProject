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
 * Class used to play auditory feedback. The class inherits from the Activity class.
 */
public class PlayMusicOld extends Activity {
    private MediaPlayer mediaPlayer;
    private AudioManager manager;
    public boolean isPlaying = false;

    /**
     * Creates a MediaPlayer instance and initialize it with different parameters
     * @param context Context - used to create a Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void initMusic(Context context, int soundFileName){
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + soundFileName);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA) //only in headphones
                        .build());

        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();
            //mediaPlayer.prepare();
            Log.d("MEDIA PLAYER", "PREPARATION COMPLETE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

    }

    /**
     * Plays the MediaPlayer instance and sets the variable isPLaying to true, so we know if the
     * MediaPlayer is playing. Sets the bluetooth headset as the preferred device to send sound.
     * @param context Context - used to check if device is connected to bluetooth
     */
    public void startPlaying(Context context) {
        AudioDeviceInfo bt_headset = findAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP, context);
        if (bt_headset == null) {
            manager.setSpeakerphoneOn(true);
        } else {
            Log.d("BLUETOOTH CONNECTION", String.valueOf(bt_headset.getType()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mediaPlayer.setPreferredDevice(bt_headset);
            }
        }
        mediaPlayer.setOnPreparedListener(mp -> {
            if(! mp.isPlaying()) {
                Log.d("MUSIC PLAYING", "YAY, I PLAY");
                mp.start();
                mp.setLooping(true);
                isPlaying = true;
            }
        });

        /*mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            // TODO Auto-generated method stub
            Log.d("MediaPlayerError","WHAT: "+ what + " EXTRA: "+extra);
            return false;
        });*/
    }

    public void pausePlaying(){
        if (isPlaying) {
            isPlaying = false;
            mediaPlayer.pause();
        }
        /*mediaPlayer.setOnPreparedListener(mp -> {
            if(mp.isPlaying()) {
                mediaPlayer.pause();
            }
        });*/
    }

    /**
     * Stops the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            // Stop playing the audio data and release the resources
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /** Function used to find out if it is possible to connect to specified device. Skeleton code
     * is originally taken from Daniel Beltrami's answer
     * <a href="https://stackoverflow.com/questions/70489941/how-do-i-get-android-media-setpreferreddevice-to-work">here</a>.
     * @param deviceType The AudioDeviceInfo of the device you want to connect to
     * @param context Context - there has to be some context or the AudioManager can't get devices
     */
     private AudioDeviceInfo findAudioDevice(int deviceType, Context context) {
        //Log.d("DEVICE INFO", String.valueOf(deviceType));
        if (context != null) {
            AudioDeviceInfo[] adis = manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            for (AudioDeviceInfo adi : adis) {
                if (adi.getType() == deviceType) {
                    Log.d("CONNECTED TO", String.valueOf(adi.getType()));
                    return adi;
                }
            }
        }
        return null;
    }
}