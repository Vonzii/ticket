package com.example.ticket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FragmentOne extends Fragment {

    private ActivityResultLauncher<Intent> getContent;
    private EditText editTextUserName;
    private TextView textViewStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize getContent launcher for image selection
        getContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            // Save the selected image to internal storage
                            saveImageToInternalStorage(selectedImageUri);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        editTextUserName = view.findViewById(R.id.edit_text_user_name);
        Button saveUserNameButton = view.findViewById(R.id.button_save_user_name);
        textViewStatus = view.findViewById(R.id.text_view_fragment_one);

        saveUserNameButton.setOnClickListener(v -> {
            String username = editTextUserName.getText().toString();
            saveUsernamePermanently(username);
        });

        updateFileStatus();

        view.findViewById(R.id.button_upload_image).setOnClickListener(v -> {
            openImagePicker();
        });

        return view;
    }

    void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getContent.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void saveImageToInternalStorage(Uri uri) {
        try (InputStream inputStream = getContext().getContentResolver().openInputStream(uri)) {
            File file = new File(getContext().getFilesDir(), "profile.jpg");
            try (OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                updateFileStatus();
                Log.d("FragmentOne", "Image saved successfully: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e("FragmentOne", "Error saving image", e);
        }
    }

    private void updateFileStatus() {
        boolean usernameExists = new File(getContext().getFilesDir(), "username.txt").exists();
        boolean profileImageExists = new File(getContext().getFilesDir(), "profile.jpg").exists();

        String statusMessage;
        if (usernameExists && profileImageExists) {
            statusMessage = "User Name and Profile Picture found. \n Sometimes, it says username found, but its not, just reenter the name for safety";
        } else if (usernameExists) {
            statusMessage = "User Name Found. Please add Profile Picture. \n Sometimes, it says username found, but its not, just reenter the name for safety";
        } else if (profileImageExists) {
            statusMessage = "Profile Picture Found. Please add User Name.";
        } else {
            statusMessage = "None found.";
        }

        textViewStatus.setText(statusMessage);
    }

    private void saveUsernamePermanently(String username) {
        try {
            // Create a file in the app's internal storage
            File file = new File(getContext().getFilesDir(), "username.txt");

            // Open a FileOutputStream to write to the file
            FileOutputStream outputStream = new FileOutputStream(file);

            // Write the username to the file
            outputStream.write(username.getBytes());

            // Close the stream
            outputStream.close();

            updateFileStatus();

            // Log success or notify the user
            Log.d("FragmentOne", "Username saved successfully: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("FragmentOne", "Error saving username", e);
            // Handle error or notify the user
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //saveUsernamePermanently(editTextUserName.getText().toString());
    }
}

