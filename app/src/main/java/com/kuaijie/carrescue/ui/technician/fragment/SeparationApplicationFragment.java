package com.kuaijie.carrescue.ui.technician.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.FragmentSeparationApplicationBinding;

/**
 * Created by MitsukiSaMa on 11-23.
 */

public class SeparationApplicationFragment extends Fragment{
    private FragmentSeparationApplicationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_separation_application, container, false);
        return binding.getRoot();

    }
}
