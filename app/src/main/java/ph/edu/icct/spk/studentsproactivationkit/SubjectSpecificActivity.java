package ph.edu.icct.spk.studentsproactivationkit;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class SubjectSpecificActivity extends AppCompatActivity {

    LocalActivityManager mLocalActivityManager;
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_specific);

        TabHost tab = (TabHost)findViewById(R.id.tabHost);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState); // state will be bundle your activity state which you get in onCreate
        tab.setup(mLocalActivityManager);

        // Profile Tab
        TabHost.TabSpec prelim = tab.newTabSpec("Prelim");
        prelim.setIndicator("Prelim");
        prelim.setContent(new Intent(this, PrelimActivity.class));
        tab.addTab(prelim);

        TabHost.TabSpec midterm = tab.newTabSpec("Midterm");
        midterm.setIndicator("Midterm");
        midterm.setContent(new Intent(this, MidtermActivity.class));
        tab.addTab(midterm);

        TabHost.TabSpec finals = tab.newTabSpec("Finals");
        finals.setIndicator("Finals");
        finals.setContent(new Intent(this, FinalsActivity.class));
        tab.addTab(finals);

        TabHost.TabSpec summary = tab.newTabSpec("Summary");
        summary.setIndicator("Summary");
        summary.setContent(new Intent(this, SummaryActivity.class));
        tab.addTab(summary);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume ()
    {
        mLocalActivityManager.dispatchResume();
        super.onResume ();
    }

    @Override
    protected void onPause ()
    {
        mLocalActivityManager.dispatchPause(isFinishing());
        super.onPause ();
    }

    @Override
    protected void onStop ()
    {
        mLocalActivityManager.dispatchStop ();
        super.onStop ();
    }

    @Override
    protected void onSaveInstanceState (Bundle outState)
    {
        mLocalActivityManager.saveInstanceState ();

    }
}
