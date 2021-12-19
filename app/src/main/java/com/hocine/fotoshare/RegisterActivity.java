package com.hocine.fotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

/**
 * Classe permettant d'enregistrer un nouvelle utilisateur FotoShare
 *
 * @author Hocine
 * @version 1.0
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * Variables
     */
    EditText prenom, nom, email, password, password_confirm;
    Button register;
    TextView txt_login;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    /**
     * Méthode permettant la création de l'activité + vérifie si les champs sont remplis et que le mot de mot de passe contient au moins 6 caractères
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
        register = findViewById(R.id.register);
        txt_login = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage(getString(R.string.wait));
                String str_prenom = prenom.getText().toString();
                String str_nom = nom.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();
                String str_password_confirm = password_confirm.getText().toString();

                if (TextUtils.isEmpty(str_prenom) || TextUtils.isEmpty(str_nom) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_prenom)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.filled_field), Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.password), Toast.LENGTH_SHORT).show();
                } else if(!(str_password.equals(str_password_confirm))){
                    Toast.makeText(RegisterActivity.this, getString(R.string.password_confirm_msg), Toast.LENGTH_SHORT).show();
                }else {
                    pd.show();
                    register(str_prenom, str_nom, str_email, str_password);
                }
            }
        });
    }

    /**
     * Méthode permettant l'enregistrement en base de données du nouvel utilisateur
     *
     * @param prenom
     * @param nom
     * @param email
     * @param password
     */
    private void register(String prenom, String nom, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("prenom", prenom.toLowerCase());
                            hashMap.put("nom", nom);
                            hashMap.put("bio", "");
                            hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/fotoshare-d92c2.appspot.com/o/user.png?alt=media&token=202427cd-2ebd-4d8a-a35f-7053b6daa08d");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}