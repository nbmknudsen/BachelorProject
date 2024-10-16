package naomi.sara.newpaintingapp;

import android.content.Context;
import android.media.AudioDeviceInfo;

/**
 * Interface used to specify which methods a class used for datadriven haptics
 */
public interface PlayInterface {
    void init(Context context, int fileName);
    void startPlaying(Context context);
    void setVolume(float leftVolume, float rightVolume);
    void pausePlaying();
    void stopPlaying();
    AudioDeviceInfo findAudioDevice(int deviceType, Context context);
}
