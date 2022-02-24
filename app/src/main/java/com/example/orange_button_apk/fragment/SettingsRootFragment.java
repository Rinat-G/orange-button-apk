package com.example.orange_button_apk.fragment;

import static com.example.orange_button_apk.fragment.SettingsRootFragmentDirections.actionSettingsRootFragmentToGuardsSettingsFragment;
import static com.example.orange_button_apk.fragment.SettingsRootFragmentDirections.actionSettingsRootFragmentToPinSettingsFragment;
import static com.example.orange_button_apk.fragment.SettingsRootFragmentDirections.actionSettingsRootFragmentToSessionSettingsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.orange_button_apk.databinding.FragmentSettingsRootBinding;

public class SettingsRootFragment extends Fragment {

    FragmentSettingsRootBinding binding;

    public SettingsRootFragment() {
        // Required empty public constructor
    }

    public static SettingsRootFragment newInstance(String param1, String param2) {
        SettingsRootFragment fragment = new SettingsRootFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsRootBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] prefs = new String[]{
                "Список доверенных", "Сменить пин-код", "Длительность сессии"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, prefs);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener((parent, view1, position, id) -> {
            NavController navController = Navigation.findNavController(view);
            switch (position) {
                case 0:
                    navController.navigate(actionSettingsRootFragmentToGuardsSettingsFragment());
                    break;
                case 1:
                    navController.navigate(actionSettingsRootFragmentToPinSettingsFragment());
                    break;
                case 2:
                    navController.navigate(actionSettingsRootFragmentToSessionSettingsFragment());
                    break;
            }

        });

    }
}