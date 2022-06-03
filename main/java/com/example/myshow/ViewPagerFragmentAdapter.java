package com.example.myshow;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * 实现viewpage2动态切换fragment
 *
**/
public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    List<Fragment> fragmentList = new ArrayList<>();
    //传入fragmentList
    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,List<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        fragmentList = fragments;

    }

    @NonNull
    @Override
    //返回一个fragment，实现切换
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
