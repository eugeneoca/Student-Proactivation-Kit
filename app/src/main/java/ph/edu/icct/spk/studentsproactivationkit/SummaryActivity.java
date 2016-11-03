package ph.edu.icct.spk.studentsproactivationkit;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SummaryActivity extends AppCompatActivity {

    String code;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Get shared prefrences
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        String selected_code = prefs.getString("selected_code", "NULL");
        code = prefs.getString("selected_code", "NULL");

        TextView title = (TextView)findViewById(R.id.txt_title);
        title.setText("Summary for "+selected_code);

        setup_grades();

    }

    public void setup_grades(){
        String data;

        EditText txt_prelim = (EditText)findViewById(R.id.txt_prelim);
        txt_prelim.setClickable(false);
        txt_prelim.setFocusable(false);
        data = String.format("%.2f", get_CS(code,"prelim")[1])+ "%";
        txt_prelim.setText(data);

        EditText txt_midterm = (EditText)findViewById(R.id.txt_midterm);
        txt_midterm.setClickable(false);
        txt_midterm.setFocusable(false);
        data = String.format("%.2f", get_CS(code,"midterm")[1]) + "%";
        txt_midterm.setText(data);

        EditText txt_final = (EditText)findViewById(R.id.txt_final);
        txt_final.setClickable(false);
        txt_final.setFocusable(false);
        data = String.format("%.2f", get_CS(code,"final")[1]) + "%";
        txt_final.setText(data);

        TextView txt_pointGrade = (TextView)findViewById(R.id.txt_pointGrade);
        txt_pointGrade.setClickable(false);
        txt_pointGrade.setFocusable(false);
        WeightedAverage ave = new WeightedAverage(get_CS(code,"final")[1]);
        data = Double.toString(ave.getAverage());
        txt_pointGrade.setText(data);

        TextView txt_back = (TextView)findViewById(R.id.btn_back);
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    Database db;
    public double[] get_CS(String code, String period){
        db = new Database(getApplicationContext());

        // SCORE
        double quiz_grade = 0;
        double recitation_grade = 0;
        double project_grade = 0;
        double assignment_grade = 0;
        double exam_grade = 0;

        // TOTAL
        double quiz_total = 0;
        double recitation_total = 0;
        double project_total = 0;
        double assignment_total = 0;
        double exam_total = 0;

        // Get QUIZ GRADE
        Cursor quiz = db.get_AllGrades(code, period, "quiz");

        if(quiz.getCount()==0){
            quiz_grade = 0;
            quiz_total = 0;
        }else{
            while(quiz.moveToNext()){
                // Gather data/assesment
                quiz_grade = quiz_grade+ Double.parseDouble(quiz.getString(1));
                quiz_total = quiz_total+ Double.parseDouble(quiz.getString(2));
            }
        }
        // END QUIZ

        // Get RECITATION GRADE
        Cursor recitation = db.get_AllGrades(code, period, "recitation");

        if(recitation.getCount()==0){
            recitation_grade = 0;
            recitation_total = 0;
        }else{
            while(recitation.moveToNext()){
                // Gather data/assesment
                recitation_grade = recitation_grade+ Double.parseDouble(recitation.getString(1));
                recitation_total = recitation_total+ Double.parseDouble(recitation.getString(2));
            }
        }
        // END RECITATION

        // Get PROJECT GRADE
        Cursor project = db.get_AllGrades(code, period, "project");

        if(project.getCount()==0){
            project_grade = 0;
            project_total = 0;
        }else{
            while(project.moveToNext()){
                // Gather data/assesment
                project_grade = project_grade+ Double.parseDouble(project.getString(1));
                project_total = project_total+ Double.parseDouble(project.getString(2));
            }
        }
        // END PROJECT

        // Get ASSIGNMENT GRADE
        Cursor assignment = db.get_AllGrades(code, period, "assignment");

        if(assignment.getCount()==0){
            assignment_grade = 0;
            assignment_total = 0;
        }else{
            while(assignment.moveToNext()){
                // Gather data/assesment
                assignment_grade = assignment_grade+ Double.parseDouble(assignment.getString(1));
                assignment_total = assignment_total+ Double.parseDouble(assignment.getString(2));
            }
        }
        // END ASSIGNMENT

        // Get ASSIGNMENT GRADE
        Cursor exam = db.get_AllGrades(code, period, "exam");

        if(exam.getCount()==0){
            exam_grade = 0;
            exam_total = 0;
        }else{
            while(exam.moveToNext()){
                // Gather data/assesment
                exam_grade = exam_grade+ Double.parseDouble(exam.getString(1));
                exam_total = exam_total+ Double.parseDouble(exam.getString(2));
            }
        }
        // END ASSIGNMENT

        double cs = (Double.isNaN((quiz_grade/quiz_total)*50+50)? 50:((quiz_grade/quiz_total)*50 + 50)) +
                (Double.isNaN((recitation_grade/recitation_total)*50+50)? 50 :((recitation_grade/recitation_total)*50+50)) +
                (Double.isNaN((project_grade/project_total)*50+50)? 50 :((project_grade/project_total)*50+50)) +
                (Double.isNaN((assignment_grade/assignment_total)*50+50)? 50 :((assignment_grade/assignment_total)*50+50));
        double cs_ave = cs/4.00; // Average
        double exam_rate = Double.isNaN((exam_grade/exam_total)*50+50)? 50 : (exam_grade/exam_total)*50+50;

        // Params {0,2} is nonsense
        double output[] = { cs_ave, ((2.00*cs_ave)+ exam_rate)/3.00, exam_rate};

        switch (period){
            case "prelim":
                output[1] = output[1];
                break;
            case "midterm":
                output[1] = ((2*output[1]) + get_CS(code, "prelim")[1])/3;
                break;
            case "final":
                output[1] = (2*output[1] + get_CS(code, "midterm")[1])/3;
        }
        return output;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup_grades();
    }
}
