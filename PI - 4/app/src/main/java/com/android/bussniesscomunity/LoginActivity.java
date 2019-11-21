package com.android.bussniesscomunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
     Button btnCadastro, btnLogin;
     EditText emailUsuario;
     EditText senhaUsuario;
     private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnCadastro = findViewById(R.id.btnCadastro);
        btnLogin = findViewById(R.id.btnLogin);
        emailUsuario = findViewById(R.id.emailUsuario);
        senhaUsuario = findViewById(R.id.senhaUsuario);


        mAuth = FirebaseAuth.getInstance();//Pega instância de autenticação


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txt_email = emailUsuario.getText().toString();
                final String txt_senha = senhaUsuario.getText().toString();
                if (txt_email.isEmpty() && txt_senha.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Todos os campos são requeridos", Toast.LENGTH_SHORT).show();
                } else  {
                    login(txt_email, txt_senha);
                }

            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(final String email, final String senha){
     //Faz a autenticação com o firebase
     mAuth.signInWithEmailAndPassword(email, senha)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                 Intent intent = new Intent(LoginActivity.this, ListaPropostasActivity.class);
                 startActivity(intent);
                 finish();
             }else {
                 Toast.makeText(LoginActivity.this, "Esse e-mail não está cadastrado ou senha inválida", Toast.LENGTH_SHORT).show();
             }
         }
     });



    }
}