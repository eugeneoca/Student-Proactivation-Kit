package ph.edu.icct.spk.studentsproactivationkit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants_column.THIRD_COLUMN;

/**
 * Created by Alfred on 10/25/2016.
 */
public class ListViewAdapter_Grades extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public ListViewAdapter_Grades(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
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
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.grades_row, null);

            txtFirst=(TextView) convertView.findViewById(R.id.txt_number);
            txtSecond=(TextView) convertView.findViewById(R.id.txt_score);
            txtThird=(TextView) convertView.findViewById(R.id.txt_numItems);
        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));

        return convertView;
    }
}
