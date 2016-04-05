package com.windward.www.casio_golf_viewer.casio.golf.entity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.windward.www.casio_golf_viewer.casio.golf.util.VideoUtils;
import com.windward.www.casio_golf_viewer.casio.golf.util.WWUitls;

import java.io.Serializable;

/**
 */
public class ListItemInfo implements Serializable {
    public static final long serialVersionUID = 9527L;
    private Context mContext;

    private String mFileName;

    private int mCaptureRate = 30;

    private String mFilePath;

    private Bitmap mThumbnail;

    private String mTime;

    private String mDay;


    private  boolean isShowVideo=true;


    public ListItemInfo(Context context,String path) {
        mContext=context;

        mFilePath = path;

        mThumbnail = createThumbnail();

        if(mFilePath.contains("/")) {
            String[] split = mFilePath.split("/");
            mFileName = split[split.length - 1];
        }

        mTime= VideoUtils.getVideoTime(mContext, path);
        //System.out.println("-----> path="+path+",mTime="+mTime);
        mDay= WWUitls.getDay(mTime);
    }

    public boolean isShowVideo() {
        return isShowVideo;
    }

    public void setIsShowVideo(boolean isShowVideo) {
        this.isShowVideo = isShowVideo;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDay() {
        return mDay;
    }

    public void setmDay(String mDay) {
        this.mDay = mDay;
    }


    public String getFilePath() {
        return mFilePath;
    }

    public String getFileName(){
        return mFileName;
    }

    public Bitmap getThumbnail() {

//        if(mThumbnail == null)
//            mThumbnail = createThumbnail();

        return mThumbnail;
    }

    public int getCaptureRate() {
        return mCaptureRate;
    }

    private Bitmap createThumbnail(){
        return ThumbnailUtils.createVideoThumbnail(mFilePath, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    @Override
    public String toString() {
        return "ListItemInfo{" +
                "mFileName='" + mFileName + '\'' +
                ", mCaptureRate=" + mCaptureRate +
                ", mFilePath='" + mFilePath + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mDay='" + mDay + '\'' +
                ", isShowVideo=" + isShowVideo +
                '}';
    }
}