package naomi.sara.newpaintingapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import android.content.Context;
import android.graphics.Bitmap;
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
    private static Bitmap emptyBitmap;
    private static Bitmap bitmap;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        drawingView = new DrawingView(context);
        bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = drawingView.getPaint();
        path = drawingView.getPath();
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
    public void changeCanvas() {
        drawingView.changeCanvas(2);
        assertEquals(drawingView.getCanvas(), 2);
    }

    @Test
    public void changeColor() {
        // the initial color is black
        drawingView.changeColor(Color.RED);
        // onDraw must be called to change color because colors are added to a list that is
        // iterated through in onDraw
        drawingView.onDraw(canvas);
        assertEquals(Color.RED, paint.getColor());
    }

    @Test
    public void changeBrush() {
        // initial brush is new Pair<>(40, Paint.Cap.ROUND)
        drawingView.changeBrush(10, Paint.Cap.BUTT, 4);
        // onDraw must be called to change the brush because brushes are added to a list that is
        // iterated through
        drawingView.onDraw(canvas);
        Pair<Integer, Paint.Cap> current_brush = new Pair<>((int) paint.getStrokeWidth(),
                paint.getStrokeCap());
        assertEquals(new Pair <>(10, Paint.Cap.BUTT), current_brush);
    }

    @Test
    public void onDraw() {
        path.moveTo(0.0f, 0.0f);
        drawingView.onDraw(canvas);
        path.lineTo(2.0f, 2.0f);
        drawingView.onDraw(canvas);
        int colorPixel = bitmap.getPixel(1, 1);

        assertEquals(Color.BLACK, colorPixel);
    }
}