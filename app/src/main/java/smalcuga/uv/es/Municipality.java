package smalcuga.uv.es;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Comparator;

// Class representing a single municipality.
public class Municipality extends AppCompatActivity implements Serializable {

    public int number;
    public int id;
    public String name;
    public int casesPCR;
    public String incidencePCR;
    public int casesPCR14;
    public String incidencePCR14;
    public int deaths;
    public String deathRate;
    public int color;

    Municipality(int number, int id, String name, int casesPCR, String incidencePCR, int casesPCR14, String incidencePCR14, int deaths, String deathRate) {
        this.number = number;
        this.id = id;
        this.name = name;
        this.casesPCR = casesPCR;
        this.incidencePCR = incidencePCR;
        this.casesPCR14 = casesPCR14;
        this.incidencePCR14 = incidencePCR14;
        this.deaths = deaths;
        this.deathRate = deathRate;

        if (casesPCR > 1000)
            this.color = Color.RED;
        else if (casesPCR > 100)
            this.color = Color.YELLOW;
    }

    public String getName() { return name; }
    public int getCasesPCR() {
        return casesPCR;
    }
}