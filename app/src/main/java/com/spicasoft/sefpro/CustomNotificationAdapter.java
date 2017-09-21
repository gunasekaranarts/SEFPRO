package com.spicasoft.sefpro;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.List;

/**
 * Created by USER on 20-09-2017.
 */

public class CustomNotificationAdapter extends BaseAdapter {
    Context context;
    public List<NotificationItem> notificationItems;
    public ImageView tempImage;

    @Override
    public int getCount() {
        return notificationItems != null ? notificationItems.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public CustomNotificationAdapter(Context context, List<NotificationItem> items) {
        this.context = context;
        this.notificationItems = items;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtMessage;
        TextView txtDatetime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtNotificationTitle);
            holder.txtMessage = (TextView) convertView.findViewById(R.id.txtNotificationMessage);
            holder.txtDatetime = (TextView) convertView.findViewById(R.id.txtNotificationDatetime);
            holder.imageView = (ImageView) convertView.findViewById(R.id.notificationImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NotificationItem notificationItem = notificationItems.get(position);

        holder.txtTitle.setText(notificationItem.getTitle());
        holder.txtMessage.setText(notificationItem.getMessage());
        holder.txtDatetime.setText(notificationItem.getDatetime());
        String imageUri = notificationItem.getImage();
        if (!("null".equals(imageUri))) {
            final ViewHolder finalHolder = holder;
            ImageRequest imageRequest = new ImageRequest(imageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                   // tempImage.setImageBitmap(response);
                    finalHolder.imageView.setImageBitmap(response);
                }
            }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getmInstance(context).addToRequestQue(imageRequest);

        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked ", Toast.LENGTH_LONG).show();
            }
        });*/
        return convertView;

    }

}
