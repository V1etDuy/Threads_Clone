package com.midterm.threads_clone.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.midterm.threads_clone.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> postList;
    private OnProfileClickListener listener;

    public interface OnProfileClickListener {
        void onProfileClick(Post post);
    }

    public PostAdapter(Context context, List<Post> postList, OnProfileClickListener listener) {
        this.context = context;
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.avatar.setImageResource(post.getProfileImage());
        holder.username.setText(post.getUsername());
        holder.time.setText(post.getTime());
        holder.content.setText(post.getContent());

        if (post.getProfileImage() != 0) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(post.getProfileImage());
        } else {
            holder.image.setVisibility(View.GONE);
        }

        if (listener != null) {
            View.OnClickListener profileClickListener = v -> listener.onProfileClick(post);
            holder.avatar.setOnClickListener(profileClickListener);
            holder.username.setOnClickListener(profileClickListener);
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar, image;
        TextView username, content, time;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.username);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.post_image);
            time = itemView.findViewById(R.id.time);
        }
    }
}
