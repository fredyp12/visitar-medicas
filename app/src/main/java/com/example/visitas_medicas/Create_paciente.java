package com.example.visitas_medicas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.visitas_medicas.modelo.Medico;
import com.example.visitas_medicas.modelo.Paciente;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_paciente extends AppCompatActivity {
    private EditText txtId,txtNombre, txtContraseña, txtEmail;
    private Button btnCreate;
    private Paciente paciente;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int adminId;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_paciente);

        Bundle extras = getIntent().getExtras();
        this.adminId=(extras.getInt("id"));

        this.txtId= this.findViewById(R.id.txt_id);
        this.txtNombre= this.findViewById(R.id.txt_nombre);
        this.txtContraseña= this.findViewById(R.id.txt_pass);
        this.txtEmail= this.findViewById(R.id.txt_createEmail);
        this.btnCreate= this.findViewById(R.id.btnCrear);

        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtId.getText().toString()!="" && txtNombre.getText().toString()!="" && txtContraseña.getText().toString()!="") {
                    paciente= new Paciente();

                    paciente.setId(txtId.getText().toString());
                    paciente.setNombre(txtNombre.getText().toString());
                    paciente.setContraseña(txtContraseña.getText().toString());
                    paciente.setCorreo(txtEmail.getText().toString());
                    databaseReference.child("Paciente").child(paciente.getId()).setValue(paciente);

                    intent= new Intent(Create_paciente.this,administrar_paciente.class);
                    intent.putExtra("id",adminId);
                    startActivity(intent);

                }
            }
        });
    }
}