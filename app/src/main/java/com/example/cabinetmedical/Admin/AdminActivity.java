package com.example.cabinetmedical.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.consultation.ConsultationActivity;
import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.dao.AppDatabase;
import com.example.cabinetmedical.model.Consultation;

public class AdminActivity extends AppCompatActivity {

    LinearLayout patientContainer, consultationContainer;
    TextView tvCount, tvRevenu, Adminn;
    AppDatabase db;
    int medcinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1admin);

        // Initialisation de la base de données Room
        db = AppDatabase.getInstance(this);

        patientContainer = findViewById(R.id.patientContainer);
        consultationContainer = findViewById(R.id.consultationContainer);

        tvCount = findViewById(R.id.tvStatConsultCount);
        tvRevenu = findViewById(R.id.tvStatRevenu);
        Adminn = findViewById(R.id.Adminn);

        // ================= GET MEDECIN ID =================
        medcinId = getIntent().getIntExtra("id", 0);

        if (medcinId == 0) {
            Toast.makeText(this, "Medcin ID invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // ================= NOM PRENOM =================
        String nomMedcin = getIntent().getStringExtra("nom");
        String prenomMedcin = getIntent().getStringExtra("prenom");
        Adminn.setText("Bienvenue " + prenomMedcin + " " + nomMedcin);

        // ================= BUTTON AJOUT PATIENT =================
        findViewById(R.id.btnAddPatient).setOnClickListener(v -> {
            Intent i = new Intent(this, ajouterP.class);
            i.putExtra("id", medcinId);
            startActivity(i);
        });
    }

    // ================= CYCLE DE VIE : REFRESH AUTOMATIQUE =================
    @Override
    protected void onResume() {
        super.onResume();
        // À chaque fois qu'on retourne sur cet écran, on rafraîchit les listes
        if (medcinId != 0) {
            chargerDonnees();
        }
    }

    // ================= MÉTHODE DE CHARGEMENT DES DONNÉES =================
    private void chargerDonnees() {
        // IMPORTANT : On vide les anciens affichages pour éviter les doublons au rafraîchissement
        patientContainer.removeAllViews();
        consultationContainer.removeAllViews();

        // ================= CHARGEMENT DES PATIENTS =================
        java.util.List<user> patients = db.userDao().getPatientsByMedcin(medcinId);

        for (user p : patients) {
            int patientId = p.getId();
            String nom = p.getNom();
            String prenom = p.getPrenom();
            String email = p.getEmail();

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(30, 30, 30, 30);
            card.setBackgroundResource(R.drawable.card_light);

            // Petite astuce de marge pour espacer les cartes
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 20);
            card.setLayoutParams(params);

            TextView name = new TextView(this);
            name.setText("👤 " + prenom + " " + nom);
            name.setTextColor(getResources().getColor(R.color.text_black));
            name.setTextSize(18);
            name.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView mail = new TextView(this);
            mail.setText(email);
            mail.setTextColor(getResources().getColor(R.color.primary_red));

            card.addView(name);
            card.addView(mail);

            // ================= OUVRE LA CONSULTATION =================
            card.setOnClickListener(v -> {
                Intent i = new Intent(this, ConsultationActivity.class);
                i.putExtra("patient_id", patientId);
                i.putExtra("medcin_id", (long)medcinId);
                startActivity(i);
            });

            patientContainer.addView(card);
        }

        // ================= CHARGEMENT DES CONSULTATIONS & STATS =================
        java.util.List<Consultation> consultations = db.consultationDao().getAllConsultations(); 
        int count = 0;
        double total = 0;

        for (Consultation c : consultations) {
            if (c.getMedecinId() == medcinId) {
                count++;
                total += c.getMontant();

                String description = c.getDiagnostic();
                String date = c.getDate();

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(30, 30, 30, 30);
                card.setBackgroundResource(R.drawable.card_light);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 20);
                card.setLayoutParams(params);

                TextView t1 = new TextView(this);
                t1.setText("🩺 " + description);
                t1.setTextColor(getResources().getColor(R.color.text_black));
                t1.setTextSize(18);
                t1.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView t2 = new TextView(this);
                t2.setText("📅 " + date);
                t2.setTextColor(getResources().getColor(R.color.primary_red));

                card.addView(t1);
                card.addView(t2);

                consultationContainer.addView(card);
            }
        }

        // Mise à jour des compteurs statistiques globaux
        tvCount.setText(String.valueOf(count));
        tvRevenu.setText(String.format("%.0f MAD", total));
    }
}
