package com.social.network.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social.network.model.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    @NonNull
    private long id;
    @NonNull
    @JsonIgnore
    private UserDto user;

    private String author;
    @NonNull
    @JsonIgnore
    private List<Category> categories;
    @NonNull
    private String content;
    @NonNull
    private String title;

    public PostDto(@NonNull long id, @NonNull UserDto user, String author, @NonNull List<Category> categories, @NonNull String content, @NonNull String title) {
        this.id = id;
        this.user = user;
        this.author = user.getLogin();
        this.categories = categories;
        this.content = content;
        this.title = title;
    }

    public void setUser(UserDto user) {
        this.user = user;
        this.author = user.getLogin();
    }
}
