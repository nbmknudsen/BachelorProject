package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for the DrawingView class, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class DrawingViewInstrumentedTest {
    private static DrawingView drawingView;
    private static Canvas canvas;
    private static Paint paint;
    private static Path path;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        drawingView = new DrawingView(context);
        drawingView.chosenBackground = 0;
        canvas = new Canvas();
        paint = drawingView.getPaint();
        path = drawingView.getPath();
        //path = new Path();
        //drawingView.invalidate();
    }

    @Test
    public void onSizeChanged() {
        /*drawingView.onDraw(canvas);
        Path oldPath = path;
        drawingView.onSizeChanged(drawingView.getWidth(), drawingView.getHeight(),
                drawingView.getWidth(), drawingView.getHeight());
        Path newPath = drawingView.getPath();
        assertEquals(oldPath, newPath);*/
    }

    @Test
    public void changeColor() {
        // the initial color is black
        drawingView.changeColor(Color.RED);
        // onDraw must be called to change color because colors are added to a list that is
        // iterated through in onDraw
        drawingView.onDraw(canvas);
        assertEquals(paint.getColor(), Color.RED);
    }

    @Test
    public void changeBrush() {
        // initial brush is new Pair<>(40, Paint.Cap.ROUND)
        drawingView.changeBrush(10, Paint.Cap.BUTT);
        // onDraw must be called to change the brush because brushes are added to a list that is
        // iterated through
        drawingView.onDraw(canvas);
        Pair<Integer, Paint.Cap> current_brush = new Pair<>((int) paint.getStrokeWidth(),
                paint.getStrokeCap());
        assertEquals(new Pair <>(10, Paint.Cap.BUTT), current_brush);
    }
}