package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.visitas_medicas.modelo.Paciente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_paciente extends AppCompatActivity {

    private Button btn_citasP, btn_administarP;
    private Intent intent;
    private TextView txtLabale;
    private Paciente paciente = new Paciente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_paciente);

        this.btn_citasP=this.findViewById(R.id.btnCitasP);
        this.btn_administarP=this.findViewById(R.id.btnAdministrarP);
        this.txtLabale=this.findViewById(R.id.txtLabel);
        recibir();

        this.btn_administarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(Home_paciente.this,Administrar_citas_p.class);
                intent.putExtra("id",paciente.getId());
                startActivity(intent);
            }
        });



    }
    public void recibir  () {
        Bundle extras = getIntent().getExtras();
        this.paciente.setId(extras.getString("id"));
        final DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
        mdatabase.child("Paciente").child(""+paciente.getId()).addValueEventListener(new ValueEventListener() {
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