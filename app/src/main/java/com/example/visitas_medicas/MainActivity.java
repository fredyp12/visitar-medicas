package com.example.visitas_medicas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.visitas_medicas.modelo.Administrador;
import com.example.visitas_medicas.modelo.Medico;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText txtNombre,txtPass;
    private Button btnLogin;
    private RadioGroup radioGroup;
    private RadioButton rdAdmin, rdPaciente;
    final MainActivity main=this;

    private boolean login;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Administrador administrador= new Administrador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();


        this.firebaseAuth= FirebaseAuth.getInstance();

        this.txtNombre=this.findViewById(R.id.txt_User);
        this.txtPass=this.findViewById(R.id.txtPass);
        this.btnLogin=this.findViewById(R.id.btnLogin);
        this.radioGroup=this.findViewById(R.id.radioGroup);
        this.rdAdmin=this.findViewById(R.id.rdAdmin);
        this.rdPaciente=this.findViewById(R.id.rdPaciente);

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser()!= null) {
//                    if(rdAdmin.isChecked()) {
//                        mdatabase.child("Administrador").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for(DataSnapshot dt: snapshot.getChildren()) {
//                                    administrador=dt.getValue(Administrador.class);
//                                    if(administrador.getCorreo().equalsIgnoreCase(txtNombre.getText().toString())) {
//                                        intent= new Intent(MainActivity.this,home_admin.class);
//                                        intent.putExtra("id",administrador.getId());
//                                        startActivity(intent);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//            }
//        };


        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rdAdmin.isChecked()) {
                    log("Administrador",mdatabase);
                }else {
                    if(rdPaciente.isChecked()) {
                        log("Paciente",mdatabase);
                    }
                }
            }
        });

    }

    @Override
    public void  onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void log (String tabla, DatabaseReference mdatabase) {
        mdatabase.child(tabla).child(txtNombre.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String pass=snapshot.child("contraseña").getValue().toString();
                    if(txtPass.getText().toString().equalsIgnoreCase(pass)) {
//                        if(user=="admin") {

                        if(rdAdmin.isChecked()) {
                            intent= new Intent(MainActivity.this,home_admin.class);
                            intent.putExtra("id",txtNombre.getText().toString());
                            startActivity(intent);
                        }else {
                            if(rdPaciente.isChecked()) {
                                intent= new Intent(MainActivity.this,Home_paciente.class);
                                intent.putExtra("id",txtNombre.getText().toString());
                                startActivity(intent);
                            }
                        }

//                        }

                    }else {
                        Toast.makeText(main,"contraseña invalida",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(main,"No existe este usuario ",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







//        this.firebaseAuth.createUserWithEmailAndPassword(txtNombre.getText().toString(),this.txtPass.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()) {
//                            Toast.makeText(main,"Error al iniciar sesion",Toast.LENGTH_LONG).show();
//                        }else {
//
//                        }
//                    }
//                });
    }
}