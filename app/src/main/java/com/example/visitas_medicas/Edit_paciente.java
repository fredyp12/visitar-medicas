package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.visitas_medicas.modelo.Medico;
import com.example.visitas_medicas.modelo.Paciente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_paciente extends AppCompatActivity {
    private int adminId;
    private String idPaciente;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Paciente paciente= new Paciente();
    private Intent intent;
    private Button btnEditP,btnVolver;
    private EditText txtnombre, txtPass, txtEdotemailP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_paciente);

        this.txtnombre= this.findViewById(R.id.txt_editNombreP);
        this.txtPass= this.findViewById(R.id.txt_editPassP);
        this.txtEdotemailP= this.findViewById(R.id.txtEditEmailP);
        this.btnEditP=this.findViewById(R.id.btnEditP);
        Bundle extras = getIntent().getExtras();
        this.adminId=extras.getInt("id");
        this.idPaciente=extras.getString("idPaciente");
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();


        this.databaseReference.child("Paciente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paciente=snapshot.child(idPaciente).getValue(Paciente.class);
                txtnombre.setText(paciente.getNombre());
                txtPass.setText(paciente.getContraseña());
                txtEdotemailP.setText(paciente.getCorreo());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnEditP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paciente.setId(idPaciente);
                paciente.setNombre(txtnombre.getText().toString());
                paciente.setCorreo(txtEdotemailP.getText().toString());
                paciente.setContraseña(txtPass.getText().toString());

                databaseReference.child("Paciente").child(paciente.getId()).setValue(paciente);
                intent= new Intent(Edit_paciente.this,administrar_paciente.class);
                intent.putExtra("id",adminId);
                startActivity(intent);
            }
        });

    }
}