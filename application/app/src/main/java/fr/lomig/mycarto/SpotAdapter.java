package fr.lomig.mycarto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class SpotAdapter extends FirestoreRecyclerAdapter<SpotModel, SpotAdapter.SpotHolder> {

    private OnItemClickListener listener;

    public SpotAdapter(@NonNull FirestoreRecyclerOptions<SpotModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SpotHolder spotHolder, int i, @NonNull SpotModel spotModel) {
        spotHolder.title.setText(spotModel.getTitle());
        spotHolder.description.setText(spotModel.getDescription());
        spotHolder.longitude.setText(String.valueOf(spotModel.getLongitude()));
        spotHolder.latitude.setText(String.valueOf(spotModel.getLatitude()));
        spotHolder.categorie.setText(spotModel.getCategory());
    }

    @NonNull
    @Override
    public SpotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_spot_single, parent, false);
        return new SpotHolder(v);
    }

    class SpotHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView latitude;
        TextView longitude;
        TextView categorie;

        SpotHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            categorie = itemView.findViewById(R.id.category);

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

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
