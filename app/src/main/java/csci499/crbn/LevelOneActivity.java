package csci499.crbn;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class LevelOneActivity extends AppCompatActivity {

    Button completeButton;
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    Button photo1;
    Button photo2;

    Button uploadphoto1;
    Button uploadphoto2;

    int CAMERA_PIC_REQUEST= 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    ImageView imageView;
    String currEmail = "";
    Button profilebtn_level1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levelone);

        db = FirebaseFirestore.getInstance();
        currEmail = sharedData.getCurr_email();

        completeButton = findViewById(R.id.completeLevel1);
        photo1 = findViewById(R.id.takephoto1);
        photo2 = findViewById(R.id.takephoto2);
        imageView = findViewById(R.id.imageview);
        uploadphoto1 = findViewById(R.id.uploadphoto1);
        uploadphoto2 = findViewById(R.id.uploadphoto2);
        profilebtn_level1 = findViewById(R.id.profilebtn_level1);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference currDoc = db.collection("users").document(currEmail);
                currDoc.update("level", "2")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Doc", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Doc", "Error writing document", e);
                            }
                        });
                Intent intent = new Intent(LevelOneActivity.this, LevelTwoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        uploadphoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPic();
            }
        });
        uploadphoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadPic();
            }
        });

        profilebtn_level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelOneActivity.this, ProfileActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    protected void uploadPic(){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //create upload task
        String path = "profilepics/" + UUID.randomUUID() + ".png";
        Log.d("Doc", "storage path: " + path);
        StorageReference profilePicRef = storage.getReference(path);
        UploadTask uploadTask = profilePicRef.putBytes(data);

        uploadTask.addOnSuccessListener(LevelOneActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        String urlString = downloadUrl.toString();
                        Log.d("Doc", "download URL: " + urlString);

                        //upload into firebase Storage
                        DocumentReference currDoc = db.collection("users").document(currEmail);
                        currDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    List<String> pictures = (List<String>) document.get("pictures");

                                    if (!document.exists()) {
                                        Log.d("document", "Document does not exist!");
                                    }
                                    else {
                                        Log.d("Document", "Document exists!");
                                        pictures.add(urlString);
                                        currDoc.update("pictures", pictures)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Doc", "DocumentSnapshot successfully written!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Doc", "Error writing document", e);
                                                    }
                                                });

                                        imageView.setVisibility(View.INVISIBLE);
                                        if(pictures.size()==2){
                                            completeButton.setVisibility(View.VISIBLE);
                                        }


                                    }
                                } else {
                                    Log.d("document", "Failed with: ", task.getException());
                                }
                            }
                        });

                    }
                });
            }
        });
    }
}