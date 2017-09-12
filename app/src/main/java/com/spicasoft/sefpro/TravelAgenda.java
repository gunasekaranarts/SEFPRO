package com.spicasoft.sefpro;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 12-09-2017.
 */

public class TravelAgenda extends Fragment {
    TableLayout tbl_Agenda;
    ListView lstSchedule;
    int Type = 0;

    List<TravelAgendaItem> list = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_travel_agenda, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstSchedule = (ListView) view.findViewById(R.id.lstSchedule);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        if (getArguments().containsKey("Type")) {
            int type = getArguments().getInt("Type");
            scrollToItem(type);
        }
    }

    private void setAdapter() {
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "United 1537 · Boeing 737", "Las Vegas", "Newark",
                "12-Dec-2017 11:16 AM", "12-Dec-2017 7:19 PM", "EWR", "LAS", "B/D6", "C/C11", "Confirm #: AWE5RT123", "5h 03m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "United 48 - Boeing 777", "Newark", "Mumbai",
                "12-Dec-2017 8:30 PM", "13-Dec-2017 8:50 PM", "EWR", "BOM", "B/C6", "D/D1", "Confirm #: AWE5RT123", "14h 50m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "Air India 657 · Airbus A319", "Mumbai", "Coimbatore",
                "14-Dec-2017 11:05 AM", "14-Dec-2017 12:50 PM", "BOM", "CBJ", "B/D6", "C/C11", "Confirm #: AWE5RT123", "1h 45m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.TAXI, "Rajkumar S", "Airport", "Hotel Gokulam Park",
                "14-Dec-2017 01:00 PM", "14-Dec-2017 01:30 PM", null,
                null, "B/D6", "C/C11", "Confirm #: AWE5RT123", "TN38 AP 2345", "+91 9500000000", "ht0tps://www.atomix.com.au/media/2015/06/atomix_user31.png"));
        list.add(new TravelAgendaItem(TravelAgendaItem.HOTEL, "Hotel Gokulam Park",
                "116/2, Avinashi Road, Mylampatti, Chinniyampalayam Post, Coimbatore, TamilNadu 641062", null,
                "14-Dec-2017 01:00 PM", "15-Dec-2017 09:00 AM", "Booking #: 0076548",
                null, "B/D6", "C/C11", "Senthil S", "342", "0422-4523030", null));
        list.add(new TravelAgendaItem(TravelAgendaItem.TAXI, "Rajkumar S", "Hotel Gokulam Park", "SEFPRO Plant",
                "15-Dec-2017 09:00 AM", "15-Dec-2017 10:00 AM", null,
                null, "B/D6", "C/C11", "Confirm #: AWE5RT123", "TN38 AP 2345", "+91 9500000000", "https://www.atomix.com.au/media/2015/06/atomix_user31.png"));
        list.add(new TravelAgendaItem(TravelAgendaItem.TAXI, "Rajkumar S", "SEFPRO Plant", "Airport",
                "15-Dec-2017 06:00 PM", "15-Dec-2017 07:00 AM", null,
                null, "B/D6", "C/C11", "Confirm #: AWE5RT123", "TN38 AP 2345", "+91 9500000000", "https://www.atomix.com.au/media/2015/06/atomix_user31.png"));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "Jet Airways 424 · Boeing 737", "Coimbatore", "Mumbai",
                "15-Dec-2017 9:00 PM", "15-Dec-2017 10:50 PM", "CBJ", "BOM", "C/C5", "B/D6", "Confirm #: YTRRT123", "1h 50m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "Jet Airways 232 · Boeing 777", "Mumbai", "Amsterdam",
                "16-Dec-2017 2:40 AM", "16-Dec-2017 11:50 AM", "BOM", "AMS", "A/A5", "C/C1", "Confirm #: YTRRT123", "9h 10m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "Delta 163 · Airbus A330", "Amsterdam", "Minneapolis",
                "16-Dec-2017 1:00 PM", "16-Dec-2017 3:12 PM", "AMS", "MSP", "A/A5", "C/C1", "Confirm #: YTRRT123", "2h 12m", null, null));
        list.add(new TravelAgendaItem(TravelAgendaItem.FLIGHT, "Delta 1551 · Boeing 737", "Minneapolis", "Las Vegas",
                "16-Dec-2017 4:30 PM", "16-Dec-2017 7:40 PM", "MSP", "LAS", "A/A5", "B/D6", "Confirm #: YTRRT123", "3h 10m", null, null));
//        list.add(new TravelAgendaItem(TravelAgendaItem.SERVICE, "Sandeep S",null,null,
//                "15-Dec-2107 10:00 AM","15-Dec-2107 04:00 PM",null,
//                null,null,null,null,null,"+91 9500000000","https://www.atomix.com.au/media/2015/06/atomix_user31.png"));
        CustomTravelAgendaAdapter adapter = new CustomTravelAgendaAdapter(getActivity(), list);

        lstSchedule.setAdapter(adapter);
    }

    public void scrollToItem(int Type) {
        final int position = getPosition(Type);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lstSchedule.smoothScrollToPosition(position);
            }
        }, 200);
    }

    public int getPosition(int type) {

        for (TravelAgendaItem agendaItem : list) {
            if (type == agendaItem.getTypeId()) {
                String testDate = agendaItem.getFrom_DateTime();
                DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy hh:mm aaa");
                try {
                    Date date = formatter.parse(testDate);
                    Date currentTime = Calendar.getInstance().getTime();
                    if(date.compareTo(currentTime)>=0) {
                        return list.indexOf(agendaItem);
                    }
                }catch (Exception e){
                    return list.indexOf(agendaItem);
                }


            }
        }
        return -1;
    }
}

