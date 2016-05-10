package com.iknow.imageselect.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iknow.imageselect.activities.MultiSelectImageActivity;

/**
 * Created by gordon on 5/10/16.
 */
public class ExampleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_example_activity);
        findViewById(R.id.card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.card) {
            Intent intent = new Intent(this, MultiSelectImageActivity.class);
            startActivity(intent);
        }
    }
}
