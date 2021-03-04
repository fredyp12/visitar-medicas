package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.visitas_medicas.modelo.Historial_clinico;
import com.example.visitas_medicas.modelo.Visitas;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.model.SliceValue;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Informe extends AppCompatActivity {


    private Button btnGenerar;
    private RadioButton rd_efectuado, rd_cancelado,rd_aprobado;
    private TextView txt_fecha;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Visitas visita;
    private Activity activity=this;
    private PieChartView pieChartView;
    private LinearLayout layout_chart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);

        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        this.btnGenerar=this.findViewById(R.id.btnGenerar_reporte);
        this.rd_aprobado=this.findViewById(R.id.rdReporte_aprobado);
        this.rd_cancelado=this.findViewById(R.id.rdReporte_cancealdo);
        this.rd_efectuado=this.findViewById(R.id.rdReporte_efectuado);
        this.txt_fecha=this.findViewById(R.id.txtReporte_fecha);
        this.layout_chart=this.findViewById(R.id.layout_chart);


        final PieChartView pieChartView = findViewById(R.id.chart);
        this.pieChartView = this.findViewById(R.id.chart);


        this.txt_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dia,mes,año;
                String fecha;
                final Calendar calendar= Calendar.getInstance();
                dia=calendar.get(Calendar.DAY_OF_MONTH);
                mes=calendar.get(Calendar.MONTH);
                año=calendar.get(Calendar.YEAR);
                fecha=dia+"/"+mes+"/"+año;
                DatePickerDialog datePickerDialog= new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        txt_fecha.setText(i2+"/"+(i1+1)+"/"+i);
                        calendar.set(i,i1,i2);
                    }
                },
                        dia,mes,año);
                datePickerDialog.show();
            }
        });

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_fecha.getText().toString().equalsIgnoreCase("Seleccionar Fecha")) {
                    if(rd_efectuado.isChecked()) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int totalV=0;
                                int totalE=0;
                                for(DataSnapshot dt: snapshot.child("Visitas").getChildren()) {
                                    visita=dt.getValue(Visitas.class);
                                    if(txt_fecha.getText().toString().equalsIgnoreCase(visita.getFecha())) {
                                        totalV++;
                                        if(visita.getEstado().equalsIgnoreCase("EFECTUADO")) {
                                            totalE++;
                                        }
                                    }
                                }

                                if(totalV>0) {
                                    List pieData = new ArrayList<>();
                                    pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                    pieData.add(new SliceValue(totalE, Color.GRAY).setLabel("Visitas Efectuadas"+totalE));
                                    PieChartData pieChartData = new PieChartData(pieData);
                                    pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                    pieChartView.setPieChartData(pieChartData);

                                }else {
                                    List pieData = new ArrayList<>();
                                    pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                    pieData.add(new SliceValue(totalE, Color.GRAY).setLabel("Visitas Efectuadas"+totalE));
                                    PieChartData pieChartData = new PieChartData(pieData);
                                    pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                    pieChartView.setPieChartData(pieChartData);
                                    if(totalV==0) Toast.makeText(activity,"No hay Visistas para este dia",Toast.LENGTH_LONG).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else {
                        if(rd_cancelado.isChecked()) {
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int totalV=0;
                                    int totalC=0;
                                    for(DataSnapshot dt: snapshot.child("Visitas").getChildren()) {
                                        visita=dt.getValue(Visitas.class);
                                        if(txt_fecha.getText().toString().equalsIgnoreCase(visita.getFecha())) {
                                            totalV++;
                                            if(visita.getEstado().equalsIgnoreCase("CANCELADO")) {
                                                totalC++;
                                            }
                                        }
                                        if(totalV>0) {
                                            List pieData = new ArrayList<>();
                                            pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                            pieData.add(new SliceValue(totalC, Color.GRAY).setLabel("Visitas Canceladas"+totalC));
                                            PieChartData pieChartData = new PieChartData(pieData);
                                            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                            pieChartView.setPieChartData(pieChartData);

                                        }else {
                                            List pieData = new ArrayList<>();
                                            pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                            pieData.add(new SliceValue(totalC, Color.GRAY).setLabel("Visitas Canceladas"+totalC));
                                            PieChartData pieChartData = new PieChartData(pieData);
                                            pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                            pieChartView.setPieChartData(pieChartData);
                                            if(totalV==0) Toast.makeText(activity,"No hay Visistas para este dia",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }else {
                            if(rd_aprobado.isChecked()) {
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int totalV=0;
                                        int totalA=0;
                                        for(DataSnapshot dt: snapshot.child("Visitas").getChildren()) {
                                            visita=dt.getValue(Visitas.class);
                                            if(txt_fecha.getText().toString().equalsIgnoreCase(visita.getFecha())) {
                                                totalV++;
                                                if(visita.getEstado().equalsIgnoreCase("APROBADO")) {
                                                    totalA++;
                                                }
                                            }
                                            if(totalV>0) {
                                                List pieData = new ArrayList<>();
                                                pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                                pieData.add(new SliceValue(totalA, Color.GRAY).setLabel("Visitas Aprovadas"+totalA));
                                                PieChartData pieChartData = new PieChartData(pieData);
                                                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                                pieChartView.setPieChartData(pieChartData);

                                            }else {
                                                List pieData = new ArrayList<>();
                                                pieData.add(new SliceValue(totalV, Color.BLUE).setLabel("Visitas Total :"+totalV));
                                                pieData.add(new SliceValue(totalA, Color.GRAY).setLabel("Visitas Aprovadas"+totalA));
                                                PieChartData pieChartData = new PieChartData(pieData);
                                                pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                                                pieChartView.setPieChartData(pieChartData);
                                                if(totalV==0) Toast.makeText(activity,"No hay Visistas para este dia",Toast.LENGTH_LONG).show();
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
            }
        });


//        PieChartView pieChartView = findViewById(R.id.chart);
//
//        pieChartView = findViewById(R.id.chart);
//
//        List pieData = new ArrayList<>();
//        pieData.add(new SliceValue(15, Color.BLUE).setLabel("Q1: $10"));
//        pieData.add(new SliceValue(25, Color.GRAY).setLabel("Q2: $4"));
//        pieData.add(new SliceValue(10, Color.RED).setLabel("Q3: $18"));
//        pieData.add(new SliceValue(60, Color.MAGENTA).setLabel("Q4: $28"));
//
//        PieChartData pieChartData = new PieChartData(pieData);
//        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
////        pieChartData.setHasCenterCircle(true).setCenterText1("Sales in million").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#0097A7"));
//        pieChartView.setPieChartData(pieChartData);





    }
}