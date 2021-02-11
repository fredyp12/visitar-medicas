package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.visitas_medicas.modelo.Medico;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Create_medico extends AppCompatActivity {

    private EditText txtId,txtNombre,txtEspecialidad;
    private Button btnCreate;
    private Medico medico;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int adminId;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_medico);

        Bundle extras = getIntent().getExtras();
        this.adminId=(extras.getInt("id"));

        this.txtId= this.findViewById(R.id.txtId);
        this.txtNombre= this.findViewById(R.id.txtNombre);
        this.txtEspecialidad= this.findViewById(R.id.txtEspecialidad);
        this.btnCreate= this.findViewById(R.id.btn_create);

        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();



        this.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtId.getText().toString()!="" && txtNombre.getText().toString()!="" && txtEspecialidad.getText().toString()!="") {
                    medico= new Medico();

                    medico.setId(txtId.getText().toString());
                    medico.setNombre(txtNombre.getText().toString());
                    medico.setEspecialidad(txtEspecialidad.getText().toString());
                    medico.setContraseña(txtId.getText().toString());
                    medico.setPregunta("pregunta");
                    medico.setRespuesta("Respuesta");
                    databaseReference.child("Medico").child(medico.getId()).setValue(medico);

                    intent= new Intent(Create_medico.this,Administrar_medicos.class);
                    intent.putExtra("id",adminId);
                    startActivity(intent);

                }
            }
        });
    }

}