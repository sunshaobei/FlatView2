package com.sunsh.sample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import sunsh.flatadapter.CommonAdapter;
import sunsh.flatadapter.FlatView;
import sunsh.flatadapter.base.FlatViewHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlatView viewById = findViewById(R.id.flatView);
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        viewById.setAdapter(new CommonAdapter<String>(this,R.layout.rv_loading,list) {
            @Override
            protected void convert(@NotNull FlatViewHolder holder,  String o, int position) {

            }
        });
    }
}
