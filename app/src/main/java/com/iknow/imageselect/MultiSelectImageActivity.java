package com.iknow.imageselect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.ImageFilePathUtil;
import com.iknow.imageselect.widget.PicItemCheckedView;
import com.iknow.imageselect.widget.TitleView;

/**
 * @Author: Jason.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月12日 5:02 PM
 * @Description:
 */
public class MultiSelectImageActivity extends AbsImageSelectActivity {

  // ===========================================================
  // Constants
  // ===========================================================

  // ===========================================================
  // Fields
  // ===========================================================
  private TextView mSendBtn,mPreviewBtn;
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
  protected void initTitleView(TitleView titleView) {
    mSendBtn = (TextView) titleView.getRBtnView();
    mSendBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View pView) {
        imageChoosePresenter.doSendAction();
      }
    });
  }

  @Override protected void initBottomView(View bottomView) {
    mPreviewBtn = (TextView) bottomView.findViewById(R.id.preview_btn);
    mPreviewBtn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View pView) {
        imageChoosePresenter.doPreview(hasCheckedImages);
      }
    });
  }

  @Override
  protected View doGetViewWork(int position,View convertView,MediaInfo imageInfo) {

    if (convertView == null) {
      convertView = new PicItemCheckedView(this);
    }

    try {
      PicItemCheckedView view = ((PicItemCheckedView) convertView);
      long picId = allMedias.get(position).fileId;
      if (picId < 0) {
        throw new RuntimeException("the pic id is not num");
      }

      final SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.getImageView();

      String path = imageInfo.fileName;

      if (!TextUtils.isEmpty(imageInfo.thumbPath)) {
        path = imageInfo.thumbPath;
      }

      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ImageFilePathUtil.getImgUrl(path)))
      .setResizeOptions(new ResizeOptions(100, 100)).build();

      PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
          .setOldController(simpleDraweeView.getController())
          .setImageRequest(request)
          .build();

      simpleDraweeView.setController(controller);

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
      item.setChecked(!isChecked);
      if(isChecked && hasCheckedImages.contains(allMedias.get(position)))
        hasCheckedImages.remove(allMedias.get(position));
      else if(!isChecked && !hasCheckedImages.contains(allMedias.get(position))){
        hasCheckedImages.add(allMedias.get(position));
      }

      if(hasCheckedImages.size() > 0 ) {
        mSendBtn.setTextAppearance(mContext,R.style.blue_text_18_style);
        mSendBtn.setEnabled(true);
      }else {
        mSendBtn.setEnabled(false);
        mSendBtn.setTextAppearance(mContext,R.style.gray_text_18_style);
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
    MultiSelectImageActivity.this.setResult(RESULT_OK, intent);
    MultiSelectImageActivity.this.finish();
  }

  // ===========================================================
  // Methods
  // ===========================================================

  // ===========================================================
  // Inner and Anonymous Classes
  // ===========================================================
}
