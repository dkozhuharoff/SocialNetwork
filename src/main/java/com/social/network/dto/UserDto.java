package com.social.network.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social.network.model.Comment;
import com.social.network.model.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NonNull
    private long id;
    @NonNull
    private String login;
    @JsonIgnore
    private List<Post> posts;
    @JsonIgnore
    private List<Comment> comments;
}
