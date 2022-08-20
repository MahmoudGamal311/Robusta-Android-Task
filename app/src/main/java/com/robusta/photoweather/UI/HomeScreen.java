package com.robusta.photoweather.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.robusta.photoweather.R;

import java.util.HashSet;
import java.util.Set;

public class HomeScreen extends AppCompatActivity {

    HistoryGridAdapter adapter;
    GridView historyGrid;
    TextView noHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView addNewImageBtn = findViewById(R.id.add_new_image_btn);
        addNewImageBtn.setOnClickListener(view -> {
            Intent mainIntent = new Intent(HomeScreen.this, AddingNewImageScreen.class);
            HomeScreen.this.startActivity(mainIntent);
            HomeScreen.this.finish();
        });

        historyGrid = findViewById(R.id.history_grid);
        noHistory = findViewById(R.id.no_history_label);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Set<String> historyList = SplashScreen.sharedPreferences.getStringSet(SplashScreen.HISTORY_LIST_TAG, new HashSet<>());

        if (historyList != null) {
            if (historyList.size() == 0) {
                historyGrid.setVisibility(View.GONE);
                noHistory.setVisibility(View.VISIBLE);
            } else {
                adapter = new HistoryGridAdapter(HomeScreen.this, historyList);
                historyGrid.setAdapter(adapter);
                historyGrid.invalidate();
            }
        } else {
            historyGrid.setVisibility(View.GONE);
            noHistory.setVisibility(View.VISIBLE);
        }
    }
}