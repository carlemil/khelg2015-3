package com.jayway.waytravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jayway.waytravel.dto.EventDTO;
import com.jayway.waytravel.dto.EventsDTO;

/**
 * Created by carlemil on 2015-10-02.
 */
public class EventsAdapter extends BaseAdapter {

    private final Context context;
    EventsDTO events;

    public EventsAdapter(Context context){
        this.context = context;
    }

    public void setEvents(EventsDTO events) {
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.event_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.event_title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.event_description);
            viewHolder.participants = (TextView) convertView.findViewById(R.id.event_participants);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        EventDTO item = getItem(position);
        viewHolder.title.setText(item.title);
        viewHolder.description.setText(item.description);
        viewHolder.participants.setText("");//item.participants.size()+"");

        return convertView;
    }

    @Override
    public int getCount() {
        if (events != null && events.events != null) {
            return events.events.size();
        } else {
            return 0;
        }
    }

    @Override
    public EventDTO getItem(int i) {
        return events.events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        public TextView title;
        public TextView description;
        public TextView participants;
    }
}
