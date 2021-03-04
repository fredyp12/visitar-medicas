package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventListener;
import java.util.UUID;

import static android.Manifest.*;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
    private ImageButton btn_pdf;
    private RadioButton rd_efectuado, rd_cancelado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_paciente);

        visita= new Visitas();
        Bundle extras = getIntent().getExtras();
        final String datID= extras.getString("visita_id");
        String NOMBRE_DIRECTORIO= "MisPDFs";
        String NOMBRE_DOCUMENTO= "HISTORIAL_CLINICO.pdf";
        FirebaseApp.initializeApp(this);
        this.firebaseDatabase= FirebaseDatabase.getInstance();
        this.databaseReference=firebaseDatabase.getReference();

        this.activity=this;
        this.txtNombre= this.findViewById(R.id.txtInfo_nombre);
        this.txtID= this.findViewById(R.id.txtInfo_id);
        this.txtEstado= this.findViewById(R.id.txtInfo_estado);
        this.btn_actualizar= this.findViewById(R.id.btnInfo_actualizar);
        this.btn_pdf= this.findViewById(R.id.btnInfo_pdf);
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

        //Generar PDF
        //PERMISOS
        if(ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)!=
        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        this.btn_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPDF(datID);
                Toast.makeText(activity,"Historial Clinico creado como PDF", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void crearPDF(final String datID) { //crear PDF



            final String[] datos= new String[3];

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Document document = new Document();
                    try {

                        File file= crearFichero("HISTORIAL_CLINICO.pdf"); //nombre documento
                        FileOutputStream ficheroPDF = new FileOutputStream(file.getAbsolutePath());

                        PdfWriter writer = PdfWriter.getInstance(document, ficheroPDF);
                        document.open();
                        // ._.
                        document.add( new Paragraph( "Historial Clinico \n"+txtNombre.getText().toString()+ "\n\n"));


                        visita= snapshot.child("Visitas").child(datID).getValue(Visitas.class);
                        for(DataSnapshot dt: snapshot.child("Historial_medico").getChildren()) {
                            historial=dt.getValue(Historial_clinico.class);

                            if(historial.getPaciente().equalsIgnoreCase(visita.getPaciente())) {
                                document.add( new Paragraph( "Fecha de Consulta: "+ historial.getFecha()));
                                Log.d("fecha", historial.getFecha());
                                datos[0]=historial.getFecha();
                                final String idMedico=historial.getMedico();
                                for(DataSnapshot dt1: snapshot.child("Medico").getChildren()) {
                                    medico=dt1.getValue(Medico.class);
                                    if(medico.getId().equalsIgnoreCase(idMedico)) {
                                        document.add( new Paragraph( "Medico asignado: "+ medico.getNombre()));
                                        document.add( new Paragraph( "Especialidad : "+ medico.getEspecialidad()));
                                    }
                                }
                                document.add( new Paragraph( "Diagnostico de la consulata: "+ historial.getValoracion()+"\n\n"));
                             }
                        }



                    }catch (DocumentException e) {

                    }catch (IOException e) {

                    }finally {
                        document.close();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




    }

    public File crearFichero(String nombreFichero) {
        File ruta = getRuta();

        File fichero= null;
        if(ruta != null) {
            fichero= new File(ruta, nombreFichero);
        }
        return fichero;
    }

    public File getRuta() {
        File ruta= null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            ruta= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "MisPDFs"); //NOMBRE_DIRECTORIO

            if(ruta!= null) {
                if(!ruta.mkdir()) {
                    if(!ruta.exists()) {
                        return null;
                    }
                }
            }
        }
        return ruta;
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