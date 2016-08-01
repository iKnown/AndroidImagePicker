package com.iknow.imageselect.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iknow.imageselect.R;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.ImageFilePathUtil;
import com.iknow.imageselect.widget.PicItemCheckedView;
import com.iknow.imageselect.widget.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月12日 5:02 PM
 * Description:
 */
public class MultiSelectImageActivity extends AbsImageSelectActivity {

    private TextView mSendBtn, mPreviewBtn;

    @Override
    protected void initTitleView(TitleView titleView) {
        mSendBtn = (TextView) titleView.getRBtnView();
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                imageChoosePresenter.doSendAction();
            }
        });
    }

    @Override
    protected void initBottomView(View bottomView) {
        mPreviewBtn = (TextView) bottomView.findViewById(R.id.preview_btn);
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                imageChoosePresenter.doPreview(hasCheckedImages);
            }
        });
    }

    @Override
    protected void onBindViewHolderToChild(MediaInfo model, ImageSelectViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(ImageFilePathUtil.getImgUrl(model.fileName), holder.picImageView);
        holder.videoIcon.setVisibility(model.mediaType == 3 ? View.VISIBLE : View.GONE);
    }

    @Override
    public View getRecyclerItemView(ViewGroup parentView, int position) {
        return new PicItemCheckedView(this);
    }


    @Override
    protected void onImageSelectItemClick(View view, int position) {
        if (view instanceof PicItemCheckedView) {
            PicItemCheckedView item = (PicItemCheckedView) view;
            boolean isChecked = item.isChecked();
            item.setChecked(!isChecked);
            if (isChecked && hasCheckedImages.contains(allMedias.get(position)))
                hasCheckedImages.remove(allMedias.get(position));
            else if (!isChecked && !hasCheckedImages.contains(allMedias.get(position))) {
                hasCheckedImages.add(allMedias.get(position));
            }

            if (hasCheckedImages.size() > 0) {
                mSendBtn.setTextAppearance(mContext, R.style.text_16_ffffff);
                mSendBtn.setEnabled(true);
                mPreviewBtn.setEnabled(true);
                mPreviewBtn.setText("预览" + "(" + hasCheckedImages.size() + ")");
                mPreviewBtn.setTextColor(Color.parseColor("#ffffff"));
            } else {
                mSendBtn.setEnabled(false);
                mPreviewBtn.setEnabled(false);
                mPreviewBtn.setText("预览");
                mSendBtn.setTextAppearance(mContext, R.style.gray_text_16);
                mPreviewBtn.setTextAppearance(mContext, R.style.gray_text_16);
            }

        }
    }

    @Override
    protected void onImageItemClick(View view, int position) {
        BrowseDetailActivity.goToBrowseDetailActivity(this, allMedias, position);
    }

    @Override
    protected void onCameraActivityResult(String path) {
        Bundle bd = new Bundle();
        hasCheckedImages.clear();
        hasCheckedImages.add(MediaInfo.buildOneImage(path));
        bd.putSerializable("ImageData", hasCheckedImages);
        Intent intent = new Intent();
        intent.putExtras(bd);
        MultiSelectImageActivity.this.setResult(RESULT_OK, intent);
        MultiSelectImageActivity.this.finish();
    }
}
