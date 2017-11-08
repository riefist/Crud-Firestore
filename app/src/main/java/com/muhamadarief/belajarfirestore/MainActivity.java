package com.muhamadarief.belajarfirestore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.friend_list)
    RecyclerView friendList;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        init();
        getFriendList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
          startActivityForResult(new Intent(getApplicationContext(), FormAddUpdateActivity.class),
                  FormAddUpdateActivity.REQUEST_ADD);
        });
    }

    private void init(){
        db = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendList.setLayoutManager(linearLayoutManager);
    }

    private void getFriendList(){
        Query query = db.collection("friends");

        FirestoreRecyclerOptions<FriendsResponse> response = new FirestoreRecyclerOptions.Builder<FriendsResponse>()
                .setQuery(query, FriendsResponse.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<FriendsResponse, FriendsHolder>(response) {

            @Override
            protected void onBindViewHolder(FriendsHolder holder, int position, FriendsResponse model) {
                progressBar.setVisibility(View.GONE);
                holder.name.setText(model.getName());
                holder.title.setText(model.getTitle());
                holder.company.setText(model.getCompany());


                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.image);

                holder.itemView.setOnClickListener(view -> {
                    /*Snackbar.make(friendList, model.getName() + ", "+model.getTitle()+" at "+model.getCompany(),
                            Snackbar.LENGTH_LONG).setAction("ACTION", null).show();*/

                    DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
                    Intent intent = new Intent(getApplicationContext(), FormAddUpdateActivity.class);
                    intent.putExtra(FormAddUpdateActivity.DOC_ID, documentSnapshot.getId());
                    MainActivity.this.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);

                });
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        friendList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FormAddUpdateActivity.REQUEST_ADD){
            if (resultCode == FormAddUpdateActivity.RESULT_ADD){
                showSnackbar("1 item berhasil ditambahkan.");
            }
        }

        if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE){
            if (resultCode == FormAddUpdateActivity.RESULT_UPDATE){
                showSnackbar("1 item berhasil diupdate.");
            }
        }

        if (resultCode == FormAddUpdateActivity.RESULT_DELETE){
            showSnackbar("1 item berhasil dihapus.");
        }
    }

    private void showSnackbar(String message){
        Snackbar.make(friendList, message, Snackbar.LENGTH_LONG).setAction("Action", null)
                .show();
    }
}
