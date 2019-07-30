package com.example.Wo_Ca_Lo.FragmentViewpage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Wo_Ca_Lo.R;

/**
 * 放介紹圖片  Fragment
 */
public class PhotoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private ImageView imageView;
    //用newInstance 在 viewpager生成
    public static PhotoFragment newInstance(int round) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,round);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photp, container, false);
        imageView=view.findViewById(R.id.main_image);

        initView();
        return view;

    }

    private void initView() {
        int index=0;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        if(index>0){
            imageView.setImageResource(index);
        }


    }
}
