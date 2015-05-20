package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.weedai.ptp.volley.ResponseListener;

import java.io.File;
import java.io.FileNotFoundException;

public class AccountAvatarsActivity extends BaseActivity {

    private final static String TAG = "AccountAvatarsActivity";

    private ImageView imgAvatar;
    private Button btnAvatar;
    private TextView tvUsername;
    private TextView tvSex;
    private TextView tvBirthdate;
    private TextView tvPhone;
    private TextView tvEmail;

    private ProgressDialog progressDialog;

    private String[] items = new String[]{"选择本地图片", "拍照"};
    /**
     * 头像名称
     */
    private static final String IMAGE_FILE_NAME = "image.jpg";

    /**
     * 请求码
     */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    private Uri imgUri = null;

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
                showCameraDialog();
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

    private void uploadAvatar() {

        String filePath = imgUri.getPath();
        System.out.println("filePath === " + filePath);
        ApiClient.uploadAvatars(TAG, filePath, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(AccountAvatarsActivity.this, null, getString(R.string.message_submit));
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

    /**
     * 显示选择对话框
     */
    private void showCameraDialog() {

        new AlertDialog.Builder(this)
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                                    File file = new File(path, IMAGE_FILE_NAME);
                                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                }
                                startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(AccountAvatarsActivity.this, "ActivityResult resultCode error", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                startPhotoZoom(data.getData());
                break;
            case CAMERA_REQUEST_CODE:
                // 判断存储卡是否可以用，可用进行存储
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File tempFile = new File(path, IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case RESULT_REQUEST_CODE:
//                if (data != null) {
//                    System.out.println("filePath " + data.toURI());
//                    getImageToView(data);
//                }
                if (imgUri != null) {
                    Bitmap bitmap = decodeUriAsBitmap(imgUri);//decode bitmap
                    imgAvatar.setImageBitmap(bitmap);

                    uploadAvatar();
                }
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        // 判断存储卡是否可以用，可用进行存储
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File tempFile = new File(path, IMAGE_FILE_NAME);
            System.out.println("path === " + tempFile.toString());
            System.out.println("path === " + tempFile.getAbsolutePath());
            System.out.println("path === " + tempFile.getPath());
            imgUri = Uri.fromFile(tempFile);
        } else {
            Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
//        intent.putExtra("return-data", true);  //true，不能把裁剪的图片输出
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            imgAvatar.setImageDrawable(drawable);
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

}
