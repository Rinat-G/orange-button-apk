package com.example.orange_button_apk;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static java.util.Objects.requireNonNull;

public class StartActivity extends AppCompatActivity {

    public static final int EXPLICIT_SIGN_IN_REQUEST_CODE = 123;
    private static final String TAG = StartActivity.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_start);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestId()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //надо!!!!!!!!!!
//        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
//        if (task.isSuccessful()) {
//            handleSignInResult(task);
//        } else {
//            task.addOnCompleteListener(
//                    this,
//                    this::handleSignInResult
//            );
//        }

        initButtons();

    }

    private void initButtons() {
        Button btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(v -> {

            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);
//            Toast.makeText(this, "butt1", Toast.LENGTH_SHORT).show();


        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(v -> {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXPLICIT_SIGN_IN_REQUEST_CODE) {
            Log.i(TAG, "onActivityResult: requestCode :" + requestCode);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i(TAG, "handleSignInResult: " + account.getEmail());


            OkHttpClient client = new OkHttpClient();

//            RequestBody formBody = new FormBody.Builder()
//                    .add("idToken", requireNonNull(account.getIdToken()))
//                    .build();
            MediaType JSONMediaType = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject(Map.of("idToken", requireNonNull(account.getIdToken())));
            RequestBody jsonBody = RequestBody.create(jsonObject.toString(), JSONMediaType);
            Request request = new Request.Builder()
                    .url(getString(R.string.base_url) + "/auth")
                    .addHeader("Content-Type", "application/json")
                    .post(jsonBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new AuthCallBack(this));

            Log.i(TAG, "handleSignInResult: Sign in success");
            Toast.makeText(this, "Sign in success", Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_REQUIRED) {
                signInExplicitly();
            }
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signInExplicitly() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, EXPLICIT_SIGN_IN_REQUEST_CODE);
    }

}
