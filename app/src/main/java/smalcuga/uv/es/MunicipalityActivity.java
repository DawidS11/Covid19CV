package smalcuga.uv.es;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

// Activity of a single municipality.
public class MunicipalityActivity extends AppCompatActivity {

    TextView casesPCRView, incidencePCRView, casesPCR14View, incidencePCR14View, deathsView, deathRateView;
    String municipalityName;
    Button reportsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        Municipality municipality = (Municipality)getIntent().getSerializableExtra("municipality");
        municipalityName = municipality.name;
        setTitle(municipalityName);

        casesPCRView = (TextView) findViewById(R.id.casesPCRView);
        incidencePCRView = (TextView) findViewById(R.id.incidencePCRView);
        casesPCR14View = (TextView) findViewById(R.id.casesPCR14View);
        incidencePCR14View = (TextView) findViewById(R.id.incidencePCR14View);
        deathsView = (TextView) findViewById(R.id.deathsView);
        deathRateView = (TextView) findViewById(R.id.deathRateView);

        casesPCRView.setText(String.valueOf(municipality.casesPCR));
        incidencePCRView.setText(municipality.incidencePCR);
        casesPCR14View.setText(String.valueOf(municipality.casesPCR14));
        incidencePCR14View.setText(municipality.incidencePCR14);
        deathsView.setText(String.valueOf(municipality.deaths));
        deathRateView.setText(municipality.deathRate);

        reportsButton = (Button) findViewById(R.id.reportsButton);
        reportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MunicipalityActivity.this, ReportsActivity.class);
                intent.putExtra("municipalityName", municipalityName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.locationItem:
                String link = "http://maps.google.co.in/maps?q=" + municipalityName;
                Uri webaddress = Uri.parse(link);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, webaddress);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}