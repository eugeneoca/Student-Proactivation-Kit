package ph.edu.icct.spk.studentsproactivationkit;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ComponentAdder extends AppCompatActivity {
    /*
    * This module is being used by multiple actions for
    * adding a component in a specific subject
    * */

    SharedPreferences prefs;
    Database db;
    String selected_code;
    String period;
    String selected_assesment;
    String title;
    static final int CAMERA_REQUEST = 1;
    ImageView img_capture;
    boolean isCaptured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_adder);

        // Get shared prefrences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        selected_code = prefs.getString("selected_code", "NULL");
        period = prefs.getString("period", "NULL");
        selected_assesment = prefs.getString("selected_assesment", "NULL");
        title = prefs.getString("title", "NULL");

        img_capture = (ImageView)findViewById(R.id.img_capture);
        TextView txt_title = (TextView)findViewById(R.id.txt_title);
        txt_title.setText(title);
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText txt_score = (EditText)findViewById(R.id.txt_score);
        final EditText txt_numItems = (EditText)findViewById(R.id.txt_numItems);
        Button btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add subject to database
                if(!txt_score.getText().toString().isEmpty() && !txt_numItems.getText().toString().isEmpty() && hasImage(img_capture)==true){
                    if(!(Integer.parseInt(txt_score.getText().toString())>100) && !(Integer.parseInt(txt_numItems.getText().toString())>100) && !(Integer.parseInt(txt_score.getText().toString())<0) && !(Integer.parseInt(txt_numItems.getText().toString())<0)){

                        if(Integer.parseInt(txt_score.getText().toString())<=Integer.parseInt(txt_numItems.getText().toString())){
                            // True Condition Block
                            db = new Database(getApplicationContext());
                            boolean insert_result = db.insert_gradeComponent(
                                    selected_code,
                                    period,
                                    selected_assesment,
                                    txt_score.getText().toString(),
                                    txt_numItems.getText().toString()
                            );

                            if(insert_result){
                                Toast.makeText(getApplicationContext(), "Grade has been added.", Toast.LENGTH_SHORT).show();
                                // Call Update ViewLayout
                                Bitmap bitmap = ((BitmapDrawable)img_capture.getDrawable()).getBitmap();
                                Log.d("APP", Boolean.toString(hasImage(img_capture)));
                                //saveImage(bitmap,selected_code,period, selected_assesment);
                                saveToInternalStorage(bitmap,selected_code,period, selected_assesment);
                                isCaptured = false;
                                db.close();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Error occured..", Toast.LENGTH_SHORT).show();
                            }
                            db.close();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Score must be less than or equal value with the number of items.", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Scores & Items should be in range 1-100.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please complete the form & capture an image.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btn_capture = (Button)findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    FileOutputStream fOut;
    File sdImageMainDirectory;
    public void saveImage(Bitmap img, String code, String period, String assesment){

        // GET THE LAST ID FROM DATABASE
        Database db = new Database(ComponentAdder.this);
        Cursor cur = db.get_AllGrades(code,period,assesment);
        int count = cur.getCount();

        try {
            File root = new File(Environment.getExternalStorageDirectory() + File.separator + "spk" + File.separator+ code +File.separator+period + File.separator + assesment + File.separator);
            root.mkdirs();
            sdImageMainDirectory = new File(root,  assesment+"_"+count+"_.png");
            img.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String code, String period, String assesment){
        // GET THE LAST ID FROM DATABASE
        Database db = new Database(ComponentAdder.this);
        Cursor cur = db.get_AllGrades(code,period,assesment);
        int count = cur.getCount();
        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "spk" + File.separator+ code +File.separator+period + File.separator + assesment + File.separator);
        root.mkdirs();
        sdImageMainDirectory = new File(root,  assesment+"_"+count+"_.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(sdImageMainDirectory.getAbsoluteFile());
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img_capture.setImageBitmap(imageBitmap);
            isCaptured=true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
