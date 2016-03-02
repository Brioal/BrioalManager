package com.brioal.tools.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.brioal.tools.R;
import com.brioal.tools.codes.CodesListActivity;
import com.brioal.tools.initinterface.InitView;

/**
 * 显示知识点的activity
 * 传入title ，content
 * 点击fab显示源代码列表 CodeListActivity
 */
public class SummaryActivity extends AppCompatActivity implements InitView{
    private Toolbar toolbar ;
    private FloatingActionButton fab ;
    private TextView tv_content ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initViews();
        initDatas();
        setViews();
    }

    @Override
    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tv_content = (TextView) findViewById(R.id.layout_content_summary).findViewById(R.id.activity_summary_content);
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void setViews() {
        toolbar.setTitle(getResources().getString(R.string.title));
        tv_content.setText(getResources().getString(R.string.content));
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SummaryActivity.this, CodesListActivity.class));
            }
        });
    }
}
