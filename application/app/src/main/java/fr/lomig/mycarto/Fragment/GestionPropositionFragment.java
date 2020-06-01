package fr.lomig.mycarto.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


import java.util.HashMap;
import java.util.Map;

import fr.lomig.mycarto.NotifPopup;

import fr.lomig.mycarto.CustomPopup;
import fr.lomig.mycarto.MainActivity;

import fr.lomig.mycarto.R;
import fr.lomig.mycarto.SpotAdapter;
import fr.lomig.mycarto.SpotModel;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static fr.lomig.mycarto.MainActivity.moderators;

public class GestionPropositionFragment extends Fragment {

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference spots = fStore.collection("spots");
    private SpotAdapter spotAdapter;
    private Fragment fragment;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();

    private String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

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

        final DocumentReference utilisateur = fStore.collection("users").document(userId);

        Query proposedSpots = spots.whereEqualTo("proposed",true).whereIn("moderatorP", MainActivity.moderators);
        FirestoreRecyclerOptions<SpotModel> options = new FirestoreRecyclerOptions.Builder<SpotModel>()
                .setQuery(proposedSpots, SpotModel.class)
                .build();
        spotAdapter = new SpotAdapter(options);

        RecyclerView recyclerView = getView().findViewById(R.id.list_categorie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(spotAdapter);

        spotAdapter.setOnItemClickListener(new SpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot spot, final int position) {
                // ici on implemente les trucs a faire apres un click sur un user de la liste
                final NotifPopup popup = new NotifPopup(getActivity());
                popup.setDescription("Voulez-vous accepter ou refuser la proposition de spot ?");
                popup.setTitle("Ajout/Refus de " + spotAdapter.getItem(position).getTitle());
                popup.setNoButtonText("Refuser");
                popup.setYesButtonText("Accepter");
                popup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        utilisateur.addSnapshotListener(fragment.getActivity(), new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot user, @Nullable FirebaseFirestoreException e) {
                                if (Objects.equals(user.getString("rank"), "admin")) {
                                    spots.document(spot.getId()).update("proposed", false);
                                }
                                else if (Objects.equals(user.getString("rank"), "modo")) {
                                    Integer increment = Integer.parseInt(spot.get("accepted").toString())+1;
                                    spots.document(spot.getId()).update("accepted", increment.toString());
                                    spots.document(spot.getId()).update("moderatorP",userId);
                                    if (Objects.equals(spot.getString("accepted"),"1")){
                                        spots.document(spot.getId()).update("proposed", false);
                                    }
                                }
                            }
                        });
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
                        fStore.collection("notifs").add(notif);

                        ft.detach(fragment).attach(fragment).commit();
                        popup.dismiss();
                    }
                });
                popup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fStore.collection("spots").document(spot.getId()).delete();
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
                        fStore.collection("notifs").add(notif);

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
