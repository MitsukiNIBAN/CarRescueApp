package com.kuaijie.carrescue.ui.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityMainBinding;
import com.kuaijie.carrescue.databinding.ActivityMainNavHeaderBinding;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.ui.dialog.LogoffDialog;
import com.kuaijie.carrescue.ui.technician.fragment.FeeRegistrationFragment;
import com.kuaijie.carrescue.ui.technician.fragment.IndexFragment;
import com.kuaijie.carrescue.ui.technician.fragment.MyOrderFragment;
import com.kuaijie.carrescue.ui.technician.fragment.PayrollInquiriesFragment;
import com.kuaijie.carrescue.ui.technician.fragment.RescueAssistanceFragment;
import com.kuaijie.carrescue.ui.technician.fragment.SeparationApplicationFragment;
import com.kuaijie.carrescue.ui.technician.fragment.ShiftLeaveFragment;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.Application;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.texttospeech.AppTTSController;


public class MainActivity extends Activity {

    private ActivityMainBinding binding;
    private Intent in;
    private View headview;

    private Fragment[] fragments;

    // 默认的首页
    private int defaultPage = 0;
    // 被选中的页面
    private int indexPage;
    // 当前的页面
    private int currentTabIndex;

    private FragmentManager fragmentManager;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setDefault();
        setListener();
    }

    private void initView() {
        in = new Intent();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        fragments = new Fragment[]{new IndexFragment(),new MyOrderFragment(),new RescueAssistanceFragment()
        ,new ShiftLeaveFragment(),new SeparationApplicationFragment(),new FeeRegistrationFragment(),new PayrollInquiriesFragment()};
        fragmentManager = getFragmentManager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.includeContent.tbTitleToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        headview = binding.navView.getHeaderView(0);
        UserDTO userDTO = DataCache.getInstance().getUser();
        if (userDTO != null) {
            ((TextView) headview.findViewById(R.id.tv_user_name)).setText(userDTO.getAccountName() + "");
//            ((TextView) headview.findViewById(R.id.tv_user_power)).setText(userDTO.getRoles(). + "");
        }
//        speakHint("登陆成功");
    }

    private void setDefault() {
        binding.includeContent.tvTitleName.setText(getResources().getString(R.string.index));
        binding.navView.getMenu().getItem(defaultPage).setChecked(true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragments[defaultPage]);
        transaction.commit();
    }

    private void setListener() {
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_index:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.index));
                    indexPage = 0;
                    break;
                case R.id.menu_my_order:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.my_order));
                    indexPage = 1;
                    break;
                case R.id.menu_rescue_assistance:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.rescue_assistance));
                    indexPage = 2;
                    break;
                case R.id.menu_shift_leave:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.shift_leave));
                    indexPage = 3;
                    break;
                case R.id.menu_separation_application:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.separation_application));
                    indexPage = 4;
                    break;
                case R.id.menu_fee_registration:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.fee_registration));
                    indexPage = 5;
                    break;
                case R.id.menu_payroll_inquiries:
                    binding.includeContent.tvTitleName.setText(getResources().getString(R.string.payroll_inquiries));
                    indexPage = 6;
                    break;
                default:
                    indexPage = -1;
                    break;
            }
            changePage();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    private void changePage() {
        if (currentTabIndex != indexPage) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(fragments[currentTabIndex]);
            if (!fragments[indexPage].isAdded())
                transaction.add(R.id.fragment_container, fragments[indexPage]);
            transaction.show(fragments[indexPage]).commit();
        }
        currentTabIndex = indexPage;
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Application.getInstance().exit();
            }
        }
    }

    public void logoffClick(View view) {
        new LogoffDialog(this).show();
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public int getIndexPage(){
        return indexPage;
    }
}
