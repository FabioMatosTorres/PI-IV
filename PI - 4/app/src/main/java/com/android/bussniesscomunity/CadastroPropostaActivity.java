package com.android.bussniesscomunity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.bussniesscomunity.Modelo.Propostas;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class CadastroPropostaActivity extends AppCompatActivity {
    Button btGaleria;
    ImageView ivImagem;
    EditText tituloProposta;
    EditText descricaoProposta;
    EditText telefone;
    private Button btnCadastroPropostas;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private final int GALERIA_IMAGENS =1;
    private final int PERMISSAO_REQUEST =2;
    String URL;
    private Uri imageUri;
    StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_proposta);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSAO_REQUEST);
            }
        }


        btGaleria = findViewById(R.id.btGaleria);
        ivImagem = findViewById(R.id.ivImagem);
        tituloProposta = findViewById(R.id.tituloProposta);
        descricaoProposta = findViewById(R.id.descricaoProposta);
        btnCadastroPropostas = findViewById(R.id.btnCadastroPropostas);
        telefone = findViewById(R.id.telefone);
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        btGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        btnCadastroPropostas.setOnClickListener(ClickbtnPropostas);
    }
    private void openImage() {
        CropImage.activity()
                .setAspectRatio(1,1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);

    }
    private String getFileExtension(Uri uri){
        String typeExtension = "";

        String ext = uri.toString();
        if(ext.contains("jpg") || ext.contains("JPG")){
            typeExtension = "jpg";
        }else if(ext.contains(".png") || ext.contains("PNG")){
            typeExtension = "png";
        }

        return typeExtension;
    }
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(CadastroPropostaActivity.this);
        pd.setMessage("Carregando");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        URL = mUri;
                        pd.dismiss();
                    }else{
                        Toast.makeText(CadastroPropostaActivity.this, "Falhou", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CadastroPropostaActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(CadastroPropostaActivity.this, "Imagem não selecionada", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageUri = resultUri;
                Glide.with(CadastroPropostaActivity.this).load(resultUri).into(ivImagem);
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(CadastroPropostaActivity.this, "Upload em progresso", Toast.LENGTH_SHORT).show();
                }else{
                    uploadImage();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(CadastroPropostaActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    View.OnClickListener ClickbtnPropostas =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String txt_tituloproposta = tituloProposta.getText().toString();
            String txt_descricaoproposta = descricaoProposta.getText().toString();
            String txt_telefone = telefone.getText().toString();



            if (txt_tituloproposta.isEmpty() && txt_descricaoproposta.isEmpty()) {

                Toast.makeText(CadastroPropostaActivity.this, "Todos os campos são requeridos", Toast.LENGTH_SHORT).show();
            } else  {

                registrar (txt_tituloproposta, txt_descricaoproposta, txt_telefone);

            }

        }
    };

    private void registrar(final String tituloproposta, final String descricaoproposta, final String telefone ){


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String propId = firebaseUser.getUid();
        Propostas chaveIdentificacao = new Propostas();
        chaveIdentificacao.setID();

        reference = FirebaseDatabase.getInstance().getReference("Propostas").child(chaveIdentificacao.getID());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("idUsuario", propId);
        hashMap.put("idProposta", chaveIdentificacao.getID());
        hashMap.put("titulo", tituloproposta);
        hashMap.put("descricao", descricaoproposta);
        hashMap.put("telefone", telefone);
        hashMap.put("imagem", URL);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                    Intent intent = new Intent(CadastroPropostaActivity.this, ListaPropostasActivity.class);
                    startActivity(intent);
                    finish();
                }

                           });

    }

}
