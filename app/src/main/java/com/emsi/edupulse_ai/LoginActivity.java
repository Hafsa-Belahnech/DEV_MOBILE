/*
 * Projet : EduPulse AI
 * Fichier : LoginActivity.java
 * Description : Interface d'authentification (identifiant, mot de passe, profil Étudiant/Enseignant)
 * Responsable : HAFSA BELAHNECH
*/
package com.emsi.edupulse_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Clés utilisées par DashboardActivity : getStringExtra("username") / getStringExtra("role")
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_ROLE = "role";

    private EditText etUsername, etPassword;
    private RadioGroup rgRole;
    private RadioButton rbStudent, rbTeacher;
    private Button btnLogin;

    // Comptes fictifs (données statiques, pas de base de données pour ce TP)
    private final Map<String, String> mots2passe = new HashMap<>();
    private final Map<String, Boolean> estEtudiant = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        rbStudent = findViewById(R.id.rbStudent);
        rbTeacher = findViewById(R.id.rbTeacher);
        btnLogin = findViewById(R.id.btnLogin);

        initialiserComptes();

        btnLogin.setOnClickListener(v -> tenterConnexion());

        // Valider depuis la touche "Terminé" du clavier
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tenterConnexion();
                return true;
            }
            return false;
        });
    }

    private void initialiserComptes() {
        mots2passe.put("etudiant", "1234");
        estEtudiant.put("etudiant", true);

        mots2passe.put("amine", "edu2026");
        estEtudiant.put("amine", true);

        mots2passe.put("prof", "1234");
        estEtudiant.put("prof", false);
    }

    private void tenterConnexion() {
        String user = etUsername.getText().toString().trim().toLowerCase(Locale.ROOT);
        String pwd = etPassword.getText().toString();

        // 1) Champs non vides
        if (user.isEmpty()) {
            etUsername.setError(getString(R.string.error_username_empty));
            etUsername.requestFocus();
            return;
        }
        if (pwd.isEmpty()) {
            etPassword.setError(getString(R.string.error_password_empty));
            etPassword.requestFocus();
            return;
        }

        // 2) Compte existant et mot de passe correct
        if (!mots2passe.containsKey(user) || !pwd.equals(mots2passe.get(user))) {
            Toast.makeText(this, R.string.error_credentials, Toast.LENGTH_SHORT).show();
            return;
        }

        // 3) Profil choisi cohérent avec le compte
        boolean profilEtudiantChoisi = rbStudent.isChecked();
        if (profilEtudiantChoisi != estEtudiant.get(user)) {
            Toast.makeText(this, R.string.error_role_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        // 4) Connexion réussie -> Tableau de bord
        String role = getString(profilEtudiantChoisi ? R.string.role_student : R.string.role_teacher);
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.putExtra(EXTRA_USERNAME, user);
        intent.putExtra(EXTRA_ROLE, role);
        startActivity(intent);
        finish(); // empêche le retour vers l'écran de connexion avec le bouton Retour
    }
}
