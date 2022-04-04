package csci499.crbn;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity  extends AppCompatActivity {

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button submitBtn;
    private TextView errorTexts;

    private String name = "";
    private String email = "";
    private String password = "";

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);

        nameEdit = findViewById(R.id.name_edit);
        emailEdit = findViewById(R.id.email_address_edit);
        passwordEdit = findViewById(R.id.password_edit);
        submitBtn = findViewById(R.id.submitButton);
        errorTexts = findViewById(R.id.error_email);

        db = FirebaseFirestore.getInstance();

        nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                name = nameEdit.getText().toString();
                if(nameEdit.length() <= 0){
                    nameEdit.setError("Enter Name");
                }
                return true;
            }

        });


        emailEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                email = emailEdit.getText().toString();
                //  boolean checkDupe = callData(email);
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.d("success", document.getId() + " => " + document.getData());
                                        //all_users.add(document.getId());
                                        if(document.getId().compareTo(email)==0){
                                            Log.d("set","SETTING");
                                            emailEdit.setError("Duplicate email");
                                            errorTexts.setText("Duplicate email");
                                        }
                                    }
                                    Log.d("success2", "success2");


                                } else {
                                    Log.d("bad", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                if(email.length() <= 1){
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
                else if(password.length() < 4) {
                    passwordEdit.setError("Password needs to be at least 4 characters long");
                }
                return true;
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdit.getText().toString();
                email = emailEdit.getText().toString();
                password = passwordEdit.getText().toString();
                ArrayList<String> pics = new ArrayList<>();
                User user = new User(name, email, password, "1", pics);
                // Log.d("TESTT",  firstName + lastName + email + password);
                db.collection("users").document(email).set(user);

                if(name.length() <= 1){
                    nameEdit.setError("Enter FirstName");
                }
                if(email.length() <= 1){
                    emailEdit.setError("Enter Email Address");
                }
                if(!email.contains("@usc.edu")){
                    emailEdit.setError("Enter USC Email Address");
                }

                if(password.length() <= 1){
                    passwordEdit.setError("Enter Password");
                }
                if(password.length() < 4) {
                    passwordEdit.setError("Password needs to be at least 4 characters long");
                }

                if(name.length() > 1 && email.length() > 1 && email.contains("usc.edu") && password.length() >= 4) {
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    startActivityForResult(intent, 0);
                }


            }
        });


    }
}