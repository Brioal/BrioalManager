package com.brioal.tools.codes;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brioal.tools.R;
import com.brioal.tools.initinterface.InitView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示源代码列表的activity
 * 不需传入参数
 * 点击列表进入源代码详情页面
 */
public class CodesListActivity extends AppCompatActivity implements InitView {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private List<Map<String, String>> lists;
    private ListView listView;
    private BaseAdapter adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codes_list);
        initViews();
        initDatas();
        setViews();
    }


    @Override
    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.activity_code_list_listview);
    }

    @Override
    public void initDatas() {
        lists = convertCodes();
    }

    @Override
    public void setViews() {
        toolbar.setTitle("源代码列表");
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return lists.size();
            }

            @Override
            public Object getItem(int position) {
                return lists.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder ;
                if (convertView == null) {
                    convertView = LayoutInflater.from(CodesListActivity.this).inflate(R.layout.item_code, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                Map<String, String> map = lists.get(position);
                holder.tv_title.setText(map.get("title"));
                holder.tv_lines.setText(map.get("lines"));
                return convertView;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CodesListActivity.this, CodeDetailActivity.class);
                Map<String, String> map = lists.get(position);
                intent.putExtra("title", map.get("title"));
                intent.putExtra("content", map.get("content"));
                intent.putExtra("lines", map.get("lines"));
                startActivity(intent);
            }
        });
    }
    public class ViewHolder {
        private TextView tv_title , tv_lines ;

        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.item_code_name);
            tv_lines = (TextView) convertView.findViewById(R.id.item_code_line);
        }
    }
    //读取asset下文件内容生产List<Map<String ,String >> list
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public List<Map<String, String>> convertCodes() {
        List<Map<String, String>> lists = new ArrayList<>();
        try {
            String[] ss = getAssets().list("");
            for (String s : ss) {
                Map<String, String> map = new HashMap<>();
                if (s.contains(".java") || s.contains(".xml") || s.contains(".txt")) {
                    System.out.println(s);
                    try (InputStream is = getAssets().open(s);
                         BufferedReader reader = new BufferedReader(new InputStreamReader(is))
                    ) {
                        StringBuilder builder = new StringBuilder();
                        String str = "";
                        int n = 1; // 用于添加行号
                        while (null != (str = reader.readLine())) {
                            builder.append(n + "     ").append(str).append("\n");
                            n++;
                        }
                        map.put("title", s);
                        map.put("content", builder.toString());
                        map.put("lines", n + "");
                        lists.add(map);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }
}


