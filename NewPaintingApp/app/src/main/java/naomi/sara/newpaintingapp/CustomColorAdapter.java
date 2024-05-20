package naomi.sara.newpaintingapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Custom adapter that provides access to the color items. This adapter is responsible for making
 * a View for each of the color items. The class inherits from the BaseAdapter class.
 */
public class CustomColorAdapter extends BaseAdapter {
    private final String[] colors;
    Context context;
    LayoutInflater inflater;

    /**
     * Constructor of the CustomColorAdapter class. It initializes the LayoutInflater inflater.
     * @param context Context - used to set the context of the class
     * @param colors String array of with hexadecimal colors
     */
    public CustomColorAdapter(Context context, String[] colors) {
        this.context = context;
        this.colors = colors;
        inflater = (LayoutInflater.from(context));
    }

    /**
     *
     * @return The number of brush items that are in the brush picture array
     */
    @Override
    public int getCount() {
        return colors.length;
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
     * displays and sets the background color of the spinner items in the custom_color_items layout.
     * @param position The position of the item within the adapter's data set of the colors
     * @param convertView The old view we save the inflated custom color items in if it is null
     * @param parent The parent that this view will eventually be attached to
     * @return The view with the brush images as a spinner
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_color_items, null);
            TextView names = (TextView) convertView.findViewById(R.id.colorView);
            names.setBackgroundColor(Color.parseColor(colors[position]));
        } else {
            TextView names = (TextView) convertView.findViewById(R.id.colorView);
            names.setBackgroundColor(Color.parseColor(colors[position]));
        }
        return convertView;
    }
}
