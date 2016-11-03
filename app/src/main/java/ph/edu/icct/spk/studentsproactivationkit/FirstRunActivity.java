package ph.edu.icct.spk.studentsproactivationkit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FirstRunActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    private static final int CAMERA_REQUEST = 1888;
    private int SELECT_PHOTO= 0;
    ImageView img_userphoto = null;
    Bitmap bImage = null;
    File folder = null;
    OutputStream fOut = null;
    Uri outputFileUri;
    boolean isset_userphoto = false;
    boolean is_rotated = false;

    boolean gender_isGirl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        // Declare default folder
        final String DEFAULT_FOLDER = "spk_folder";

        // Declare Shared preferences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);

        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_cancel){
                    Toast.makeText(getApplicationContext(), "Goodbye...", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        final EditText txt_username = (EditText)findViewById(R.id.txt_username);
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_save){

                    // SUBJECT TO CHANGE
                    /*else if(!isset_userphoto){
                        Toast.makeText(getApplicationContext(), "Please select a profile photo.", Toast.LENGTH_SHORT).show();
                    }*/

                    if(txt_username.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please specify at least your name.", Toast.LENGTH_SHORT).show();
                    }else{
                        /*if(is_rotated){
                            img_userphoto.setRotation(270f);
                        }*/
                        Toast.makeText(getApplicationContext(), "Welcome "+txt_username.getText(), Toast.LENGTH_LONG).show();
                        prefs.edit().putBoolean("firstrun", false).apply();
                        prefs.edit().putString("username", txt_username.getText().toString()).apply();

                        /*
                        * Store the profile picture in a folder
                        *
                        * Problem: Unexpected rotation of images
                        * */
                        /*
                        img_userphoto.buildDrawingCache();
                        bImage = img_userphoto.getDrawingCache();

                        try {
                            File root = new File(Environment.getExternalStorageDirectory()
                                    + File.separator + DEFAULT_FOLDER + File.separator);
                            root.mkdirs();
                            File sdImageMainDirectory = new File(root, "avatar.png");
                            outputFileUri = Uri.fromFile(sdImageMainDirectory);
                            fOut = new FileOutputStream(sdImageMainDirectory);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            bImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        */

                        // SET PROFILE PICTURE
                        if(!gender_isGirl){
                            // BOY IS SELECTED
                            prefs.edit().putString("gender","boy").apply();
                        }else{
                            // GIRL IS SELECTED
                            prefs.edit().putString("gender","girl").apply();
                        }
                        // Go to dashboard
                        Intent intent = new Intent(FirstRunActivity.this, DashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });


        //img_userphoto = (ImageView)findViewById(R.id.img_userphoto);

        Button btn_capture_camera = (Button)findViewById(R.id.btn_capture_camera);
        btn_capture_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capture Image
                if(v.getId()==R.id.btn_capture_camera){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        SELECT_PHOTO = 12345;
        Button btn_album = (Button)findViewById(R.id.btn_album);
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select photo from gallery
                if(v.getId()==R.id.btn_album){
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }
            }
        });

        // EMOTICON SELECTION
        ImageView img_nerd = (ImageView)findViewById(R.id.img_nerd);
        ImageView img_heart = (ImageView)findViewById(R.id.img_heart);
        ImageView img_angel = (ImageView)findViewById(R.id.img_angel);
        ImageView img_crush = (ImageView)findViewById(R.id.img_crush);


        // Emojis Events
        img_nerd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.img_nerd){
                    bImage = BitmapFactory.decodeResource(getResources(), R.drawable.em_nerd);
                    img_userphoto.setImageBitmap(bImage);
                    img_userphoto.setRotation(0f);
                    isset_userphoto = true;
                }
            }
        });

        img_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.img_heart){
                    bImage = BitmapFactory.decodeResource(getResources(), R.drawable.em_heart);
                    img_userphoto.setImageBitmap(bImage);
                    img_userphoto.setRotation(0f);
                    isset_userphoto = true;
                    is_rotated = false;
                }
            }
        });

        img_angel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.img_angel){
                    bImage = BitmapFactory.decodeResource(getResources(), R.drawable.em_angel);
                    img_userphoto.setImageBitmap(bImage);
                    img_userphoto.setRotation(0f);
                    isset_userphoto = true;
                }
            }
        });

        img_crush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.img_crush){
                    bImage = BitmapFactory.decodeResource(getResources(), R.drawable.em_crush);
                    img_userphoto.setImageBitmap(bImage);
                    img_userphoto.setRotation(0f);
                    isset_userphoto = true;
                    is_rotated = false;
                }
            }
        });

        final ImageView img_female = (ImageView)findViewById(R.id.img_female);
        final ImageView img_male = (ImageView)findViewById(R.id.img_male);

        img_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView)v.findViewById(R.id.img_female);
                img.setBackgroundColor(Color.CYAN);
                gender_isGirl = true;
                img_male.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


        img_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView)v.findViewById(R.id.img_male);
                img.setBackgroundColor(Color.CYAN);
                gender_isGirl = false;
                img_female.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    // Listener for captured image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Listener for captured image **********
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            img_userphoto.setImageBitmap(getRoundedShape(photo));
            img_userphoto.setRotation(0f);
            isset_userphoto = true;
            is_rotated = false;
        }

        // Listener for picked image from local library ************
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            img_userphoto.setImageBitmap(getRoundedShape(bitmap));
            img_userphoto.setRotation(-90f);
            is_rotated = true;
            isset_userphoto = true;

            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

    // Method for reshaping the square image into circular
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 200;
        int targetHeight = 200;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
