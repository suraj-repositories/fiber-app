package com.oranbyte.fiber;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class NextPageDetailFragment extends Fragment {

    private static final String ARG_DEVICE_KEY = "device_key";

    public static NextPageDetailFragment newInstance(String deviceKey){
        NextPageDetailFragment f = new NextPageDetailFragment();
        Bundle b = new Bundle();
        b.putString(ARG_DEVICE_KEY, deviceKey);
        f.setArguments(b);
        return f;
    }

    private String deviceKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) deviceKey=getArguments().getString(ARG_DEVICE_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_next_page_detail, container, false);
        TextView tv = view.findViewById(R.id.device_key_tv);
        tv.setText("Device Key: "+deviceKey);
        return view;
    }
}
