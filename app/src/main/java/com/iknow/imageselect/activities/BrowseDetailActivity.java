package com.iknow.imageselect.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.iknow.imageselect.R;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.DrawableUtil;
import com.iknow.imageselect.utils.ImageFilePathUtil;
import com.iknow.imageselect.utils.PhotoDraweeViewUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rawe.gordon.com.photodraweeview.PhotoDraweeView;

/**
 * Created by gordon on 5/9/16.
 */
public class BrowseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MEDIA_KEY = "MEDIA_KEY";
    public static final String CHOOSEN_PICS = "CHOOSEN_PICS";
    public static final int CODE_BROWSE_AND_CHOOSE = 0x1128;

    public static final String TYPE_SELECTED = "TYPE_SELECTED";
    public static final String TYPE_EMPTY = "TYPE_EMPTY";
    public static String ENTRY_TYPE = "ENTRY_TYPE";
    public static String CURRENT_INDEX = "CURRENT_INDEX";

    private String fromType = TYPE_EMPTY;

    private ImageView backImage, orginalImage, chooseImage;
    private View send, backArea, originalArea, chooseArea;
    private TextView navigationText;
    private ViewPager viewPager;
    private List<BrowseDetailModel> medias;
    private int count = 0;

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
    }

    private void prepareData() {
        medias = new ArrayList<>();
        Intent fromIntent = getIntent();
        if (fromIntent.getExtras() != null) {
            currentIndex = fromIntent.getExtras().getInt(CURRENT_INDEX);
            fromType = fromIntent.getExtras().getString(ENTRY_TYPE);
            List<MediaInfo> pass = (List<MediaInfo>) fromIntent.getExtras().getSerializable(MEDIA_KEY);
            for (MediaInfo info : pass) {
                medias.add(fromType.equals(TYPE_EMPTY) ? new BrowseDetailModel(info) : new BrowseDetailModel(true, info));
            }

        }
//        medias.addAll(Arrays.asList(new BrowseDetailModel(new MediaInfo()), new BrowseDetailModel(new MediaInfo()), new BrowseDetailModel(new MediaInfo())));
        count = medias.size();
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
        viewPager.setCurrentItem(currentIndex);
        setNavigationText((currentIndex + 1) + "/" + count);
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
        setNavigationText((currentIndex + 1) + "/" + count);
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
                handleBackAction();
                break;
            case R.id.send:
                handleSureAction();
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
            View itemView;
            container.addView(itemView = views.get(position));
            PhotoDraweeViewUtil.display((PhotoDraweeView) itemView.findViewById(R.id.photo_view), Uri.parse(ImageFilePathUtil.getImgUrl(medias.get(position).getMedia().fileName)));
            return itemView;
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

    private void handleSureAction() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHOOSEN_PICS, (Serializable) getValidModels(medias));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void handleBackAction() {
        finish();
    }

    public static List<MediaInfo> getValidModels(List<BrowseDetailModel> models) {
        List<MediaInfo> res = new ArrayList<>();
        for (BrowseDetailModel model : models) {
            if (model.isSelected()) res.add(model.getMedia());
        }
        return res;
    }

    public static void goToBrowseDetailActivity(Activity from, List<MediaInfo> mediaInfos, int index) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MEDIA_KEY, (Serializable) mediaInfos);
        bundle.putString(ENTRY_TYPE, TYPE_EMPTY);
        bundle.putInt(CURRENT_INDEX, index);
        Intent intent = new Intent(from, BrowseDetailActivity.class);
        intent.putExtras(bundle);
        from.startActivityForResult(intent, CODE_BROWSE_AND_CHOOSE);
    }

    public static void goToBrowseDetailActivitySelected(Activity from, List<MediaInfo> mediaInfos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MEDIA_KEY, (Serializable) mediaInfos);
        bundle.putString(ENTRY_TYPE, TYPE_SELECTED);
        bundle.putInt(CURRENT_INDEX, 0);
        Intent intent = new Intent(from, BrowseDetailActivity.class);
        intent.putExtras(bundle);
        from.startActivityForResult(intent, CODE_BROWSE_AND_CHOOSE);
    }
}
