package com.midterm.threads_clone.ui.home;

public class Post {

    private int profileImage;   // ảnh đại diện
    private String username;    // tên người đăng
    private String content;     // nội dung bài viết
    private int postImage;      // ảnh bài viết (0 nếu không có)
    private String time;        // thời gian đăng
    private int repliesCount;   // số replies
    private int likesCount;     // số likes
    private boolean isLiked;    // đã bấm tim chưa

    public Post(int profileImage, String username, String content, int postImage,
                String time, int repliesCount, int likesCount, boolean isLiked) {
        this.profileImage = profileImage;
        this.username = username;
        this.content = content;
        this.postImage = postImage;
        this.time = time;
        this.repliesCount = repliesCount;
        this.likesCount = likesCount;
        this.isLiked = isLiked;
    }

    // Getter & Setter
    public int getProfileImage() {
        return profileImage;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public int getPostImage() {
        return postImage;
    }

    public String getTime() {
        return time;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void toggleLike() {
        isLiked = !isLiked;
        likesCount += isLiked ? 1 : -1;
    }
}