package naomi.sara.newpaintingapp;

import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the ControlFeedback class, which will execute on an Android device.
*/
@RunWith(AndroidJUnit4.class)
public class ControlFeedbackInstrumentedTest {
    ControlFeedback controlFeedback;
    PlayMusic playMusic;

    int chosenBrush;
    int chosenBackground;
    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        controlFeedback = new ControlFeedback();
        playMusic = new PlayMusic();

        chosenBrush = 0;
        chosenBackground = 0;
        controlFeedback.initializeClip(playMusic, context, chosenBrush, chosenBackground);

        //SystemClock.sleep(100);
    }

    @Test
    public void initializeClip() {
        SystemClock.sleep(1000);
        assertTrue(playMusic.isPlaying());
    }




}
