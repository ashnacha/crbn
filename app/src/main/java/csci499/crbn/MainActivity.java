package csci499.crbn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private Button createAccountButton;

    private TextView error_text;

    private String email;
    private String password;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEdit = findViewById(R.id.email_address_edit);
        passwordEdit = findViewById(R.id.password_edit);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        error_text = findViewById(R.id.error_text);
        error_text.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();


        emailEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                email = emailEdit.getText().toString();
                if(email.length() <= 0){
                    emailEdit.setError("Enter Email Address");
                }
                if(!email.contains("@usc.edu")){
                    emailEdit.setError("Enter USC Email Address");
                }
                return true;
            }
        });

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                password = passwordEdit.getText().toString();
                if(password.length() <= 0){
                    passwordEdit.setError("Enter Password");
                }
                return true;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailEdit.getText().toString();
                sharedData.setCurr_email(email);
                password = passwordEdit.getText().toString();
                Log.d("test", "email" + email);
                DocumentReference docIdRef = db.collection("users").document(email);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.d("heree1", "here");
                            if (!document.exists()) {
                                error_text.setVisibility(View.VISIBLE);
                                error_text.setText("Email Address not found");
                                Log.d("document", "Document does not exist!");
                                Log.d("here2", "here");
                            }
                            else{
                                String pswd = document.getString("password");

                                if(!pswd.equals(password)){
                                    error_text.setVisibility(View.VISIBLE);
                                    error_text.setText("Password does not match");
                                    Log.d("document", "password is wrong");
                                }

                                else {
                                    Log.d("heree3", "here");
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    //intent.putExtra("email", email);
                                    startActivityForResult(intent, 0);
                                    Log.d("Document", "Document exists!");
                                }
                            }

                        } else {
                            Log.d("document", "Failed with: ", task.getException());
                            Log.d("here5", "here");
                        }
                    }
                });

            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }
}