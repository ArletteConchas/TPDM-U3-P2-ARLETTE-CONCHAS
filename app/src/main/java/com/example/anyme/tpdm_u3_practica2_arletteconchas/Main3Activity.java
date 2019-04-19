package com.example.anyme.tpdm_u3_practica2_arletteconchas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main3Activity extends AppCompatActivity {

    EditText rfc, nombre, departamento, domicilio;
    Button insertar, actualizar, borrar, buscar;
    FirebaseFirestore base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        rfc =findViewById(R.id.rfc);
        nombre=findViewById(R.id.nombre2);
        departamento=findViewById(R.id.departamento);
        domicilio=findViewById(R.id.domicilio);
        insertar=findViewById(R.id.insertar2);
        actualizar=findViewById(R.id.actualizar2);
        borrar=findViewById(R.id.borrar2);
        buscar=findViewById(R.id.buscar2);

        base=FirebaseFirestore.getInstance();

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarCampos();
                rfc.setEnabled(false);
                actualizar.setEnabled(true);
                borrar.setEnabled(true);
                insertar.setEnabled(false);
            }
        });

        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertar_datos();
            }
        });
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar_dato();
            }
        });
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar_dato();
            }
        });

    }

    private void recuperarCampos() {
        DocumentReference producto=base.collection("maestro").document(rfc.getText().toString());
        producto.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot resul=task.getResult();
                    String nom=resul.get("nombre").toString();
                    String carr=resul.get("departamento").toString();
                    String sem=resul.get("domicilio").toString();
                    nombre.setText(nom);
                    departamento.setText(carr);
                    domicilio.setText(sem);
                }
                else{
                    mensaje("Error al recuperar datos");
                }
            }
        });

    }

    private void insertar_datos() {
        base.collection("maestro").document(rfc.getText().toString()).set(obtenerCampos()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void documentReference) {
                mensaje("Se ha insertado con exito");
                limpiarCampos();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al insertar");
            }
        });
    }

    private void actualizar_dato() {
        base.collection("maestro").document(rfc.getText().toString()).update(obtenerCampos()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al actualizar");
                limpiarCampos();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se actualizo correctamente");
                limpiarCampos();
                finish();
            }
        });
    }

    private void eliminar_dato() {
        base.collection("maestro").document(rfc.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mensaje("Se elimino correctamente");
                limpiarCampos();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensaje("Error al eliminar");
            }
        });
    }

    private void mensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private Map<String,Object> obtenerCampos(){
        Map<String,Object> data=new HashMap<>();
        data.put("nombre",nombre.getText().toString());
        data.put("departamento",departamento.getText().toString());
        data.put("domicilio",domicilio.getText().toString());
        return data;
    }

    private void limpiarCampos () {
        rfc.setText("");
        nombre.setText("");
        domicilio.setText("");
        departamento.setText("");
        rfc.setEnabled(true);
        actualizar.setEnabled(false);
        borrar.setEnabled(false);
        insertar.setEnabled(true);

    }
}
