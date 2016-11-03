package ph.edu.icct.spk.studentsproactivationkit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIFTH_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FOURTH_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.THIRD_COLUMN;

public class PrelimActivity extends AppCompatActivity {

    SharedPreferences prefs;
    TextView txt_code;
    String period = "prelim";
    String selected_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelim);

        txt_code = (TextView)findViewById(R.id.txt_code);

        // Get shared prefrences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        selected_code = prefs.getString("selected_code", "NULL");
        String data = "\tCourse Code: "+selected_code;
        txt_code.setText(data);

        // Assesments
        Button btn_quiz = (Button) findViewById(R.id.btn_quiz);
        Button btn_recitation = (Button) findViewById(R.id.btn_recitation);
        Button btn_projects = (Button) findViewById(R.id.btn_project);
        Button btn_assignment = (Button) findViewById(R.id.btn_assignment);
        Button btn_exam = (Button) findViewById(R.id.btn_exam);
        Button btn_prelim_grade = (Button)findViewById(R.id.btn_prelim_grade);

        Button btn_info = (Button)findViewById(R.id.btn_info);
        Button btn_back = (Button)findViewById(R.id.btn_back);

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder diag = new AlertDialog.Builder(PrelimActivity.this);
                diag.setTitle("SUBJECT INFORMATION");
                final List<Map<String, String>> data = new ArrayList<>();
                Map<String, String> map = new HashMap<>(5);
                map.put("code", get_info()[0][1]);
                map.put("description", get_info()[1][1]);
                map.put("units", get_info()[2][1]);
                map.put("professor", get_info()[3][1]);
                map.put("contact", get_info()[4][1]);
                data.add(map);
                SimpleAdapter adapter = new SimpleAdapter(PrelimActivity.this, data,
                        R.layout.subject_info_layout,
                        new String[] {"code", "description", "units", "professor", "contact"},
                        new int[] {R.id.txt_code,R.id.txt_description,R.id.txt_units,R.id.txt_professor,R.id.txt_contact
                        });
                diag.setAdapter(adapter,null);
                diag.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                diag.create().show();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // QUIZ
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Prelim Quizzes", selected_code, "quiz", PrelimActivity.this, GradeList.class);
            }
        });

        // RECITAION
        btn_recitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Prelim Recitations", selected_code, "recitation", PrelimActivity.this, GradeList.class);
            }
        });

        // PROJECT
        btn_projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Prelim Projects", selected_code, "project", PrelimActivity.this, GradeList.class);
            }
        });

        // ASSIGNMENT
        btn_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Prelim Assignments", selected_code, "assignment", PrelimActivity.this, GradeList.class);
            }
        });

        // EXAM
        btn_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will happen once.
                grade_Handler("Prelim Exam", selected_code, "exam", PrelimActivity.this, GradeList.class);
            }
        });

        // END PERIOD GRADE
        btn_prelim_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Prelim Grade", selected_code, "prelim", PrelimActivity.this, EndperiodActivity.class);
            }
        });
    }

    Database db;
    public String[][] get_info(){
        db = new Database(getApplicationContext());
        Cursor data = db.get_Info(this.selected_code);
        String code = "";
        String description = "";
        String units = "";
        String professor = "";
        String contact = "";
        if(data.getCount()==0){
            //Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            String items[][] = {{"No data available",""}};
            return items;
        }else{
            while(data.moveToNext()){
                code = data.getString(1);
                description = data.getString(2);
                units = data.getString(3);
                professor = data.getString(4);
                contact = data.getString(5);
            }
        }

        String items[][] = {{"Course Code:",code},{"Description:",description},{"Units:",units},{"Professor:",professor},{"Contact:",contact}};
        return items;
    }

    public void grade_Handler(String title, String subject_code, String assesment,Context packageContext, Class<?> cls){
        prefs.edit().putString("title",title).apply();
        prefs.edit().putString("period", this.period).apply();
        prefs.edit().putString("selected_code",subject_code).apply();
        prefs.edit().putString("selected_assesment",assesment).apply();
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
