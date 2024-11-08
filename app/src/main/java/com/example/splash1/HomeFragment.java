package com.example.splash1;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
  //  private static final String USERNAME_KEY = "username_key";
    private ImageView handFocus;
    private TextView messageCountTextView;
    private SharedPrefManager sharedPrefManager;
    public static HomeFragment newInstance(String username) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
     //   args.putString(USERNAME_KEY, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //handFocus = view.findViewById(R.id.hand_focus);
        //highlightOption();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        messageCountTextView = view.findViewById(R.id.message_count);
        // Assuming you have the userId available
        sharedPrefManager = new SharedPrefManager(getActivity());
        String userId = sharedPrefManager.getUserId();
        fetchMessages(userId);
        Log.d("UserID" , "user id =,"+userId);
        //updateMessageCount();
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView welcomeMessage = view.findViewById(R.id.welcome_message);
        Button btnStartStepper = view.findViewById(R.id.btn_start_stepper);


        // Retrieve the username from arguments
        //   String username = getArguments() != null ? getArguments().getString(USERNAME_KEY) : "User";
      SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
      //  String username = sharedPrefManager.getUsername();
        welcomeMessage.setText("Welcome, " + username + "!");

        btnStartStepper.setOnClickListener(v -> {
            // highlightOption(); // Highlight the button when clicked
            openStepperActivity();
        });
    }


    private void openStepperActivity() {
        Intent intent = new Intent(getActivity(), StepperActivity.class);
        startActivity(intent);
    }

    /*private void openSendMessageActivity() {
        Intent intent = new Intent(getActivity(), SendMessageActivity.class);
        startActivity(intent);
    }*/


    /*private void highlightOption() {
        Log.d("HomeFragment", "Highlighting option");
        handFocus.setVisibility(View.VISIBLE);

        Animation flashAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.flash_hand_focus);
        handFocus.startAnimation(flashAnimation);

        handFocus.postDelayed(() -> {
            handFocus.setVisibility(View.INVISIBLE);
            Log.d("HomeFragment", "Hand icon hidden");
        }, 5000);
    }*/

    private void fetchMessages(String userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getMessagesByUserId(userId).enqueue(new Callback<List<Message>>() {
         //   Log.d("msessaag", "Message = " +userId);
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> messages = response.body();
                    Log.d("HomeFragment", "Messages fetched successfully");
                    messageAdapter = new MessageAdapter(messages, message -> openChatActivity(message));
                    recyclerView.setAdapter(messageAdapter);
                    updateMessageCount();
                    recyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
                } else {
                    recyclerView.setVisibility(View.GONE); // Hide if there's no data
                    messageCountTextView.setText("Total Messages: 0");
                    Log.e("HomeFragment", "Error: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("HomeFragment", "API Call failed: " + t.getMessage());
            }
        });
    }


    private void openChatActivity(Message message) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("MESSAGE_BODY", message.getBody());
        intent.putExtra("FROM_NUMBER", message.getFromNumber());
        intent.putExtra("TO_NUMBER", message.getToNumber());
        intent.putExtra("CHANNEL_ID", message.getChannelId());
        intent.putExtra("CHANNEL_NAME", message.getChannelName());
        startActivity(intent);
    }

    private void updateMessageCount() {
        int count = messageAdapter.getItemCount(); // Get the item count from the adapter
        messageCountTextView.setText("Total Messages:" + count); // Update the TextView
    }


}
