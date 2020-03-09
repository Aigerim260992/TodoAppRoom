package com.geektech.todoapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.todoapp.App;
import com.geektech.todoapp.FormActivity;
import com.geektech.todoapp.R;
import com.geektech.todoapp.model.Work;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private WorkAdapter adapter;
    private List<Work> list;
    int pos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        list = new ArrayList<>();
        adapter = new WorkAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                App.getDatabase().workDao().delete(list.get(position));
                pos = position;
                Intent intent = new Intent(getActivity(), FormActivity.class);
                intent.putExtra("work", list.get(position));
                getActivity().startActivityForResult(intent, 42);

            }

            @Override
            public void onItemLongClick(final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Вы действительно хотите удалить?").setMessage("Удалить?").
                        setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getDatabase().workDao().delete(list.get(position)); //todo удаление с базы данных
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
        App.getDatabase().workDao().getAll().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                list.clear();
                list.addAll(works);
                adapter.notifyDataSetChanged();

            }
        });
    }


}