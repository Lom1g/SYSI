package fr.lomig.mycarto.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import fr.lomig.mycarto.MainActivity;
import fr.lomig.mycarto.R;

public class ModoFragment extends Fragment {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private Button btn_gest_modo, btn_gest_proposition, btn_gest_signalement;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_modo, container, false);
<<<<<<< HEAD
        btn_gest_modo= rootView.findViewById(R.id.btn_gest_modo);
=======
        btn_gest_modo = rootView.findViewById(R.id.btn_gest_modo);
>>>>>>> 5c32d07a8043fb3b3251081a5f617b0152f7cfc2
        btn_gest_proposition = rootView.findViewById(R.id.btn_gest_proposition);
        btn_gest_signalement = rootView.findViewById(R.id.btn_gest_signalement);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.getString("rank").equals("modo")) {
                    btn_gest_modo.setVisibility(View.GONE);
                } else {
                    btn_gest_modo.setVisibility(View.VISIBLE);
                }

            }
        });


        btn_gest_modo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("TAG1", "T as bien appuyé sur le bouton mon con");
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new GestionModoFragment()).commit();
            }
        });

        btn_gest_proposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new GestionPropositionFragment()).commit();
            }
        });

        btn_gest_signalement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new GestionSignalementFragment()).commit();
            }
        });

        return rootView;
    }

}
