package com.social.network.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.DETACH;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, cascade = {REFRESH, DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_posts", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private User user;

    @JsonIgnore
    @ManyToMany(targetEntity = Category.class, fetch = FetchType.EAGER, cascade = {REFRESH, DETACH})
    @JoinTable(name = "categories_posts", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    private List<Category> categories;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = {MERGE, REMOVE, REFRESH, DETACH}, fetch = FetchType.EAGER)
    private List<Comment> comments;

    private String content;

    private String title;

    public Post(User user, String content, String title) {
        this.user = user;
        this.content = content;
        this.title = title;
    }
}
