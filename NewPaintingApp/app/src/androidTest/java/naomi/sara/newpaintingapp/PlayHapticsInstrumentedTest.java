package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the PlayHaptics class, which will execute on an Android device.
 * Note that tests on playing the sound can't be done since an emulator doesn't have a aux line.
 */
@RunWith(AndroidJUnit4.class)
public class PlayHapticsInstrumentedTest {

    PlayHaptics playHaptics;
    MediaPlayer mp;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        playHaptics = new PlayHaptics();
        playHaptics.init(context, R.raw.haptics_round_canvas_f);
    }

    @Test
    public void init() {
        assertNotEquals(null, playHaptics.getMediaPlayer());
    }

    @Test
    public void setVolume() {
        // Only checks if it sets variable volume to 1.0f when MediaPlayer isn't null
        playHaptics.setVolume(1.0f,1.0f);
        assertEquals(1.0f, playHaptics.getVolume(), 0.0);
    }

}