package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the PlayHaptics class, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class PlayMusicOldInstrumentedTest {

    PlayMusicOld playMusicOld;
    MediaPlayer mp;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        playMusicOld = new PlayMusicOld();
        /*playMusic.init(context, R.raw.haptics_round_canvas_f);
        playMusic.startPlaying(context);*/
        playMusicOld.init(context, R.raw.sound_round_canvas_f);
    }

    @Test
    public void startPlaying() {
        playMusicOld.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        assertTrue(playMusicOld.isPlaying());
    }

    @Test
    public void setVolume() {
        playMusicOld.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusicOld.setVolume(0.0f,0.0f);
        assertEquals(0.0f, playMusicOld.getVolume(), 0.0);
    }

    @Test
    public void pausePlaying() {
        playMusicOld.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusicOld.pausePlaying();
        assertFalse(playMusicOld.isPlaying());
    }

    @Test
    public void stopPlaying() {
        playMusicOld.startPlaying(context);
        // Need to wait a bit for MediaPlayer to be prepared
        SystemClock.sleep(100);
        playMusicOld.stopPlaying();
        assertFalse(playMusicOld.isPlaying());
    }
}