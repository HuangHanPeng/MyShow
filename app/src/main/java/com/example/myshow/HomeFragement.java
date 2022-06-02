package com.example.myshow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.PhantomReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragement extends Fragment {


    private static final String ARG_TEXT = "param1";

    private View rootView = null;
    private String mText;

    public HomeFragement() {
        // Required empty public constructor
    }

    public static HomeFragement newInstance(String param1) {
        HomeFragement fragment = new HomeFragement();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, param1);

        fragment.setArguments(args);

        return fragment;
    }

    private void initView() {
        TextView textView = rootView.findViewById(R.id.text1);
        textView.setText(mText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mText = getArguments().getString(ARG_TEXT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home_fragement,
                    container, 
                    false);
        }
        initView();
        // Inflate the layout for this fragment
        return rootView;
    }
}