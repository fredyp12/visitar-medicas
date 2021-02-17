package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Medico;
import com.example.visitas_medicas.modelo.Paciente;
import com.example.visitas_medicas.modelo.Visitas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventListener;
import java.util.UUID;

import static com.example.visitas_medicas.R.layout.layout_select;

public class Create_visita extends AppCompatActivity {


    private Spinner especialidad;
    private Spinner doctor;
    private EditText txtFecha;
    private EditText txtHora;
    private Button btnNew, btnCancel;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String[] optionEspecialiad,optionMedico;
    private ArrayList arrayEpecialidad;
    private Medico medico;
    private Activity activity;
    private Intent intent;

    private int dia,mes,año,hora,minutos;
    private boolean diaWeek=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_visita);

        Bundle extras = getIntent().getExtras();
        final String id=(extras.getString("id"));
        this.activity=this;
        this.especialidad=this.findViewById(R.id.select_especialidadP);
        this.doctor= this.findViewById(R.id.select_doctorP);
        this.txtFecha= this.findViewById(R.id.txtFecha_agendarP);
        this.txtHora=this.findViewById(R.id.txtHora_agendarP);
        this.btnNew=this.findViewById(R.id.btn_registrar_citaP);
        this.btnCancel= this.findViewById(R.id.btnCancel_agendar_citaP);

        this.btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(diaWeek==false && !txtFecha.getText().toString().equals("") && !txtHora.getText().toString().equals("")) {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Paciente p= new Paciente();
                            Medico m= new Medico();
                            if(snapshot.exists()) {
                                if(snapshot.child("Paciente").child(id).exists()) {
                                    p= snapshot.child("Paciente").child(id).getValue(Paciente.class);
                                }
                                String mS= doctor.getSelectedItem().toString(); //medico Spinner
                                for(DataSnapshot dt: snapshot.child("Medico").getChildren()) { //medico For
                                    Medico mF=dt.getValue(Medico.class);
                                    if(mF.getNombre().equalsIgnoreCase(mS)) {
                                        m=dt.getValue(Medico.class);
                                    }
                                }
                                //Insert public Visitas(String id, String paciente, String estado, String fecha, String doctor, String especialidad, String hora)
                                if(snapshot.child("Visitas").exists()) {
                                    int idVisita= (int) snapshot.child("Visitas").getChildrenCount();
                                    Visitas visitas= new Visitas(UUID.randomUUID().toString(),p.getId(),"POR APROBAR",txtFecha.getText().toString(),m.getId(),m.getEspecialidad(),txtHora.getText().toString());
                                    databaseReference.child("Visitas").child(visitas.getId()).setValue(visitas);
                                }else {
                                    Visitas visitas= new Visitas(UUID.randomUUID().toString(),p.getId(),"POR APROBAR",txtFecha.getText().toString(),m.getId(),m.getEspecialidad(),txtHora.getText().toString());
                                    databaseReference.child("Visitas").child(visitas.getId()).setValue(visitas);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    intent= new Intent(Create_visita.this,Administrar_citas_p.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }else {
                    if(diaWeek==true) {
                        Toast.makeText(activity,"Los dias de ateccion son de Lunes a Viernes",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(activity,"Deben llenarse todos los campos",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent= new Intent(Create_visita.this,Administrar_citas_p.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });



        final Calendar calendar= Calendar.getInstance();
        this.txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia=calendar.get(Calendar.DAY_OF_MONTH);
                mes=calendar.get(Calendar.MONTH);
                año=calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog= new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtFecha.setText(i2+"/"+(i1+1)+"/"+i);

                        calendar.set(i,i1,i2);
                        int day=calendar.get(Calendar.DAY_OF_WEEK);
                        if(day==Calendar.SUNDAY) {
                            diaWeek=true;
                        }else  {
                            if(day==Calendar.SATURDAY) {
                                diaWeek=true;
                            } else {
                                diaWeek=false;
                            }
                        }
                    }
                },
                        dia,mes,año);
                datePickerDialog.show();

            }
        });
        txtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora=calendar.get(Calendar.HOUR_OF_DAY);
                minutos=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog= new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        txtHora.setText(i+":"+i1);
                    }
                },
                        hora,minutos,false);
                timePickerDialog.show();
            }
        });

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
                                if(arrayEpecialidad.get(i).equals(medico.getEspecialidad())) {
                                    dat++;
                                    if(dat>=1) {
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
                             if(medico.getEspecialidad().equalsIgnoreCase(selecSpinner)) {
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