package com.example.orange_button_apk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.orange_button_apk.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment {
    private FragmentRegistrationBinding binding;
    private int counter = 1;


    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddMore.setOnClickListener(v -> {
            EditText newInput = new EditText(getContext());
            ((LinearLayout) view).addView(newInput, counter);
            counter++;
        });

        binding.regButtonForward.setOnClickListener(v -> {

        });

    }
}