package smalcuga.uv.es;

import android.provider.BaseColumns;

// Class representing columns of the data base.
public class ReportsColumns {

    public static abstract class ReportEntry implements BaseColumns {

        public static final String TABLE_NAME = "Report_table";
        public static final String COL_1 = "Diagnostic_code";
        public static final String COL_2 = "Symptom_start_date";

        public static final String SYMPTOMS_1 = "FEVER_OR_CHILLS";
        public static final String SYMPTOMS_2 = "COUGH";
        public static final String SYMPTOMS_3 = "BREATHING_PROBLEMS";
        public static final String SYMPTOMS_4 = "FATIGUE";
        public static final String SYMPTOMS_5 = "MUSCLE_OR_BODY_ACHES";
        public static final String SYMPTOMS_6 = "HEADACHE";
        public static final String SYMPTOMS_7 = "NEW_LOSS_OF__TASTE_OR_SMELL";
        public static final String SYMPTOMS_8 = "SORE_THROAT";
        public static final String SYMPTOMS_9 = "CONGESTION_OR_RUNNY_NOSE";
        public static final String SYMPTOMS_10 = "NAUSEA_OR_VOMITING";
        public static final String SYMPTOMS_11 = "DIARRHEA";

        public static final String COL_4 = "Close_contact";
        public static final String COL_5 = "Municipality";
    }
}
