package com.kuaijie.carrescue.ui.technician.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.FragmentPayrollInquiriesBinding;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by MitsukiSaMa on 11-23.
 */

public class PayrollInquiriesFragment extends Fragment{
    private FragmentPayrollInquiriesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payroll_inquiries, container, false);

        DataCache.getInstance().clear();


        return binding.getRoot();
    }
}
