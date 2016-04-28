package com.iknow.imageselect.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import com.iknow.imageselect.model.AlbumInfo;
import com.iknow.imageselect.model.MediaInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @Author: J.Chou
 * @Email: who_know_me@163.com
 * @Created: 2016年04月06日 4:24 PM
 * @Description:
 */

public class MediaFileUtil {
    /**
     * 获取图库专辑封面相关图片
     * @param mContext
     * @return
     */
    public static LinkedList<AlbumInfo> getThumbnailsPhotosInfo(Context mContext) {

        LinkedList<AlbumInfo> bitmaps = new LinkedList<AlbumInfo>();
        Cursor cursor = null;

        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
            if (cursor == null) {
                return bitmaps;
            }
        } catch (Exception err) {
            if (cursor != null)
                cursor.close();
            return bitmaps;
        }

        AlbumInfo albumInfo = null;

        HashMap<String, LinkedList<MediaInfo>> albums = getAlbumsInfo(mContext, cursor);
        cursor.close();

        String key = null;
        for (Iterator<?> it = albums.entrySet().iterator(); it.hasNext();) {
            Map.Entry e = (Map.Entry) it.next();
            LinkedList<MediaInfo> album = (LinkedList<MediaInfo>) e.getValue();

            if (album != null && album.size() > 0) {
              albumInfo = new AlbumInfo();
              key = (String) e.getKey();
              albumInfo.name = key.substring(key.lastIndexOf("/") + 1);

              albumInfo.fileId = album.get(0).fileId;
              albumInfo.filePath = album.get(0).filePath;
              ArrayList<MediaInfo> list = new ArrayList<MediaInfo>();
              for (int i = album.size() - 1; i >= 0; i--) {
                  list.add(album.get(i));
              }
              albumInfo.images = list;

              if (albumInfo.filePath.endsWith("DCIM/Camera")) {
                albumInfo.name = "相册";
                  bitmaps.addFirst(albumInfo);
              } else {
                  bitmaps.addLast(albumInfo);
              }

            }
        }

        return bitmaps;
    }

    public static HashMap<String, LinkedList<MediaInfo>> getAlbumsInfo(Context mContext,Cursor cursor) {
        HashMap<String, LinkedList<MediaInfo>> albumsInfos = new HashMap<String, LinkedList<MediaInfo>>();
        String _path = MediaStore.Images.Media.DATA;
        String _album = MediaStore.Images.Media.DEFAULT_SORT_ORDER;
        String _time = MediaStore.Images.Media.DATE_ADDED;
        String _rotate = MediaStore.Images.Media.ORIENTATION;

        HashMap<Integer, String> thumbInfos = getThumbImgInfo(mContext);

        MediaInfo imageInfo = null;
        File file = null;
        if (cursor.moveToFirst()) {
            do {
                int _id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                String path = cursor.getString(cursor.getColumnIndex(_path));
                String album = cursor.getString(cursor.getColumnIndex(_album));
                String time = cursor.getString(cursor.getColumnIndex(_time));
                int rotate = cursor.getInt(cursor.getColumnIndex(_rotate));

                file = new File(path);
                if (!file.exists() || file.length() == 0) {
                    continue;
                }

                String subPath = path.substring(0, path.lastIndexOf("/"));

                if (albumsInfos.containsKey(getAlbumKey(subPath, album))) {
                    LinkedList<MediaInfo> albums = albumsInfos
                            .remove(getAlbumKey(subPath, album));
                    imageInfo = new MediaInfo();
                    imageInfo.fileId = _id;
                    imageInfo.filePath = subPath;
                    imageInfo.fileName = path;
                    imageInfo.createTime = time;
                    imageInfo.rotate = rotate;
                    if(thumbInfos.containsKey(_id)){
                        imageInfo.thumbPath = thumbInfos.get(_id);
                    }
                    albums.add(imageInfo);
                    albumsInfos.put(getAlbumKey(subPath, album), albums);
                } else {
                    LinkedList<MediaInfo> albums = new LinkedList<MediaInfo>();
                    imageInfo = new MediaInfo();
                    imageInfo.fileId = _id;
                    imageInfo.filePath = subPath;
                    imageInfo.fileName = path;
                    imageInfo.createTime = time;
                    imageInfo.rotate = rotate;
                    if(thumbInfos.containsKey(_id)){
                        imageInfo.thumbPath = thumbInfos.get(_id);
                    }
                    albums.add(imageInfo);
                    albumsInfos.put(getAlbumKey(subPath, album), albums);
                }
            } while (cursor.moveToNext());
        }
        return albumsInfos;
    }

    private static String getAlbumKey(String path, String name) {
        return path + "/" + name;
    }

    public static int getPicId(String pathAll) {
        String strId = pathAll.split("&")[0];
        int id = -1;
        try {
            id = Integer.valueOf(strId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static Bitmap getThumbImage(Context mContext, int id) {
        return MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), id,
                MediaStore.Images.Thumbnails.MICRO_KIND, new BitmapFactory.Options());

    }

    public static String getFileName(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static Bitmap getThumbImage(Context mContext, int id, BitmapFactory.Options options) {
        return MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), id,
                MediaStore.Images.Thumbnails.MICRO_KIND, options);
    }

    private static HashMap<Integer, String> getThumbImgInfo(Context mContext){
        HashMap<Integer, String> thumbMap = new HashMap<Integer, String>();

        if(mContext == null){
            return thumbMap;
        }

        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA };
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                do {
                    // Get the field values
                    image_id = cursor.getInt(image_idColumn);
                    image_path = cursor.getString(dataColumn);

                    if(!new File(image_path).exists()){
                        continue;
                    }

                    if(!TextUtils.isEmpty(image_path)){
                        thumbMap.put(image_id, image_path);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }

        return thumbMap;
    }

    /**
     * 获取缩略图路径通过图片的id
     * @param mContext
     * @param id
     * @return
     */
    public static String getThumbPathById(Context mContext, int id) {

        if(mContext == null){
            return null;
        }

        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA };

        String whereClause = MediaStore.Images.Thumbnails.IMAGE_ID + " = '"+ id + "'";

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, whereClause, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null)
                cursor.close();
            return null;
        }

        String thumbPath = "";
        try {
            if (cursor.moveToFirst()) {
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                thumbPath =  cursor.getString(dataColumn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(cursor != null){
                cursor.close();
            }
        }

        return thumbPath;
    }

    public static Bitmap getThumbBitmap(Context mContext, String fileName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // select condition.
        String whereClause = MediaStore.Images.Media.DISPLAY_NAME + " = '"
                + fileName + "'";

        // colection of results.
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, whereClause,
                null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null)
                cursor.close();
            return null;
        }
        cursor.moveToFirst();
        // image id in image table.
        String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        cursor.close();
        if (videoId == null) {
            return null;
        }
        long videoIdLong = Long.parseLong(videoId);
        // via imageid get the bimap type thumbnail in thumbnail table.
        bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                mContext.getContentResolver(), videoIdLong,
                MediaStore.Images.Thumbnails.MINI_KIND, options);
        return bitmap;
    }

    /***
     * 获取本机相册所有的图片
     * @param mContext
     */
    public static ArrayList<MediaInfo> getAllImageFiles(Context mContext){
        MediaInfo info;
        ArrayList<MediaInfo> _images = new ArrayList<>();
        try {
            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
            Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                info = new MediaInfo();
                info.fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                info.name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                info.createTime = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                info.fileId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                info.lat = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                info.lon = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                if(!TextUtils.isEmpty(info.lat)){
                    Log.e("jason","info.lat ="+info.lat);
                }
                if(!TextUtils.isEmpty(info.lon)){
                    Log.e("jason","info.lon ="+info.lon);
                }
                _images.add(info);
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return _images;

    }

    public static ArrayList<MediaInfo> getAllVideoFiles(Context mContext){
        MediaInfo mediaInfo;
        ArrayList<MediaInfo> videos = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        try {
            Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,
                    null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            while (cursor.moveToNext()) {
                mediaInfo = new MediaInfo();
                mediaInfo.fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                mediaInfo.createTime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                mediaInfo.name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                videos.add(mediaInfo);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videos;
    }
}
