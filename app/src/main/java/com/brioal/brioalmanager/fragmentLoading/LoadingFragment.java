package com.brioal.brioalmanager.fragmentLoading;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brioal.brioalmanager.R;

/**
 * TODO testapp过多时显示加载动画
 * Created by brioal on 16-2-16.
 */
public class LoadingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loading, null);

        return rootView;
    }
}
