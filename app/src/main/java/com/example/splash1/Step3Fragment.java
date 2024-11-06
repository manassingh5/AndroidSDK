// Step3Fragment.java
package com.example.splash1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Step3Fragment extends Fragment {

    private RadioGroup radioGroupPlans;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step3, container, false);

        radioGroupPlans = view.findViewById(R.id.radio_group_plans);

        radioGroupPlans.setOnCheckedChangeListener((group, checkedId) -> {
            StepperActivity activity = (StepperActivity) getActivity();
            if (activity != null) {
                activity.updateButtons(2);
            }
        });

        return view;
    }

    public boolean isPlanSelected() {
        return radioGroupPlans.getCheckedRadioButtonId() != -1;
    }

    public int getSelectedPlan() {
        int selectedId = radioGroupPlans.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = getView().findViewById(selectedId);
            switch (selectedRadioButton.getText().toString()) {
                case "Free":
                    return 1;
                case "Basic":
                    return 2;
                case "Standard":
                    return 3;
                case "Premium":
                    return 4;
                default:
                    return 0;
            }
        }
        return 0;
    }
}
