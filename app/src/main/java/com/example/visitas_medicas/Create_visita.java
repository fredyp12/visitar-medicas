package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.visitas_medicas.modelo.Medico;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

import static com.example.visitas_medicas.R.layout.layout_select;

public class Create_visita extends AppCompatActivity {

    private String id;
    private Spinner especialidad;
    private Spinner doctor;
    private EditText fecha;
    private EditText hora;
    private Button btnNew;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String[] optionEspecialiad,optionMedico;
    private ArrayList arrayEpecialidad;
    private Medico medico;
    private Activity activity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_visita);


        Bundle extras = getIntent().getExtras();
        this.id=(extras.getString("id"));

        this.activity=this;
        this.especialidad=this.findViewById(R.id.select_especialidadP);
        this.doctor= this.findViewById(R.id.select_doctorP);


        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        databaseReference.child("Medico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    medico= new Medico();
                    for(DataSnapshot dt: snapshot.getChildren()) {
                        medico= dt.getValue(Medico.class);
                        if(arrayEpecialidad==null) {
                            arrayEpecialidad= new ArrayList();
                            arrayEpecialidad.add(medico.getEspecialidad());
                        }else {
                            boolean aux=false;
                            int dat=0;
                            for(int i=0; i<arrayEpecialidad.size();i++) {
                                if(arrayEpecialidad.get(i)==medico.getEspecialidad()) {
                                    dat++;
                                    if(dat>1) {
                                        aux=true;
                                    }
                                }
                            }
                            if(aux==false) {
                                arrayEpecialidad.add(medico.getEspecialidad());
                            }
                        }
                    }
                }
                optionEspecialiad= new String[arrayEpecialidad.size()];
                for(int i=0;i<optionEspecialiad.length;i++) {
                    optionEspecialiad[i]= (String) arrayEpecialidad.get(i);
                }
                ArrayAdapter <String> adapter= new ArrayAdapter<String>(activity, R.layout.layout_select,optionEspecialiad);
                especialidad.setAdapter(adapter);
//                ArrayList medicos= new ArrayList();
//                String selecSpinner=especialidad.getSelectedItem().toString();
//                for(DataSnapshot dt: snapshot.getChildren()) {
//                    medico= dt.getValue(Medico.class);
//                    if(medico.getEspecialidad()==selecSpinner) {
//                        medicos.add(medico.getNombre());
//                    }
//                }
//                if(medicos!= null) {
//                    optionMedico= new String[medicos.size()];
//                    for(int i=0; i<optionMedico.length; i++) {
//                        optionMedico[i]= (String) medicos.get(i);
//                    }
//                    ArrayAdapter <String> adapter1= new ArrayAdapter<String>(activity, R.layout.layout_select,optionMedico);
//                    doctor.setAdapter(adapter1);
//                }

                datSpinner();
            }
            


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        especialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                datSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void datSpinner() {
        databaseReference.child("Medico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(snapshot.exists()) {
                         medico= new Medico();
                         String selecSpinner=especialidad.getSelectedItem().toString();
                         ArrayList medicos= new ArrayList();
                         for(DataSnapshot dt: snapshot.getChildren()) {
                             medico= dt.getValue(Medico.class);
                             if(medico.getEspecialidad()==selecSpinner) {
                                 medicos.add(medico.getNombre());
                             }
                         }
                         if(medicos!= null) {
                             optionMedico= new String[medicos.size()];
                             for(int i=0; i<optionMedico.length; i++) {
                                 optionMedico[i]= (String) medicos.get(i);
                             }
                             ArrayAdapter <String> adapter1= new ArrayAdapter<String>(activity, R.layout.layout_select,optionMedico);
                             doctor.setAdapter(adapter1);
                         }
                     }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}