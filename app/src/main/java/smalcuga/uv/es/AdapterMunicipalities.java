package smalcuga.uv.es;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// Municipalities adapter.
public class AdapterMunicipalities extends RecyclerView.Adapter<AdapterMunicipalities.ViewHolder> implements Filterable {

    private ArrayList<Municipality> municipalities, municipalitiesFull;
    public Context context;
    View.OnClickListener mOnItemClickListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdapterMunicipalities(Context c) {
        context = c;
        //Init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdapterMunicipalities(Context c, ArrayList<Municipality> municipalities) {
        context = c;
        this.municipalities.addAll(municipalities);
        this.municipalitiesFull.addAll(municipalities);
    }

    // Downloading data from the website.
    // Commented the way of reading from data.json (1st session).
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Writer Init() {
        municipalities = new ArrayList<>();
        municipalitiesFull = new ArrayList<>();
        /*
        InputStream is = context.getResources().openRawResource(R.raw.data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

        //Perform the request and get the answer
        String url = "https://dadesobertes.gva.es/es/api/3/action/datastore_search?resource_id=f6cb1e39-2839-4d38-8fd7-74430775a97e&limit=1000";
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

    @Override
    public int getItemCount() {
        if(municipalities == null)
            return 0;
        return municipalities.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView itemName;
        private final TextView itemNumber;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemNumber = (TextView) view.findViewById(R.id.itemNumber);

            view.setTag(this);
        }

        public CardView getCardView() { return cardView; }
        public TextView getItemName() { return itemName; }
        public TextView getItemNumber() { return itemNumber; }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_municipalities, viewGroup, false);
        view.setOnClickListener(mOnItemClickListener);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get element from your data set at this position and replace the
        // contents of the view with that element

        Municipality municipality = municipalities.get(position);
        holder.getItemName().setText(String.valueOf(municipality.name));
        holder.getItemNumber().setText(String.valueOf(municipality.casesPCR));

        if (municipality.casesPCR > 1000)
            holder.getCardView().setBackgroundColor(Color.RED);
        else if (municipality.casesPCR > 100)
            holder.getCardView().setBackgroundColor(Color.YELLOW);
        else
            holder.getCardView().setBackgroundColor(Color.WHITE);
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public void setMunicipalities(ArrayList<Municipality> muns) {
        municipalities = new ArrayList<>();
        municipalitiesFull = new ArrayList<>();
        municipalities.addAll(muns);
        municipalitiesFull.addAll(muns);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().trim();
                ArrayList<Municipality> filteredList = new ArrayList<>();
                if (charString.isEmpty() || charString == null) {
                    filteredList.addAll(municipalitiesFull);
                } else {
                    for (Municipality row : municipalitiesFull) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                municipalities.clear();
                municipalities.addAll((ArrayList<Municipality>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}
