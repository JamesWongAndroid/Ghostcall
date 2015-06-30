package com.tapfury.ghostcall;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * Created by James on 6/29/15.
 */
public class TutorialImageFragment extends Fragment {

    public static TutorialImageFragment newInstance(int imageResId) {
        TutorialImageFragment imageFragment = new TutorialImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("imageResId", imageResId);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tutorial_image_fragment, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.image);
        iv.setImageResource(getArguments().getInt("imageResId"));


        return view;
    }



}
