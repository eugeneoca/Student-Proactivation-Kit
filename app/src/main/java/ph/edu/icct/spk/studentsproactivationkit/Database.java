package ph.edu.icct.spk.studentsproactivationkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alfred on 10/21/2016.
 */
public class Database extends SQLiteOpenHelper {

    protected static  int DB_VERSION = 1;
    protected static String DB_NAME = "spk.db";
    SQLiteDatabase db;

    protected static final String tbl_subjects = "tbl_subjects";
    protected static final String create_tbl_subjects = "CREATE TABLE IF NOT EXISTS "+tbl_subjects
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " subject_code TEXT NOT NULL," +
            " description TEXT NOT NULL," +
            " units INTEGER NOT NULL," +
            " professor TEXT NOT NULL," +
            " contact TEXT NOT NULL )";

    protected static final String tbl_grade = "tbl_grade";
    protected static final String create_tbl_grade = "CREATE TABLE IF NOT EXISTS "+tbl_grade
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " subject_code TEXT NOT NULL," +
            " period TEXT NOT NULL," +
            " assesment TEXT NOT NULL," +
            " score TEXT NOT NULL," +
            " total_item TEXT NOT NULL )";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db =  this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_tbl_subjects);
        db.execSQL(create_tbl_grade);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // On Update Version, this method will be triggered
        db.execSQL("DROP TABLE IF EXISTS "+tbl_subjects);
        db.execSQL("DROP TABLE IF EXISTS "+tbl_grade);
        onCreate(db);
    }

    // Database Commands

    public boolean insert_subject(String subject_code, String description, int  units, String professor, String contact){
        // SQLdatabase is already decleared outside.

        ContentValues values = new ContentValues();
        values.put("subject_code", subject_code);
        values.put("description", description);
        values.put("units", units);
        values.put("professor", professor);
        values.put("contact", contact);
        long result = db.insert(tbl_subjects, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insert_gradeComponent(String subject_code, String period, String assesment, String score, String total_item){
        // SQLdatabase is already decleared outside.

        ContentValues values = new ContentValues();
        values.put("subject_code", subject_code);
        values.put("period", period);
        values.put("assesment", assesment);
        values.put("score", score);
        values.put("total_item", total_item);
        long result = db.insert(tbl_grade, null, values);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor get_AllGrades(String subject_code, String period, String assesment){
        db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT id,score,total_item FROM "+ tbl_grade + " where subject_code LIKE '"+subject_code+"' AND period LIKE '"+period+"' AND assesment LIKE '"+assesment+"'", null);
        return result;
    }

    public Cursor get_Info(String subject_code){
        db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT id,subject_code,description,units,professor,contact FROM "+ tbl_subjects+ " where subject_code LIKE '"+subject_code+"'", null);
        return result;
    }

    public Cursor get_AllSubjects(){
        db = getWritableDatabase();
        Cursor result = db.rawQuery("SELECT id,subject_code,description,units,professor,contact FROM "+tbl_subjects, null);
        return result;
    }

    public void reset(){
        db = getWritableDatabase();
        onUpgrade(db,1,1);
    }
}
