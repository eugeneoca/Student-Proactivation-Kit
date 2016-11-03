package ph.edu.icct.spk.studentsproactivationkit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.THIRD_COLUMN;

public class GradeList extends AppCompatActivity {

    /*
    * This module is being used by other activities to show list of grades.
    * */

    SharedPreferences prefs;
    TextView txt_title;
    public static ArrayList<HashMap<String, String>> list;

    String selected_code;
    String selected_assesment;
    String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_list);

        txt_title = (TextView)findViewById(R.id.txt_title);

        // Get shared prefrences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        String title = prefs.getString("title","NULL");
        selected_code = prefs.getString("selected_code", "NULL");
        selected_assesment = prefs.getString("selected_assesment", "NULL");
        period = prefs.getString("period", "NULL");
        txt_title.setText(title);

        list_grade =(ListView)findViewById(R.id.list_grade);
        list_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                File root = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "spk" + File.separator+ selected_code +File.separator+period + File.separator + selected_assesment + File.separator);
                File sdImageMainDirectory = new File(root,  selected_assesment+"_"+pos+"_.png");
                Log.d("APP", sdImageMainDirectory.toString());
                if(sdImageMainDirectory.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(sdImageMainDirectory.getPath());
                    ImageView img_preview = (ImageView)findViewById(R.id.img_preview);
                    img_preview.setImageBitmap(myBitmap);
                }
            }
        });

        Button btn_addComponent = (Button)findViewById(R.id.btn_addComponent);
        btn_addComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradeList.this, ComponentAdder.class);
                startActivity(intent);
            }
        });

        Button btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        get_AllGrades();
    }

    ListView list_grade;
    Database db;

    public void get_AllGrades(){
        db = new Database(getApplicationContext());
        list = new ArrayList<HashMap<String,String>>();
        Cursor data = db.get_AllGrades(selected_code,period,selected_assesment);
        if(data.getCount()==0){
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            // Add data to scroll view
            HashMap<String,String> head = new HashMap<String, String>();
            head.put(FIRST_COLUMN, "#");
            head.put(SECOND_COLUMN, "Score");
            head.put(THIRD_COLUMN, "Number of Items");
            list.add(head);
            int x = 1;
            int total_score=0;
            int total_item=0;
            int score;
            int items;
            double grade;
            while(data.moveToNext()){
                // Build Array
                score = Integer.parseInt(data.getString(1));
                items = Integer.parseInt(data.getString(2));
                total_score = total_score+score;
                total_item = total_item+items;

                HashMap<String,String> temp = new HashMap<String, String>();
                temp.put(FIRST_COLUMN, Integer.toString(x));
                temp.put(SECOND_COLUMN, Integer.toString(score));
                temp.put(THIRD_COLUMN, Integer.toString(items));
                list.add(temp);
                x++;
            }

            // QUIZ GRADE
            grade = ((double)total_score/(double)total_item)*(double)50 + (double)50;
            HashMap<String,String> quiz_grade = new HashMap<String, String>();
            quiz_grade.put(FIRST_COLUMN, selected_assesment.toUpperCase()+" GRADE");
            quiz_grade.put(SECOND_COLUMN, "");
            quiz_grade.put(THIRD_COLUMN, String.format("%.2f", grade) + "%");
            list.add(quiz_grade);
        }

        // LIST ALL

        ListViewAdapter_Grades adapter = new ListViewAdapter_Grades(this, list);
        list_grade.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_AllGrades();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
