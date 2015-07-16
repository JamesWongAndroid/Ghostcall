package com.tapfury.ghostcall;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CodeVerificationScreen extends AppCompatActivity {

    EditText smsCodeField;
    Button resendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification_screen);

        smsCodeField = (EditText) findViewById(R.id.smsCodeInput);
        resendButton = (Button) findViewById(R.id.resendButton);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivity(new Intent(CodeVerificationScreen.this, HomeScreen.class));
            }
        });
    }

    public class verifyCodeTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
