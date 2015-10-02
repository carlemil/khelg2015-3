package com.jayway.waytravel;


import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jayway.waytravel.dto.EventDTO;
import com.jayway.waytravel.dto.EventsDTO;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by carlemil on 2015-10-02.
 */
public class EventFragment extends ListFragment {
    Gson gson = new Gson();
    private EventsDTO events;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final android.os.Handler handler = new android.os.Handler();
        handler.post(invokeGetJob(handler));

            // initialize and set the list adapter
            setListAdapter(new EventsAdapter(getActivity(), 0));
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // remove the dividers from the ListView of the ListFragment
            getListView().setDivider(null);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // retrieve theListView item
            EventDTO item = events.events.get(position);

            // do something
            Toast.makeText(getActivity(), item.title, Toast.LENGTH_SHORT).show();
        }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.tab_fragment2, null);
//    }

    public Runnable invokeGetJob(final Handler handler) {
        return new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                final Request request = new Request.Builder().addHeader("Accept", "application/json").url("http://travelway-server.herokuapp.com/events").build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {
                         events = gson.fromJson("{\"events\":" + response.body().string() + "}", EventsDTO.class);
                        //Log.d("TAG", "response: " + response.body().string());
                        response.body().close();
                    }
                });
            }
        };
    }

}
