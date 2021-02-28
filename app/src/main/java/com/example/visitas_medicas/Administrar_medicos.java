package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Medico;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Administrar_medicos extends AppCompatActivity {
    private int adminId;
    private LinearLayout layout;
    private Button btnNew;
    private Medico medic;
    private Intent intent;
    private TableLayout table;
    private TableRow row;
    private TextView txtcell;
    private TextView txtMedicoNo;
    private Activity activity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_medicos);

        Bundle extras = getIntent().getExtras();
        this.adminId=extras.getInt("id");
        activity= this;
        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();
        this.layout= this.findViewById(R.id.layout_table);
        this.btnNew= this.findViewById(R.id.btnNew);

        createTable();

        this.btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table= new TableLayout(activity);
                databaseReference.child("Medico").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount()<10) {
                            intent= new Intent(Administrar_medicos.this,Create_medico.class);
                            intent.putExtra("id", adminId);
                            startActivity(intent);
                        }else {

                        }Toast.makeText(activity,"YA NO SE PUEDEN AÑADIR MAS MEDICOS",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    public void createTable() {
        databaseReference.child("Medico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtMedicoNo = new TextView(activity);
                if(snapshot.exists()) {
                    table= new TableLayout(activity);
                    for(DataSnapshot dt: snapshot.getChildren()) {
                        btnEdit= new Button(activity);
                        btnDelete = new Button(activity);
                        txtcell=new TextView(activity);
                        row= new TableRow(activity);
                        medic=dt.getValue(Medico.class);
                        txtcell.setText(medic.getId());
                        row.addView(txtcell);
                        txtcell= new TextView(activity);
                        txtcell.setText(medic.getNombre());
                        row.addView(txtcell);

                        final String idMedico=medic.getId();
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent= new Intent(Administrar_medicos.this,Edit_medico.class);
                                intent.putExtra("id", adminId);
                                intent.putExtra("idMedico",idMedico);
                                startActivity(intent);
                            }
                        });
                        row.addView(btnEdit);
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReference.child("Medico").child(idMedico).removeValue();
                                Toast.makeText(activity,"Medico ELiminado",Toast.LENGTH_LONG).show();
                                layout.removeView(table);
                            }
                        });
                        row.addView(btnDelete);
                        table.addView(row);
                    }
                    layout.addView(table);
                    txtMedicoNo.setText("");
                }else {
                    txtMedicoNo = new TextView(activity);
                    txtMedicoNo.setText("No hay Medicos aun");
                    layout.addView(txtMedicoNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}