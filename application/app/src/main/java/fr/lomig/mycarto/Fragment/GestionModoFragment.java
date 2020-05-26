package fr.lomig.mycarto.Fragment;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import fr.lomig.mycarto.CustomPopup;
import fr.lomig.mycarto.MainActivity;
import fr.lomig.mycarto.R;
import fr.lomig.mycarto.UsersAdapter;
import fr.lomig.mycarto.UsersModel;

public class GestionModoFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("users");
    private UsersAdapter usersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gestionmodo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        EditText editText = getView().findViewById(R.id.search_users);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //mettre ici le code pour chercher les users par nom
                Log.d("TAG", s.toString());
                //Query query = collectionReference.orderBy("fName").startAt(s.toString()).endAt(s.toString()+"\uf8ff");
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = collectionReference.orderBy("points", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<UsersModel> options = new FirestoreRecyclerOptions.Builder<UsersModel>()
                .setQuery(query, UsersModel.class)
                .build();
        usersAdapter = new UsersAdapter(options);

        RecyclerView recyclerView = getView().findViewById(R.id.list_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(usersAdapter);

        usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {
                // ici on implemente les trucs a faire apres un click sur un user de la liste
                final CustomPopup popup = new CustomPopup(getActivity());
                popup.setDescription("Choissisez si vous voulez améliorer ou bien baisser le rang du profil");
                popup.setTitle("Changer le rang de " + usersAdapter.getItem(position).getfName());
                popup.setNeutralButtonText("Retour");
                popup.setNoButtonText("Baisser");
                popup.setYesButtonText("Améliorer");
                popup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!usersAdapter.getItem(position).getRank().equals("modo")) {
                            firebaseFirestore.collection("users").document(documentSnapshot.getId()).update("rank", "modo");
                            popup.dismiss();
                        }
                    }
                });
                popup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!usersAdapter.getItem(position).getRank().equals("user")) {
                            firebaseFirestore.collection("users").document(documentSnapshot.getId()).update("rank", "user");
                            popup.dismiss();
                        }
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
        usersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        usersAdapter.stopListening();
    }

}