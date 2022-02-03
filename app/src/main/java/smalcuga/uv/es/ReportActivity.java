package smalcuga.uv.es;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

// Activity of a single report.
public class ReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ReportDBHelper db;
    String [] extraArray;
    TextView diagnosticCodeView, symptomStartDate, closeContact, municipalityNameView;
    TextView [] symptomView;
    AlertDialog [] dialog;
    EditText [] editText;
    CheckBox [] symptomsCheckBox;
    Spinner spinnerCloseContact;
    ArrayAdapter<CharSequence> adapter;
    Button btnCheckBox;
    String municipalityName;
    boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new ReportDBHelper(this);
        extraArray = new String[15];
        symptomView = new TextView[11];
        dialog = new AlertDialog[3];
        editText = new EditText[3];
        symptomsCheckBox = new CheckBox[11];

        if(getIntent().hasExtra("extraArray")) {
            setContentView(R.layout.activity_report_show);
            setViews();
            extraArray = (String[])getIntent().getSerializableExtra("extraArray");
            setTitle(extraArray[0]);
            setTexts();
            isEditing = false;
        }
        else if(getIntent().hasExtra("municipalityName")){
            setContentView(R.layout.activity_report);
            setTitle("New report");
            municipalityName = (String)getIntent().getSerializableExtra("municipalityName");
            String[] tmp = {"Text", "Text", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", "No", municipalityName};
            extraArray = tmp;
            addUpdate();
            spinnerCloseContact = findViewById(R.id.closeContactSpinner);
            adapter = ArrayAdapter.createFromResource(this, R.array.string_spinnerCloseContact, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCloseContact.setAdapter(adapter);
            spinnerCloseContact.setOnItemSelectedListener(this);

            btnCheckBox = (Button) findViewById(R.id.btnCheckBox);
            btnCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String [] tmp2;
                    tmp2 = new String[11];
                    for(int i = 0; i < 11; i++)
                        tmp2[i] = extraArray[i + 2];
                    db.insertReport(extraArray[1], tmp2, extraArray[13], extraArray[14]);
                    finish();
                }
            });
            isEditing = true;
        }
        else {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isEditing) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.report_menu_show, menu);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.updateReport:
                if(!isEditing) {
                    setContentView(R.layout.activity_report);
                    setTitle(extraArray[0]);
                    addUpdate();
                    btnCheckBox = (Button) findViewById(R.id.btnCheckBox);
                    btnCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String [] tmp;
                            tmp = new String[11];
                            for(int i = 0; i < 11; i++)
                                tmp[i] = extraArray[i + 2];
                            db.updateReport(extraArray[0], extraArray[1], tmp, extraArray[13], extraArray[14]);
                            finish();
                        }
                    });

                    spinnerCloseContact = findViewById(R.id.closeContactSpinner);
                    adapter = ArrayAdapter.createFromResource(this, R.array.string_spinnerCloseContact, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCloseContact.setAdapter(adapter);
                    spinnerCloseContact.setOnItemSelectedListener(this);

                    isEditing = true;
                }

                else {
                    setContentView(R.layout.activity_report_show);

                    setViews();
                    setTexts();
                    isEditing = false;
                }

                return true;

            case R.id.deleteReport:
                AlertDialog.Builder alert = new AlertDialog.Builder(ReportActivity.this);
                alert.setTitle("Alert!");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteReport(extraArray[0]);
                        dialog.dismiss();
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViews() {
        diagnosticCodeView = findViewById(R.id.diagnosticCodeView);
        symptomStartDate = findViewById(R.id.symptomStartDate);
        symptomView[0] = findViewById(R.id.symptom0);
        symptomView[1] = findViewById(R.id.symptom1);
        symptomView[2] = findViewById(R.id.symptom2);
        symptomView[3] = findViewById(R.id.symptom3);
        symptomView[4] = findViewById(R.id.symptom4);
        symptomView[5] = findViewById(R.id.symptom5);
        symptomView[6] = findViewById(R.id.symptom6);
        symptomView[7] = findViewById(R.id.symptom7);
        symptomView[8] = findViewById(R.id.symptom8);
        symptomView[9] = findViewById(R.id.symptom9);
        symptomView[10] = findViewById(R.id.symptom10);
        closeContact = findViewById(R.id.closeContact);
        municipalityNameView = findViewById(R.id.municipalityNameView);
    }

    private void setTexts() {
        diagnosticCodeView.setText(extraArray[0]);
        symptomStartDate.setText(extraArray[1]);
        for (int i = 0; i < 11; i++)
            symptomView[i].setText(extraArray[i + 2]);
        closeContact.setText(extraArray[13]);
        municipalityNameView.setText(extraArray[14]);
    }

    private void addUpdate() {
        diagnosticCodeView = findViewById(R.id.diagnosticCodeView);
        diagnosticCodeView.setText(extraArray[0]);
        dialog[0] = new AlertDialog.Builder(this).create();
        editText[0] = new EditText(this);
        dialog[0].setTitle(" Edit text ");
        dialog[0].setView(editText[0]);

        symptomStartDate = findViewById(R.id.symptomStartDate);
        symptomStartDate.setText(extraArray[1]);
        dialog[1] = new AlertDialog.Builder(this).create();
        editText[1] = new EditText(this);
        dialog[1].setTitle(" Edit text ");
        dialog[1].setView(editText[1]);

        dialog[0].setButton(DialogInterface.BUTTON_POSITIVE, "Save text", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diagnosticCodeView.setText(editText[0].getText());
                extraArray[0] = diagnosticCodeView.getText().toString();
            }
        });
        diagnosticCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText[0].setText(diagnosticCodeView.getText());
                dialog[0].show();
            }
        });

        dialog[1].setButton(DialogInterface.BUTTON_POSITIVE, "Save text", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                symptomStartDate.setText(editText[1].getText());
                extraArray[1] = symptomStartDate.getText().toString();
            }
        });
        symptomStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText[1].setText(symptomStartDate.getText());
                dialog[1].show();
            }
        });

        symptomsCheckBox[0] = findViewById(R.id.symptom0Check);
        symptomsCheckBox[1] = findViewById(R.id.symptom1Check);
        symptomsCheckBox[2] = findViewById(R.id.symptom2Check);
        symptomsCheckBox[3] = findViewById(R.id.symptom3Check);
        symptomsCheckBox[4] = findViewById(R.id.symptom4Check);
        symptomsCheckBox[5] = findViewById(R.id.symptom5Check);
        symptomsCheckBox[6] = findViewById(R.id.symptom6Check);
        symptomsCheckBox[7] = findViewById(R.id.symptom7Check);
        symptomsCheckBox[8] = findViewById(R.id.symptom8Check);
        symptomsCheckBox[9] = findViewById(R.id.symptom9Check);
        symptomsCheckBox[10] = findViewById(R.id.symptom10Check);

        for(int i = 0; i < 11; i++)
            symptomsCheckBox[i].setChecked(extraArray[i+2].equals("Yes"));

        symptomsCheckBox[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[0].isChecked())
                    extraArray[2] = "Yes";
                else
                    extraArray[2] = "No";
            }
        });
        symptomsCheckBox[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[1].isChecked())
                    extraArray[3] = "Yes";
                else
                    extraArray[3] = "No";
            }
        });
        symptomsCheckBox[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[2].isChecked())
                    extraArray[4] = "Yes";
                else
                    extraArray[4] = "No";
            }
        });
        symptomsCheckBox[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[3].isChecked())
                    extraArray[5] = "Yes";
                else
                    extraArray[5] = "No";
            }
        });
        symptomsCheckBox[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[4].isChecked())
                    extraArray[6] = "Yes";
                else
                    extraArray[6] = "No";
            }
        });
        symptomsCheckBox[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[5].isChecked())
                    extraArray[7] = "Yes";
                else
                    extraArray[7] = "No";
            }
        });
        symptomsCheckBox[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[6].isChecked())
                    extraArray[8] = "Yes";
                else
                    extraArray[8] = "No";
            }
        });
        symptomsCheckBox[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[7].isChecked())
                    extraArray[9] = "Yes";
                else
                    extraArray[9] = "No";
            }
        });
        symptomsCheckBox[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[8].isChecked())
                    extraArray[10] = "Yes";
                else
                    extraArray[10] = "No";
            }
        });
        symptomsCheckBox[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[9].isChecked())
                    extraArray[11] = "Yes";
                else
                    extraArray[11] = "No";
            }
        });
        symptomsCheckBox[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(symptomsCheckBox[10].isChecked())
                    extraArray[12] = "Yes";
                else
                    extraArray[12] = "No";
            }
        });

        municipalityNameView = findViewById(R.id.municipalityNameView);
        municipalityNameView.setText(extraArray[14]);
        dialog[2] = new AlertDialog.Builder(this).create();
        editText[2] = new EditText(this);
        dialog[2].setTitle(" Edit text ");
        dialog[2].setView(editText[2]);

        dialog[2].setButton(DialogInterface.BUTTON_POSITIVE, "Save text", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                municipalityNameView.setText(editText[2].getText());
                extraArray[14] = municipalityNameView.getText().toString();
            }
        });
        municipalityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText[2].setText(municipalityNameView.getText());
                dialog[2].show();
            }
        });
    }

    // For spinner:
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT);
        if(text == "Yes")
            extraArray[13] = "Yes";
        else
            extraArray[13] = "No";
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}