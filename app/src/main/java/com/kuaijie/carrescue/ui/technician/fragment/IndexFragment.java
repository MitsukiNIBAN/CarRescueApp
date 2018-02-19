package com.kuaijie.carrescue.ui.technician.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.databinding.FragmentMainContentBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.util.PushMsgContainer;
import com.kuaijie.carrescue.viewmodel.fragment.OrderListViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-22.
 */

public class IndexFragment extends Fragment {

    private static final String TAG = "IndexFragment";

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
        orderListViewModel = new OrderListViewModel(this.getActivity(), binding, OrderKey.UNDO_ORDER_LIST);
        orderListViewModel.init();
        binding.setOrderViewModel(orderListViewModel);
        orderListViewModel.refreshData();

        binding.lvOrderList.setOnItemClickListener((adapterView, view, i, l) -> {
            Loading.getInstance().loading(getActivity());
            new Thread(() -> {
                //item点击的时候，先缓存单击的订单的信息
                Long id = orderListViewModel.saveOrderAndGetId(i);
                PageJump.getInstance().jump((Activity) getActivity(), new Long(-4l), id, 3);
                new Handler(getActivity().getMainLooper()).post(() -> Loading.getInstance().unloading());
            }).start();

        });
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            orderListViewModel.refreshData();
        } else {
            orderListViewModel.stopCount();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Main onResume");
        orderListViewModel.refreshData();
        PushMsgContainer.getInstance().removeConsumer(OrderKey.UNDO_ORDER_LIST);
        Consumer consumer = o -> {
            orderListViewModel.refreshData();
        };
        PushMsgContainer.getInstance().putConsumer(OrderKey.UNDO_ORDER_LIST, consumer);

    }

    @Override
    public void onPause() {
        super.onPause();
        orderListViewModel.stopCount();
        PushMsgContainer.getInstance().removeConsumer(OrderKey.UNDO_ORDER_LIST);
        Consumer<Integer> consumer = state -> {
        };
        PushMsgContainer.getInstance().putConsumer(OrderKey.UNDO_ORDER_LIST, consumer);
    }
}
