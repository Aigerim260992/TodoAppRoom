package com.geektech.todoapp.ui.tools_notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.geektech.todoapp.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class NotesFragment extends Fragment {

    private EditText editText;
    private File file;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText = view.findViewById(R.id.editText);
        File folder = new File(Environment.getExternalStorageDirectory(), "TodoApp");
        folder.mkdir();
        file = new File(folder, "note.txt");
        try {
            String text = FileUtils.readFileToString(file, "utf-8");
            editText.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    private void save() {
        String text = editText.getText().toString();
//        File folder = new File(Environment.getExternalStorageDirectory(), "TodoApp");
//        folder.mkdir();
//        File file = new File(folder, "note.txt");
        try {
            FileUtils.writeStringToFile(file, text, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
