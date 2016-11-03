package ph.edu.icct.spk.studentsproactivationkit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalsActivity extends AppCompatActivity {

    TextView txt_code;
    SharedPreferences prefs;
    String period = "final";
    String selected_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finals);

        txt_code = (TextView)findViewById(R.id.txt_code);

        // Get shared prefrences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        selected_code = prefs.getString("selected_code", "NULL");
        String data = "\tCourse Code: "+selected_code;
        txt_code.setText(data);

        // Assesments
        Button btn_quiz = (Button) findViewById(R.id.btn_quiz);
        Button btn_recitation = (Button) findViewById(R.id.btn_recitation);
        Button btn_project = (Button) findViewById(R.id.btn_project);
        Button btn_assignment = (Button) findViewById(R.id.btn_assignment);
        Button btn_exam = (Button) findViewById(R.id.btn_exam);
        Button btn_finals_grade = (Button)findViewById(R.id.btn_finals_grade);

        Button btn_info = (Button)findViewById(R.id.btn_info);
        Button btn_back = (Button)findViewById(R.id.btn_back);

        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder diag = new AlertDialog.Builder(FinalsActivity.this);
                diag.setTitle("SUBJECT INFORMATION");
                final List<Map<String, String>> data = new ArrayList<>();
                Map<String, String> map = new HashMap<>(5);
                map.put("code", get_info()[0][1]);
                map.put("description", get_info()[1][1]);
                map.put("units", get_info()[2][1]);
                map.put("professor", get_info()[3][1]);
                map.put("contact", get_info()[4][1]);
                data.add(map);
                SimpleAdapter adapter = new SimpleAdapter(FinalsActivity.this, data,
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
                grade_Handler("Final Quizzes", selected_code, "quiz", FinalsActivity.this, GradeList.class);
            }
        });

        // RECITAION
        btn_recitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Final Recitations", selected_code, "recitation", FinalsActivity.this, GradeList.class);
            }
        });

        // PROJECT
        btn_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Final Projects", selected_code, "project", FinalsActivity.this, GradeList.class);
            }
        });

        // ASSIGNMENT
        btn_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Final Assignments", selected_code, "assignment", FinalsActivity.this, GradeList.class);
            }
        });

        // EXAM
        btn_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will happen once.
                grade_Handler("Final Exam", selected_code, "exam", FinalsActivity.this, GradeList.class);
            }
        });

        // END PERIOD GRADE
        btn_finals_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade_Handler("Final Grade", selected_code, "final", FinalsActivity.this, EndperiodActivity.class);
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

    public void grade_Handler(String title, String subject_code, String assesment, Context packageContext, Class<?> cls){
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
