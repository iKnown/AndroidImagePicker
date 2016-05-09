package com.iknow.imageselect.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iknow.imageselect.R;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.DrawableUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gordon on 5/9/16.
 */
public class BrowseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MEDIA_KEY = "MEDIA_KEY";

    private ImageView backImage, orginalImage, chooseImage;
    private View send, backArea, originalArea, chooseArea;
    private TextView navigationText;
    private ViewPager viewPager;
    private List<BrowseDetailModel> medias;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();
        setContentView(R.layout.layout_browse_detail_activity);
        findViews();
        addIcons();
        addListeners();
        initialize();
    }

    private void findViews() {
        navigationText = (TextView) findViewById(R.id.nav_text);
        backArea = findViewById(R.id.back_container);
        backImage = (ImageView) findViewById(R.id.back_image);
        orginalImage = (ImageView) findViewById(R.id.radio_original);
        chooseImage = (ImageView) findViewById(R.id.radio_choose);
        originalArea = findViewById(R.id.original_container);
        chooseArea = findViewById(R.id.choose_container);
        send = findViewById(R.id.send);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setNavigationText("8/112");
    }

    private void prepareData() {
        medias = new ArrayList<>();
        Intent fromIntent = getIntent();
        if (fromIntent.getExtras() != null) {
            List<MediaInfo> pass = (List<MediaInfo>) fromIntent.getExtras().getSerializable(MEDIA_KEY);
            for (MediaInfo info : pass) {
                medias.add(new BrowseDetailModel(info));
            }
        }
        medias.addAll(Arrays.asList(new BrowseDetailModel(new MediaInfo()), new BrowseDetailModel(new MediaInfo()), new BrowseDetailModel(new MediaInfo())));
    }

    public void initialize() {
        viewPager.setAdapter(new CycleBrowseAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    private int currentIndex = 0;
    private boolean isOriginal = false;

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        refreshState();
    }

    private void refreshState() {
        setChosen(medias.get(currentIndex).isSelected());
    }

    private void setNavigationText(String navigation) {
        this.navigationText.setText(navigation);
    }

    public boolean isChosen() {
        return medias.get(currentIndex).isSelected();
    }

    public void setChosen(boolean chosen) {
        medias.get(currentIndex).setSelected(chosen);
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
                setChosen(!isChosen());
                break;
            case R.id.original_container:
                setOriginal(!isOriginal());
                break;
            case R.id.back_container:
                break;
            case R.id.send:
                break;
        }
    }

    class CycleBrowseAdapter extends PagerAdapter {

        private List<View> views;

        public CycleBrowseAdapter() {
            views = new ArrayList<>();
            for (BrowseDetailModel model : medias) {
                views.add(getLayoutInflater().inflate(R.layout.layout_photo_view_item, viewPager, false));
            }
            Log.d("gordon", "views count ->" + views.size());
        }

        @Override
        public int getCount() {
            return medias.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class BrowseDetailModel implements Serializable {
        private boolean isSelected = false;
        private MediaInfo media;

        public BrowseDetailModel(boolean isChosen, MediaInfo media) {
            this.isSelected = isChosen;
            this.media = media;
        }

        public BrowseDetailModel(MediaInfo media) {
            this.media = media;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public MediaInfo getMedia() {
            return media;
        }

        public void setMedia(MediaInfo media) {
            this.media = media;
        }
    }

    public static void goToBrowseDetailActivity(Activity from, List<MediaInfo> mediaInfos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MEDIA_KEY, (Serializable) mediaInfos);
        Intent intent = new Intent(from, BrowseDetailActivity.class);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }
}
