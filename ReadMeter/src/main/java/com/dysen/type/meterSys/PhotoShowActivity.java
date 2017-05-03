package com.dysen.type.meterSys;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.MyDateUtils;
import com.dysen.mylibrary.utils.picture.ImageTools;
import com.dysen.mylibrary.utils.util.FilesUtils;
import com.dysen.qj.wMeter.BuildConfig;
import com.dysen.qj.wMeter.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class PhotoShowActivity extends MyActivityTools {

    private ImageView mImageView;
    private Button mButtonCamera;
    private Button mButtonPhoto;
    TextView txt;

    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;

    private static final int SCALE = 3;//照片缩小比例
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_show);

        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        TextView tvHint = bindView(R.id.tv_hint);
        tvHint.setText("抄表异常");
        tvHint.setTextSize(18);

        mId = getIntent().getStringExtra("meter_num");
        mImageView = (ImageView) this.findViewById(R.id.imageview_preview);
        mButtonCamera = (Button) this.findViewById(R.id.button_cameraButton);
        mButtonPhoto = (Button) this.findViewById(R.id.button_photoButton);
        txt = bindView(R.id.txt_);

        mButtonCamera.setOnClickListener(new View.OnClickListener() { //打开Camera
            @Override
            public void onClick(View v) {
                openTakePhoto();
            }
        });

        mButtonPhoto.setOnClickListener(new View.OnClickListener() {  //获取相册
            @Override
            public void onClick(View v) {
                getAlbum();
            }
        });

    }

    /**
     * 拍照
     */
    private void openTakePhoto(){
        /**
         * 在启动拍照之前最好先判断一下sdcard是否可用
         */
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)){   //如果可用

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));

            Uri imageUri = FileProvider.getUriForFile(PhotoShowActivity.this,
                    BuildConfig.APPLICATION_ID + ".fileProvider",
                    new File(Environment.getExternalStorageDirectory(),"image.jpg"));
            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            /**android 6.0 权限申请**/
            if (ContextCompat.checkSelfPermission(PhotoShowActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(PhotoShowActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                //判断是否需要 向用户解释，为什么要申请该权限
                ActivityCompat.shouldShowRequestPermissionRationale(PhotoShowActivity.this, Manifest.permission.READ_CONTACTS);
            } else {
//                    startActivityForResult(
//                            new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
//                                    MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), 1);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }
//            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }else {
            ToastDemo.myHint(PhotoShowActivity.this, "sdcard不可用", 4);
        }
    }

    /**
     * 相册
     */
    private void getAlbum(){

        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);

//        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
//        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(albumIntent, CHOOSE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
                    Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

//                    Bitmap water = BitmapFactory.decodeResource(getResources(), R.drawable.about);
                    newBitmap = createBitmap(newBitmap, "当前表号：\t"+mId);
                    //将处理过的图片显示在界面上，并保存到本地
                    mImageView.setImageBitmap(newBitmap);
//                    txt.setText("当前表号：\t"+mId);
                    ImageTools.savePhotoToSDCard(newBitmap, FilesUtils.setDirName(Environment.getExternalStorageDirectory().getAbsolutePath()+"/潜江抄表系统", "抄表异常图片"), mId+"_"+String.valueOf(MyDateUtils.formatDate(new Date(), "yyyyMMddHHmmss")));
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
//                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
//                            //释放原始图片占用的内存，防止out of memory异常发生
//                            photo.recycle();
//                            LogUtils.i(originalUri.getPath()+"文件路径：\t"+originalUri.toString());

//                           if (originalUri.toString().contains(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM")){
//                               String mId = getFileName(originalUri.toString());
//                               LogUtils.i(mId.length()+"文件名称：\t"+mId);
//                               mId = mId.substring(0, mId.lastIndexOf("_"));
//                               txt.setText("当前表号：\t"+mId);
//                               mImageView.setImageBitmap(smallBitmap);
//                           }else {
//                                ToastDemo.myHint(this, "请手动选择DCIM里的照片", 3);
//                           }
                            mImageView.setImageBitmap(photo);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**

     　　* 进行添加水印图片和文字
     　　*
     　　* @param src
     　　* @param waterMak
     　　* @return
     　　*/
    public static Bitmap createBitmap(Bitmap src, String title) {

        // 获取原始图片与水印图片的宽与高
        int w = src.getWidth();
        int h = src.getHeight();
//        int ww = waterMak.getWidth();
//        int wh = waterMak.getHeight();
//        LogUtils.i("w = " + w + ",h = " + h + ",ww = " + ww + ",wh = "+ wh);
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(newBitmap);
        // 往位图中开始画入src原始图片
        mCanvas.drawBitmap(src, 0, 0, null);
        // 在src的右下角添加水印
        Paint paint = new Paint();
        //paint.setAlpha(100);
//        mCanvas.drawBitmap(waterMak, w - ww - 5, h - wh - 5, paint);
        // 开始加入文字
        if (null != title) {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.RED);
            textPaint.setTextSize(48);
            String familyName = "宋体";
            Typeface typeface = Typeface.create(familyName,Typeface.BOLD_ITALIC);
            textPaint.setTypeface(typeface);
            textPaint.setTextAlign(Paint.Align.CENTER);
            mCanvas.drawText(title, w / 2, 45, textPaint);
            }
        mCanvas.save(Canvas.ALL_SAVE_FLAG);
        mCanvas.restore();
        return newBitmap;
        }

    public String getFileName(String pathandname){

        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }

    }
}
