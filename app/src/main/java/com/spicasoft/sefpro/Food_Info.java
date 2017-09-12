package com.spicasoft.sefpro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by USER on 12-09-2017.
 */

public class Food_Info extends Fragment {
    private String[] food_items={"Soup-Veg","Starter-Cauliflower Chilli","Roti(Bread)","Veg-Rice", "Veg-Gravy","White Rice","Curd"};
    private String[] items_desc={"Soup made with vegitables","Cauliflower Chilli","Roti(Bread)","Rice with vegitables", "Vegitable Gravy","White Rice","Curd"};
    private Integer[] imageid={R.drawable.veg_soup,R.drawable.cauliflower_chilli,R.drawable.roti,R.drawable.veg_rice,R.drawable.veg_gravey,R.drawable.white_rice, R.drawable.curd};
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);
        CustomFoodAdapter adapter = new CustomFoodAdapter(view.getContext(), food_items, items_desc, imageid);
        listView = (ListView) view.findViewById(R.id.lstFood);
        listView.setAdapter(adapter);
        return view;
    }
}