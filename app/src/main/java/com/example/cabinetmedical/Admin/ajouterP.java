package com.example.cabinetmedical.Admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabinetmedical.R;
import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.dao.AppDatabase;
import com.example.cabinetmedical.dao.UserDao;

public class ajouterP extends AppCompatActivity {

    EditText etNom, etPrenom, etEmail, etnum_c;
    Button btnSave;

    AppDatabase db;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter);

        etNom = findViewById(R.id.etNom);
        etPrenom = findViewById(R.id.etPrenom);
        etEmail = findViewById(R.id.etEmail);
        etnum_c = findViewById(R.id.etnum_c);
        btnSave = findViewById(R.id.btnSave);

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        btnSave.setOnClickListener(v -> {

            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String numCarte = etnum_c.getText().toString().trim();

            if (nom.isEmpty() ||
                    prenom.isEmpty() ||
                    email.isEmpty() ||
                    numCarte.isEmpty()) {

                Toast.makeText(this,
                        "Remplir tous les champs",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // ================= GET MEDECIN ID =================
            int medcinId = getIntent().getIntExtra("id", 0);

            if (medcinId == 0) {

                Toast.makeText(this,
                        "Erreur medecin",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            // ================= CHECK IF EMAIL EXISTS =================
            if (userDao.getUserByEmail(email) != null) {
                Toast.makeText(this, "Email existe déjà", Toast.LENGTH_SHORT).show();
                return;
            }

            // ================= INSERT PATIENT =================
            user newUser = new user(0, nom, prenom, email, numCarte, "patient", medcinId);
            userDao.insert(newUser);

            Toast.makeText(this, "Patient ajouté", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}