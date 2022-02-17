package com.example.clientapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RVAdapterEvents extends RecyclerView.Adapter<RVAdapterEvents.ViewHolder> {
    private List<Event> events;
    private Context context;

    public RVAdapterEvents(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public RVAdapterEvents.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event,parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RVAdapterEvents.ViewHolder holder, int position) {
        Event event = events.get(position);

        try {
            holder.fromDate.setText(formatDate(event.fromDate));
            holder.toDate.setText(formatDate(event.toDate));
            holder.fromTime.setText(formatTime(event.fromTime));
            holder.toTime.setText(formatTime(event.toTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.card.setCardBackgroundColor(Color.parseColor(getStringColorByName(event.defaultColor)));
        holder.title.setText(event.title);
        holder.location.setText(event.location);
        holder.description.setText(event.description);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public String getStringColorByName(String name) {
        String[] ringNames = this.context.getResources().getStringArray(R.array.ring_color_name);
        String[] ringColors = this.context.getResources().getStringArray(R.array.ring_color);

        for (int i = 0; i < ringNames.length;i++) {
            if (name.toLowerCase().equals(ringNames[i].toLowerCase())) {
                return ringColors[i];
            }
        }

        return "Not Found";
    }

    public String formatDate(String toFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = formatter.parse(toFormat);
        SimpleDateFormat formatr = new SimpleDateFormat("MMMM dd yyyy");
        return formatr.format(date);
    }

    public String formatTime(String time) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        String[] val = time.split(":");
        Date d = new Date();
        Date date = new Date(d.getYear(), d.getMonth(),d.getDate(), Integer.parseInt(val[0]),Integer.parseInt(val[1]));
        return formatter.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView title, fromDate, toDate, fromTime, toTime, location, description, adminLastname;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.eCardEvent);
            title = itemView.findViewById(R.id.eCardTitle);
            fromDate = itemView.findViewById(R.id.eCardFromDate);
            toDate = itemView.findViewById(R.id.eCardToDate);
            fromTime =  itemView.findViewById(R.id.eCardFromTime);
            toTime = itemView.findViewById(R.id.eCardToTime);
            location = itemView.findViewById(R.id.eCardLocation);
            description = itemView.findViewById(R.id.eCardDescription);
        }
    }
}
