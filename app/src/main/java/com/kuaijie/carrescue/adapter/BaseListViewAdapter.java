package com.kuaijie.carrescue.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kuaijie.carrescue.BR;

import java.util.ArrayList;

/**
 * Created by MitsukiSaMa on 12-5.
 */

public abstract class BaseListViewAdapter <M, B extends ViewDataBinding> extends BaseAdapter{

    protected Context context;
    protected ArrayList<M> items;
    private LayoutInflater inflater;
    private B binding;

    public BaseListViewAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public ArrayList<M> getItems(){
        return items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            binding = DataBindingUtil.inflate(inflater, this.getLayoutResId(), viewGroup, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (B) convertView.getTag();
        }
        this.onBindItem(binding, this.items.get(i));
        return convertView;
    }
    protected abstract @LayoutRes int getLayoutResId();

    protected abstract void onBindItem(B binding, M item);

}
