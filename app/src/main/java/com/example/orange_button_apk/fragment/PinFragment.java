package com.example.orange_button_apk.fragment;


import static com.example.orange_button_apk.utils.Toaster.makeToastShort;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.orange_button_apk.HttpClientHandler;
import com.example.orange_button_apk.R;
import com.example.orange_button_apk.databinding.FragmentPinBinding;
import com.example.orange_button_apk.model.SignUpRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@AndroidEntryPoint
public class PinFragment extends Fragment {
    @Inject
    public HttpClientHandler httpClientHandler;
    private List<String> emails;
    private FragmentPinBinding binding;

    public PinFragment() {
        // Required empty public constructor
    }

    public static PinFragment newInstance() {
        return new PinFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPinBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emails = Arrays.asList(PinFragmentArgs.fromBundle(getArguments()).getEmails());

        binding.pinButtonBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.pinButtonForward.setOnClickListener(v -> {

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            String token = sharedPref.getString("token", null);

            Callback callback = new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    makeToastShort(getActivity(), "SignUp Request Failed");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        makeToastShort(getActivity(), "SignUp Succeed");

                    } else {
                        makeToastShort(getActivity(), "SignUp Failed. Code:" + response.code());
                    }
                }
            };

            httpClientHandler.makeSignUpRequest(
                    token,
                    callback,
                    getString(R.string.base_url),
                    new SignUpRequest(emails, binding.pinInput.getText().toString())
            );

            Navigation.findNavController(v).navigate(PinFragmentDirections.actionPinFragmentToMainActivity());

        });

        binding.pinInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.pinButtonForward.setEnabled(s.length() >= 4);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}