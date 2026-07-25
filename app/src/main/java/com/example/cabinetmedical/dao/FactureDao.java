package com.example.cabinetmedical.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.cabinetmedical.Entity.Facture;
import java.util.List;

@Dao
public interface FactureDao {
    @Insert
    void insert(Facture facture);

    @Query("SELECT * FROM facture WHERE consultation_id = :consultationId")
    List<Facture> getFacturesByConsultation(int consultationId);

    @Query("SELECT * FROM facture")
    List<Facture> getAllFactures();
}