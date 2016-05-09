package com.iknow.imageselect.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iknow.imageselect.R;
import com.iknow.imageselect.utils.DrawableUtil;

/**
 * Created by gordon on 5/9/16.
 */
public class BrowseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backImage, orginalImage, chooseImage;
    private View send, backArea, originalArea, chooseArea;
    private TextView navigationText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_browse_detail_activity);
        findViews();
    }

    private void findViews() {
        backArea = findViewById(R.id.back_container);
        backImage = (ImageView) findViewById(R.id.back_image);
        orginalImage = (ImageView) findViewById(R.id.radio_original);
        chooseImage = (ImageView) findViewById(R.id.radio_choose);
        originalArea = findViewById(R.id.original_container);
        chooseArea = findViewById(R.id.choose_container);
        send = findViewById(R.id.send);
        addIcons();
        addListeners();
    }

    private void addIcons() {
        backImage.setImageDrawable(DrawableUtil.decodeFromVector(getApplicationContext(), R.drawable.ic_arrow_back));
        orginalImage.setImageDrawable(DrawableUtil.decodeFromVector(getApplicationContext(), R.drawable.ic_radio_button_unchecked));
        chooseImage.setImageDrawable(DrawableUtil.decodeFromVector(getApplicationContext(), R.drawable.ic_choosable));
    }

    private void addListeners() {
        send.setOnClickListener(this);
        backArea.setOnClickListener(this);
        chooseArea.setOnClickListener(this);
        originalArea.setOnClickListener(this);
    }

    /**
     * variables
     */
    private boolean isOriginal = false;
    private boolean isChosen = false;

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
        chooseImage.setImageDrawable(chosen ? DrawableUtil.decodeFromVector(getApplicationContext(),
                R.drawable.ic_chosen) : DrawableUtil.decodeFromVector(getApplicationContext(), R.drawable.ic_choosable));
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
        orginalImage.setImageDrawable(original ? DrawableUtil.decodeFromVector(getApplicationContext(),
                R.drawable.ic_radio_button_checked) : DrawableUtil.decodeFromVector(getApplicationContext(), R.drawable.ic_radio_button_unchecked));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_container:
                setChosen(!isChosen);
                break;
            case R.id.original_container:
                setOriginal(!isOriginal);
                break;
            case R.id.back_container:
                break;
            case R.id.send:
                break;
        }
    }
}
