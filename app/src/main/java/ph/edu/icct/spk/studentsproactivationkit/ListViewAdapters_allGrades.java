package ph.edu.icct.spk.studentsproactivationkit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.THIRD_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FOURTH_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_allGrades.FIFTH_COLUMN;

/**
 * Created by Alfred on 10/23/2016.
 */
public class ListViewAdapters_allGrades extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    TextView txtFifth;
    String list_green = "";
    String list_orange = "";
    String list_red = "";

    public ListViewAdapters_allGrades(Activity activity, ArrayList<HashMap<String, String>> list, String list_green, String list_orange, String list_red){
        super();
        this.activity=activity;
        this.list=list;
        this.list_green=list_green;
        this.list_orange=list_orange;
        this.list_red=list_red;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.allgrades_row, null);

            txtFirst=(TextView) convertView.findViewById(R.id.txt_code);
            txtSecond=(TextView) convertView.findViewById(R.id.txt_grade);
            txtThird=(TextView) convertView.findViewById(R.id.txt_units);
            txtFourth=(TextView) convertView.findViewById(R.id.txt_professor);
            txtFifth=(TextView) convertView.findViewById(R.id.txt_num);

            // GREEN
            if(list_green.contains(",")){
                String green[] = list_green.split(",");
                for (String pos:green){
                    if(toInt(pos)==position && toInt(pos)!=0){
                        txtSecond.setTextColor(Color.GREEN);
                    }
                }
            }else{
                String pos = list_green;
                if(toInt(pos)==position && toInt(pos)!=0){
                    txtSecond.setTextColor(Color.GREEN);
                }
            }

            // ORANGE
            if(list_orange.contains(",")){
                String orange[] = list_orange.split(",");
                for (String pos:orange){
                    if(toInt(pos)==position && toInt(pos)!=0){
                        txtSecond.setTextColor(Color.parseColor("#f49542"));
                    }
                }
            }else{
                String pos = list_orange;
                if(toInt(pos)==position && toInt(pos)!=0){
                    txtSecond.setTextColor(Color.parseColor("#f49542"));
                }
            }

            // RED
            if(list_red.contains(",")){
                String red[] = list_red.split(",");
                for (String pos:red){
                    if(toInt(pos)==position && toInt(pos)!=0){
                        txtSecond.setTextColor(Color.RED);
                    }
                }
            }else{
                String pos = list_red;
                if(toInt(pos)==position && toInt(pos)!=0){
                    txtSecond.setTextColor(Color.RED);
                }
            }
        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));
        txtFifth.setText(map.get(FIFTH_COLUMN));

        return convertView;
    }

    private int toInt(String x){
        String k = (x.equals("")) ? "0":x;
        return Integer.parseInt(k);
    }
}
