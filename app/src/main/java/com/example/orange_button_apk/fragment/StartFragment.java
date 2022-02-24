package com.example.orange_button_apk.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.orange_button_apk.HttpClientHandler;
import com.example.orange_button_apk.R;
import com.example.orange_button_apk.SessionRequestCallBack;
import com.example.orange_button_apk.activities.StartActivity;
import com.example.orange_button_apk.databinding.FragmentStartBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartFragment extends Fragment {

    private static final int EXPLICIT_SIGN_IN_REQUEST_CODE = 123;
    private static final String TAG = StartActivity.class.getSimpleName();
    @Inject
    public HttpClientHandler httpClientHandler;
    private FragmentStartBinding binding;
    private GoogleSignInClient mGoogleSignInClient;


    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGoogleSignInClient = initSignInClient();

        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
        if (task.isSuccessful()) {
            handleSignInResult(task);
        } else {
            task.addOnCompleteListener(
                    getActivity(),
                    this::handleSignInResult
            );
        }

        binding.tryAgain.setOnClickListener(v -> handleSignInResult(task));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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


            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.common_res_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", account.getIdToken());
            editor.apply();

            httpClientHandler.makeSessionRequest(
                    account.getIdToken(),
                    new SessionRequestCallBack(getActivity(), getView()),
                    getString(R.string.base_url)
            );

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

    private GoogleSignInClient initSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestId()
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(getActivity(), gso);
    }

}