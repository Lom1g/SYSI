package fr.lomig.mycarto;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.DocumentSnapshot;


public class UsersAdapter extends FirestoreRecyclerAdapter<UsersModel, UsersAdapter.UsersHolder> {

    private OnItemClickListener listener;
    private static final long POINT_MODO = 500;

    public UsersAdapter(@NonNull FirestoreRecyclerOptions<UsersModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersHolder usersHolder, int i, @NonNull UsersModel usersModel) {
        usersHolder.fName.setText(usersModel.getfName());
        usersHolder.email.setText(usersModel.getEmail());
        usersHolder.rank.setText(usersModel.getRank());
        usersHolder.points.setText(String.valueOf(usersModel.getPoints()));
        if (usersModel.getRank().equals("admin")) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) usersHolder.itemView.getLayoutParams();
            param.height = 0;
            param.bottomMargin = 0;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            usersHolder.itemView.setVisibility(View.INVISIBLE);
        }
        if (usersModel.getPoints() >= POINT_MODO && usersModel.getRank().equals("user")) {
            usersHolder.fName.setBackgroundColor(Color.parseColor("#29D03B"));
        }
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_single, parent, false);
        return new UsersHolder(v);
    }

    class UsersHolder extends RecyclerView.ViewHolder {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
