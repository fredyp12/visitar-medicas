package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.visitas_medicas.modelo.Administrador;
import com.example.visitas_medicas.modelo.Medico;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_medico extends AppCompatActivity {

    private Medico medico= new Medico();
    private TextView txtLabale;
    private Button btnAdministrar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_medico);

        this.txtLabale=this.findViewById(R.id.txtLabel_medico);
        this.btnAdministrar=this.findViewById(R.id.btn_administrar_citasM);
        recibir();

        this.btnAdministrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(Home_medico.this,Administrar_citas_m.class);
                intent.putExtra("id",medico.getId());
                startActivity(intent);
            }
        });
    }
    public void recibir () {
        Bundle extras = getIntent().getExtras();
        this.medico.setId(extras.getString("id"));
        final DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Medico").child(""+medico.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    txtLabale.setText("Que deseas hacer "+snapshot.child("nombre").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}