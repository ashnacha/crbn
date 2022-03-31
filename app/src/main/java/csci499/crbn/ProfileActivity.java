package csci499.crbn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    String currEmail = "";
    User currUser = null;
    private FirebaseFirestore db;

    TextView textName;
    TextView textLevel;
    Button communityButton;
    Button challengeButton;
    TextView congrats_text;

    String level = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        currEmail = sharedData.getCurr_email();
        db = FirebaseFirestore.getInstance();
        textName = findViewById(R.id.text_view_name);
        textLevel = findViewById(R.id.text_view_level);
        congrats_text = findViewById(R.id.congrats_text);

        communityButton = findViewById(R.id.community_page);
        challengeButton = findViewById(R.id.go_to_challenge);

        DocumentReference docIdRef = db.collection("users").document(currEmail);
        Log.d("email", "cur"  + currEmail);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("heree1", "here");
                    currUser = new User(
                            document.getString("name"),
                            document.getString("email"),
                            document.getString("password"),
                            document.getString("level"));

                    textName.setText(currUser.getName());
                    textLevel.setText("Level " + currUser.getLevel());
                    level = currUser.getLevel();
                    if(currUser.getLevel().equals("4")){

                        congrats_text.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d("document", "Failed with: ", task.getException());
                    Log.d("here5", "here");
                }
            }
        });


        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CommunityActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("level", "l" + level);
                if (level.equals("1")) {
                    Intent intent = new Intent(ProfileActivity.this, LevelOneActivity.class);
                    startActivityForResult(intent, 0);
                }else if(level.equals("2")){
                    Intent intent = new Intent(ProfileActivity.this, LevelTwoActivity.class);
                    startActivityForResult(intent, 0);
                }
                else if(level.equals("3")){
                    Intent intent = new Intent(ProfileActivity.this, LevelThreeActivity.class);
                    startActivityForResult(intent, 0);

                }

            }
        });

    }
}
