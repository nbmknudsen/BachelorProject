package naomi.sara.newpaintingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom adapter that provides access to the canvas items. This adapter is responsible for making
 * a View for each of the canvas items. Skeleton code is originally taken from
 * <a href="https://abhiandroid.com/ui/custom-spinner-examples.html#gsc.tab=0">here</a>.
 * The class inherits from the BaseAdapter class.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] canvases;
    String[] canvasNames;
    LayoutInflater inflater;

    /**
     * Constructor of the CustomAdapter class. It initializes the LayoutInflater inflater.
     * @param applicationContext Context - used to set the context of the class
     * @param canvases Int array of canvas pictures
     * @param canvasNames String array of canvas names
     */
    public CustomAdapter(Context applicationContext, int[] canvases, String[] canvasNames) {
        this.context = applicationContext;
        this.canvases = canvases;
        this.canvasNames = canvasNames;
        inflater = (LayoutInflater.from(applicationContext));
    }

    /**
     *
     * @return The number of canvas items that are in the canvas picture array
     */
    @Override
    public int getCount() {
        return canvases.length;
    }

    /**
     *
     * @param i Position of the item whose data we want within the adapter's
     * data set.
     * @return null
     */
    @Override
    public Object getItem(int i) {
        return null;
    }

    /**
     *
     * @param i The position of the item within the adapter's data set whose row id we want.
     * @return 0
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Overrides the getView function of the BaseAdapter class. The function gets a View that
     * displays and sets the canvas picture and canvas name in the custom_spinner_items layout.
     * @param i The position of the item within the adapter's data set of the canvases
     * @param view The old view we save the inflated custom spinner items in
     * @param viewGroup The parent that this view will eventually be attached to
     * @return The view with the canvas images and canvas names as a spinner
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_canvas_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(canvases[i]);
        names.setText(canvasNames[i]);
        return view;
    }
}
