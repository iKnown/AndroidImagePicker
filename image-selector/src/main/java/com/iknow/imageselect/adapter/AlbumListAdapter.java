package com.iknow.imageselect.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.iknow.imageselect.R;
import com.iknow.imageselect.model.AlbumInfo;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.DrawableUtil;
import com.iknow.imageselect.utils.ImageFilePathUtil;
import java.util.LinkedList;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月12日 3:26 PM
 * Description:
 */
public class AlbumListAdapter extends BaseAdapter{
  private LinkedList<AlbumInfo> adapterListData;
  private Context context;
  private String hasChooseItemName;
  public AlbumListAdapter(Context context,LinkedList<AlbumInfo> dataList,String itemName) {
    super();
    this.context = context;
    this.adapterListData = dataList;
    this.hasChooseItemName = itemName;
  }

  @Override public int getCount() {
    return adapterListData.size();
  }

  @Override public Object getItem(int id) {
    return adapterListData.get(id);
  }

  @Override public long getItemId(int id) {
    return id;
  }

  @Override public View getView(int position, View convertView, ViewGroup viewGroup) {
    ViewHolder h;
    AlbumInfo albumInfo = (AlbumInfo) getItem(position);
    if(null == convertView){
      convertView = LayoutInflater.from(context).inflate(R.layout.ablum_list_view_item, null);
      h = new ViewHolder();
      h.albumCover = (SimpleDraweeView) convertView.findViewById(R.id.album_cover);
      h.albumName = (TextView) convertView.findViewById(R.id.album_name);
      h.albumNumber = (TextView) convertView.findViewById(R.id.album_count);
      h.chooseIcon = (ImageView) convertView.findViewById(R.id.icon_item_choosen);
      convertView.setTag(h);
    }else{
      h = (ViewHolder) convertView.getTag();
    }

    MediaInfo imageInfo = albumInfo.medias.get(0);
    String path = imageInfo.fileName;
    if(!TextUtils.isEmpty(imageInfo.thumbPath)){
      path = imageInfo.thumbPath;
    }

    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ImageFilePathUtil.getImgUrl(path)))
        .setResizeOptions(new ResizeOptions(100, 100)).build();

    PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
        .setOldController(h.albumCover.getController())
        .setImageRequest(request)
        .build();
    h.albumCover.setController(controller);

    h.albumName.setText(albumInfo.name);
    h.albumNumber.setText(""+albumInfo.medias.size());
    if(hasChooseItemName.equals(albumInfo.name)) {
      h.chooseIcon.setImageDrawable(DrawableUtil.decodeFromVector(context, R.drawable.ic_radio_button_checked));
      h.chooseIcon.setVisibility(View.VISIBLE);
    }else
      h.chooseIcon.setVisibility(View.GONE);
    return convertView;
  }

  static class ViewHolder{
    SimpleDraweeView albumCover;
    TextView albumName,albumNumber;
    ImageView chooseIcon;
  }
}

