package com.example.adminapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tabLayout = findViewById(R.id.main_tab_layout);
        viewPager = findViewById(R.id.main_view_pager);

        ArrayList<String> titles = new ArrayList<>();

        titles.add("Home");
        titles.add("Events");
        titles.add("Account");

        tabLayout.setupWithViewPager(viewPager);

        prepareViewPager(viewPager, titles);
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> titles) {
        SessionManagement sessionManagement = new SessionManagement(this);
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        MainFragment mainFragment = new MainFragment(sessionManagement, this);

        for (int i = 0; i < titles.size();i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", titles.get(i));
            mainFragment.setArguments(bundle);
            adapter.addFragment(mainFragment, titles.get(i));
            mainFragment = new MainFragment(sessionManagement, this);
        }

        viewPager.setAdapter(adapter);
    }

    private  class MainAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        int[] imageList =  {R.drawable.ic_home,R.drawable.ic_confetti,R.drawable.ic_user};

        public void addFragment(Fragment fragment, String s) {
            fragments.add(fragment);
            strings.add(s);
        }

        public  MainAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), imageList[position]);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            SpannableString spannableString = new SpannableString("   " + strings.get(position));
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return spannableString;
        }
    }
}