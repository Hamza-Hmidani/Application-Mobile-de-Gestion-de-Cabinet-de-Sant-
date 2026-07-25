package com.example.cabinetmedical.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "facture")
public class Facture {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int consultation_id;
    private double montant;
    private String status;

    public Facture(int consultation_id, double montant, String status) {
        this.consultation_id = consultation_id;
        this.montant = montant;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getConsultation_id() { return consultation_id; }
    public void setConsultation_id(int consultation_id) { this.consultation_id = consultation_id; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}