package fr.lomig.mycarto;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class NotifAdapter extends FirestoreRecyclerAdapter<NotifModel, NotifAdapter.NotifHolder> {

    private OnItemClickListener listener;

    public NotifAdapter(@NonNull FirestoreRecyclerOptions<NotifModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotifHolder notifHolder, int i, @NonNull NotifModel notifModel) {
        notifHolder.title.setText(notifModel.getTitle());
        notifHolder.message.setText(notifModel.getMessage());
        notifHolder.btn_accept.setText(notifModel.getSpotname());
    }

    @NonNull
    @Override
    public NotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notif_single, parent, false);
        return new NotifHolder(v);
    }

    class NotifHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        Button btn_accept;

        NotifHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            btn_accept = itemView.findViewById(R.id.btn_accept);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(NotifAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
    }



