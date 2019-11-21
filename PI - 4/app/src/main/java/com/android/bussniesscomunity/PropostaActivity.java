package com.android.bussniesscomunity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bussniesscomunity.Adapter.PropostaAdapter;
import com.android.bussniesscomunity.Modelo.Propostas;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class PropostaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView titulo, telefone, descricao;
    ImageView imagem;
    Button btn_voltar;
    public List<Propostas> listaProposta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposta);
        titulo = findViewById(R.id.tituloProposta);
        descricao = findViewById(R.id.descricaoProposta);
        telefone = findViewById(R.id.telefoneProposta);
        imagem = findViewById(R.id.fotoProposta);
        btn_voltar = findViewById(R.id.btn_voltar);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("");


        Intent i = getIntent();

        titulo.setText("Titulo:" + i.getStringExtra("titulo"));
        descricao.setText("Descrição:" + i.getStringExtra("descricao"));
        telefone.setText("Telefone:" + i.getStringExtra("telefone"));
        Glide.with(PropostaActivity.this).load(i.getStringExtra("imagem")).into(imagem);

        btn_voltar.setOnClickListener(voltar);

    }

    View.OnClickListener voltar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
}
