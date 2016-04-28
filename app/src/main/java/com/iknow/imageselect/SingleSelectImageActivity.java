package com.iknow.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.widget.PicItemCheckedView;
import com.iknow.imageselect.widget.TitleView;
import java.util.ArrayList;

/**
 * @Author: Jason.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月12日 5:03 PM
 * @Description:
 */
public class SingleSelectImageActivity extends AbsImageSelectActivity {

  // ===========================================================
  // Constants
  // ===========================================================

  // ===========================================================
  // Fields
  // ===========================================================
  private ArrayList<PicItemCheckedView> hasCheckedView = new ArrayList<>();
  private View mCameraIv;
  private TextView mPreviewBtn;
  // ===========================================================
  // Constructors
  // ===========================================================
  public static void startActivityForResult(Activity context){
    context.startActivityForResult(new Intent(context,SingleSelectImageActivity.class),
        SINGLE_PIC_SELECT_REQUEST);
  }
  // ===========================================================
  // Getter & Setter
  // ===========================================================

  // ===========================================================
  // Methods for/from SuperClass/Interfaces
  // ===========================================================
  @Override
  protected void initTitleView(TitleView titleView) {
    this.mCameraIv = titleView.getRBtnView();
    titleView.setOnRightBtnClickListener(new TitleView.OnRightBtnClickListener() {
      @Override public void onRBtnClick(View v) {
        mTakeCameraImagePath = imageChoosePresenter.takePhotos();
      }
    });
    titleView.setOnLeftBtnClickListener(new TitleView.OnLeftBtnClickListener() {
      @Override public void onRBtnClick(View v) {
        imageChoosePresenter.doCancel();
      }
    });

  }

  @Override protected void initBottomView(View bottomView) {
    mPreviewBtn = (TextView) bottomView.findViewById(R.id.preview_btn);
    mPreviewBtn.setTextAppearance(mContext,R.style.gray_text_18_style);
    mPreviewBtn.setEnabled(false);
    mPreviewBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View pView) {
        Bundle bd = new Bundle();
        imageChoosePresenter.doPreview(hasCheckedImages);
      }
    });
  }

  @Override
  protected View doGetViewWork(int position,View convertView,MediaInfo imageInfo) {
    if (convertView == null) {
      convertView = new PicItemCheckedView(this,true);
    }

    try {
      PicItemCheckedView view = ((PicItemCheckedView) convertView);
      long picId = allMedias.get(position).fileId;
      if (picId < 0) {
        throw new RuntimeException("the pic id is not num");
      }

      final ImageView iv = view.getImageView();
      iv.setScaleType(ImageView.ScaleType.FIT_XY);

      String path = imageInfo.fileName;

      if (!TextUtils.isEmpty(imageInfo.thumbPath)) {
        path = imageInfo.thumbPath;
      }

      if (hasCheckedImages.contains(allMedias.get(position))) {
        view.setChecked(true);
      } else {
        view.setChecked(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return convertView;
  }

  @Override
  protected void onImageSelectItemClick(AdapterView<?> parent, View view, int position,long id) {
    if(view instanceof PicItemCheckedView){

      PicItemCheckedView item = (PicItemCheckedView)view;
      boolean isChecked = item.isChecked();

      if(isChecked) {
        hasCheckedImages.remove(allMedias.get(position));
        item.setChecked(false);
      }
      else{
        resetImagesChecked();
        hasCheckedView.add(item);
        hasCheckedImages.add(allMedias.get(position));
        item.setChecked(true);
      }

      if(hasCheckedImages.size() > 0 ) {
        mPreviewBtn.setTextAppearance(mContext,R.style.blue_text_18_style);
        mPreviewBtn.setEnabled(true);
      }else {
        mPreviewBtn.setTextAppearance(mContext,R.style.gray_text_18_style);
        mPreviewBtn.setEnabled(false);
      }
    }
  }

  @Override protected void onCameraActivityResult(String path) {
    Bundle bd = new Bundle();
    hasCheckedImages.clear();
    hasCheckedImages.add(MediaInfo.buildOneImage(path));
    bd.putSerializable("ImageData",hasCheckedImages);
    Intent intent = new Intent();
    intent.putExtras(bd);
    SingleSelectImageActivity.this.setResult(RESULT_OK, intent);
    SingleSelectImageActivity.this.finish();
  }

  // ===========================================================
  // Methods
  // ===========================================================
  private void resetImagesChecked(){
    if(hasCheckedView.size()>0){
        hasCheckedView.get(0).setChecked(false);
        hasCheckedView.clear();
        hasCheckedImages.clear();
    }
  }
  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
}
