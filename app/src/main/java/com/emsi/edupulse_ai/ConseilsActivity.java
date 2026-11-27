package com.emsi.edupulse_ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ConseilsActivity extends AppCompatActivity {

    private TextView tvModule, tvVigilance;
    private ListView listConseils;
    private RadioGroup rgRessenti;
    private CheckBox cbComprehension, cbTemps, cbMotivation, cbOrganisation;
    private Button btnEnvoyer, btnRetour;

    private String moduleName;
    private String vigilance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseils);

        // Toolbar avec flèche de retour
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // findViewById : relier le Java au XML
        tvModule = findViewById(R.id.tvModule);
        tvVigilance = findViewById(R.id.tvVigilance);
        listConseils = findViewById(R.id.listConseils);
        rgRessenti = findViewById(R.id.rgRessenti);
        cbComprehension = findViewById(R.id.cbComprehension);
        cbTemps = findViewById(R.id.cbTemps);
        cbMotivation = findViewById(R.id.cbMotivation);
        cbOrganisation = findViewById(R.id.cbOrganisation);
        btnEnvoyer = findViewById(R.id.btnEnvoyer);
        btnRetour = findViewById(R.id.btnRetour);

        // 1) Récupérer les données envoyées par l'activité précédente (Intent)
        Intent intent = getIntent();
        moduleName = intent.getStringExtra("module_name");
        vigilance = intent.getStringExtra("vigilance");
        if (moduleName == null) moduleName = "Général";
        if (vigilance == null) vigilance = "moyen";

        tvModule.setText("Module : " + moduleName);
        tvVigilance.setText("Vigilance : " + vigilance);

        // 2) Données (ArrayList) + ArrayAdapter + ListView
        final ArrayList<String> conseils = getConseils(vigilance);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                conseils
        );
        listConseils.setAdapter(adapter);

        // Clic sur un conseil : on l'affiche dans un Toast
        listConseils.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String conseil = (String) parent.getItemAtPosition(position);
                Toast.makeText(ConseilsActivity.this, conseil, Toast.LENGTH_SHORT).show();
            }
        });

        // 3) Bouton Envoyer : vérifier le ressenti puis demander confirmation
        btnEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rgRessenti.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(ConseilsActivity.this,
                            "Veuillez indiquer votre ressenti", Toast.LENGTH_SHORT).show();
                } else {
                    afficherConfirmation();
                }
            }
        });

        // 4) Bouton Retour -> revient au Dashboard
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** Liste de conseils selon le niveau de vigilance. */
    private ArrayList<String> getConseils(String niveau) {
        ArrayList<String> liste = new ArrayList<>();
        if (niveau.equalsIgnoreCase("faible")) {
            liste.add("Continuez sur cette bonne lancée");
            liste.add("Révisez régulièrement pour consolider vos acquis");
            liste.add("Aidez vos camarades : expliquer renforce la compréhension");
        } else if (niveau.equalsIgnoreCase("élevé") || niveau.equalsIgnoreCase("eleve")) {
            liste.add("Contactez votre enseignant dès que possible");
            liste.add("Planifiez des sessions courtes mais quotidiennes");
            liste.add("Reprenez les bases du module avant les exercices");
            liste.add("Demandez un soutien personnalisé ci-dessous");
        } else { // moyen
            liste.add("Planifiez vos révisions chaque semaine");
            liste.add("Faites les exercices corrigés du module");
            liste.add("Posez vos questions en cours ou en TD");
        }
        return liste;
    }

    /** Construit le résumé de la demande. */
    private String construireResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("Module : ").append(moduleName).append("\n");

        int id = rgRessenti.getCheckedRadioButtonId();
        if (id == R.id.rbBien) {
            sb.append("Ressenti : bien\n");
        } else if (id == R.id.rbMoyen) {
            sb.append("Ressenti : un peu perdu(e)\n");
        } else if (id == R.id.rbDifficile) {
            sb.append("Ressenti : grande difficulté\n");
        }

        sb.append("Difficultés :");
        if (cbComprehension.isChecked()) sb.append(" compréhension");
        if (cbTemps.isChecked()) sb.append(" temps");
        if (cbMotivation.isChecked()) sb.append(" motivation");
        if (cbOrganisation.isChecked()) sb.append(" organisation");
        if (!cbComprehension.isChecked() && !cbTemps.isChecked()
                && !cbMotivation.isChecked() && !cbOrganisation.isChecked()) {
            sb.append(" aucune");
        }
        return sb.toString();
    }

    /** AlertDialog de confirmation avant l'envoi. */
    private void afficherConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer la demande")
                .setMessage(construireResume() + "\n\nEnvoyer la demande de soutien ?")
                .setPositiveButton("Envoyer", (dialog, which) -> {
                    Toast.makeText(ConseilsActivity.this,
                            "Demande de soutien envoyée !", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    /** Flèche de la Toolbar -> retour. */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
