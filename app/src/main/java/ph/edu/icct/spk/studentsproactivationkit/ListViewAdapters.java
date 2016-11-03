package ph.edu.icct.spk.studentsproactivationkit;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static ph.edu.icct.spk.studentsproactivationkit.Constants.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants.THIRD_COLUMN;

/**
 * Created by Alfred on 10/23/2016.
 */
public class ListViewAdapters extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    public ListViewAdapters(Activity activity,ArrayList<HashMap<String, String>> list){
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

            convertView=inflater.inflate(R.layout.subjects_row, null);

            txtFirst=(TextView) convertView.findViewById(R.id.txt_code);
            txtSecond=(TextView) convertView.findViewById(R.id.txt_desc);
            txtThird=(TextView) convertView.findViewById(R.id.txt_units);
        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));

        return convertView;
    }
}
