package com.social.network.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social.network.model.Post;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private long id;

    @NonNull
    private String name;

    @JsonIgnore
    private List<Post> posts;
}
