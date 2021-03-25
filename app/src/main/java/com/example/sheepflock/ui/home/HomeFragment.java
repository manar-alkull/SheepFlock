package com.example.sheepflock.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sheepflock.MainActivity;
import com.example.sheepflock.MapsActivity;
import com.example.sheepflock.R;
import com.example.sheepflock.Sheep;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        SheepsListAdapter adapter = new SheepsListAdapter(getActivity(),MainActivity.sheeps);
        listView=(ListView) root.findViewById(R.id.sheepsLst);
        listView.setAdapter(adapter);
        return root;
    }
    class SheepsListAdapter extends ArrayAdapter<Sheep> {

        private final Activity context;
        private final ArrayList<Sheep> sheeps;

        public SheepsListAdapter(Activity context, ArrayList<Sheep> sheeps) {
            super(context, R.layout.sheep_item, sheeps);
            this.context=context;
            this.sheeps=sheeps;
        }

        public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.sheep_item, null,true);

            TextView sheepId = (TextView) rowView.findViewById(R.id.sheepIdTxt);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.sheepImg);
            TextView sheepInfoTxt = (TextView) rowView.findViewById(R.id.sheepInfoTxt);

            android.text.format.DateFormat df = new android.text.format.DateFormat();
            sheepId.setText(sheeps.get(position).id);
            sheepInfoTxt.setText(df.format("yyyy-MM-dd hh:mm:ss a", sheeps.get(position).lastFeedDate));
            //imageView.setImageResource(imgid[position]);

            final int position1=position;
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("itemId", position1);
                    context.startActivity(intent);

                }
            });
            return rowView;
        };
    }

}


