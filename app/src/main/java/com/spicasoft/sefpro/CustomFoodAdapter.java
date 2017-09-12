package com.spicasoft.sefpro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USER on 12-09-2017.
 */

public class CustomFoodAdapter extends ArrayAdapter<String> {
    private String[] food_Items;
    private String[] items_desc;
    private Integer[] imageid;
    private Context context;

    public CustomFoodAdapter(Context context, String[] names, String[] desc, Integer[] imageid) {
        super(context, R.layout.lunch_menu_list, names);
        this.context = context;
        this.food_Items = names;
        this.items_desc = desc;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = mInflater.inflate(R.layout.lunch_menu_list, null, true);
        TextView txtFootTitle = (TextView) listViewItem.findViewById(R.id.txtFootTitle);
        TextView txtFoodDesc = (TextView) listViewItem.findViewById(R.id.txtFoodDesc);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imgFood);

        txtFootTitle.setText(food_Items[position]);
        txtFoodDesc.setText(items_desc[position]);
        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
