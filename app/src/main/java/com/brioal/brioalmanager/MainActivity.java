package com.brioal.brioalmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.brioal.brioalmanager.fragmentLoading.LoadingFragment;
import com.brioal.brioalmanager.fragmentMain.AppListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivityInfo";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private boolean isBackPress = false; // 存储是否已经点击过一次返回键
    private long lastBackPress; // 存储上一次点击的毫秒值
    //Fragment
    public static LoadingFragment loadFragment;
    private AppListFragment appListFragment;
    public static View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rootView = findViewById(R.id.layout_app_bar_main).findViewById(R.id.main_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        appListFragment = new AppListFragment();
        getFragmentManager().beginTransaction().add(rootView.getId(), appListFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!isBackPress) {
            Snackbar.make(rootView, "再按一次退出应用", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            isBackPress = true;
            lastBackPress = System.currentTimeMillis();
            Log.i(TAG, "显示提醒");
        } else if ((System.currentTimeMillis() - lastBackPress) < 3000) {
            Log.i(TAG, "两次返回连续，直接退出");
            super.onBackPressed();

        } else {
            Log.i(TAG, "两次间隔太长");
            Snackbar.make(rootView, "再按一次退出应用", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            isBackPress = true;
            lastBackPress = System.currentTimeMillis();

        }
    }

    //TODO 更改头布局
    //TODO 设置颜色搭配
    //TODO 添加root权限获取 显示shareFileds
    //TODO 设置一个好看的图标
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //TODo 选中item不显示选中
        if (id == R.id.nav_applist) {
            if (appListFragment == null) {
                appListFragment = new AppListFragment();
            }
            if (appListFragment.isAdded()) {
                getFragmentManager().beginTransaction().replace(rootView.getId(), appListFragment).commit();
            } else {
                getFragmentManager().beginTransaction().add(rootView.getId(), appListFragment).commit();
            }
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            getFragmentManager().beginTransaction().replace(rootView.getId(), appListFragment).commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
