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
import com.example.visitas_medicas.modelo.Paciente;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class administrar_paciente extends AppCompatActivity {
    private int adminId;
    private Activity activity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LinearLayout layoutTable;
    private Button btnNew;
    private Button btnEdit, btnDelete;
    private Paciente paciente;
    private TextView txtPacenteNo;
    private TableLayout table;
    private TableRow row;
    private TextView txtcell;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_paciente);

        Bundle extras = getIntent().getExtras();
        this.adminId=extras.getInt("id");
        activity= this;
        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();
        this.btnNew= this.findViewById(R.id.btn_nuevo);
        this.layoutTable=this.findViewById(R.id.layout_tableP);

        createTable();

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(administrar_paciente.this,Create_paciente.class);
                intent.putExtra("id", adminId);
                startActivity(intent);
            }
        });



    }
    public void createTable() {
        databaseReference.child("Paciente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtPacenteNo = new TextView(activity);
                if(snapshot.exists()) {
                    table= new TableLayout(activity);
                    for(DataSnapshot dt: snapshot.getChildren()) {
                        btnEdit= new Button(activity);
                        btnDelete = new Button(activity);
                        txtcell=new TextView(activity);
                        row= new TableRow(activity);
                        paciente=dt.getValue(Paciente.class);
                        txtcell.setText(paciente.getId());
                        row.addView(txtcell);
                        txtcell= new TextView(activity);
                        txtcell.setText(paciente.getNombre());
                        row.addView(txtcell);

                        final String idPaciente=paciente.getId();
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent= new Intent(administrar_paciente.this,Edit_paciente.class);
                                intent.putExtra("id", adminId);
                                intent.putExtra("idPaciente",idPaciente);
                                startActivity(intent);
                            }
                        });
                        row.addView(btnEdit);
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                databaseReference.child("Paciente").child(idPaciente).removeValue();
                                Toast.makeText(activity,"Paciente ELiminado",Toast.LENGTH_LONG).show();
                                layoutTable.removeView(table);
                                createTable();
                            }
                        });
                        row.addView(btnDelete);
                        table.addView(row);
                    }
                    layoutTable.addView(table);
                    txtPacenteNo.setText("");
                }else {
                    txtPacenteNo = new TextView(activity);
                    txtPacenteNo.setText("No hay Pacientes aun");
                    layoutTable.addView(txtPacenteNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}