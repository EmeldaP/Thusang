package com.example.codetribe.thusang;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    private Uri fileUri;
    private ImageView image;
    private VideoView video;
    private Button Picture, Video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Picture = (Button) findViewById(R.id.cam);
        Video = (Button) findViewById(R.id.vid);
        image = (ImageView) findViewById(R.id.image);
        video = (VideoView) findViewById(R.id.vd);

        Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
            //Check if device has camera
            private boolean isDeviceSupportCamera() {
                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {
                    return true;
                } else {
                    return false;
                }
            }

            private void captureImage() {
                Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                image.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the image capture Intent
                startActivityForResult(image, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                // if the result is capturing Image
                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        // successfully captured the image
                        // display it in image view
                        // previewCapturedImage();
                    } else if (resultCode == RESULT_CANCELED) {
                        // user cancelled Image capture
                        Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // failed to capture image
                        Toast.makeText(getApplicationContext(),
                                "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            }

            private void previewCapturedImage() {
                try {
                    // hide video preview
                    video.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    // bimatp factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger images
                    options.inSampleSize = 8;
                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            options);

                    image.setImageBitmap(bitmap);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo();
            }

            private void recordVideo() {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
                // set video quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                // start the video capture Intent
                startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
            }
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                // if the result is capturing Image
                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    // code to check capture image response
                } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {
                        // video successfully recorded
                        // preview the recorded video
                        previewVideo();
                    } else if (resultCode == RESULT_CANCELED) {
                        // user cancelled recording
                        Toast.makeText(getApplicationContext(),
                                "User cancelled video recording", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // failed to record video
                        Toast.makeText(getApplicationContext(),
                                "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            private void previewVideo() {
                try {
                    // hide image preview
                    image.setVisibility(View.GONE);

                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath(fileUri.getPath());
                    // start playing
                    video.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
     // returning image / video
         private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault())
             .format(new Date());
              File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;

    }
}