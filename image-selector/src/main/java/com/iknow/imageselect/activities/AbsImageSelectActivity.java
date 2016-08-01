package com.iknow.imageselect.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iknow.imageselect.ImageSelectContextHolder;
import com.iknow.imageselect.R;
import com.iknow.imageselect.adapter.AlbumListAdapter;
import com.iknow.imageselect.core.CoreActivity;
import com.iknow.imageselect.model.AlbumInfo;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.presenter.IImageChoosePresenter;
import com.iknow.imageselect.presenter.ImageChoosePresenterCompl;
import com.iknow.imageselect.utils.MediaFileUtil;
import com.iknow.imageselect.view.IImageChooseView;
import com.iknow.imageselect.widget.PicItemCheckedView;
import com.iknow.imageselect.widget.SpacesItemDecoration;
import com.iknow.imageselect.widget.TitleView;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Author: J.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月06日 3:16 PM
 * Description:
 */

public abstract class AbsImageSelectActivity extends CoreActivity implements IImageChooseView,
    View.OnClickListener {
  // ===========================================================
  // Constants
  // ===========================================================
  public static final int PHOTO_REQUEST_CAMERA = 0x007;
  public static final int MULTI_PIC_SELECT_REQUEST = 0x008;
  public static final int SINGLE_PIC_SELECT_REQUEST = 0x009;
  private static final String ALL_VIDEO = "所有视频";
  private static final String PIC_AND_VIDEO = "图片和视频";
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
  protected ArrayList<MediaInfo> allMedias = new ArrayList<>();
  protected ArrayList<MediaInfo> imageAndVideoAlbums = new ArrayList<>();
  protected ArrayList<MediaInfo> hasCheckedImages = new ArrayList<>();
  private AlbumInfo allAlbumInfo = new AlbumInfo();
  private AlbumInfo videoAlbumInfo = new AlbumInfo();
  private LinkedList<AlbumInfo> albumLinkedList = new LinkedList<>();
  protected IImageChoosePresenter imageChoosePresenter;
  private RecyclerAdapter imageGridAdapter;
  private RecyclerView recyclerView;

  protected abstract void initTitleView(TitleView titleView);
  protected abstract void initBottomView(View bottomView);
  protected abstract void onBindViewHolderToChild(MediaInfo model,ImageSelectViewHolder holder,int position);
  protected abstract View getRecyclerItemView(ViewGroup parentView, int position);
  protected abstract void onImageSelectItemClick(View view, int position);
  protected abstract void onImageItemClick(View view, int position);
  protected abstract void onCameraActivityResult(String path);

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
    allMedias = MediaFileUtil.getAllMediaFiles(ImageSelectContextHolder.getInstance().getContext());
    imageAndVideoAlbums = (ArrayList<MediaInfo>) allMedias.clone();
    videoAlbumInfo.medias = MediaFileUtil.getAllVideoFiles(ImageSelectContextHolder.getInstance().getContext());
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
          Toast.makeText(ImageSelectContextHolder.getInstance().getContext(), "照相失败", Toast.LENGTH_SHORT).show();
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
        try {
          albumLinkedList.clear();
          allAlbumInfo.medias = imageAndVideoAlbums;
          if(!allAlbumInfo.medias.isEmpty()){
            allAlbumInfo.name = PIC_AND_VIDEO;
            albumLinkedList.add(allAlbumInfo);
          }else {
            allAlbumInfo.medias = MediaFileUtil.getAllMediaFiles(ImageSelectContextHolder.getInstance().getContext());
            allAlbumInfo.name = PIC_AND_VIDEO;
            albumLinkedList.add(allAlbumInfo);
          }

          if(!videoAlbumInfo.medias.isEmpty()) {
            videoAlbumInfo.name = ALL_VIDEO;
            albumLinkedList.add(videoAlbumInfo);
          }else{
            videoAlbumInfo.medias = MediaFileUtil.getAllVideoFiles(ImageSelectContextHolder.getInstance().getContext());
            videoAlbumInfo.name = ALL_VIDEO;
            albumLinkedList.add(videoAlbumInfo);
          }

          albumLinkedList.addAll(MediaFileUtil.getThumbnailsPhotosInfo(this));
          albumListView.setAdapter(new AlbumListAdapter(this, albumLinkedList,allImagesTv.getText().toString()));
        }catch(Exception e) {
          e.printStackTrace();
        }
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    allMedias = null;
    imageAndVideoAlbums = null;
    albumLinkedList = null;
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
        allMedias = albumInfo.medias;
        recyclerView.setAdapter(imageGridAdapter = new RecyclerAdapter(albumInfo.medias));
        allImagesTv.setText(albumInfo.name);
        gsTitleView.setTitleText(albumInfo.name);
        hideAlbumListView();
      }
    });

    allImagesTv = (TextView) findViewById(R.id.all_album_tv);
    allImagesTv.setOnClickListener(this);

    recyclerView = (RecyclerView) this.findViewById(R.id.album_pic_recyclerview);
    GridLayoutManager manager = new GridLayoutManager(this, 3);
    recyclerView.addItemDecoration(new SpacesItemDecoration(5));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(imageGridAdapter = new RecyclerAdapter(allMedias));
    recyclerView.setLayoutManager(manager);
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

  public class RecyclerAdapter extends RecyclerView.Adapter<ImageSelectViewHolder> {

    private ArrayList<MediaInfo> albumMedias;

    public RecyclerAdapter(ArrayList<MediaInfo> allMedias) {
      this.albumMedias = allMedias;
    }

    @Override public ImageSelectViewHolder onCreateViewHolder(ViewGroup parent, int position) {
      View view = getRecyclerItemView(parent,position);
      return new ImageSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageSelectViewHolder holder, final int position) {
      MediaInfo mediaInfo = albumMedias.get(position);
      onBindViewHolderToChild(mediaInfo,holder,position);
    }

    @Override public int getItemCount() {
      return albumMedias.size();
    }

  }

  public class ImageSelectViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView picImageView;
    public View panelView;
    public ImageView videoIcon;

    public ImageSelectViewHolder(final View itemView) {
      super(itemView);
      if(itemView instanceof PicItemCheckedView){
        picImageView = ((PicItemCheckedView) itemView).getImageView();
        videoIcon = ((PicItemCheckedView) itemView).getVideoIcon();
        panelView = ((PicItemCheckedView) itemView).getSelectPanelView();
        panelView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View pView) {
            onImageSelectItemClick(itemView,getAdapterPosition());
          }
        });
        picImageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            onImageItemClick(view,getAdapterPosition());

          }
        });
      }
    }
  }
}
