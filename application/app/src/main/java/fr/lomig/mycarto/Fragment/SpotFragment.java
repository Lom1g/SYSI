package fr.lomig.mycarto.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import fr.lomig.mycarto.R;
import fr.lomig.mycarto.SpotAdapter;
import fr.lomig.mycarto.SpotModel;

public class SpotFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("spots");
    private SpotAdapter spotAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        //Toast.makeText(getContext(),"id :" + bundle, Toast.LENGTH_SHORT).show();
        //String categorie = bundle.getString("c");

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        Query query = collectionReference.orderBy("category", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<SpotModel> options = new FirestoreRecyclerOptions.Builder<SpotModel>()
                .setQuery(query, SpotModel.class)
                .build();
        spotAdapter = new SpotAdapter(options);

        RecyclerView recyclerView =  getView().findViewById(R.id.list_categorie);
        recyclerView .setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(spotAdapter);

        spotAdapter.setOnItemClickListener(new SpotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                // ici on implemente les trucs a faire apres un click sur un spot de la liste
                String id = documentSnapshot.getId();
                Toast.makeText(getContext(),"id :" + id, Toast.LENGTH_SHORT).show();
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