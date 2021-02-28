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
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Historial_clinico;
import com.example.visitas_medicas.modelo.Medico;
import com.example.visitas_medicas.modelo.Paciente;
import com.example.visitas_medicas.modelo.Visitas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.UUID;

public class Info_paciente extends AppCompatActivity {
    private Activity activity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Medico medico;
    private TextView txtNombre, txtID, txtEstado, txtvaloracion;
    private Intent intent;
    private Visitas visita;
    private Paciente paciente;
    private Historial_clinico historial;
    private Button btn_actualizar;
    private RadioButton rd_efectuado, rd_cancelado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_paciente);

        visita= new Visitas();
        Bundle extras = getIntent().getExtras();
        final String datID= extras.getString("visita_id");
        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        this.txtNombre= this.findViewById(R.id.txtInfo_nombre);
        this.txtID= this.findViewById(R.id.txtInfo_id);
        this.txtEstado= this.findViewById(R.id.txtInfo_estado);
        this.btn_actualizar= this.findViewById(R.id.btnInfo_actualizar);
        this.rd_efectuado=this.findViewById(R.id.rd_efectudo);
        this.rd_cancelado=this.findViewById(R.id.rd_cancelado);
        this.txtvaloracion=this.findViewById(R.id.txtInfo_valoracion);

        recibir();

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambiar estado de la visita
                if(rd_efectuado.isChecked()) {
                    databaseReference.child("Visitas").child(datID).child("estado").setValue("EFECTUADO");
                }
                if(rd_cancelado.isChecked()) {
                    databaseReference.child("Visitas").child(datID).child("estado").setValue("CANCELADO");
                }
                if(!txtvaloracion.getText().toString().equalsIgnoreCase("")) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dt: snapshot.child("Visitas").getChildren()) {
                                visita=dt.getValue(Visitas.class);
                                if(visita.getId().equalsIgnoreCase(datID)) {
                                    historial= new Historial_clinico();
                                    historial.setId(UUID.randomUUID().toString());
                                    historial.setFecha(visita.getFecha());
                                    historial.setMedico(visita.getDoctor());
                                    historial.setPaciente(visita.getPaciente());
                                    historial.setValoracion(txtvaloracion.getText().toString());
                                    databaseReference.child("Historial_medico").child(historial.getId()).setValue(historial);
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });



    }

    public void recibir() {
        Bundle extras = getIntent().getExtras();
        final String datID= extras.getString("visita_id");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Visitas").exists()) {
                    for(DataSnapshot dt: snapshot.child("Visitas").getChildren()) {
                        visita=dt.getValue(Visitas.class);
                        if(datID.equalsIgnoreCase(visita.getId())) {
                            txtEstado.setText("Estado "+visita.getEstado());
                            final String IDPaciente=visita.getPaciente();
                            //Info paciente
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.child("Paciente").exists()) {
                                        for(DataSnapshot dt: snapshot.child("Paciente").getChildren()) {
                                            paciente=dt.getValue(Paciente.class);
                                            if(IDPaciente.equalsIgnoreCase(paciente.getId())) {
                                                txtNombre.setText("Nombre "+paciente.getNombre());
                                                txtID.setText("ID "+paciente.getId());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}