package naomi.sara.newpaintingapp;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * Class used to play auditory feedback. The class implements the PlayInterface interface.
 */
public class PlayMusic {
    private boolean isPlaying = false;

    private AudioManager manager;

    private SoundPool soundPool;

    int fastSoundID;
    int mediumSoundID;
    int slowSoundID;

    int fastStreamID;
    int mediumStreamID;
    int slowStreamID;

    int fastVol;
    int mediumVol;
    int slowVol;

    /**
     * Creates a SoundPool instance and initialize it with different parameters.
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
            /*soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    startPlaying(context, fastSoundID, mediumSoundID, slowSoundID);
                }
            });*/
            fastSoundID = soundPool.load(context, fileNameFast, 1);
            mediumSoundID = soundPool.load(context, fileNameMedium, 1);
            slowSoundID = soundPool.load(context, fileNameSlow, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the SoundPool instance and sets the variable isPLaying to true, so we know if the
     * SoundPool is playing. This is only done if an aux line isn't connected.
     * @param context Context - used to check if device is connected to bluetooth
     */
    public void startPlaying(Context context) {
        AudioDeviceInfo aux_line = findAudioDevice(AudioDeviceInfo.TYPE_WIRED_HEADPHONES, context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            /*if (aux_line != null) {
                manager.setSpeakerphoneOn(false);
            } else {*/
                manager.setSpeakerphoneOn(true);

                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        isPlaying = true;
                        fastStreamID = soundPool.play(fastSoundID, 0.0f, 0.0f, 0, -1,1.0f);
                        mediumStreamID = soundPool.play(mediumSoundID, 0.0f, 0.0f, 0, -1,1.0f);
                        slowStreamID = soundPool.play(slowSoundID, 0.0f, 0.0f, 0, -1,1.0f);
                    }
                });
            //}
        }
    }

    /**
     * Sets the left and right volume of the different streams. We use this to mute the streams we
     * don't want to hear and unmute when we want to hear the streams again. 0 is mute and 1 is unmute.
     * @param velocity  velocity
     */
    public void setVolume(double velocity) {
        if (soundPool != null && isPlaying) {
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
     * Pauses all the streams playing with the SoundPool instance and sets the variable isPLaying to
     * false, so we know that the streams isn't playing.
     */
    public void pausePlaying(){
        isPlaying = false;
        soundPool.unload(fastSoundID);
        soundPool.unload(mediumSoundID);
        soundPool.unload(slowSoundID);
        soundPool.autoPause();

    }

    /**
     * Stops all the streams playing with the SoundPool instance and sets the variable isPLaying to
     * false, so we know that the streams isn't playing.
     * It also releases the SoundPool resources so it has to be initialized again.
     */
    public void stopPlaying() {
        if (isPlaying) {
            isPlaying = false;
            soundPool.stop(fastStreamID);
            soundPool.stop(mediumStreamID);
            soundPool.stop(slowStreamID);
            soundPool.release();
            soundPool = null;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getVolume(String SoundSpeedType) {
        if (SoundSpeedType == "fast") {
            return fastVol;
        } else if (SoundSpeedType == "medium") {
            return mediumVol;
        } else {
            return slowVol;
        }
    }

    public SoundPool getSoundPool() {
        return soundPool;
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
