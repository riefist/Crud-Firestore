package com.muhamadarief.belajarfirestore;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormAddUpdateActivity extends AppCompatActivity {

    public static final String DOC_ID = "document_id";

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_image)
    EditText edtImage;
    @BindView(R.id.edt_title)
    EditText edtTitle;
    @BindView(R.id.edt_company)
    EditText edtCompany;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private FirebaseFirestore db;
    private DocumentReference docRef;
    String doc_id;
    boolean formAdd;

    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        db = FirebaseFirestore.getInstance();

        doc_id = getIntent().getStringExtra(DOC_ID);
        formAdd = TextUtils.isEmpty(doc_id);

        if (!formAdd) {
            btnAdd.setText("Update");

            docRef = db.collection("friends").document(doc_id);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                FriendsResponse friend = documentSnapshot.toObject(FriendsResponse.class);
                edtName.setText(friend.getName());
                edtImage.setText(friend.getImage());
                edtTitle.setText(friend.getTitle());
                edtCompany.setText(friend.getCompany());
            });

        }
    }

    @OnClick(R.id.btn_add)
    public void btnAdd() {
        String name = edtName.getText().toString();
        String imageUrl = edtImage.getText().toString();
        String title = edtTitle.getText().toString();
        String company = edtCompany.getText().toString();

        if (formAdd) {
            addFriend(name, imageUrl, title, company);
        } else {
            updateFriend(name, imageUrl, title, company);
        }
    }

    public void addFriend(String name, String imageUrl, String title, String company) {
        FriendsResponse friend = new FriendsResponse(name, imageUrl, title, company);
        db.collection("friends")
                .add(friend)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Add", "" + documentReference.getId());
                    setResult(RESULT_ADD);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.d("Add", "error add doc : " + e);
                });
    }

    public void updateFriend(String name, String imageUrl, String title, String company) {
        docRef.update(
                "name", name,
                "image", imageUrl,
                "title", title,
                "company", company)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Update", "DocumentSnapshot succesfully update");
                    Toast.makeText(this, "1 item berhasil diupdate.", Toast.LENGTH_LONG).show();
                    setResult(RESULT_UPDATE);
                    finish();
                })
                .addOnFailureListener(e -> Log.d("Update", "error : " + e.getMessage()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            showAlertDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog() {
        String dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
        String dialogTitle = "Hapus Friend";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", (dialogInterface, i) -> {

                    db.collection("friends").document(doc_id)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Delete", "DocumentSnapshot successfully deleted!");
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Delete", "Error deleting document", e);
                            });

                    setResult(RESULT_DELETE);
                    finish();

                })
                .setNegativeButton("Tidak", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
