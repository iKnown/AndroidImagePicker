package com.iknow.imageselect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.iknow.imageselect.adapter.AlbumListAdapter;
import com.iknow.imageselect.core.CoreActivity;
import com.iknow.imageselect.model.AlbumInfo;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.presenter.IImageChoosePresenter;
import com.iknow.imageselect.presenter.ImageChoosePresenterCompl;
import com.iknow.imageselect.utils.MediaFileUtil;
import com.iknow.imageselect.view.IImageChooseView;
import com.iknow.imageselect.widget.TitleView;
import java.io.File;
import java.util.ArrayList;

/**
 * @Author: J.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月06日 3:16 PM
 * @Description:
 */

public abstract class AbsImageSelectActivity extends CoreActivity implements IImageChooseView,
    View.OnClickListener {
  // ===========================================================
  // Constants
  // ===========================================================
  public static final int PHOTO_REQUEST_CAMERA = 0x007;
  public static final int MULTI_PIC_SELECT_REQUEST = 0x008;
  public static final int SINGLE_PIC_SELECT_REQUEST = 0x009;
  // ===========================================================
  // Fields
  // ===========================================================
  protected AppCompatActivity mContext;
  protected String mTakeCameraImagePath;
  private boolean showAlbumList = false;
  private ListView albumListView;
  private View albumView;
  protected TextView allImagesTv;
  protected TitleView gsTitleView;
  protected ArrayList<MediaInfo> allMedias = new ArrayList<MediaInfo>();
  protected ArrayList<MediaInfo> hasCheckedImages = new ArrayList<MediaInfo>();
  protected IImageChoosePresenter imageChoosePresenter;
  private ImageGridAdapter imageGridAdapter;
  private GridView mImageGv;

  protected abstract void initTitleView(TitleView titleView);
  protected abstract void initBottomView(View bottomView);
  protected abstract View doGetViewWork(int position,View convertView,MediaInfo imageInfo);
  protected abstract void onImageSelectItemClick(AdapterView<?> parent, View view, int position, long id);
  protected abstract void onCameraActivityResult(String path);
;
  // ===========================================================
  // Constructors
  // ===========================================================

  // ===========================================================
  // Getter & Setter
  // ===========================================================

  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = this;
    setContentView(R.layout.abs_image_select_layout);
    allMedias = MediaFileUtil.getAllMediaFiles(ZApplication.getApplication());
    imageChoosePresenter = new ImageChoosePresenterCompl(this, this);
    initTitleView(gsTitleView = (TitleView) this.findViewById(R.id.titlebar));
    initBottomView(this.findViewById(R.id.bottomView));
    initContentView();
  }


  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {


      if (requestCode == PHOTO_REQUEST_CAMERA) {//拍照返回
        if (TextUtils.isEmpty(mTakeCameraImagePath)) {
          return;
        }

        final File f = new File(mTakeCameraImagePath);
        if (f == null || !f.exists()) {
          Toast.makeText(ZApplication.getApplication(), "照相失败", Toast.LENGTH_SHORT).show();
          return;
        }

        try {
          Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
          scanIntent.setData(Uri.fromFile(f));
          AbsImageSelectActivity.this.sendBroadcast(scanIntent);

          onCameraActivityResult(mTakeCameraImagePath);
        } catch (Throwable e) {
          Toast.makeText(AbsImageSelectActivity.this, "照相失败", Toast.LENGTH_SHORT).show();
          e.printStackTrace();
        }

      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onClick(View view) {
    if(R.id.all_album_tv == view.getId()) {
      if(!showAlbumList) {
        showAlbumListView();
        albumListView.setAdapter(new AlbumListAdapter(this, MediaFileUtil.getThumbnailsPhotosInfo(this)));
      }else{
        hideAlbumListView();
      }
    }else if(R.id.empty_place == view.getId()) {
      hideAlbumListView();
    }
  }

  @Override
  public void reloadData() {
  }
  // ===========================================================
  // Methods
  // ===========================================================
  private void initContentView() {

    findViewById(R.id.empty_place).setOnClickListener(this);
    albumView = findViewById(R.id.albumView);
    albumListView = (ListView) findViewById(R.id.all_album_list);
    albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        AlbumInfo albumInfo = (AlbumInfo) adapterView.getItemAtPosition(position);
        allMedias.clear();
        allMedias = albumInfo.medias;
        imageGridAdapter.notifyDataSetChanged();
        /**
         * scroll to top
         */
        mImageGv.post(new Runnable() {
          @Override public void run() {
            mImageGv.setSelection(0);
          }
        });
        allImagesTv.setText(albumInfo.name);
        gsTitleView.setTitleText(albumInfo.name);
        hideAlbumListView();
      }
    });

    allImagesTv = (TextView) findViewById(R.id.all_album_tv);
    allImagesTv.setOnClickListener(this);

    mImageGv = (GridView) this.findViewById(R.id.album_pic_gridView);
    mImageGv.setAdapter(imageGridAdapter = new ImageGridAdapter(this));
    mImageGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onImageSelectItemClick(parent,view,position,id);
      }
    });

  }

  private void showAlbumListView() {
    showAlbumList = true;
    albumView.setVisibility(View.VISIBLE);
  }

  private void hideAlbumListView() {
    albumView.setVisibility(View.INVISIBLE);
    showAlbumList = false;
  }

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
  private class ImageGridAdapter extends BaseAdapter {

    public ImageGridAdapter(Context mContext) {
    }

    @Override
    public int getCount() {
      return allMedias.size();
    }

    @Override
    public Object getItem(int id) {
      return allMedias.get(id);
    }

    @Override
    public long getItemId(int id) {
      return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      return doGetViewWork(position, convertView, allMedias.get(position));
    }
  }
}
