package com.example.retrofitexample.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.retrofitexample.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrorsFragment extends Fragment {

    private ImageView cloudIcon, errorIcon;
    private TextView internetMessage, errorMessage;
    private LinearLayout internetLayout, errorLayout;

    public ErrorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_errors, container, false);

        internetLayout = view.findViewById(R.id.internetLayout);
        cloudIcon = view.findViewById(R.id.cloudIcon);
        internetMessage = view.findViewById(R.id.internetMessage);

        errorLayout = view.findViewById(R.id.errorsLayout);
        errorIcon = view.findViewById(R.id.errorIcon);
        errorMessage = view.findViewById(R.id.errorMessage);

        return view;
    }

    public void onDeviceOffline(){

         errorLayout.setVisibility(View.GONE);

         internetLayout.setVisibility(View.VISIBLE);
         internetMessage.setText("There is no Internet Connection");
    }

    public void onError(String message){

        internetMessage.setVisibility(View.GONE);

        errorLayout.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
    }



}
