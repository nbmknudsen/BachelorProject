package naomi.sara.newpaintingapp;

import android.content.Context;
import android.graphics.Path;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;


import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

/**
 * Class used to play auditory feedback. The class implements the PlayInterface interface.
 */
public class PlayClip { //extends Activity The class inherits from the Activity class.
    private boolean isPlaying = false;
    private float volume = 0f;
    private AudioManager manager;

    private SoundPool soundPool;

    int fastSoundID;
    int mediumSoundID;
    int slowSoundID;

    int fastStreamID;
    int mediumStreamID;
    int slowStreamID;

    Pair<Integer, Integer>[] soundIDs;
    List<Integer> streamIDs;

    int fastVol;
    int mediumVol;
    int slowVol;

    /**
     * Creates a MediaPlayer instance and initialize it with different parameters
     *
     * @param context Context - used to create a Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void init(Context context, int fileNameFast, int fileNameMedium, int fileNameSlow) {
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // setup Soundpool
        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(3)
                .setAudioAttributes(new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build())
                .build();

        try {
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    startPlaying(context, fastSoundID, mediumSoundID, slowSoundID);
                }
            });
            fastSoundID = soundPool.load(context, fileNameFast, 1);
            mediumSoundID = soundPool.load(context, fileNameMedium, 1);
            slowSoundID = soundPool.load(context, fileNameSlow, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the MediaPlayer instance and sets the variable isPLaying to true, so we know if the
     * MediaPlayer is playing. Sets the bluetooth headset as the preferred device to send sound.
     *
     * @param context Context - used to check if device is connected to bluetooth
     */
    public void startPlaying(Context context, int fastID, int mediumID, int slowID) {
        AudioDeviceInfo bt_headset = findAudioDevice(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP, context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (bt_headset == null) {
                //AudioDeviceInfo speaker = findAudioDevice(AudioDeviceInfo.TYPE_BUILTIN_SPEAKER, context);
                manager.setSpeakerphoneOn(true);
                //mediaPlayer.setPreferredDevice(speaker);
            } else {
                Log.d("BLUETOOTH CONNECTION", String.valueOf(bt_headset.getType()));
                manager.startBluetoothSco();
                manager.setBluetoothScoOn(true);
                manager.setSpeakerphoneOn(false);
            }
        }
        Log.d("LOAD", fastSoundID + ", " + mediumSoundID + ", " + slowSoundID);
        fastStreamID = soundPool.play(fastID, 0.0f, 0.0f, 0, -1,1.0f);
        mediumStreamID = soundPool.play(mediumID, 0.0f, 0.0f, 0, -1,1.0f);
        slowStreamID = soundPool.play(slowID, 1.0f, 1.0f, 0, -1,1.0f);
        isPlaying = true;
    }

    /**
     * Sets the left and right volume of the MediaPLayer. We use this to mute the players we don't
     * want to hear and unmute when we want to hear the players again. 0 is mute and 1 is unmute.
     *
     * @param velocity  velocity
     *
     */
    public void setVolume(double velocity) {
        if (soundPool != null) {
            if (velocity > 0.045) {
                soundPool.setVolume(fastStreamID, 1.0f, 1.0f);
                soundPool.setVolume(mediumStreamID, 0.0f, 0.0f);
                soundPool.setVolume(slowStreamID, 0.0f, 0.0f);
                fastVol = 1;
                mediumVol = 0;
                slowVol = 0;
            } else if (velocity > 0.025) {
                soundPool.setVolume(fastStreamID, 0.0f,0.0f);
                soundPool.setVolume(mediumStreamID,1.0f,1.0f);
                soundPool.setVolume(slowStreamID, 0.0f,0.0f);
                fastVol = 0;
                mediumVol = 1;
                slowVol = 0;
            } else {
                soundPool.setVolume(fastStreamID, 0.0f,0.0f);
                soundPool.setVolume(mediumStreamID, 0.0f,0.0f);
                soundPool.setVolume(slowStreamID,1.0f,1.0f);
                fastVol = 0;
                mediumVol = 0;
                slowVol = 1;
            }
        }
    }


    /**
     * Pauses the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing.
     */
    public void pausePlaying(){
        soundPool.unload(fastSoundID);
        soundPool.unload(mediumSoundID);
        soundPool.unload(slowSoundID);
        soundPool.autoPause();

    }

    /**
     * Stops the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing. It also releases the MediaPlayer resources so it have to be
     * initialized again.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            soundPool.stop(fastStreamID);
            soundPool.stop(mediumStreamID);
            soundPool.stop(slowStreamID);
            soundPool.release();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public float getVolume() {
        return volume;
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
