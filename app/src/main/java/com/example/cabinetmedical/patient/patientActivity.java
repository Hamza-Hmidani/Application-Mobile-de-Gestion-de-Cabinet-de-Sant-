package com.example.cabinetmedical.patient;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.database.DatabaseHelper;

public class patientActivity extends AppCompatActivity {

    // ================= COLORS =================
    private static final int RED_PRIMARY = Color.parseColor("#D32F2F");
    private static final int RED_DARK    = Color.parseColor("#9A0007");
    private static final int WHITE       = Color.WHITE;
    private static final int BACKGROUND  = Color.parseColor("#F5F5F5");
    private static final int TEXT_BLACK  = Color.parseColor("#212121");
    private static final int TEXT_GRAY   = Color.parseColor("#757575");

    private int patientId;
    private String email;
    private TextView totalPriceText;
    private TextView welcomeText, emailText;
    private LinearLayout container;
    private com.example.cabinetmedical.dao.AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient);

        initData();
        initViews();
        // Since getConsultationsByPatient returns LiveData, we should observe it.
        // For quick unification, I'll use a simple observation or a sync fetch if I add one.
        loadConsultations();
    }

    // ---------- INIT ----------
    private void initData() {
        Intent intent = getIntent();
        patientId = intent.getIntExtra("id", 0);
        email = intent.getStringExtra("email");
        db = com.example.cabinetmedical.dao.AppDatabase.getInstance(this);
    }

    private void initViews() {
        totalPriceText = findViewById(R.id.totalPriceText);
        welcomeText = findViewById(R.id.welcomeText);
        emailText = findViewById(R.id.emailText);
        container = findViewById(R.id.consultationsContainer);

        welcomeText.setText("Bienvenue, Patient");
        welcomeText.setTextColor(RED_PRIMARY);
        emailText.setText(email != null ? email : "");
        findViewById(android.R.id.content).setBackgroundColor(BACKGROUND);
    }

    private void loadConsultations() {
        // We use the LiveData from ConsultationDao
        db.consultationDao().getConsultationsByPatient(patientId).observe(this, consultations -> {
            container.removeAllViews();
            double total = 0;

            if (consultations != null && !consultations.isEmpty()) {
                for (com.example.cabinetmedical.model.Consultation c : consultations) {
                    container.addView(buildCard(c));
                    total += c.getMontant();
                }
            } else {
                container.addView(emptyView());
            }

            totalPriceText.setText(String.format("%.2f MAD", total));
            totalPriceText.setTextColor(RED_PRIMARY);
        });
    }

    // ---------- CARD UI ----------
    private View buildCard(com.example.cabinetmedical.model.Consultation consultation) {

        long id = consultation.getId();
        String desc = consultation.getDiagnostic();
        String date = consultation.getDate();
        double prix = consultation.getMontant();
        
        // Fetch doctor name from UserDao since it's not in Consultation
        com.example.cabinetmedical.Entity.user doctor = db.userDao().getUserById((int)consultation.getMedecinId());
        String prenom = doctor != null ? doctor.getPrenom() : "Dr.";
        String nom = doctor != null ? doctor.getNom() : "";

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(WHITE);
        card.setPadding(0, 0, 0, 0);

        card.addView(createTopBar());
        card.addView(createBody((int)id, desc, date, prix, prenom, nom));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(params);

        return card;
    }

    private View createTopBar() {
        View bar = new View(this);
        bar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(4)));
        bar.setBackgroundColor(RED_PRIMARY);
        return bar;
    }

    private LinearLayout createBody(int id, String desc, String date,
                                    double prix, String prenom, String nom) {

        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(16), dp(12), dp(16), dp(16));

        body.addView(headerRow(id, prenom, nom));
        body.addView(description(desc));
        body.addView(divider());
        body.addView(bottomRow(date, prix));

        return body;
    }

    // ---------- ROWS ----------
    private LinearLayout headerRow(int id, String prenom, String nom) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView badge = new TextView(this);
        badge.setText("#" + id);
        badge.setBackgroundColor(RED_PRIMARY);
        badge.setTextColor(WHITE);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setPadding(dp(8), dp(4), dp(8), dp(4));

        TextView doctor = new TextView(this);
        doctor.setText("Dr. " + prenom + " " + nom);
        doctor.setTextColor(TEXT_BLACK);
        doctor.setTypeface(null, Typeface.BOLD);
        doctor.setPadding(dp(8), 0, 0, 0);

        row.addView(badge);
        row.addView(doctor);

        return row;
    }

    private TextView description(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(TEXT_GRAY);
        tv.setPadding(0, dp(8), 0, 0);
        return tv;
    }

    private View divider() {
        View v = new View(this);
        v.setBackgroundColor(Color.parseColor("#DDDDDD"));
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1)));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
        params.setMargins(0, dp(12), 0, dp(12));
        return v;
    }

    private LinearLayout bottomRow(String date, double prix) {

        LinearLayout row = new LinearLayout(this);

        TextView dateTv = new TextView(this);
        dateTv.setText("📅 " + date);
        dateTv.setTextColor(TEXT_GRAY);
        dateTv.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView priceTv = new TextView(this);
        priceTv.setText(String.format("%.2f MAD", prix));
        priceTv.setTextColor(RED_PRIMARY);
        priceTv.setTypeface(null, Typeface.BOLD);

        row.addView(dateTv);
        row.addView(priceTv);

        return row;
    }

    // ---------- EMPTY ----------
    private TextView emptyView() {
        TextView tv = new TextView(this);
        tv.setText("Aucune consultation trouvée.");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.GRAY);
        tv.setPadding(0, dp(20), 0, dp(20));
        return tv;
    }

    // ---------- UTIL ----------
    private int dp(int v) {
        return Math.round(v * getResources().getDisplayMetrics().density);
    }
}