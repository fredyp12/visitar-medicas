package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.visitas_medicas.modelo.Administrador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home_admin extends AppCompatActivity {

    private Administrador admin = new Administrador();
    private TextView txtusuario;
    private Button btnMedicos,btnPaciente, btnSalir;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        this.btnMedicos= this.findViewById(R.id.btnMedicos);
        this.btnPaciente= this.findViewById(R.id.btn_pacientes);
        this.btnSalir= this.findViewById(R.id.brn_salirAdmin);
        recibir();

        this.txtusuario= this.findViewById(R.id.txtHome);
        this.btnMedicos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(home_admin.this,Administrar_medicos.class);
                intent.putExtra("id",admin.getId());
                startActivity(intent);
            }
        });
        this.btnPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(home_admin.this,administrar_paciente.class);
                intent.putExtra("id",admin.getId());
                startActivity(intent);
            }
        });

        this.btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(home_admin.this,MainActivity.class);
                startActivity(intent);
            }
        });





    }

    public void recibir  () {
        Bundle extras = getIntent().getExtras();
        this.admin.setId(Integer.parseInt(extras.getString("id")));
        final DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Administrador").child(""+admin.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    txtusuario.setText("Que deseas hacer "+snapshot.child("nombre").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}