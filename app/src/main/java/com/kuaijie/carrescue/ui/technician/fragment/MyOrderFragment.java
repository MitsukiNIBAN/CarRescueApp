package com.kuaijie.carrescue.ui.technician.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.databinding.FragmentMainContentBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.ui.technician.OrderDetailActivity;
import com.kuaijie.carrescue.viewmodel.fragment.OrderListViewModel;

/**
 * Created by MitsukiSaMa on 11-23.
 */

public class MyOrderFragment extends Fragment{
    private FragmentMainContentBinding binding;
    private OrderListViewModel orderListViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_content, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        binding.lvOrderList.setEmptyView(binding.srlIndexDownRefreshEmpty);
//        binding.srlIndexDownRefreshEmpty.setVisibility(View.GONE);
        orderListViewModel = new OrderListViewModel(this.getActivity(),binding, OrderKey.ORDER_LIST);
        orderListViewModel.init();
        binding.setOrderViewModel(orderListViewModel);
        orderListViewModel.refreshList();

        binding.lvOrderList.setOnItemClickListener((adapterView, view, i, l) -> {
            Loading.getInstance().loading(getActivity());
            Long id = orderListViewModel.saveOrderAndGetId(i);
            Intent intent = new Intent();
            intent.setClass(this.getActivity(), OrderDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            orderListViewModel.refreshData();
        }
    }
}
