package com.example.cabinetmedical.dao; // ou com.example.cabinetmedical.database selon ton choix

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.cabinetmedical.model.Consultation;
import com.example.cabinetmedical.model.Medecin;
import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.Entity.Facture;

@Database(entities = {Consultation.class, Medecin.class, user.class, Facture.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract ConsultationDao consultationDao();
    public abstract MedecinDao medecinDao();
    public abstract UserDao userDao();
    public abstract FactureDao factureDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cabinet_medical_db"
                    ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    
}