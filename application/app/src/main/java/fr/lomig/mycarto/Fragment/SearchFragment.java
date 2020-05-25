package fr.lomig.mycarto.Fragment;

import android.content.Context;
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

import java.util.Objects;

import fr.lomig.mycarto.CategoryAdapter;
import fr.lomig.mycarto.CategoryModel;
import fr.lomig.mycarto.R;

public class SearchFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("spots");
    private CategoryAdapter categoryAdapter;
    private SearchFragmentListener listener;

    public interface SearchFragmentListener {
        void onInputASent(CharSequence category);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        Query query = collectionReference.orderBy("category");
        FirestoreRecyclerOptions<CategoryModel> options = new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(query, CategoryModel.class)
                .build();
        categoryAdapter = new CategoryAdapter(options);

        //Toast.makeText(getContext(),"id :" + options, Toast.LENGTH_LONG).show();

        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.list_categorie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                // ici on implemente les trucs a faire apres un click sur une catégorie de la liste
                String cat = Objects.requireNonNull(documentSnapshot.get("category")).toString();
                Toast.makeText(getContext(), "id :" + cat, Toast.LENGTH_SHORT).show();
                listener.onInputASent(cat);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpotFragment()).commit();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        categoryAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        categoryAdapter.stopListening();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentListener) {
            listener = (SearchFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}