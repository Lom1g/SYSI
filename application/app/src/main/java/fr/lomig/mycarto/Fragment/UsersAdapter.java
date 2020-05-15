package fr.lomig.mycarto.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import fr.lomig.mycarto.R;

public class UsersAdapter extends FirestoreRecyclerAdapter<UsersModel, UsersAdapter.UsersHolder> {

    UsersAdapter(@NonNull FirestoreRecyclerOptions<UsersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersHolder usersHolder, int i, @NonNull UsersModel usersModel) {
        usersHolder.fName.setText(usersModel.getfName());
        usersHolder.email.setText(usersModel.getEmail());
        usersHolder.rank.setText(usersModel.getRank());
        usersHolder.points.setText(String.valueOf(usersModel.getPoints()));
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_single, parent, false);
        return new UsersHolder(v);
    }

    static class UsersHolder extends RecyclerView.ViewHolder {
        TextView fName;
        TextView email;
        TextView rank;
        TextView points;

        UsersHolder(View itemView) {
            super(itemView);
            fName = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
            rank = itemView.findViewById(R.id.rank);
            points = itemView.findViewById(R.id.points);
        }
    }
}
