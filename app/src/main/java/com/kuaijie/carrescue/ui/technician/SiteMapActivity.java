package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivitySiteMapBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.ui.base.MainActivity;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by Mitsuki on 12-25.
 */

public class SiteMapActivity extends Activity {

    private ActivitySiteMapBinding binding;
    private Long id;

    private float latitude = 29.9116600000f;
    private float longitude = 121.6102200000f;

    private int type;

    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_site_map);

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        type = in.getIntExtra("type", Integer.MIN_VALUE);

        Order order = DataCache.getInstance().getOneOrder(id);

        if (type == 1) {
            binding.includeTitleBar.setTitleName("救援地");
            binding.tvPointName.setText(order.getAidAddress());
            latitude = order.getAidLatitude().floatValue();
            longitude = order.getAidLongitude().floatValue();
        } else if (type == 2) {
            binding.includeTitleBar.setTitleName("目的地");
            binding.tvPointName.setText(order.getGoalAddress());
            latitude = order.getGoalLatitude().floatValue();
            longitude = order.getGoalLongitude().floatValue();
        }
        binding.includeTitleBar.ibTitleMore.setVisibility(View.GONE);

        binding.mvMap.onCreate(savedInstanceState);
        binding.btnNavigation.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            intent.setClass(this, NavigationActivity.class);
            startActivity(intent);
        });

        if (aMap == null) {
            aMap = binding.mvMap.getMap();
        }
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                new LatLng(latitude, longitude), 15, 30, 30)));
        aMap.clear();
        aMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        speakHint("点击导航按钮开始导航");
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mvMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mvMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mvMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mvMap.onSaveInstanceState(outState);
    }
}
