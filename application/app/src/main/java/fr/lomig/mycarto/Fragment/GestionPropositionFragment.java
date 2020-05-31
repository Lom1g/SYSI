package fr.lomig.mycarto.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

import fr.lomig.mycarto.NotifPopup;
import fr.lomig.mycarto.R;
import fr.lomig.mycarto.SpotAdapter;
import fr.lomig.mycarto.SpotModel;

public class GestionPropositionFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("spots");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SpotAdapter spotAdapter;
    private Fragment fragment;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.fragment=this;
        return inflater.inflate(R.layout.fragment_search,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = collectionReference.whereEqualTo("proposed",true).orderBy("title");
        FirestoreRecyclerOptions<SpotModel> options = new FirestoreRecyclerOptions.Builder<SpotModel>()
                .setQuery(query, SpotModel.class)
                .build();
        spotAdapter = new SpotAdapter(options);

        RecyclerView recyclerView = getView().findViewById(R.id.list_categorie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(spotAdapter);

        spotAdapter.setOnItemClickListener(new SpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {
                // ici on implemente les trucs a faire apres un click sur un user de la liste
                final NotifPopup popup = new NotifPopup(getActivity());
                popup.setDescription("Voulez-vous accepter ou refuser la proposition de spot ?");
                popup.setTitle("Ajout/Refus de " + spotAdapter.getItem(position).getTitle());
                popup.setNoButtonText("Refuser");
                popup.setYesButtonText("Accepter");
                popup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseFirestore.collection("spots").document(documentSnapshot.getId()).update("proposed", false);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (Build.VERSION.SDK_INT >= 26) {
                            ft.setReorderingAllowed(false);
                        }

                        String message = popup.getMessage().getText().toString();
                        String author=documentSnapshot.getString("author");
                        String spotname=documentSnapshot.getString("title");

                        Map<String, Object> notif = new HashMap<>();
                        notif.put("message",message);
                        notif.put("author", author);
                        notif.put("spotname",spotname);
                        notif.put("type","Proposition_Acceptee");
                        db.collection("notifs").add(notif);

                        ft.detach(fragment).attach(fragment).commit();
                        popup.dismiss();
                    }
                });
                popup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firebaseFirestore.collection("spots").document(documentSnapshot.getId()).delete();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        if (Build.VERSION.SDK_INT >= 26) {
                            ft.setReorderingAllowed(false);
                        }

                        String message = popup.getMessage().getText().toString();
                        String author=documentSnapshot.getString("author");
                        String spotname=documentSnapshot.getString("title");

                        Map<String, Object> notif = new HashMap<>();
                        notif.put("message",message);
                        notif.put("author", author);
                        notif.put("spotname",spotname);
                        notif.put("type","Proposition_Refusee");
                        db.collection("notifs").add(notif);

                        ft.detach(fragment).attach(fragment).commit();
                        popup.dismiss();
                    }
                });
                popup.build();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        spotAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        spotAdapter.stopListening();
    }
}
