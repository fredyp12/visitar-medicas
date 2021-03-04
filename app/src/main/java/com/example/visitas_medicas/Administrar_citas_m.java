package com.example.visitas_medicas;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Medico;
import com.example.visitas_medicas.modelo.Visitas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Administrar_citas_m extends AppCompatActivity {

    private Activity activity;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LinearLayout layoutTable;
    private Button btnAdd;
    private Button btnDelete, btnShow;
    private Medico medico;
    private TextView txtNothing, txtfecha_intro;
    private TableLayout table;
    private TableRow row;
    private TextView txtcell;
    private Intent intent;
    private Visitas visita;
    private String fecha;


    private int dia,mes,año,hora,minutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_citas_m);

        this.btnShow=this.findViewById(R.id.btn_ver_citas_m);
        this.btnAdd= this.findViewById(R.id.btn_add_citas_m);
        this.layoutTable=this.findViewById(R.id.layoutCitaP);
        this.txtfecha_intro=this.findViewById(R.id.txt_fecha_citasM);

        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        activity= this;

        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();
        this.layoutTable=this.findViewById(R.id.container_citas_m);

        this.txtfecha_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar= Calendar.getInstance();
                dia=calendar.get(Calendar.DAY_OF_MONTH);
                mes=calendar.get(Calendar.MONTH);
                año=calendar.get(Calendar.YEAR);
                fecha=dia+"/"+mes+"/"+año;
                DatePickerDialog datePickerDialog= new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txtfecha_intro.setText(i2+"/"+(i1+1)+"/"+i);
                        calendar.set(i,i1,i2);
                    }
                },
                        dia,mes,año);
                datePickerDialog.show();
            }
        });


        this.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Visitas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!txtfecha_intro.getText().toString().equals("Selecionar fehcha")) {
                            if(snapshot.exists()) {
                                table= new TableLayout(activity);
                                for(DataSnapshot dt: snapshot.getChildren()) {
                                    visita=dt.getValue(Visitas.class);
                                    if(visita.getDoctor().equals(id) && txtfecha_intro.getText().toString().equals(visita.getFecha()) && visita.getEstado().equals("APROBADO")) {
                                        btnDelete = new Button(activity);
                                        txtcell=new TextView(activity);
                                        row= new TableRow(activity);
                                        txtcell.setText(visita.getFecha());
                                        row.addView(txtcell);
                                        txtcell= new TextView(activity);
                                        txtcell.setText(visita.getPaciente());
                                        row.addView(txtcell);
                                        txtcell= new TextView(activity);
                                        txtcell.setText(visita.getEstado());
                                        row.addView(txtcell);
                                        final String idVisita=visita.getId();
                                        btnDelete.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
//                                                    layoutTable.removeView(table);
////                                                    databaseReference.child("Visita").child(idVisita).removeValue();
////                                                    Toast.makeText(activity,"Visita Eliminada",Toast.LENGTH_LONG).show();
                                                    intent= new Intent(Administrar_citas_m.this,Info_paciente.class);
                                                    intent.putExtra("visita_id",idVisita);
                                                    startActivity(intent);
                                                }
                                            });

                                        row.addView(btnDelete);
                                        table.addView(row);
                                    }
                                }
                                layoutTable.addView(table);
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

     this.btnAdd.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             databaseReference.child("Visitas").addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(!txtfecha_intro.getText().toString().equals("Selecionar fehcha")) {
                         table= new TableLayout(activity);
                         txtNothing= new TextView(activity);
                         txtNothing.setText("");
                         table.addView(txtNothing);
                         layoutTable.addView(table);
                         layoutTable.removeView(table);
                         for(final DataSnapshot dt: snapshot.getChildren()) {
                             visita=dt.getValue(Visitas.class);
                             if(visita.getDoctor().equals(id) && txtfecha_intro.getText().toString().equals(visita.getFecha()) && visita.getEstado().equalsIgnoreCase("POR APROBAR")) {
                                 Log.d("estado","._.");
                                 btnDelete = new Button(activity);
                                 txtcell=new TextView(activity);
                                 row= new TableRow(activity);
                                 txtcell.setText(visita.getFecha());
                                 row.addView(txtcell);
                                 txtcell= new TextView(activity);
                                 txtcell.setText(visita.getPaciente());
                                 row.addView(txtcell);
                                 txtcell= new TextView(activity);
                                 txtcell.setText(visita.getEstado());
                                 row.addView(txtcell);
                                 final String idVisita=visita.getId();
                                 btnDelete.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) { //editar
                                         databaseReference.child("Visitas").addValueEventListener(new ValueEventListener() {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                 int dat=0;
                                                 Visitas visitas= new Visitas();
                                                 for(DataSnapshot dt: snapshot.getChildren()) {
                                                     Visitas visita1=dt.getValue(Visitas.class);
                                                     if (visita1.getId().equalsIgnoreCase(idVisita)) {
                                                         visitas=visita1;
                                                     }


                                                     if(visita1.getId().equals(id)) {
                                                         dat++;
                                                     }
                                                 }
                                                 if(dat<10) {
                                                     layoutTable.removeView(table);
                                                     visitas.setEstado("APROBADO");
                                                     Log.d("._.", visita.getId());
                                                     databaseReference.child("Visitas").child(idVisita).setValue(visitas);
                                                     Toast.makeText(activity,"Visita Agregada",Toast.LENGTH_LONG).show();
                                                     layoutTable.removeView(table);
                                                 }else{
                                                     Toast.makeText(activity,"NO SE PUEDEN AGENDAR MAS VISITAS POR ESTE DIA",Toast.LENGTH_LONG).show();
                                                 }
                                             }

                                             @Override
                                             public void onCancelled(@NonNull DatabaseError error) {

                                             }
                                         });

                                     }
                                 });

                                 row.addView(btnDelete);
                                 table.addView(row);
                             }
                         }
                         layoutTable.addView(table);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
         }
     });
    }
}