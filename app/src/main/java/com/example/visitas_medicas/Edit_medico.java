package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.visitas_medicas.modelo.Medico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Edit_medico extends AppCompatActivity {

    private int adminId;
    private String medicoId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Medico medico= new Medico();
    private Intent intent;
    private Button btnEdit,btnVolver;
    private EditText txtnombre, txtEspecialidad, txtPregunta, txtRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medico);

        this.btnEdit=this.findViewById(R.id.btnEditar);
        this.txtnombre=this.findViewById(R.id.txtNombre);
        this.txtEspecialidad=this.findViewById(R.id.txtEspecialidad);
        this.txtPregunta=this.findViewById(R.id.txtPegrunta);
        this.txtRespuesta=this.findViewById(R.id.txtRespuesta);

        Bundle extras = getIntent().getExtras();
        this.adminId=extras.getInt("id");
        this.medicoId=extras.getString("idMedico");
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        this.databaseReference.child("Medico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medico=snapshot.child(medicoId).getValue(Medico.class);
                txtnombre.setText(medico.getNombre());
                txtEspecialidad.setText(medico.getEspecialidad());
                txtPregunta.setText(medico.getPregunta());
                txtRespuesta.setText(medico.getRespuesta());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medico.setId(medicoId);
                medico.setNombre(txtnombre.getText().toString());
                medico.setEspecialidad(txtEspecialidad.getText().toString());
                medico.setPregunta(txtPregunta.getText().toString());
                medico.setRespuesta(txtRespuesta.getText().toString());

                databaseReference.child("Medico").child(medico.getId()).setValue(medico);
                intent= new Intent(Edit_medico.this,Administrar_medicos.class);
                intent.putExtra("id",adminId);
                startActivity(intent);

            }
        });






    }
}