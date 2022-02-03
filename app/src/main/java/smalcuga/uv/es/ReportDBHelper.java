package smalcuga.uv.es;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static smalcuga.uv.es.ReportsColumns.ReportEntry;

import androidx.annotation.Nullable;

import java.util.Random;

// Reports data base helper.
public class ReportDBHelper extends SQLiteOpenHelper {

    SQLiteDatabase myDB;
    public static final String DATABASE_NAME = "ReportsDB.db";

    public ReportDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + ReportEntry.TABLE_NAME + " ("
                + ReportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ReportEntry.COL_1 + " TEXT NOT NULL,"
                + ReportEntry.COL_2 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_1 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_2 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_3 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_4 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_5 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_6 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_7 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_8 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_9 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_10 + " TEXT NOT NULL,"
                + ReportEntry.SYMPTOMS_11 + " TEXT NOT NULL,"
                + ReportEntry.COL_4 + " TEXT NOT NULL,"
                + ReportEntry.COL_5 + " TEXT NOT NULL,"
                + "UNIQUE (" + ReportEntry.COL_1 + "))");

        myDB = db;
        mockData(myDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReportEntry.TABLE_NAME);
        onCreate(db);
    }

    // Inserting starting reports.
    String firstSymptoms[] = {"Yes", "Yes", "Yes", "No", "No", "No", "Yes", "Yes", "Yes", "No", "No"};
    String secondSymptoms[] = {"Yes", "No", "No", "Yes", "Yes", "Yes", "No", "No", "No", "Yes", "Yes"};

    private void mockData(SQLiteDatabase db) {
        insertReport("01.02.21", firstSymptoms, "Yes", "Ademuz");
        insertReport("02.02.21", secondSymptoms, "No", "Ademuz");
        insertReport("03.02.21", firstSymptoms, "Yes", "Ademuz");
        insertReport("04.02.21", secondSymptoms, "No", "Ademuz");
        insertReport("05.02.21", firstSymptoms, "Yes", "Ademuz");
        insertReport("12.12.20", firstSymptoms, "Yes", "Ador");
        insertReport("08.01.21", secondSymptoms, "No", "Ador");
        insertReport("10.01.21", firstSymptoms, "Yes", "Adsubia");
    }

    public Cursor findReportsByMunicipality(String municipalityName) {
        String selectQuery = "SELECT * FROM " + ReportEntry.TABLE_NAME + " where Municipality = ?" + " order by " + ReportEntry.COL_5 + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{municipalityName});
        return cursor;
    }

    public long insertReport(String symptomStartDate, String[] symptoms, String closeContact, String municipalityName) {
        if(myDB == null)
            myDB = getWritableDatabase();

        ContentValues values = new ContentValues();
        Random rand = new Random();
        values.put(ReportEntry.COL_1, municipalityName + "-" + symptomStartDate + "-" + rand.nextInt(1000));
        values.put(ReportEntry.COL_2, symptomStartDate);
        values.put(ReportEntry.SYMPTOMS_1, symptoms[0]);
        values.put(ReportEntry.SYMPTOMS_2, symptoms[1]);
        values.put(ReportEntry.SYMPTOMS_3, symptoms[2]);
        values.put(ReportEntry.SYMPTOMS_4, symptoms[3]);
        values.put(ReportEntry.SYMPTOMS_5, symptoms[4]);
        values.put(ReportEntry.SYMPTOMS_6, symptoms[5]);
        values.put(ReportEntry.SYMPTOMS_7, symptoms[6]);
        values.put(ReportEntry.SYMPTOMS_8, symptoms[7]);
        values.put(ReportEntry.SYMPTOMS_9, symptoms[8]);
        values.put(ReportEntry.SYMPTOMS_10, symptoms[9]);
        values.put(ReportEntry.SYMPTOMS_11, symptoms[10]);

        values.put(ReportEntry.COL_4, closeContact);
        values.put(ReportEntry.COL_5, municipalityName);

        long result = myDB.insert(ReportEntry.TABLE_NAME,null, values);
        return result;
    }

    public boolean updateReport(String diagnosticCode, String symptomStartDate, String[] symptoms, String closeContact, String municipalityName) {
        if(myDB == null)
            myDB = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReportEntry.COL_1, diagnosticCode);
        values.put(ReportEntry.COL_2, symptomStartDate);
        values.put(ReportEntry.SYMPTOMS_1, symptoms[0]);
        values.put(ReportEntry.SYMPTOMS_2, symptoms[1]);
        values.put(ReportEntry.SYMPTOMS_3, symptoms[2]);
        values.put(ReportEntry.SYMPTOMS_4, symptoms[3]);
        values.put(ReportEntry.SYMPTOMS_5, symptoms[4]);
        values.put(ReportEntry.SYMPTOMS_6, symptoms[5]);
        values.put(ReportEntry.SYMPTOMS_7, symptoms[6]);
        values.put(ReportEntry.SYMPTOMS_8, symptoms[7]);
        values.put(ReportEntry.SYMPTOMS_9, symptoms[8]);
        values.put(ReportEntry.SYMPTOMS_10, symptoms[9]);
        values.put(ReportEntry.SYMPTOMS_11, symptoms[10]);
        values.put(ReportEntry.COL_4, closeContact);
        values.put(ReportEntry.COL_5, municipalityName);

        myDB.update(ReportEntry.TABLE_NAME, values, "Diagnostic_code = ?", new String[] {diagnosticCode});
        return true;
    }

    public int deleteReport(String diagnosticCode) {
        if(myDB == null)
            myDB = getWritableDatabase();

        String selection = ReportEntry.COL_1 + " LIKE ?";
        String[] selectionArgs = {diagnosticCode};
        int deletedRows = myDB.delete(ReportEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;
    }
}
