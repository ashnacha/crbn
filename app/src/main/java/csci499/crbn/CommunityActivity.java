package csci499.crbn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommunityActivity  extends AppCompatActivity {

    private RecyclerView mFireStoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button viewProfileButtonCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        mFireStoreList = findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        viewProfileButtonCommunity = findViewById(R.id.viewProfileButtonCommunity);

        Query query = firebaseFirestore.collection("users");

        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>().setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(users) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_page_layout, parent, false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i, @NonNull User user) {

                userViewHolder.userName.setText(user.getName());
                userViewHolder.level.setText("Level " + user.getLevel());
                if(user.getPictures().isEmpty()){
                    Picasso.get().load(R.drawable.icon).into(userViewHolder.imageView);
                }
                else {
                    Picasso.get().load(user.getPictures().get(user.getPictures().size() - 1)).into(userViewHolder.imageView);
                }


            }
        };

        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList.setAdapter(adapter);

        viewProfileButtonCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, ProfileActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView level;
        private ImageView imageView;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        public User u;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.nameTextView);
            level = itemView.findViewById(R.id.levelTextView);
            imageView = itemView.findViewById(R.id.imageViewCard);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

}