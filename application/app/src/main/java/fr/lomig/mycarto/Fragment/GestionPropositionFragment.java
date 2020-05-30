package fr.lomig.mycarto.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Objects;

import fr.lomig.mycarto.CustomPopup;
import fr.lomig.mycarto.R;
import fr.lomig.mycarto.SpotAdapter;
import fr.lomig.mycarto.SpotModel;

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

        Query proposedSpots = spots.whereEqualTo("proposed",true).orderBy("title");
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
                final CustomPopup popup = new CustomPopup(getActivity());
                popup.setDescription("Voulez-vous ajouter le spot sur la carte ?");
                popup.setTitle("Ajout de " + spotAdapter.getItem(position).getTitle());
                popup.setNotepop("");
                popup.setNeutralButtonText("Retour");
                popup.setNoButtonText("Refuser");
                popup.setYesButtonText("Ajouter");
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
                        ft.detach(fragment).attach(fragment).commit();
                        popup.dismiss();
                    }
                });
                popup.getNeutralButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
