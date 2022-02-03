package smalcuga.uv.es;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MunicipalitiesActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // Class to download data from the website.
    class HTTPConnector extends AsyncTask<AdapterMunicipalities, Void, Writer> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Writer doInBackground(AdapterMunicipalities... params) {
            return adapter.Init();
        }

        @Override
        protected void onPostExecute(Writer writer) {

            // Commented the way of reading from data.json (1st session).
            try {
                JSONObject jsonObject = new JSONObject(writer.toString());
                //JSONArray jsonArray = jsonObject.getJSONArray("result");
                JSONObject jsonObject2 = jsonObject.getJSONObject("result");
                JSONArray jsonArray = jsonObject2.getJSONArray("records");

                for (int i = 0; i < jsonArray.length(); i++) {
                    /*
                    JSONArray itemObj = jsonArray.getJSONArray(i);
                    int number = itemObj.getInt(0);
                    int id = itemObj.getInt(1);
                    String name = itemObj.getString(2);
                    int casesPCR = itemObj.getInt(3);
                    String incidencePCR = itemObj.getString(4);
                    int casesPCR14 = itemObj.getInt(5);
                    String incidencePCR14 = itemObj.getString(6);
                    int deaths = itemObj.getInt(7);
                    String deathRate = itemObj.getString(8);
                     */
                    JSONObject itemObj = jsonArray.getJSONObject(i);
                    int number = itemObj.getInt("_id");
                    int id = itemObj.getInt("CodMunicipio");
                    String name = itemObj.getString("Municipi");
                    int casesPCR = itemObj.getInt("Casos PCR+");
                    String incidencePCR = itemObj.getString("Incidència acumulada PCR+");
                    int casesPCR14 = itemObj.getInt("Casos PCR+ 14 dies");
                    String incidencePCR14 = itemObj.getString("Incidència acumulada PCR+14");
                    int deaths = itemObj.getInt("Defuncions");
                    String deathRate = itemObj.getString("Taxa de defunció");

                    Municipality municipality = new Municipality(number, id, name, casesPCR, incidencePCR, casesPCR14, incidencePCR14, deaths, deathRate);
                    municipalities.add(municipality);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.setMunicipalities(municipalities);
            recyclerView = findViewById(R.id.MyRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
    }

    // Class to take id of the data set (to update it in the app if necessary).
    class HTTPConnector2 extends AsyncTask<String, Void, Writer> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Writer doInBackground(String... params) {
            return getIdDataSet();
        }

        @Override
        protected void onPostExecute(Writer writer) {

            try {
                JSONObject jsonObject = new JSONObject(writer.toString());
                JSONObject jsonArray = jsonObject.getJSONObject("result");
                idDataSet = jsonArray.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<Municipality> municipalities;
    RecyclerView recyclerView;
    AdapterMunicipalities adapter;
    boolean alphabetically;
    SearchView searchView;
    Context context;
    String cityName;
    String idDataSet;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipalities);

        adapter = new AdapterMunicipalities(this);
        context = this;
        municipalities = new ArrayList<>();
        new HTTPConnector().execute(adapter);
        new HTTPConnector2().execute();
        adapter.setOnItemClickListener(onItemClickListener);
        cityName = "";
        idDataSet = "";
        alphabetically = true;

        FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        cityName = hereLocation(location.getLatitude(), location.getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MunicipalitiesActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                    }
                }
                Snackbar.make(view, "Adding an item", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(MunicipalitiesActivity.this, ReportActivity.class);
                intent.putExtra("municipalityName", cityName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        cityName = hereLocation(location.getLatitude(), location.getLongitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MunicipalitiesActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        //MenuItem searchItem = menu.findItem(R.id.searchItem);
        //searchView = (SearchView) menu.findItem(R.id.searchItem).getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchItem).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.websiteItem:
                String link = "https://dadesobertes.gva.es/es/dataset/covid-19-casos-confirmats-pcr-casos-pcr-en-els-ultims-14-dies-i-persones-mortes-per-municipi-2021";
                Uri webaddress = Uri.parse(link);
                Intent goToWebsite = new Intent(Intent.ACTION_VIEW, webaddress);
                startActivity(goToWebsite);
                return true;

            case R.id.decreasingItem:
                Collections.sort(municipalities, Comparator.comparing(Municipality::getCasesPCR).reversed());
                alphabetically = false;
                adapter = new AdapterMunicipalities(this);
                adapter.setMunicipalities(municipalities);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);
                return true;

            case R.id.increasingItem:
                Collections.sort(municipalities, Comparator.comparing(Municipality::getCasesPCR));
                alphabetically = false;
                adapter = new AdapterMunicipalities(this);
                adapter.setMunicipalities(municipalities);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);
                return true;

            case R.id.alphabeticallyItem:
                if(alphabetically)
                    Collections.sort(municipalities, Comparator.comparing(Municipality::getName).reversed());
                else
                    Collections.sort(municipalities, Comparator.comparing(Municipality::getName));
                alphabetically = !alphabetically;
                adapter = new AdapterMunicipalities(this);
                adapter.setMunicipalities(municipalities);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);
                return true;

            case R.id.refreshItem:
                String tmp = idDataSet;
                new HTTPConnector2().execute();
                if(tmp != idDataSet) {
                    Snackbar.make(recyclerView, "Updating data set.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    new HTTPConnector().execute();
                }
                else
                    Snackbar.make(recyclerView, "No changes found.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int pos = viewHolder.getAdapterPosition();
            Municipality municipality = municipalities.get(pos);
            Intent intent = new Intent(MunicipalitiesActivity.this, MunicipalityActivity.class);
            intent.putExtra("municipality", municipality);
            startActivity(intent);
        }
    };

    // Obtaining location.
    private String hereLocation(double lat, double lon) {
        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    // Obtaining id of data set from the website.
    private Writer getIdDataSet() {
        String url = "https://dadesobertes.gva.es/api/3/action/package_show?id=38e6d3ac-fd77-413e-be72-aed7fa6f13c2";
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            con.setRequestProperty("accept", "application/json;");
            con.setRequestProperty("accept-language", "es");
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            int n;
            while ((n = in.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer;
    }
}