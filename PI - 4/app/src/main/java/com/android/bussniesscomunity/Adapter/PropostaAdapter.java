package com.android.bussniesscomunity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.bussniesscomunity.Modelo.Propostas;
import com.android.bussniesscomunity.PropostaActivity;
import com.android.bussniesscomunity.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PropostaAdapter extends RecyclerView.Adapter<PropostaAdapter.ViewHolder> {
    private Context mContext;
    private List<Propostas> propostas;
    private String imageURL;
    private String usuarioAtual;

    public PropostaAdapter(Context mContext, List<Propostas> propostas, String usuarioAtual) {
        this.mContext = mContext;
        this.propostas = propostas;
        this.usuarioAtual = usuarioAtual;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.linha_proposta, parent, false);
        return new PropostaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Propostas propostas1 = propostas.get(position);
        holder.tituloProposta.setText(propostas1.getTitulo());
        holder.telefoneProposta.setText("Telefone: " + propostas1.getTelefone());
        holder.descricaoProposta.setText("Descrição: " + propostas1.getDescricao());
        Glide.with(mContext).load(propostas1.getImagem()).into(holder.imagemProposta);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PropostaActivity.class);
                intent.putExtra("titulo", propostas1.getTitulo());
                intent.putExtra("descricao", propostas1.getDescricao());
                intent.putExtra("telefone", propostas1.getTelefone());
                intent.putExtra("imagem", propostas1.getImagem());

                mContext.startActivity(intent);
            }
        });
        //Desabilita o botão se o usuário atual não for dono da proposta
        if(!propostas1.getIdUsuario().equals(usuarioAtual)){
            holder.deletar.setVisibility(View.GONE);
        }else{//Para não sumir após descer a lista
            holder.deletar.setVisibility(View.VISIBLE);
        }

        holder.deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                if(propostas1.getIdUsuario().equals(fUser.getUid())){
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Propostas");
                    reference.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            reference.child(propostas1.getIdProposta()).removeValue();
                            Toast.makeText(mContext, "Sua proposta foi removida!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }



    @Override
    public int getItemCount() {

        return propostas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tituloProposta;
        public TextView telefoneProposta;
        public TextView descricaoProposta;
        public ImageView imagemProposta;
        public Button deletar;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloProposta = (TextView) itemView.findViewById(R.id.tituloProposta);
            imagemProposta =  itemView.findViewById(R.id.fotoProposta);
            telefoneProposta = itemView.findViewById(R.id.telefoneProposta);
            descricaoProposta = itemView.findViewById(R.id.descricaoProposta);
            deletar = itemView.findViewById(R.id.deletar);

        }
    }
}
