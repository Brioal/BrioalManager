package com.brioal.brioalmanager.fragmentMain;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brioal.brioalmanager.MainActivity;
import com.brioal.brioalmanager.R;
import com.brioal.brioalmanager.base.AppInfo;
import com.brioal.brioalmanager.fragmentLoading.LoadingFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 知识点：
 *  1.List排序 -》
 *  2.读取安装的软件及其软件的一些属性的获取 ->
 *  3.RecyclerView的使用以及事件监听  ->
 *  4.SharePreference的使用方法及存储属性 ->
 */
public class AppListFragment extends Fragment {
    private static final String TAG = "ApplistFragment";
    @Bind(R.id.main_recycler)
    RecyclerView mainRecycler;
    private List<AppInfo> lists;
    private AppAdapter appAdapter;
    private LoadingFragment loadingFragment;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                getFragmentManager().beginTransaction().hide(loadingFragment).commit();
                Comparator comp = new SortComparator();
                Collections.sort(lists, comp);
                appAdapter = new AppAdapter();
                mainRecycler.setLayoutManager(new LinearLayoutManager(getActivity()) {
                    @Override
                    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                        setMeasuredDimension(widthSpec, heightSpec);
                    }
                });

                mainRecycler.setAdapter(appAdapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applist, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        loadingFragment = MainActivity.loadFragment ;
        if (loadingFragment == null) {
            loadingFragment = new LoadingFragment();
        }
        if (loadingFragment.isAdded()) {
            getActivity().getFragmentManager().beginTransaction().show(loadingFragment).commit();
        } else {
            getActivity().getFragmentManager().beginTransaction().add(R.id.drawer_layout, loadingFragment).commit();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initData() {
        lists = new ArrayList<>();
        AppInfo info = null;
        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.contains("brioal")) {
                String appName = packageInfo.applicationInfo.loadLabel(getActivity().getPackageManager()).toString(); // 名称
                String appPackage = packageInfo.packageName; //包名
                long time = packageInfo.firstInstallTime; //安装时间（毫秒值）
                String s = String.valueOf(time);
//                安装时间
                Drawable appIcon = packageInfo.applicationInfo.loadIcon(getActivity().getPackageManager());//图标
                Context otherAppsContext = null;
                String appDesc = null;
                try {
                    otherAppsContext = getActivity().createPackageContext(appPackage, Context.CONTEXT_IGNORE_SECURITY);
                    SharedPreferences sharedPreferences = otherAppsContext.getSharedPreferences("Brioal", Context.MODE_WORLD_READABLE);
                    appDesc = sharedPreferences.getString("Desc", "暂无描述");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                info = new AppInfo(appName, appPackage, appDesc, time, appIcon);
                lists.add(info);
            }
        }
        //通知handle更新数据
        handler.sendEmptyMessage(0x123);
    }

    public class AppAdapter extends RecyclerView.Adapter<AppHolder> {

        @Override
        public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            AppHolder holder = new AppHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_app, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(AppHolder holder, int position) {

            final AppInfo info = lists.get(position);
            holder.appName.setText(info.getAppName());
            holder.appPackage.setText(info.getAppPackage());
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String appTime = sDateFormat.format(new Date(info.getAppTime() + 0));
            holder.appTime.setText(appTime);
            holder.appDesc.setText(info.getAppDesc());
            holder.appIcon.setImageDrawable(info.getAppIcon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runApp(info.getAppPackage());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //TODO 添加长按菜单
                    return false;
                }
            });

        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

    public void runApp(String packageName) {
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.setPackage(info.packageName);
            PackageManager pManager = getActivity().getPackageManager();
            //查询所有的activity
            List<ResolveInfo> apps = pManager.queryIntentActivities(intent, 0);
            ResolveInfo resolveInfo = apps.iterator().next();
            if (resolveInfo != null) {
                packageName = resolveInfo.activityInfo.packageName;
                String className = resolveInfo.activityInfo.name;
                Intent intent1 = new Intent(Intent.ACTION_MAIN);
                ComponentName componentName = new ComponentName(packageName, className);
                intent1.setComponent(componentName);
                startActivity(intent1);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    //用于排序
    public class SortComparator implements Comparator {

        @Override
        public int compare(Object lhs, Object rhs) {
            AppInfo first = (AppInfo) lhs;
            AppInfo second = (AppInfo) rhs;

            return -(int) (first.getAppTime() - second.getAppTime());
        }
    }

    public class AppHolder extends RecyclerView.ViewHolder {
        private View itemView;
        ImageView appIcon;
        TextView appName;
        TextView appPackage;
        TextView appDesc;
        TextView appTime;

        public AppHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            appName = (TextView) itemView.findViewById(R.id.item_app_name);
            appTime = (TextView) itemView.findViewById(R.id.item_app_time);
            appPackage = (TextView) itemView.findViewById(R.id.item_app_package);
            appDesc = (TextView) itemView.findViewById(R.id.item_app_desc);
        }
    }
}
