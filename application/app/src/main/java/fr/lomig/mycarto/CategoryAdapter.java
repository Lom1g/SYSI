package fr.lomig.mycarto;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Random;


public class CategoryAdapter extends FirestoreRecyclerAdapter<CategoryModel, CategoryAdapter.CategoryHolder> {

    private OnItemClickListener listener;

    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<CategoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int i, @NonNull CategoryModel categoryModel) {
        categoryHolder.categorie.setText(categoryModel.getCategory());

        int color = Color.argb(255, 255, 255, 153);
        categoryHolder.categorie.setBackgroundColor(color);
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_single, parent, false);
        return new CategoryHolder(v);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        TextView categorie;

        CategoryHolder(View itemView) {
            super(itemView);
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
