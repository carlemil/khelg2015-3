package com.jayway.waytravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jayway.waytravel.dto.EventDTO;
import com.jayway.waytravel.dto.EventsDTO;

/**
 * Created by carlemil on 2015-10-02.
 */
public class EventsAdapter extends ArrayAdapter<EventDTO> {

    EventsDTO events;

    public EventsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void setEvents(EventsDTO events) {
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_item, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.event_name);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        EventDTO item = getItem(position);
        viewHolder.title.setText(item.title);

        return convertView;
    }

    private class ViewHolder {
        public TextView title;
        public TextView description;
        public TextView participants;
    }
}
