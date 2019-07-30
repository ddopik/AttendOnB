package com.kspidersholidays.attendonb.utilites;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.kspidersholidays.attendonb.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
/**
 * Created by ddopik..@_@
 */
public class ImageUtils {
    public static final int COMPRESSION_RATIO = 70;
    public static final int FILE_CODE = 200;
    public static final int CAMERA_CODE = 100;
    private static final float CORNER_RADIUS = 2.0f;
    public ArrayList<String> imagesPaths = new ArrayList<String>();
    public String imgBase64 = "", imgName;






    public static Bitmap getScaledBitmap(Bitmap btimap, int maxWidth, int maxHeight) {
        int width = btimap.getWidth(), height = btimap.getHeight();

        if (width >= height && width > maxWidth) {
            height = Math.round(height * maxWidth / width);
            width = maxWidth;
        } else if (height > maxHeight) {
            width = Math.round(width * maxHeight / height);
            height = maxHeight;
        }
        return Bitmap.createScaledBitmap(btimap, width, height, true);
    }

    public static String getImgName(String imgPath) {
        return imgPath.substring(imgPath.lastIndexOf("/") + 1);
    }

    public static boolean isImageExist(String imagePath) {
        File file = new File(imagePath);
        return file.exists();
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            // if we unable to get background drawable then we will set white color as wallpaper
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
    //-------------------Functions to upload multiple Images at once for OOH---------end----

    public static String getSelectedImagePath(Activity activity, int requestCode, int resultCode, Intent data, int randomNumber) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_CODE) {
                Uri selectedImageUri = data.getData();
                //mImgPath = new FileUtils().getPathFromUri(selectedImageUri, context);
                return new FileUtils().getPathFromUri(selectedImageUri, activity);
            } else if (requestCode == CAMERA_CODE) {
                //mImgPath = cameraImage.getPath();
                return new File(FileUtils.TEMP_FILES, randomNumber + ".jpg").getPath();

            }
        }

        return "";
    }

    public static AlertDialog.Builder getPhotoChooserDialog(final Activity activity, final int randomNumber) {

        CharSequence photoChooserOptions[] = new CharSequence[]{activity.getResources().getString(R.string.general_photo_chooser_camera), activity.getResources().getString(R.string.general_photo_chooser_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.general_photo_chooser_title));
        builder.setItems(photoChooserOptions, (dialog, option) -> {
            if (option == 0) {

                if (!new File(FileUtils.TEMP_FILES).isDirectory())
                    new File(FileUtils.TEMP_FILES).mkdir();
                File cameraImage = new File(FileUtils.TEMP_FILES, randomNumber + ".jpg");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImage));
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                   activity. startActivityForResult(intent, randomNumber);
                }


            } else if (option == 1) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                activity.startActivityForResult(Intent.createChooser(intent, activity.getResources().getString(R.string.general_gallery_chooser_title)), FILE_CODE);
            }
        });
        return builder;
    }

    public static void loadOnlineImage(Context context, final String onlinePhoto, final ImageView image, final String downloadDirectory) {


    }

    public static void loadOfflineImage(Context context, String onlinePhoto, final ImageView image, final String downloadDirectory) {

    }

    public static void loadImage(Context context, String onlinePhoto, String offlinePhoto, ImageView image, final String downloadDirectory) {

        if (onlinePhoto.equals(offlinePhoto))
            loadOfflineImage(context, offlinePhoto, image, downloadDirectory);
        else
            loadOnlineImage(context, onlinePhoto, image, downloadDirectory);

    }

    public static void compressImage(final Context context, final Handler handler, final String filePath, final int width, final int height) {

    }

    public void convertImgToBase64WithoutGlide(String mImgPath,final int width, final int height) {
        Bitmap bitmap = rotateImage(mImgPath, BitmapFactory.decodeFile(mImgPath));
        //Bitmap bitmap = BitmapFactory.decodeFile(mImgPath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        getScaledBitmap(bitmap, width, height)
                .compress(Bitmap.CompressFormat.JPEG, COMPRESSION_RATIO, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        this.imgName = getImgName(mImgPath);
    }

    //-------------------Functions to upload multiple Images at once for OOH---------begin----
    public void convertMuliImagesToBase64WithoutGlide(final int width, final int height) {

        for(int i=0;i<imagesPaths.size();i++) {
            Bitmap bitmap = rotateImage(imagesPaths.get(i), BitmapFactory.decodeFile(imagesPaths.get(i)));
            //Bitmap bitmap = BitmapFactory.decodeFile(mImgPath);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            getScaledBitmap(bitmap, width, height)
                    .compress(Bitmap.CompressFormat.JPEG, COMPRESSION_RATIO, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if(i != imagesPaths.size()-1)
                this.imgBase64+="\""+ Base64.encodeToString(byteArray, Base64.NO_WRAP)+"\",";
            else {
                //String text;
                //String lastCharacter;
                this.imgBase64+="\""+ Base64.encodeToString(byteArray, Base64.NO_WRAP)+"\"";
                //lastCharacter= text.substring(text.length()-1);
                //this.imgBase64 += text.substring(0, text.length()-1)+lastCharacter;
                //this.imgBase64 += text.substring(0, text.length()-6);
                //this.imgBase64 += text;
            }
            ///this.imgName = getImgName(mImgPath);
        }

        //this.imgBase64 = "["+this.imgBase64+"]";

        //---------test to save file and check its size-------
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(FileUtils.MAIN_DIRECTORY+"/test.txt", "UTF-8");
            writer.println(this.imgBase64);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Bitmap rotateImage(String filePath, Bitmap bitmap) {
        Bitmap resultBitmap = bitmap;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();

            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            // Rotate the bitmap
            resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultBitmap;
    }



}

