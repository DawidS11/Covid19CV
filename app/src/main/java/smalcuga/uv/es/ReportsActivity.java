package smalcuga.uv.es;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

// Activity with a ListView of reports.
public class ReportsActivity extends AppCompatActivity {

    ReportDBHelper db;
    ListView reportsListView;
    ReportsCursorAdapter reportsAdapter;
    String municipalityName;
    FloatingActionButton addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
    }

    // onResume() in order to update the ListView while updating / deleting / inserting a report.
    @Override
    protected void onResume() {
        super.onResume();
        municipalityName = (String)getIntent().getSerializableExtra("municipalityName");
        setTitle(municipalityName);

        db = new ReportDBHelper(this);
        if(db == null) {
            return;
        }
        Cursor reportsByMunicipality = db.findReportsByMunicipality(municipalityName);
        if(reportsByMunicipality == null) {
            return;
        }
        reportsAdapter = new ReportsCursorAdapter(this, reportsByMunicipality);
        reportsListView = findViewById(R.id.reportsListView);
        reportsListView.setAdapter(reportsAdapter);

        reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String diagnosticCode = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.COL_1));
                String symptomStartDate = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.COL_2));
                String symptom0 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_1));
                String symptom1 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_2));
                String symptom2 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_3));
                String symptom3 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_4));
                String symptom4 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_5));
                String symptom5 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_6));
                String symptom6 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_7));
                String symptom7 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_8));
                String symptom8 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_9));
                String symptom9 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_10));
                String symptom10 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.SYMPTOMS_11));
                String closeContact = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.COL_4));
                String municipalityName2 = cursor.getString(cursor.getColumnIndexOrThrow(ReportsColumns.ReportEntry.COL_5));

                String [] extraArray = {diagnosticCode, symptomStartDate, symptom0, symptom1, symptom2, symptom3, symptom4, symptom5, symptom6,
                        symptom7, symptom8, symptom9, symptom10, closeContact, municipalityName2};

                Intent intent = new Intent(ReportsActivity.this, ReportActivity.class);
                intent.putExtra("extraArray", extraArray);

                startActivity(intent);
            }
        });

        FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.addItem2);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adding an item", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(ReportsActivity.this, ReportActivity.class);
                intent.putExtra("municipalityName", municipalityName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
