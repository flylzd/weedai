package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.ImageUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AccountAvatarsActivity extends BaseActivity {

    private final static String TAG = "AccountAvatarsActivity";

    private ImageView imgAvatar;
    private Button btnAvatar;
    private TextView tvUsername;
    private TextView tvSex;
    private TextView tvBirthdate;
    private TextView tvPhone;
    private TextView tvEmail;

    private boolean isTakePhoto;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int SCALE_CAMERA = 8; // 缩放比例
    private static final int SCALE_PHOTO = 2; // 缩放比例

    private static final String IMG_SCALE_NAME = "/img_scale_name.jpg";
    private static final String IMG_NAME = "/img_name.jpg";

    private AlertDialog cameraDialog;
    private String filePath;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_avatars);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_account_avatars;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        btnAvatar = (Button) findViewById(R.id.btnAvatar);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvSex = (TextView) findViewById(R.id.tvSex);
        tvBirthdate = (TextView) findViewById(R.id.tvBirthdate);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("filePath " + filePath);
                openCameraDialog();

                if (TextUtils.isEmpty(filePath)) {
                    Toast.makeText(AccountAvatarsActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiClient.uploadAvatars(TAG, filePath, new ResponseListener() {
                    @Override
                    public void onStarted() {
                        progressDialog = ProgressDialog.show(AccountAvatarsActivity.this, null, getString(R.string.message_waiting));
                    }

                    @Override
                    public void onResponse(Object response) {
                        progressDialog.dismiss();

                        BaseModel result = (BaseModel) response;
                        if (result.code != Constant.CodeResult.SUCCESS) {
                            Toast.makeText(AccountAvatarsActivity.this, result.message, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (result.message.equals("edit_success")) {

                            Toast.makeText(AccountAvatarsActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AccountAvatarsActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
        setInfo();
    }

    private void setInfo() {

        UserData data = User.userInfo;
        String url = data.touxiang;
        if (!TextUtils.isEmpty(url)) {
            url = Urls.SERVER_URL + url;
            ImageLoader.getInstance().displayImage(url, imgAvatar);
        }

        tvUsername.setText(DataUtil.urlDecode(data.username));

        String sex = data.sex == 1 ? "男" : "女";
        tvSex.setText(sex);
//        tvBirthdate.setText(data.);
        tvPhone.setText(data.phone);
        tvEmail.setText(data.email);
    }

    private void openCameraDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AccountAvatarsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_camera, null);
        TextView tvCamera = (TextView) view.findViewById(R.id.tvCamera);
        TextView tvPhoto = (TextView) view.findViewById(R.id.tvPhoto);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
//            tvCamera.setOnClickListener(this);
//            tvPhoto.setOnClickListener(this);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamera();
            }
        });
        tvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraDialog.dismiss();
            }
        });

        builder.setTitle("选择图片");
        builder.setView(view);
        cameraDialog = builder.create();
        cameraDialog.show();
    }

    private void takeCamera() {

        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机
        Uri imageUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), IMG_NAME));
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent,
                CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        cameraDialog.dismiss();
    }

    private void takePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

        cameraDialog.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            isTakePhoto = true;
            // 将保存在本地的图片取出并缩小后显示在界面上
            Bitmap bitmap = BitmapFactory.decodeFile(Environment
                    .getExternalStorageDirectory() + IMG_NAME);
            Bitmap smallBitmap = ImageUtil.zoomBitmap(bitmap, bitmap.getWidth()
                    / SCALE_CAMERA, bitmap.getHeight() / SCALE_CAMERA);

            // 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
            bitmap.recycle();
            // 将处理过的图片显示在界面上，并保存到本地
            imgAvatar.setImageBitmap(smallBitmap);
            ImageUtil.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
                    AccountAvatarsActivity.IMG_SCALE_NAME);
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + IMG_SCALE_NAME;

        } else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {

            isTakePhoto = true;
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                    Bitmap smallBitmap = ImageUtil.zoomBitmap(photo, photo.getWidth() / SCALE_PHOTO, photo.getHeight()
                            / SCALE_PHOTO);
                    // 释放原始图片占用的内存，防止out of memory异常发生
                    photo.recycle();
                    imgAvatar.setImageBitmap(smallBitmap);
                    ImageUtil.savePhotoToSDCard(smallBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(),
                            IMG_SCALE_NAME);
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + IMG_SCALE_NAME;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
