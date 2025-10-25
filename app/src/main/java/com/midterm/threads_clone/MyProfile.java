package com.midterm.threads_clone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.midterm.threads_clone.ui.home.Post;
import com.midterm.threads_clone.ui.home.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyProfile extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postList.add(new Post(R.drawable.ic_launcher_background, "zuck", "10 million sign ups in seven hours.",
                0, "33m", 26, 112, false));
        postList.add(new Post(R.drawable.ic_launcher_background, "zuck", "10 million sign ups in seven hours.",
                0, "33m", 26, 112, false));

        postAdapter = new PostAdapter(this, postList, null);
        recyclerView.setAdapter(postAdapter);


    }
}