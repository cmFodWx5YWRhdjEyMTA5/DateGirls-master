package com.amsu.dategirls.fragment.picture;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amsu.dategirls.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastPictureFragment extends Fragment {


    public LastPictureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_last_picture, container, false);
    }

}
