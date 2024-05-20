package naomi.sara.newpaintingapp;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for app that initializes the canvas. Inherits from the AppCompatActivity class
 */
public class MainActivity extends AppCompatActivity {
    private DrawingView drawingView;

    String[] canvasNames;
    int[] canvases = {R.drawable.g4_wood_type_silver_oak, R.drawable.g2_granite_type_veneziano,
                      R.drawable.g9_textile_version2, R.drawable.glass_plate_background};
    Spinner canvas_options;

    int[] brushes = {R.drawable.round_brush, R.drawable.flat_brush,
            R.drawable.filbert_brush};
    Spinner brush_options;

    String[] colorArray;
    Spinner color_options;

    /**
     * Overrides the onCreate function of the AppCompatActivity class. Initializes the app.
     * @param savedInstanceState Is used to save the state of an Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar setup as here:
        // https://developer.android.com/develop/ui/views/components/appbar/setting-up
        Toolbar myToolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(myToolbar);

        // Inflates the toolbar items from the toolbar_buttons layout
        final LayoutInflater factory = getLayoutInflater();
        final View relative  = factory.inflate(R.layout.toolbar_buttons, null);
        getSupportActionBar().setCustomView(relative);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        // Dictates what happens when the clear_button is clicked
        Button clear = (Button) findViewById(R.id.clear_button);
        clear.setOnClickListener(v -> {
            drawingView = findViewById(R.id.drawingView);
            drawingView.clearView();
            Log.d("BUTTONS", "User tapped the Clear button");
        });

        // Request bluetooth access, if the user haven't already given permission
        if (ContextCompat.checkSelfPermission(MainActivity.this,
            android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
            }
        }
    }

    /**
     * Initializes the contents of the menu. These are the different spinners.
     * @param menu The options menu in which spinner items is placed.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Canvas spinner
        canvas_options = (Spinner) menu.findItem(R.id.canvas_dropdown).getActionView();
        canvasNames = getResources().getStringArray(R.array.canvasNames);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), canvases, canvasNames);
        canvas_options.setAdapter(customAdapter);
        canvas_spinner_selection();

        // Color spinner
        color_options = (Spinner) menu.findItem(R.id.color_dropdown).getActionView();
        colorArray = getResources().getStringArray(R.array.colorValues);
        CustomColorAdapter colorAdapter = new CustomColorAdapter(getApplicationContext(), colorArray);
        color_options.setAdapter(colorAdapter);
        color_spinner_selection();

        // Brush spinner
        brush_options = (Spinner) menu.findItem(R.id.brush_dropdown).getActionView();
        CustomBrushAdapter brushAdapter = new CustomBrushAdapter(getApplicationContext(), brushes);
        brush_options.setAdapter(brushAdapter);
        brush_spinner_selection();
        return true;
    }

    /**
     * Dictates what should be done when a canvas is selected in the spinner.
     */
    private void canvas_spinner_selection() {
        canvas_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            /**
             * Overrides the onItemSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Write something on screen with small icon
                /*Toast.makeText(getApplicationContext(), canvasNames[position], Toast.LENGTH_LONG).show();*/
                drawingView = findViewById(R.id.drawingView);

                switch(canvasNames[position]) {//switch(chosen_canvas) {
                    case "Wood":
                        drawingView.chosenBackground = 0;
                        drawingView.clearView();
                        break;
                    //should be canvas
                    case "Granite":
                        drawingView.chosenBackground = 1;
                        drawingView.clearView();
                        break;
                    // Should be silk
                    case "Textile":
                        drawingView.chosenBackground = 2;
                        drawingView.clearView();
                        break;
                    case "Glass":
                        drawingView.chosenBackground = 3;
                        drawingView.clearView();
                        break;
                    default:
                        drawingView.chosenBackground = 0;
                }
            }

            /**
             * Overrides the onNothingSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                drawingView.chosenBackground = 0;
            }
        });
    }

    /**
     * Dictates what should be done when a color is selected in the spinner.
     */
    private void color_spinner_selection() {
        color_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            /**
             * Overrides the onItemSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                drawingView = findViewById(R.id.drawingView);
                drawingView.changeColor(Color.parseColor(colorArray[position]));
            }

            /**
             * Overrides the onNothingSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                drawingView.changeColor(Color.BLACK);
            }
        });
    }

    /**
     * Dictates what should be done when a brush is selected in the spinner.
     */
    private void brush_spinner_selection() {
        brush_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()  {
            /**
             * Overrides the onItemSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                drawingView = findViewById(R.id.drawingView);

                switch(position) {
                    // round (thin) brush
                    case 0:
                        drawingView.chosenBrush = position;
                        drawingView.changeBrush(40, Paint.Cap.ROUND);
                        break;
                    // flat brush
                    case 1:
                        drawingView.chosenBrush = position;
                        drawingView.changeBrush(80, Paint.Cap.SQUARE);
                        break;
                    // filbert (round) brush
                    case 2:
                        drawingView.chosenBrush = position;
                        drawingView.changeBrush(80, Paint.Cap.ROUND);
                        break;
                    default:
                        drawingView.chosenBrush = 0;
                }
            }

            /**
             * Overrides the onNothingSelected function of the Spinner class'
             * setOnItemSelectedListener function.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                drawingView.chosenBrush = 0;
            }
        });
    }
}