package com.kuaijie.carrescue.ui.technician.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.FragmentFeeRegistrationBinding;

/**
 * Created by MitsukiSaMa on 11-23.
 */

public class FeeRegistrationFragment extends Fragment{
    private FragmentFeeRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fee_registration, container, false);
        return binding.getRoot();
    }
}
