/*
 * Projet : EduPulse AI
 * Fichier : DashboardActivity.java
 * Description : Tableau de bord (message de bienvenue, niveau de vigilance, progression, navigation)
 * Responsable : HAFSA AJABBOUNE
 */
package com.emsi.edupulse_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvVigilance, tvProgression;
    private Button btnModules, btnSuivi, btnConseils;
    private String username = "", role = "";
    private String vigilance = "Moyen"; // Valeur fictive pour l'instant

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // 1. Récupérer les données de LoginActivity
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");
            role = intent.getStringExtra("role");
        }
        if (username == null || username.isEmpty()) username = "Étudiant";

        // 2. Configurer la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tableau de bord");
        }

        // 3. Lier les vues
        tvWelcome = findViewById(R.id.tvWelcome);
        tvVigilance = findViewById(R.id.tvVigilance);
        tvProgression = findViewById(R.id.tvProgression);
        btnModules = findViewById(R.id.btnModules);
        btnSuivi = findViewById(R.id.btnSuivi);
        btnConseils = findViewById(R.id.btnConseils);

        // 4. Afficher les infos
        tvWelcome.setText("Bienvenue, " + username + " (" + role + ")");
        tvVigilance.setText("Niveau de vigilance : " + vigilance);
        tvProgression.setText("Progression : 65% — 3 modules suivis");

        // 5. Bouton Mes modules
        btnModules.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, ModulesActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        });

        // 6. Bouton Mon suivi
        btnSuivi.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, SuiviActivity.class);
            startActivity(i);
        });

        // 7. Bouton Conseils
        btnConseils.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, ConseilsActivity.class);
            i.putExtra("vigilance", vigilance);
            startActivity(i);
        });

        // 8. PopupMenu sur le message de bienvenue
        tvWelcome.setOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.menu_dashboard, popup.getMenu());
        popup.setOnMenuItemClickListener(this::handleMenuItem);
        popup.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return handleMenuItem(item);
    }

    private boolean handleMenuItem(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profil) {
            Toast.makeText(this, "Profil de " + username, Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.action_apropos) {
            new AlertDialog.Builder(this)
                    .setTitle("À propos")
                    .setMessage("EduPulse AI\nAssistant mobile d'aide à la réussite des étudiants.\nVersion 1.0")
                    .setPositiveButton("OK", null)
                    .show();
            return true;

        } else if (id == R.id.action_deconnexion) {
            new AlertDialog.Builder(this)
                    .setTitle("Déconnexion")
                    .setMessage("Voulez-vous vraiment vous déconnecter ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    })
                    .setNegativeButton("Non", null)
                    .show();
            return true;
        }
        return false;
    }
}