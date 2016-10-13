package org.lowcarbon.soda.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.ui.fragment.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laizhenqi on 2016/10/4.
 */
public class MainActivity extends RxAppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawerlayout_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ImageView mBtnOpen;

    @BindView(R.id.tablayout_main)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initTabLayout();
        initView();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        if (toolbar != null) {
            mBtnOpen = (ImageView) toolbar.findViewById(R.id.image_toolbar);
        }
    }

    @Override
    protected void onDestroy() {
        mDrawerLayout.removeDrawerListener(mDrawerListener);
        super.onDestroy();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initTabLayout() {
        String[] carTypes = getResources().getStringArray(R.array.car_type);
        for (String type : carTypes) {
            mTabLayout.addTab(mTabLayout.newTab().setText(type));
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        mDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        mDrawerLayout.addDrawerListener(mDrawerListener);

        if (mBtnOpen != null) {
            mBtnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_main);
        if (fragment == null) {
            fragment = new MainFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_main, fragment, "MainFragment");
            ft.commit();
        }
    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}
