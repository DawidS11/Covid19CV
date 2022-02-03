package smalcuga.uv.es;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static smalcuga.uv.es.ReportsColumns.ReportEntry;

// Reports cursor adapter.
public class ReportsCursorAdapter extends CursorAdapter {

    public ReportsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.item_reports, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView reportDiagCode = (TextView) view.findViewById(R.id.reportDiagCode);

        String code = cursor.getString(cursor.getColumnIndex(ReportEntry.COL_1));

        reportDiagCode.setText(code);
    }
}
