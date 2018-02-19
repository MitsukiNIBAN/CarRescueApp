package com.kuaijie.carrescue.viewmodel.activity;

import com.kuaijie.carrescue.databinding.ActivityMainBinding;
import com.kuaijie.carrescue.util.Activity;

/**
 * Created by MitsukiSaMa on 11-23.
 */

public class MainViewModel {
    private Activity activity;
    private ActivityMainBinding binding;
    public MainViewModel(Activity activity, ActivityMainBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

}
