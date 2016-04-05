package com.windward.www.casio_golf_viewer.casio.golf.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import com.windward.www.casio_golf_viewer.R;
import com.windward.www.casio_golf_viewer.casio.golf.adapter.VideoViewPagerAdapter;
import com.windward.www.casio_golf_viewer.casio.golf.fragment.PlayerFirstFragment;
import com.windward.www.casio_golf_viewer.casio.golf.player.MultiPlayerController;
import com.windward.www.casio_golf_viewer.casio.golf.player.PlayerInfo;
import com.windward.www.casio_golf_viewer.casio.golf.player.SinglePlayerController;
import com.windward.www.casio_golf_viewer.casio.golf.player.VideoInfo;
import com.windward.www.casio_golf_viewer.casio.golf.util.ScreenUtil;
import java.util.ArrayList;

public class PlayOneVideoActivity extends BaseActivity {
    private Intent mIntent;
    private RelativeLayout mBackRelativeLayout;
    private RelativeLayout mOperateRelativeLayout;
    private ImageView mEditImageView;
    private AlertDialog mOperateDialog;
    private AlertDialog mRevolutionDialog;
    private Dialog mVideoInfoDialog;
    private Dialog mGuideDialog;
    private  Dialog mTipsDialog;
    private ViewPager mViewPager;
    private VideoViewPagerAdapter mViewPagerAdapter;
    private ImageView[] dotImageViews;
    private PageChangeListenerImpl mPageChangeListenerImpl;
    private LinearLayout mDotsLinearLayout;
    private ImageView mPlayImageView;


    private ArrayList<PlayerFirstFragment> mPlayerFirstFragmentList;          // プレイヤーリスト
    private ArrayList<String> mVideoPathsArrayList;                            // 再生リスト
    private SinglePlayerController mSinglePlayerController = null;  // シングルプレイヤーコントローラのインスタンス
    private MultiPlayerController mMultiPlayerController = null;    // マルチプレイヤーコントローラのインスタンス
    private ToggleButton mSyncBtn;                                  // 同期ボタンのインスタンス
    private int mNextId;                                             // 次のPlayerID
    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
    }

    @Override
    protected void initView() {
        mContext = this;
        mNextId = 0;

        mBackRelativeLayout=(RelativeLayout)findViewById(R.id.backRelativeLayout);
        mEditImageView=(ImageView)findViewById(R.id.editImageView);
        mOperateRelativeLayout =(RelativeLayout)findViewById(R.id.operateRelativeLayout);
        mPlayImageView=(ImageView)findViewById(R.id.playImageView);

        //ファイル選択アクティビティから画面モードと再生リストを取得
        Bundle bundle = getIntent().getExtras();
        //保存了待播放视频的路径。一个或者两个
        mVideoPathsArrayList = bundle.getStringArrayList("key_playlist");

        //プレイヤーリスト作成
        //有几个待播放视频就生成几个PlayerFragment！！！且将这些PlayerFragment保存到mPlayerFragmentList中
        makePlayerFragmentList();

        //调用先前生成的PlayerFragment播放一个或者两个视频 注意使用的是PlayerFragment！！！！

        //播放两个视频
        if (mVideoPathsArrayList.size() == 2) {
            //2画面を起動
            initMultiPlayerView();
            return;
        }

        //1画面で起動
        //播放一个视频
        initSinglePlayerView();


        //ファイルオープンとタイムマネージャの設定
        //利用PlayerFragment播放视频
        //每个PlayerFragment与一个路径关联起来
        initPlayerFragments();
    }

    @Override
    protected void initListener() {
        addListener(mBackRelativeLayout);
        addListener(mOperateRelativeLayout);
        addListener(mEditImageView);
        addListener(mPlayImageView);
    }

    @Override
    protected void initData() {

    }




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.backRelativeLayout:
                finish();
                break;
            case R.id.editImageView:
                showTipsDialog();
                break;
            case R.id.operateRelativeLayout:
                showOperateDialog();
                break;
            case R.id.move_cut_LinearLayout:
                mIntent=new Intent(mContext,CutVideoActivity.class);
                startActivity(mIntent);
                break;
            case R.id.move_combine_LinearLayout:
                System.out.println("-----> PPT上未说明跳转");
                break;
            case R.id.move_reflect_LinearLayout:
                System.out.println("-----> 视频翻转,不用页面跳转.");
                if(null!=mOperateDialog&&mOperateDialog.isShowing()){
                    mOperateDialog.dismiss();
                }
                System.out.println("-----> 在此测试教程.以后请注释下行代码");
                showGuideDialog();
                break;
            case R.id.move_revolution_LinearLayout:
                System.out.println("-----> 视频旋转,不用页面跳转.");
                if(null!=mOperateDialog&&mOperateDialog.isShowing()){
                    mOperateDialog.dismiss();
                }
                showRevolutionDialog();
                break;
            case R.id.move_movtx_LinearLayout:
                System.out.println("-----> 显示视频信息,不用页面跳转.");
                if(null!=mOperateDialog&&mOperateDialog.isShowing()){
                    mOperateDialog.dismiss();
                }
                showVideoInfoDialog();
                break;
            case R.id.revolution_TextView_90:
                System.out.println("-----> 旋转90");
                if(null!=mRevolutionDialog&&mRevolutionDialog.isShowing()){
                    mRevolutionDialog.dismiss();
                }
                break;
            case R.id.revolution_TextView_180:
                System.out.println("-----> 旋转180");
                if(null!=mRevolutionDialog&&mRevolutionDialog.isShowing()){
                    mRevolutionDialog.dismiss();
                }
                break;
            case R.id.revolution_TextView_270:
                System.out.println("-----> 旋转270");
                if(null!=mRevolutionDialog&&mRevolutionDialog.isShowing()){
                    mRevolutionDialog.dismiss();
                }
                break;
            case R.id.closeVideoInfoRelativeLayout:
                System.out.println("-----> 关闭对话框");
                if(null!=mVideoInfoDialog&&mVideoInfoDialog.isShowing()){
                    mVideoInfoDialog.dismiss();
                }
                break;
            case R.id.okTextView:
                if(null!=mTipsDialog&&mTipsDialog.isShowing()){
                    mTipsDialog.dismiss();
                }
                System.out.println("-----> 跳转到 ppt e14 最复杂的画面");
                break;
            case R.id.cancelTextView:
                if(null!=mTipsDialog&&mTipsDialog.isShowing()){
                    mTipsDialog.dismiss();
                }
                break;
            case R.id.playImageView:
                //播放视频
                break;
        }
    }



    //显示操作(剪切,翻转,旋转等)对话框
    private void showOperateDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_operate, null);
        ScreenUtil.initScale(dialogView);
        mOperateDialog= new AlertDialog.Builder(mContext,R.style.dialog).create();
        mOperateDialog.show();
        Window window = mOperateDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //去掉Dialog本身的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = mOperateDialog.getWindow().getAttributes();
        //设置宽度为屏幕宽度
        layoutParams.width =ScreenUtil.getScreenWidth(mContext);
        mOperateDialog.getWindow().setAttributes(layoutParams);
        mOperateDialog.setContentView(dialogView);
        initOperateDialogItems(dialogView);
    }

    private void initOperateDialogItems(View dialogView){
        addListener(dialogView.findViewById(R.id.move_revolution_LinearLayout));
        addListener(dialogView.findViewById(R.id.move_combine_LinearLayout));
        addListener(dialogView.findViewById(R.id.move_cut_LinearLayout));
        addListener(dialogView.findViewById(R.id.move_movtx_LinearLayout));
        addListener(dialogView.findViewById(R.id.move_reflect_LinearLayout));
    }

    //显示视频旋转(90度,180度,270度)对话框
    private void showRevolutionDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_revolution, null);
        ScreenUtil.initScale(dialogView);
        mRevolutionDialog= new AlertDialog.Builder(mContext,R.style.dialog).create();
        mRevolutionDialog.show();
        Window window = mRevolutionDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //去掉Dialog本身的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = mRevolutionDialog.getWindow().getAttributes();
        //设置宽度为屏幕宽度
        layoutParams.width =ScreenUtil.getScreenWidth(mContext);
        mRevolutionDialog.getWindow().setAttributes(layoutParams);
        mRevolutionDialog.setContentView(dialogView);
        initRevolutionDialogItems(dialogView);
    }

    private void initRevolutionDialogItems(View dialogView){
        addListener(dialogView.findViewById(R.id.revolution_TextView_90));
        addListener(dialogView.findViewById(R.id.revolution_TextView_180));
        addListener(dialogView.findViewById(R.id.revolution_TextView_270));
    }

    //显示Video信息
    private void showVideoInfoDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_video_info, null);
        ScreenUtil.initScale(dialogView);
        mVideoInfoDialog= new Dialog(mContext,R.style.dialog);
        mVideoInfoDialog.setContentView(dialogView);
        Window dialogWindow = mVideoInfoDialog.getWindow();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.47);
        layoutParams.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.7);
        dialogWindow.setAttributes(layoutParams);
        mVideoInfoDialog.show();
        initVideoInfoDialog(dialogView);
    }

    private void initVideoInfoDialog(View dialogView){
        addListener(dialogView.findViewById(R.id.closeVideoInfoRelativeLayout));
    }


    //提醒用户是否要编辑视频
    private void showTipsDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_edit_video_tips, null);
        ScreenUtil.initScale(dialogView);
        mTipsDialog= new Dialog(mContext,R.style.dialog);
        mTipsDialog.setContentView(dialogView);
        Window dialogWindow = mTipsDialog.getWindow();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.52);
        layoutParams.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.9);
        dialogWindow.setAttributes(layoutParams);
        mTipsDialog.show();
        initTipsDialog(dialogView);
    }

    private void initTipsDialog(View dialogView){
        addListener(dialogView.findViewById(R.id.okTextView));
        addListener(dialogView.findViewById(R.id.cancelTextView));
    }



    //--------------> 以下代码与教程相关
    //显示ViewPager的Dialog

    //注意ViewPager第二页展示的图片是错误的,需要重新切图！！！！！
    //即view_video_guide_b中的img_g是不对的
    private void showGuideDialog(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_video_guide, null);
        ScreenUtil.initScale(dialogView);
        mGuideDialog= new Dialog(mContext,R.style.dialog);
        mGuideDialog.setContentView(dialogView);
        Window dialogWindow = mGuideDialog.getWindow();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = (int) (ScreenUtil.getScreenHeight(mContext) * 0.47);
        layoutParams.width = (int) (ScreenUtil.getScreenWidth(mContext) * 0.8);
        dialogWindow.setAttributes(layoutParams);
        mGuideDialog.show();
        initViewPager(dialogView);
    }

    private void initViewPager(View dialogView){
        mViewPager = (ViewPager)dialogView.findViewById(R.id.guide_viewpager);
        mDotsLinearLayout = (LinearLayout)dialogView.findViewById(R.id.dotsLinearLayout);
        mViewPagerAdapter = new VideoViewPagerAdapter(mContext,mGuideDialog);
        mPageChangeListenerImpl = new PageChangeListenerImpl();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(mPageChangeListenerImpl);
        initDots();
    }

    //初始化小圆点
    private void initDots() {
        dotImageViews = new ImageView[mViewPagerAdapter.getCount()];
        for (int i = 0; i < dotImageViews.length; i++) {
            LinearLayout layout = new LinearLayout(mContext);
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ScreenUtil.getScalePxValue(24), ScreenUtil.getScalePxValue(24)));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.guide_dot_white);
            } else {
                layout.setPadding(ScreenUtil.getScalePxValue(16), 0, 0, 0);
                imageView.setBackgroundResource(R.drawable.guide_dot_black);
            }
            dotImageViews[i] = imageView;
            layout.addView(imageView);
            mDotsLinearLayout.addView(layout);
        }
    }

    private class PageChangeListenerImpl implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < dotImageViews.length; i++) {
                dotImageViews[arg0].setBackgroundResource(R.drawable.guide_dot_white);
                if (arg0 != i) {
                    dotImageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }
    //--------------> 以上代码与教程相关











    @Override
    protected void onDestroy() {
        super.onDestroy();


        releaseTimeManager();
    }

    /**
     * PlayerFragmentのリストを作成
     * 　PlayList数分のPlayerを生成しリストに保持
     *
     * 有几个待播放视频就生成几个PlayerFragment！！！！！！！
     * 且将这些PlayerFragment保存到mPlayerFragmentList中
     */
    private void makePlayerFragmentList(){

        mPlayerFirstFragmentList = new ArrayList<PlayerFirstFragment>(); //プレイヤーリストを作成

        for (int i=0; i < mVideoPathsArrayList.size(); i++) {
            PlayerFirstFragment player = new PlayerFirstFragment();

            //PlayerIDを設定する
            Bundle bundle = new Bundle();
            bundle.putInt("key_PlayerId", generatePlayerId());
            player.setArguments(bundle);

            mPlayerFirstFragmentList.add(player);
        }
    }
    /**
     * PlayerIdの生成
     * @return PlayerID
     */
    private int generatePlayerId(){

        int playerId = mNextId;
        mNextId++;

        return playerId;
    }






    /**
     * 1画面時の表示面の初期化
     *  播放一个视频
     *  注意使用的是PlayerFragment！！！！
     */
    private void initSinglePlayerView() {
        //setContentView(R.layout.activity_single_player);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String key = "player" + Integer.toString(0);

        //PlayerBaseActivityにPlayerFragmentを追加
        if (manager.findFragmentByTag(key) == null) {
            transaction.add(R.id.SinglePlayerContainer, mPlayerFirstFragmentList.get(0), key);
            transaction.commit();
        }
    }

    /**
     * 2画面時の表示面の初期化
     */
    private void initMultiPlayerView()
    {
        setContentView(R.layout.activity_multi_player);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String key0 = "player" + Integer.toString(0);
        String key1 = "player" + Integer.toString(1);

        //PlayerBaseActivityにPlayerFragmentを追加
        if (manager.findFragmentByTag(key0) == null) {
            transaction.add(R.id.MultiPlayerContainer1, mPlayerFirstFragmentList.get(0), key0);
        }
        if (manager.findFragmentByTag(key1) == null) {
            transaction.add(R.id.MultiPlayerContainer2, mPlayerFirstFragmentList.get(1), key1);
        }
        transaction.commit();

        //同期ボタンの生成とボタンの内容を記述
        mSyncBtn = (ToggleButton)findViewById((R.id.MultiPlayerSyncButton));
        mSyncBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //同期処理
                    mMultiPlayerController.createSyncGroup(getSyncPlayerInfoList());
                } else {
                    //同期解除処理
                    mMultiPlayerController.createSyncGroup(getNonSyncPlayerInfoList());
                }
            }
        });
    }

    /**
     * PlayerFragmentの初期化を行う
     * 　全ての動画のファイルオープンとTimeManagerの初期設定を行う
     *
     * 　PlayerFragmentのファイルオープンの完了はPlayerBaseActivityのonCreateとは非同期であるため
     * 　別スレッドにて行っている
     *
     *   利用PlayerFragment播放视频,每个PlayerFragment与一个路径关联起来
     */
    private void initPlayerFragments() {

        //別スレッド生成
        Thread openthread = new Thread(new Runnable() {

            @Override
            public void run() {

                /**
                 * ①各PlayerFragmentのファイルオープン
                 * ②タイムマネージャの設定
                 */

                //①各PlayerFragmentのファイルオープン
                for (int i = 0; i < mVideoPathsArrayList.size(); i++) {
                    PlayerFirstFragment player = mPlayerFirstFragmentList.get(i);
                    String videoPath = mVideoPathsArrayList.get(player.getPlayerId());


                    //ファイルが開けるまでループ
                    while (true) {
                        int status = player.openFile(videoPath);

                        if(status == 0) {
                            //Log.d(TAG,"ファイルオープン成功");
                            break;
                        }else if(status == -1) {
                            //Log.d(TAG,"描画ライブラリ準備完了待ち");
                            //何もしない
                        }else{
                            //Log.d(TAG,"ファイルオープン失敗");
                            return;
                        }

                        try {
                            Thread.sleep(5);//5ms待ち
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }

                //②タイムマネージャの設定
                initTimeManager();
            }
        });

        //スレッド開始
        openthread.start();
    }

    /**
     * タイムマネージャーの設定
     * 设置时间管理
     */
    private void initTimeManager() {

        //一画面の場合
        if (mPlayerFirstFragmentList.size() == 1) {

            PlayerFirstFragment player = mPlayerFirstFragmentList.get(0);

            //PlayerInfoの生成
            //videoInfoの取得はPlayerFragmentのファイルオープン後でないとできないことに注意
            VideoInfo videoInfo = player.getVideoInfo();
            PlayerInfo playerInfo = new PlayerInfo(player.getPlayerId(), player.getPresentationTimeUs(), videoInfo.getCaptureRate(), videoInfo.getDurationUs());

            if (mSinglePlayerController == null)
                mSinglePlayerController = new SinglePlayerController();

            mSinglePlayerController.createTimeManager(playerInfo);
            player.setTimeManager(mSinglePlayerController.getTimeManager());

        } else {//複数画面の場合

            if (mMultiPlayerController == null) {
                mMultiPlayerController = new MultiPlayerController(mContext);
            }

            //同期状態の設定
            if (mSyncBtn.isChecked()) {
                //非同期設定
                mMultiPlayerController.createSyncGroup(getSyncPlayerInfoList());
            } else {
                //同期解除設定
                mMultiPlayerController.createSyncGroup(getNonSyncPlayerInfoList());
            }

            for (int i = 0; i < mPlayerFirstFragmentList.size(); i++) {

                PlayerFirstFragment player = mPlayerFirstFragmentList.get(i);

                player.setTimeManager(mMultiPlayerController.getTimeManager());
                player.setInstructionSyncController(mMultiPlayerController.getInstructionSyncController());
            }
        }
    }

    /**
     * タイムマネージャの解放
     *  タイムマネージャを利用しない場合、必ず必要
     */
    private void releaseTimeManager(){

        if (mPlayerFirstFragmentList.size() == 1) {
            if(mSinglePlayerController != null)
                mSinglePlayerController.finish();
        } else {
            if(mMultiPlayerController != null)
                mMultiPlayerController.finish();
        }
    }

    /**
     * 非同期時のプレイヤー情報配列作成関数
     * @return 非同期時のプレイヤー情報配列
     */
    private ArrayList<ArrayList<PlayerInfo>> getNonSyncPlayerInfoList(){

        ArrayList<ArrayList<PlayerInfo>> syncGroupList = new ArrayList<ArrayList<PlayerInfo>>();

        for(int i=0; i< mPlayerFirstFragmentList.size();i++){
            ArrayList<PlayerInfo> syncGroup = new ArrayList<PlayerInfo>();
            PlayerFirstFragment player = mPlayerFirstFragmentList.get(i);
            VideoInfo videoInfo = player.getVideoInfo();
            if(videoInfo != null){
                PlayerInfo playerInfo = new PlayerInfo(player.getPlayerId(), player.getPresentationTimeUs(), videoInfo.getCaptureRate(),videoInfo.getDurationUs());
                syncGroup.add(playerInfo);
                syncGroupList.add(syncGroup);
            }
        }
        return syncGroupList;
    }

    /**
     * 同期時のプレイヤー情報配列作成関数
     * @return 同期時のプレイヤー情報配列
     */
    private ArrayList<ArrayList<PlayerInfo>> getSyncPlayerInfoList() {

        ArrayList<ArrayList<PlayerInfo>> syncGroupList = new ArrayList<ArrayList<PlayerInfo>>();
        ArrayList<PlayerInfo> syncGroup = new ArrayList<PlayerInfo>();

        for(int i=0; i< mPlayerFirstFragmentList.size();i++){
            PlayerFirstFragment player = mPlayerFirstFragmentList.get(i);
            VideoInfo videoInfo = player.getVideoInfo();
            if(videoInfo != null){
                PlayerInfo playerInfo = new PlayerInfo(player.getPlayerId(), player.getPresentationTimeUs(), videoInfo.getCaptureRate(),videoInfo.getDurationUs());
                syncGroup.add(playerInfo);
            }
        }
        syncGroupList.add(syncGroup);
        return syncGroupList;
    }




}