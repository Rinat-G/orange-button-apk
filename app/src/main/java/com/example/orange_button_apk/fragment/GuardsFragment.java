package com.example.orange_button_apk.fragment;

import static com.example.orange_button_apk.fragment.GuardsFragmentDirections.actionGuardsFragmentToPinFragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.orange_button_apk.databinding.FragmentGuardsBinding;

import java.util.ArrayList;
import java.util.List;

public class GuardsFragment extends Fragment {
    private FragmentGuardsBinding binding;
    private int counter = 0;

    public GuardsFragment() {
        // Required empty public constructor
    }

    public static GuardsFragment newInstance() {
        return new GuardsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuardsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddMore.setOnClickListener(v -> {
            LinearLayout container = binding.guardsContainer;

            LinearLayout layout = new LinearLayout(getContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            EditText newInput = new EditText(getContext());
            LinearLayout.LayoutParams inputLP = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            inputLP.weight = 1;
            newInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            newInput.setLayoutParams(inputLP);
            layout.addView(newInput);

            Button removeButton = new Button(getContext());
            removeButton.setText("Убрать");
            removeButton.setOnClickListener(clickView -> {
                ((ViewGroup) newInput.getParent()).removeView(newInput);
                ((ViewGroup) removeButton.getParent()).removeView(removeButton);
                ((ViewGroup) layout.getParent()).removeView(layout);
                counter--;
            });
            layout.addView(removeButton);

            container.addView(layout, counter);

            counter++;

        });

        binding.regButtonForward.setOnClickListener(v -> {
            List<String> emails = new ArrayList<>();
            for (int i = 0; i < binding.guardsContainer.getChildCount(); i++) {
                LinearLayout subLayout = (LinearLayout) binding.guardsContainer.getChildAt(i);
                EditText input = (EditText) subLayout.getChildAt(0);
                emails.add(input.getText().toString());
            }

            Navigation.findNavController(v).navigate(actionGuardsFragmentToPinFragment(emails.toArray(new String[0])));
        });

        binding.regButtonCancel.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

    }
}