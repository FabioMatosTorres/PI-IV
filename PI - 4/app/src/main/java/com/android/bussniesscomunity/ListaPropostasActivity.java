package com.android.bussniesscomunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.bussniesscomunity.Adapter.PropostaAdapter;
import com.android.bussniesscomunity.Modelo.Propostas;
import com.android.bussniesscomunity.Modelo.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaPropostasActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public List<Propostas> listaProposta;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    Button btnCadastroP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_propostas);
        recyclerView = findViewById(R.id.listaPropostas);
        recyclerView.setHasFixedSize(true);
        btnCadastroP = findViewById(R.id.btnCadastroP);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListaPropostasActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        listaProposta = new ArrayList<>();
        listar();

        btnCadastroP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ListaPropostasActivity.this, CadastroPropostaActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    private void listar(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Propostas");
        listaProposta = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaProposta.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Propostas props = snapshot.getValue(Propostas.class);
                    listaProposta.add(props);
                }
                PropostaAdapter propAdapter = new PropostaAdapter(ListaPropostasActivity.this, listaProposta, firebaseUser.getUid());
                recyclerView.setAdapter(propAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


