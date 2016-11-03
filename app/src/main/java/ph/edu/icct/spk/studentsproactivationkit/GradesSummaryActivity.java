package ph.edu.icct.spk.studentsproactivationkit;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.THIRD_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FOURTH_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIFTH_COLUMN;

public class GradesSummaryActivity extends AppCompatActivity {

    ListView list_allGrades;
    SharedPreferences prefs;

    String list_green = "";
    String list_orange = "";
    String list_red = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_summary);
        list_allGrades = (ListView)findViewById(R.id.list_allGrades);
        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        get_AllSubjects();
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        get_AllSubjects();
        setupGradeColors();
    }
*/
    Database db;
    Database db_a;
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


    public static ArrayList<HashMap<String, String>> list;

    String grade;
    WeightedAverage ave;
    public void get_AllSubjects(){
        List<String> red = new ArrayList<>();
        List<String> orange = new ArrayList<>();
        List<String> green = new ArrayList<>();
        int total_units = 0;
        double gwa = 0;
        db_a = new Database(getApplicationContext());
        list = new ArrayList<HashMap<String,String>>();
        Cursor data = db_a.get_AllSubjects();
        if(data.getCount()==0){
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            // Add data to scroll view
            HashMap<String,String> head = new HashMap<String, String>();
            head.put(FIRST_COLUMN, "Course Code");
            head.put(SECOND_COLUMN, "Grade");
            head.put(THIRD_COLUMN, "Units");
            head.put(FOURTH_COLUMN, "Professor");
            head.put(FIFTH_COLUMN, "Mobile#");
            list.add(head);
            int curr = 1;
            while(data.moveToNext()){
                // Build Array
                ave = new WeightedAverage(get_CS(data.getString(1),"final")[1]);
                gwa = gwa + (ave.getAverage() * Integer.parseInt(data.getString(3)));
                grade = String.format("%.0f", ave.getAverage());
                HashMap<String,String> temp = new HashMap<String, String>();
                temp.put(FIRST_COLUMN, data.getString(1));
                temp.put(SECOND_COLUMN, grade);
                temp.put(THIRD_COLUMN, data.getString(3));
                total_units = total_units + Integer.parseInt(data.getString(3));
                temp.put(FOURTH_COLUMN, data.getString(4));
                temp.put(FIFTH_COLUMN, data.getString(5));
                list.add(temp);

                if(!grade.equals("Grade")){
                    if(ave.getAverage()<=2.0){
                        if(!list_green.equals("")){
                            list_green = list_green + "," + Integer.toString(curr);
                        }else{
                            list_green = list_green + Integer.toString(curr);
                        }
                    }else if(ave.getAverage()<=3.0){
                        if(!list_orange.equals("")){
                            list_orange = list_orange + "," + Integer.toString(curr);
                        }else{
                            list_orange = list_orange + Integer.toString(curr);
                        }
                    }else if(ave.getAverage()<=5.0){
                        if(!list_red.equals("")){
                            list_red = list_red + "," + Integer.toString(curr);
                        }else{
                            list_red = list_red + Integer.toString(curr);
                        }
                    }
                }

                curr++;
            }

            String final_gwa = String.format("%.0f", (Double.isNaN(gwa/(double)total_units)? 5: gwa/(double)total_units));
            if(Double.parseDouble(final_gwa)<=2.0){
                if(!list_green.equals("")){
                    list_green = list_green + "," + Integer.toString(curr);
                }else{
                    list_green = list_green + Integer.toString(curr);
                }
            }else if(Double.parseDouble(final_gwa)<=3.0){
                if(!list_orange.equals("")){
                    list_orange = list_orange + "," + Integer.toString(curr);
                }else{
                    list_orange = list_orange + Integer.toString(curr);
                }
            }else if(Double.parseDouble(final_gwa)<=5.0){
                if(!list_red.equals("")){
                    list_red = list_red + "," + Integer.toString(curr);
                }else{
                    list_red = list_red + Integer.toString(curr);
                }
            }

            // GWA
            HashMap<String,String> gwa_value = new HashMap<String, String>();
            gwa_value.put(FIRST_COLUMN, "GWA & UNITS");
            gwa_value.put(SECOND_COLUMN, final_gwa);
            gwa_value.put(THIRD_COLUMN, Integer.toString(total_units));
            gwa_value.put(FOURTH_COLUMN, "");
            gwa_value.put(FIFTH_COLUMN, "");
            list.add(gwa_value);
        }

        // LIST ALL
        Log.d("APP - ","GREEN{"+list_green+"} ORANGE{"+list_orange+"} RED{"+list_red+"}");
        ListViewAdapters_allGrades adapter = new ListViewAdapters_allGrades(this, list, list_green, list_orange, list_red);
        list_allGrades.setAdapter(adapter);
    }
}
