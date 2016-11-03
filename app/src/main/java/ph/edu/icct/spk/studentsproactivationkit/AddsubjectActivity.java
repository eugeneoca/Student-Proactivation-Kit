package ph.edu.icct.spk.studentsproactivationkit;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddsubjectActivity extends AppCompatActivity {

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsubject);

        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_cancel){
                    finish();
                }
            }
        });

        // Input Widgets
        final EditText txt_subject_code = (EditText)findViewById(R.id.txt_subject_code);
        final EditText txt_description = (EditText)findViewById(R.id.txt_description);
        final EditText txt_units = (EditText)findViewById(R.id.txt_units);
        final EditText txt_professor = (EditText)findViewById(R.id.txt_professor);
        final EditText txt_contact = (EditText)findViewById(R.id.txt_contact);

        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_save){
                    // Add subject to database
                    if(!(txt_subject_code.getText().toString().isEmpty() ||
                            txt_description.getText().toString().isEmpty() ||
                            txt_units.getText().toString().isEmpty() ||
                            txt_professor.getText().toString().isEmpty() ||
                            txt_contact.getText().toString().isEmpty())){

                        // True Condition Block
                        db = new Database(getApplicationContext());
                        boolean insert_result = db.insert_subject(
                                txt_subject_code.getText().toString().toUpperCase().replace(" ",""),
                                txt_description.getText().toString().toUpperCase(),
                                Integer.parseInt(txt_units.getText().toString()),
                                txt_professor.getText().toString().toUpperCase(),
                                txt_contact.getText().toString().toUpperCase()
                        );

                        if(insert_result){
                            Toast.makeText(getApplicationContext(), "Subject has been added.", Toast.LENGTH_SHORT).show();
                            // Call Update ViewLayout
                        }else{
                            Toast.makeText(getApplicationContext(), "Error occured..", Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Please complete the form.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

}
