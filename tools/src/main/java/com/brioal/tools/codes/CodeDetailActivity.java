package com.brioal.tools.codes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.brioal.tools.R;

public class CodeDetailActivity extends AppCompatActivity {
    private TextView tv_content ;
    private Toolbar toolbar ;
    private FloatingActionButton fab ;
    private String title , content,lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_detail);
        initViews();
        initDatas();
        setViews();

    }

    public void initViews() {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         fab = (FloatingActionButton) findViewById(R.id.fab);
        tv_content = (TextView) findViewById(R.id.content_code_detail).findViewById(R.id.item_code_detail);
    }

    public void initDatas() {
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        lines = getIntent().getStringExtra("lines");
    }

    public void setViews() {
        toolbar.setTitle("  "+title+"   "+lines);
        tv_content.setText(content);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "代码详情面板", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
