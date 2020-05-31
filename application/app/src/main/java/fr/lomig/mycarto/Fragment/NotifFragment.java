package fr.lomig.mycarto.Fragment;

import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import fr.lomig.mycarto.NotifAdapter;
import fr.lomig.mycarto.NotifModel;
import fr.lomig.mycarto.R;

public class NotifFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("notifs");
    private NotifAdapter notifAdapter;
    private FirebaseAuth fAuth;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notif, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        fAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        Query query = collectionReference.whereEqualTo("author",userId);
        FirestoreRecyclerOptions<NotifModel> options = new FirestoreRecyclerOptions.Builder<NotifModel>()
                .setQuery(query, NotifModel.class)
                .build();


        notifAdapter = new NotifAdapter(options);
        RecyclerView recyclerView =  Objects.requireNonNull(getView()).findViewById(R.id.list_notif);
        recyclerView .setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notifAdapter);

        notifAdapter.setOnItemClickListener(new NotifAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {
                // ici on implemente les trucs a faire apres un click sur un user de la liste
                firebaseFirestore.collection("notifs").document(documentSnapshot.getId()).delete();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        notifAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        notifAdapter.stopListening();
    }

}

