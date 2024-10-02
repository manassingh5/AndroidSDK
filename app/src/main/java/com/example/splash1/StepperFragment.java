package com.example.splash1;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.button.MaterialButton;

public class StepperFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stepper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        StepAdapter adapter = new StepAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        MaterialButton btnNext = view.findViewById(R.id.btn_next);
        MaterialButton btnPrevious = view.findViewById(R.id.btn_previous);

        btnNext.setOnClickListener(v -> {
            int nextItem = viewPager.getCurrentItem() + 1;
            if (nextItem < adapter.getCount()) {
                viewPager.setCurrentItem(nextItem);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            int previousItem = viewPager.getCurrentItem() - 1;
            if (previousItem >= 0) {
                viewPager.setCurrentItem(previousItem);
            }
        });
    }

    private static class StepAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_STEPS = 4;

        public StepAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Step1Fragment();
                case 1:
                    return new Step2Fragment();
                case 2:
                    return new Step3Fragment();
                case 3:
                    return new Step4Fragment();
                default:
                    return new Step1Fragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_STEPS;
        }
    }
}
