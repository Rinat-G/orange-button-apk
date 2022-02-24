package com.example.orange_button_apk.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.orange_button_apk.databinding.FragmentGuardsSettingsBinding;

public class GuardsSettingsFragment extends Fragment {

    private FragmentGuardsSettingsBinding binding;


    public GuardsSettingsFragment() {
        // Required empty public constructor
    }

    public static GuardsSettingsFragment newInstance(String param1, String param2) {
        GuardsSettingsFragment fragment = new GuardsSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuardsSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        return view;

    }
}