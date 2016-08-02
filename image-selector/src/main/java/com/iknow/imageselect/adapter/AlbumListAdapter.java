package com.iknow.imageselect.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iknow.imageselect.R;
import com.iknow.imageselect.display.DisplayOptions;
import com.iknow.imageselect.model.AlbumInfo;
import com.iknow.imageselect.model.MediaInfo;
import com.iknow.imageselect.utils.DrawableUtil;
import com.iknow.imageselect.utils.ImageFilePathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月12日 3:26 PM
 * Description:
 */
public class AlbumListAdapter extends BaseAdapter {
    private LinkedList<AlbumInfo> adapterListData;
    private Context context;
    private String hasChooseItemName;

    public AlbumListAdapter(Context context, LinkedList<AlbumInfo> dataList, String itemName) {
        super();
        this.context = context;
        this.adapterListData = dataList;
        this.hasChooseItemName = itemName;
    }

    @Override
    public int getCount() {
        return adapterListData.size();
    }

    @Override
    public Object getItem(int id) {
        return adapterListData.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder h;
        AlbumInfo albumInfo = (AlbumInfo) getItem(position);
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ablum_list_view_item, null);
            h = new ViewHolder();
            h.albumCover = (ImageView) convertView.findViewById(R.id.album_cover);
            h.albumName = (TextView) convertView.findViewById(R.id.album_name);
            h.albumNumber = (TextView) convertView.findViewById(R.id.album_count);
            h.chooseIcon = (ImageView) convertView.findViewById(R.id.icon_item_choosen);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        MediaInfo imageInfo = albumInfo.medias.get(0);
        String path = imageInfo.fileName;
        if (!TextUtils.isEmpty(imageInfo.thumbPath)) {
            path = imageInfo.thumbPath;
        }

        ImageLoader.getInstance().displayImage(path, h.albumCover, DisplayOptions.getCacheOptions());
        h.albumCover.setImageBitmap(BitmapFactory.decodeFile(ImageFilePathUtil.getImgUrl(path)));
        h.albumName.setText(albumInfo.name);
        h.albumNumber.setText("" + albumInfo.medias.size());
        if (hasChooseItemName.equals(albumInfo.name)) {
            h.chooseIcon.setImageDrawable(DrawableUtil.decodeFromVector(context, R.drawable.ic_radio_button_checked));
            h.chooseIcon.setVisibility(View.VISIBLE);
        } else
            h.chooseIcon.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        ImageView albumCover;
        TextView albumName, albumNumber;
        ImageView chooseIcon;
    }
}

