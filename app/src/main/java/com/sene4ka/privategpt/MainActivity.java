package com.sene4ka.privategpt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button login_button = (Button)findViewById(R.id.button_login);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            System.out.println("USER ALREADY LOGGED");

        }



        login_button.setOnClickListener(new View.OnClickListener() {
            final EditText login_EditText = findViewById(R.id.editLogin);
            final EditText password_EditText = findViewById(R.id.editTextTextPassword);

            @Override
            public void onClick(View v) {
                String login = login_EditText.getText().toString();
                String password = password_EditText.getText().toString();
                if(login.equals("") || password.equals("")){
                    Toast notification = new Toast(getApplicationContext());
                    notification.setText(R.string.signInFailedNotification);
                    notification.show();
                    return;
                }

                try {
                    if (auth.getCurrentUser() != null) {

                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent);
                        System.out.println("USER ALREADY LOGGED");

                    } else {
                        auth.signInWithEmailAndPassword(login, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                                            startActivity(intent);
                                            System.out.println("SUCCESSFULLY LOGIN");
                                        } else {
                                            Toast notification = new Toast(getApplicationContext());
                                            notification.setText(R.string.signInFailedNotification);
                                            notification.show();
                                        }
                                    }
                                });
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }
}