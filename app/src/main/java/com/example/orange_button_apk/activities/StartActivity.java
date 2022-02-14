package com.example.orange_button_apk.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.orange_button_apk.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartActivity extends AppCompatActivity {
//
//    private static final int EXPLICIT_SIGN_IN_REQUEST_CODE = 123;
//    private static final String TAG = StartActivity.class.getSimpleName();
//
//    @Inject
//    public HttpClientHandler httpClientHandler;
//    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_start);

//        mGoogleSignInClient = initSignInClient();
//
//        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
//        if (task.isSuccessful()) {
//            handleSignInResult(task);
//        } else {
//            task.addOnCompleteListener(
//                    this,
//                    this::handleSignInResult
//            );
//        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == EXPLICIT_SIGN_IN_REQUEST_CODE) {
//            Log.i(TAG, "onActivityResult: requestCode :" + requestCode);
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//            Log.i(TAG, "handleSignInResult: " + account.getEmail());
//
//            httpClientHandler.makeSessionRequest(
//                    account.getIdToken(),
//                    new AuthCallBack(this),
//                    getString(R.string.base_url)
//            );
//
//        } catch (ApiException e) {
//            if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_REQUIRED) {
//                signInExplicitly();
//            }
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//        }
//    }

//    private void signInExplicitly() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, EXPLICIT_SIGN_IN_REQUEST_CODE);
//    }

//    private GoogleSignInClient initSignInClient() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.server_client_id))
//                .requestId()
//                .requestEmail()
//                .build();
//        return GoogleSignIn.getClient(this, gso);
//    }
}
