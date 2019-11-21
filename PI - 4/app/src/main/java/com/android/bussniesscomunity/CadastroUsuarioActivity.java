package com.android.bussniesscomunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class CadastroUsuarioActivity extends AppCompatActivity {

   private EditText email;
   private EditText senha;
   private EditText nome;
   private EditText cpf;
   private Button btnCadastroUsuario;
   private  EditText DescricaoPessoa;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        nome = findViewById(R.id.nome);
        cpf = findViewById(R.id.cpf);
        DescricaoPessoa = findViewById(R.id.DescricaoPessoa);


        mAuth = FirebaseAuth.getInstance();// Pega instância de autenticação


//--------------------------------------------------------
        btnCadastroUsuario = findViewById(R.id.btnCadastroUsuario);
        btnCadastroUsuario.setOnClickListener(ClickbtnUsuario);
    }
  View.OnClickListener ClickbtnUsuario = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
            String txt_email = email.getText().toString();
            String txt_senha = senha.getText().toString();
            String txt_cpf = cpf.getText().toString();
            String txt_nome = nome.getText().toString();
            String txt_descricao = DescricaoPessoa.getText().toString();



          if (txt_email.isEmpty() && txt_senha.isEmpty() && txt_cpf.isEmpty()
                  && txt_nome.isEmpty() && txt_descricao.isEmpty()) {

              Toast.makeText(CadastroUsuarioActivity.this, "Todos os campos são requeridos", Toast.LENGTH_SHORT).show();
          } else  {

            registrar(txt_email, txt_senha, txt_nome, txt_cpf, txt_descricao);

          }

      }
  };
    private void registrar(final String email, final String senha, final String nome,
                           final String cpf, final String DescricaoPessoa){

         mAuth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){

                FirebaseUser firebaseUser = mAuth.getCurrentUser();// Usuário atual
                assert firebaseUser != null;
                String userId = firebaseUser.getUid(); // Pega o Id do usuário atual (que está requisitando)

                reference = FirebaseDatabase.getInstance().getReference("Usuario").child(userId);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userId);
                hashMap.put("nome", nome);
                hashMap.put("cpf", cpf);
                hashMap.put("descricaoPessoa", DescricaoPessoa);



                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    //Não funciona por motivo desconhecido
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(CadastroUsuarioActivity.this, ListaPropostasActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else {
                Toast.makeText(CadastroUsuarioActivity.this, "Esse e-mail não está cadastrado ou senha inválida", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

}


