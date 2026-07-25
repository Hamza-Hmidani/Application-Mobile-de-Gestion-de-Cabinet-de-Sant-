package com.example.cabinetmedical.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.cabinetmedical.Entity.user;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(user user);

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    user login(String email, String password);

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    user getUserByEmail(String email);

    @Query("SELECT * FROM user WHERE role = 'patient' AND medcin_id = :medcinId")
    List<user> getPatientsByMedcin(int medcinId);
    
    @Query("SELECT * FROM user WHERE id = :id")
    user getUserById(int id);
}
