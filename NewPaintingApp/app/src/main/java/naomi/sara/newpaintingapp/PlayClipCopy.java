package naomi.sara.newpaintingapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.List;

import kotlin.Triple;

/**
 * Class used to play auditory feedback. The class implements the PlayInterface interface.
 */
public class PlayClipCopy { //extends Activity The class inherits from the Activity class.
    private boolean isPlaying = false;
    private float volume = 0f;
    private AudioManager manager;

    private SoundPool soundPool;

    int fast;
    int medium;
    int slow;

    List<Pair<Integer, Integer>> soundIDs;
    List<Triple<Integer, Integer, Integer>> streamIDs;

    int fastVol;
    int mediumVol;
    int slowVol;

    /*public PlayClip(Context context) {
        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(3)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build())
                .build();
        try {
            fast = soundPool.load(context, R.raw.sound_square_wood_f, 1);
            medium = soundPool.load(context, R.raw.haptics_square_wood_m, 1);
            slow = soundPool.load(context, R.raw.haptics_square_wood_s, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }*/

    /**
     * Creates a MediaPlayer instance and initialize it with different parameters
     *
     * @param context Context - used to create a Uri
     */
    @RequiresApi(api = Build.VERSION_CODES.S)
    public void init(Context context, int fileNameFast, int fileNameMedium, int fileNameSlow) {
        // setup Soundpool
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(3)
                .setAudioAttributes(new AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build())
                .build();

            for (int i = 0;  i < soundIDs.size(); i++) {
                if (soundIDs.get(i).second == fileNameFast) {
                    startPlaying(context, soundIDs.get(i).first, soundIDs.get(i + 1).first,
                            soundIDs.get(i + 2).first);
                    return;
                }
            }

        soundIDs.add(new Pair<>(fileNameFast, soundPool.load(context, fileNameFast, 1)));
        soundIDs.add(new Pair<>(fileNameFast, soundPool.load(context, fileNameMedium, 1)));
        soundIDs.add(new Pair<>(fileNameFast, soundPool.load(context, fileNameSlow, 1)));
        startPlaying(context, soundIDs.get(soundIDs.size() - 3).first,
                soundIDs.get(soundIDs.size() - 2).first, soundIDs.get(soundIDs.size() - 1).first);
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
        soundPool.play(fastID, 0.0f, 0.0f, 0, -1,1.0f);
        soundPool.play(mediumID, 0.0f, 0.0f, 0, -1,1.0f);
        soundPool.play(slowID, 1.0f, 1.0f, 0, -1,1.0f);
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
                soundPool.setVolume(fast, 1.0f, 1.0f);
                soundPool.setVolume(medium, 0.0f, 0.0f);
                soundPool.setVolume(slow, 0.0f, 0.0f);
                fastVol = 1;
                mediumVol = 0;
                slowVol = 0;
            } else if (velocity > 0.025) {
                soundPool.setVolume(fast, 0.0f,0.0f);
                soundPool.setVolume(medium,1.0f,1.0f);
                soundPool.setVolume(slow, 0.0f,0.0f);
                fastVol = 0;
                mediumVol = 1;
                slowVol = 0;
            } else {
                soundPool.setVolume(fast, 0.0f,0.0f);
                soundPool.setVolume(medium, 0.0f,0.0f);
                soundPool.setVolume(slow,1.0f,1.0f);
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
        soundPool.autoPause();
        /*soundPool.unload(fast);
        soundPool.unload(medium);
        soundPool.unload(slow);*/
    }

    /**
     * Stops the MediaPlayer instance and sets the variable isPLaying to false, so we know that the
     * MediaPlayer isn't playing. It also releases the MediaPlayer resources so it have to be
     * initialized again.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            soundPool.stop(fast);
            soundPool.stop(medium);
            soundPool.stop(slow);
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
