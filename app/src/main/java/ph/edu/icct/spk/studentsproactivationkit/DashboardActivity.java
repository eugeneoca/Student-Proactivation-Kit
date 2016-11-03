package ph.edu.icct.spk.studentsproactivationkit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ph.edu.icct.spk.studentsproactivationkit.Constants.FIRST_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants.SECOND_COLUMN;
import static ph.edu.icct.spk.studentsproactivationkit.Constants.THIRD_COLUMN;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Database db;
    Button btn_refresh;
    public static String target_code = "";

    public static ArrayList<HashMap<String, String>> list;
    SharedPreferences prefs = null; // Preferences Storage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(DashboardActivity.this, AddsubjectActivity.class);
                startActivityForResult(intent,100);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /* CUSTOM CODE START FROM HERE*/
        list_subjects =(ListView)findViewById(R.id.list_subjects);

        prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        String user = prefs.getString("username","USERNAME");
        View v = navigationView.getHeaderView(0);
        final TextView username = (TextView ) v.findViewById(R.id.txt_username);
        final ImageView img_gender = (ImageView) v.findViewById(R.id.img_Profile);
        username.setText(user.toUpperCase());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder diag = new AlertDialog.Builder(DashboardActivity.this);
                diag.setTitle("Update username");

                diag.setView(R.layout.update_username_layout);

                diag.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText txt_newUser = (EditText) ((AlertDialog)dialog).findViewById(R.id.txt_newUser);
                        if(!txt_newUser.getText().toString().equals("")){
                            prefs.edit().putString("username", txt_newUser.getText().toString()).apply();
                            AlertDialog.Builder success = new AlertDialog.Builder(DashboardActivity.this);
                            success.setTitle("Update username");

                            success.setMessage("Username has been updated successfully!");
                            success.setCancelable(false);
                            success.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doRestart(getApplicationContext());
                                }
                            });
                            success.create().show();
                        }else{
                            // IF NOT EMPTY TEXTBOX
                            Toast.makeText(getApplicationContext(), "Username will not be updated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                diag.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                return;
                            }
                        });

                diag.create().show();
            }
        });

        String gender = prefs.getString("gender","male");
        Log.d("APP",gender);
        if(gender.equals("male")){
            // SET PROFILE PIC TO BOY
            img_gender.setImageResource(R.drawable.male_pic);
        }else{
            // SET PROFILE PIC TO GIRL
            img_gender.setImageResource(R.drawable.female_pic);
        }
        img_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder diag = new AlertDialog.Builder(DashboardActivity.this);
                diag.setTitle("UPDATE PROFILE PICTURE");
                diag.setMessage("Which one that you want to use?");
                diag.setPositiveButton("MALE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putString("gender","male").commit();
                        doRestart(DashboardActivity.this);
                    }
                });
                diag.setNegativeButton("FEMALE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putString("gender","female").commit();
                        doRestart(DashboardActivity.this);
                    }
                });
                diag.create().show();
            }
        });

        list_subjects.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                TextView code = (TextView)view.findViewById(R.id.txt_code);
                if(code.getText() != "Course Code"){
                    // Put data to shared preferences
                    prefs.edit().putString("selected_code", code.getText().toString()).apply();

                    // Pass to another activity
                    Intent intent = new Intent(DashboardActivity.this, SubjectSpecificActivity.class);
                    startActivity(intent);
                }
            }

        });

        get_AllSubjects();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        get_AllSubjects();
    }

    // Result Activity Listener
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Listener for Additional Subjects **********
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            get_AllSubjects();
        }
    }

    ListView list_subjects;

    public void get_AllSubjects(){
        db = new Database(getApplicationContext());
        list = new ArrayList<HashMap<String,String>>();
        Cursor data = db.get_AllSubjects();
        if(data.getCount()==0){
            Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        }else{
            // Add data to scroll view
            HashMap<String,String> head = new HashMap<String, String>();
            head.put(FIRST_COLUMN, "Course Code");
            head.put(SECOND_COLUMN, "Descriptive Title");
            head.put(THIRD_COLUMN, "Units");
            list.add(head);
            while(data.moveToNext()){
                // Build Array

                HashMap<String,String> temp = new HashMap<String, String>();
                temp.put(FIRST_COLUMN, data.getString(1));
                temp.put(SECOND_COLUMN, data.getString(2));
                temp.put(THIRD_COLUMN, data.getString(3));
                list.add(temp);
            }
        }

        // LIST ALL

        ListViewAdapters adapter = new ListViewAdapters(this, list);
        list_subjects.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_AllSubjects();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to logout?");
            // alert.setMessage("Message");

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                    System.exit(0);
                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });

            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_missionVision) {
            Intent intent = new Intent(DashboardActivity.this, MissionVision.class);
            startActivity(intent);
        } else if (id == R.id.nav_hymn) {
            Intent intent = new Intent(DashboardActivity.this, Hymn.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(DashboardActivity.this, About.class);
            startActivity(intent);
        } else if (id == R.id.nav_gradeEquivalent) {
            Intent intent = new Intent(DashboardActivity.this, GradingSystem.class);
            startActivity(intent);
        } else if (id == R.id.nav_grades) {
            Intent intent = new Intent(DashboardActivity.this, GradesSummaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_reset) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you really want to reset everything?");
            // alert.setMessage("Message");

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    clearData();
                    try{
                        File dir = new File(Environment.getExternalStorageDirectory()+File.separator + "spk" + File.separator);
                        deleteRecursive(dir);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    doRestart(getApplicationContext());
                }
                void deleteRecursive(File fileOrDirectory) {
                    if (fileOrDirectory.isDirectory())
                        for (File child : fileOrDirectory.listFiles())
                            deleteRecursive(child);

                    fileOrDirectory.delete();
                }
            });

            alert.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });

            alert.show();
        } else if (id == R.id.nav_exit) {
            //super.onBackPressed();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Do you want to logout?");
            // alert.setMessage("Message");

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                    System.exit(0);
                }
            });

            alert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    });

            alert.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearData() {
        Database db = new Database(getApplicationContext());
        db.reset();
        //prefs = getSharedPreferences("ph.edu.icct.spk.studentsproactivationkit", MODE_PRIVATE);
        prefs.edit().clear().apply();
        finish();
    }

    public static void doRestart(Context c) {
        String TAG = "SPK - ";
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }
}
