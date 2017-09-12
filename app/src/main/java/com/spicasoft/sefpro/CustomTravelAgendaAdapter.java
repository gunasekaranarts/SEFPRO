package com.spicasoft.sefpro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by USER on 12-09-2017.
 */

public class CustomTravelAgendaAdapter extends BaseAdapter {

    Context context;
    List<TravelAgendaItem> rowItems;

    public CustomTravelAgendaAdapter(Context context, List<TravelAgendaItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView ImgUrl;
        TextView txtName;
        TextView txtFromTo;
        TextView txtTo;
        TextView txtFrom_DateTime;
        TextView txtTo_DateTime;
        TextView txtFrom_Code;
        TextView txtTo_Code;
        TextView txtFrom_Gate;
        TextView txtTo_Gate;
        TextView txtBooking_Name;
        TextView txtRoom_No;
        TextView txtMobile;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomTravelAgendaAdapter.ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TravelAgendaItem rowItem = (TravelAgendaItem) getItem(position);
        // if (convertView == null) {
        switch(rowItem.getTypeId()) {
            case TravelAgendaItem.FLIGHT:
                convertView = mInflater.inflate(R.layout.flight_schedule, null);
                holder = new CustomTravelAgendaAdapter.ViewHolder();
                holder.txtFromTo = (TextView) convertView.findViewById(R.id.txtflightfromto);
                holder.txtName = (TextView) convertView.findViewById(R.id.txtFlightName);
                holder.txtFrom_Code = (TextView) convertView.findViewById(R.id.txtFromCode);
                holder.txtTo_Code = (TextView) convertView.findViewById(R.id.txtToCode);
                holder.txtFrom_DateTime = (TextView) convertView.findViewById(R.id.txtDepart);
                holder.txtTo_DateTime = (TextView) convertView.findViewById(R.id.txtArive);
                holder.txtFrom_Gate = (TextView) convertView.findViewById(R.id.txtFromGate);
                holder.txtTo_Gate = (TextView) convertView.findViewById(R.id.txtToGate);
                holder.txtRoom_No = (TextView) convertView.findViewById(R.id.txtDuration);
                convertView.setTag(holder);
                holder=setFlightDetails(holder,rowItem);
                break;
            case TravelAgendaItem.TAXI:
                convertView = mInflater.inflate(R.layout.taxi_schedule, null);
                holder = new CustomTravelAgendaAdapter.ViewHolder();
                holder.txtFromTo = (TextView) convertView.findViewById(R.id.txtFromTo);
                holder.txtRoom_No = (TextView) convertView.findViewById(R.id.txtTaxiNo);
                holder.txtName = (TextView) convertView.findViewById(R.id.txtDriverName);
                holder.txtFrom_DateTime = (TextView) convertView.findViewById(R.id.txtPickupTime);
                holder.txtTo_DateTime = (TextView) convertView.findViewById(R.id.txtDropTime);
                holder.txtMobile = (TextView) convertView.findViewById(R.id.txtMobile);
                convertView.setTag(holder);
                holder=setTaxiDetails(holder,rowItem);
                break;
            case TravelAgendaItem.HOTEL:
                convertView = mInflater.inflate(R.layout.hotel_booking, null);
                holder = new CustomTravelAgendaAdapter.ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txtHotelName);
                holder.txtFromTo = (TextView) convertView.findViewById(R.id.txtAddress);
                holder.txtRoom_No = (TextView) convertView.findViewById(R.id.txtRoomNo);
                holder.txtBooking_Name = (TextView) convertView.findViewById(R.id.txtBookingName);
                holder.txtFrom_DateTime = (TextView) convertView.findViewById(R.id.txtCheckin);
                holder.txtTo_DateTime = (TextView) convertView.findViewById(R.id.txtCheckout);
                holder.txtMobile = (TextView) convertView.findViewById(R.id.txtHotelContact);
                holder.txtFrom_Code = (TextView) convertView.findViewById(R.id.txtBookingNo);
                convertView.setTag(holder);
                holder=setHotelDetails(holder,rowItem);
                break;
            case TravelAgendaItem.SERVICE:
                convertView = mInflater.inflate(R.layout.contact_list, null);
                holder = new CustomTravelAgendaAdapter.ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
                // holder.txtemail = (TextView) convertView.findViewById(R.id.txtemail);
                //holder.txtphone = (TextView) convertView.findViewById(R.id.txtphone);
                // holder.imageView = (ImageView) convertView.findViewById(R.id.contact_image);
                convertView.setTag(holder);
                break;

        }
//        } else {
//            holder = (CustomTravelAgendaAdapter.ViewHolder) convertView.getTag();
//        }
//        holder.txtName.setText(rowItem.getName());
//        holder.txtFromTo.setText(rowItem.getFrom()+" - "+rowItem.getTo());
//        holder.txtFrom_Code.setText(rowItem.getFrom_Code());
//        holder.txtTo_Code.setText(rowItem.getTo_Code());
//        holder.txtFrom_DateTime.setText(rowItem.getFrom_DateTime());
//        holder.txtTo_DateTime.setText(rowItem.getTo_DateTime());
//        holder.txtFrom_Gate.setText(rowItem.getFrom_Gate());
//        holder.txtTo_Gate.setText(rowItem.getTo_Gate());
//        holder.txtRoom_No.setText(rowItem.getBooking_Name()+ "  Duration: "+rowItem.getRoom_No());



        //holder.txtemail.setText(rowItem.getEmail());
        //holder.txtphone.setText(rowItem.getMobile());
        //holder.imageView.setImageResource(rowItem.getImageId());
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked ", Toast.LENGTH_LONG).show();
            }
        });*/
        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    public CustomTravelAgendaAdapter.ViewHolder setFlightDetails(
            CustomTravelAgendaAdapter.ViewHolder holder, TravelAgendaItem rowItem){

        holder.txtName.setText(rowItem.getName());
        holder.txtFromTo.setText(rowItem.getFrom()+" - "+rowItem.getTo());
        holder.txtFrom_Code.setText(rowItem.getFrom_Code());
        holder.txtTo_Code.setText(rowItem.getTo_Code());
        holder.txtFrom_DateTime.setText(rowItem.getFrom_DateTime());
        holder.txtTo_DateTime.setText(rowItem.getTo_DateTime());
        holder.txtFrom_Gate.setText(rowItem.getFrom_Gate());
        holder.txtTo_Gate.setText(rowItem.getTo_Gate());
        holder.txtRoom_No.setText(rowItem.getBooking_Name()+ "  Duration: "+rowItem.getRoom_No());
        return holder;
    }
    public CustomTravelAgendaAdapter.ViewHolder setTaxiDetails(
            CustomTravelAgendaAdapter.ViewHolder holder, TravelAgendaItem rowItem){
        holder.txtName.setText(rowItem.getName());
        holder.txtFromTo.setText(rowItem.getFrom()+" - "+rowItem.getTo());
        holder.txtRoom_No.setText(rowItem.getRoom_No());
        holder.txtFrom_DateTime.setText("Pickup: \n"+rowItem.getFrom_DateTime());
        holder.txtTo_DateTime.setText("Drop: \n"+rowItem.getTo_DateTime());
        holder.txtMobile.setText(rowItem.getMobile());
        return holder;
    }
    public CustomTravelAgendaAdapter.ViewHolder setHotelDetails(
            CustomTravelAgendaAdapter.ViewHolder holder, TravelAgendaItem rowItem){

        holder.txtName.setText(rowItem.getName());
        holder.txtFromTo.setText(rowItem.getFrom());
        holder.txtFrom_Code.setText(rowItem.getFrom_Code());
        holder.txtFrom_DateTime.setText("Checkin : \n"+rowItem.getFrom_DateTime());
        holder.txtTo_DateTime.setText("Checkout : \n"+rowItem.getTo_DateTime());
        holder.txtRoom_No.setText("Room no: \n"+rowItem.getRoom_No());
        holder.txtBooking_Name.setText("Booking Name:\n"+rowItem.getBooking_Name());
        holder.txtMobile.setText("Contact : \n"+rowItem.getMobile());
        return holder;
    }
}


