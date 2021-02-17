package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Paciente;
import com.example.visitas_medicas.modelo.Visitas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Administrar_citas_p extends AppCompatActivity {

    private Activity activity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LinearLayout layoutTable;
    private Button btnNew;
    private Button btnDelete;
    private Paciente paciente;
    private TextView txtCitaNP;
    private TableLayout table;
    private TableRow row;
    private TextView txtcell;
    private Intent intent;
    private Visitas visita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_citas_p);

        this.btnNew=this.findViewById(R.id.btnNewCitaP);
        this.layoutTable=this.findViewById(R.id.layoutCitaP);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        activity= this;

        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();
        this.btnNew= this.findViewById(R.id.btnNewCitaP);
        this.layoutTable=this.findViewById(R.id.layoutCitaP);

        createTable(id);

        this.btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(Administrar_citas_p.this,Create_visita.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }
    public void createTable(String id) {
        table= new TableLayout(activity);
        layoutTable.addView(table);
        layoutTable.removeView(table);
        final String p=id;
        databaseReference.child("Visitas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtCitaNP = new TextView(activity);
                if(snapshot.exists()) {
                    table= new TableLayout(activity);
                    for(DataSnapshot dt: snapshot.getChildren()) {
                        visita=dt.getValue(Visitas.class);
//                        String dat= (String) dt.child("paciente").getValue(); //?¿
                        if(visita.getPaciente().equals(p)) {
                            Log.d("show",visita.getId());
                            btnDelete = new Button(activity);
//                            txtcell=new TextView(activity);
//                            row= new TableRow(activity);
//                            visita=dt.getValue(Visitas.class);
//                            txtcell.setText(visita.getId());
                            row= new TableRow(activity);
                            txtcell= new TextView(activity);
                            txtcell.setText(visita.getFecha());
                            row.addView(txtcell);
                            txtcell= new TextView(activity);
                            txtcell.setText(visita.getEstado());
                            row.addView(txtcell);
                            txtcell= new TextView(activity);
                            txtcell.setText(visita.getDoctor());
                            row.addView(txtcell);
                            txtcell= new TextView(activity);
                            txtcell.setText(visita.getEspecialidad());
                            row.addView(txtcell);
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    layoutTable.removeView(table);
                                    databaseReference.child("Visitas").child(visita.getId()).removeValue();
                                    Toast.makeText(activity,"visita Medica Cancelada",Toast.LENGTH_LONG).show();
                                    layoutTable.removeView(table);
                                    txtCitaNP.setText("");
                                    createTable(p);
                                }
                            });
                            row.addView(btnDelete);
                            table.addView(row);
                            txtCitaNP.setText("");
                        }
                    }
                    layoutTable.addView(table);

                }else {
                    txtCitaNP = new TextView(activity);
                    txtCitaNP.setText("No hay Citas Medicas aun");
                    layoutTable.addView(txtCitaNP);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}