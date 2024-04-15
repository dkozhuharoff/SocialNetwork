package com.social.network.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    @NonNull
    private long id;
    @JsonIgnore
    @NonNull
    private UserDto user;
    private String author;
    @JsonIgnore
    @NonNull
    private PostDto post;
    @NonNull
    private String content;

    public CommentDto(@NonNull long id, @NonNull UserDto user, String author, @NonNull PostDto post, @NonNull String content) {
        this.id = id;
        this.user = user;
        this.author = user.getLogin();
        this.post = post;
        this.content = content;
    }

    public void setUser(UserDto user) {
        this.user = user;
        this.author = user.getLogin();
    }
}
