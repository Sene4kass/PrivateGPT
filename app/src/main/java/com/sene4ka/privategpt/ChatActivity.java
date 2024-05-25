package com.sene4ka.privategpt;

import static androidx.core.graphics.drawable.DrawableCompat.setTint;

import android.annotation.SuppressLint;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonSend = (Button)findViewById(R.id.buttonSend);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText questionTextEdit = (EditText)findViewById(R.id.questionTextEdit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout layout = findViewById(R.id.SctrollLinearLayout);
        final ScrollView scrollview = findViewById(R.id.scrollView);
        ScrollView scroll = findViewById(R.id.scrollView);
        ChatGPT gpt = new ChatGPT(getBaseContext());
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                String question_text = questionTextEdit.getText().toString();
                question_text = question_text.replace("\\", "/");
                question_text = question_text.replace("\n", ". ");
                String answer;
                try {
                    answer = gpt.chatGPT(question_text);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                TextView question_block = new TextView(ChatActivity.this);
                question_block.setText("You\n"+question_text);
                layout.addView(question_block);
                TextView answer_block = new TextView(ChatActivity.this);
                String result_answer = "ChatGPT\n" + answer;
                String result = result_answer.replace("\\n\\n", " \n");
                result = result.replace("\\n", " \n");
                result = result.replace("```", "\n\n");
                result = result.replace("\\\\", " ");
                result = result.replace("\\\"", "\"");
                System.out.println(answer);
                System.out.println(result);
                answer_block.setText(result);
                //question_block.setBackgroundResource(R.color.question);
                //answer_block.setBackgroundResource(R.color.answer);
                //question_block.setTextColor(R.color.white);
                //answer_block.setTextColor(R.color.white);
                //question_block.setPadding(25,25,25,25);
                //answer_block.setPadding(25,25,25,25);
                question_block.setBackground(getResources().getDrawable(R.drawable.question_block_style));
                answer_block.setBackground(getResources().getDrawable(R.drawable.answer_block_style));
                question_block.setTextIsSelectable(true);
                answer_block.setTextIsSelectable(true);

                layout.addView(answer_block);
                questionTextEdit.setText("");

            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Button logoutButton = findViewById(R.id.buttonSignOut);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}