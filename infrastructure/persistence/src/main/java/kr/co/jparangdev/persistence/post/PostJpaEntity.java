package kr.co.jparangdev.persistence.post;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.co.jparangdev.persistence.common.BaseJpaEntity;
import kr.co.jparangdev.domain.post.Post;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class PostJpaEntity extends BaseJpaEntity {
    private String title;
    private String content;
    private Long authorId;

    public static PostJpaEntity from(Post post) {
        PostJpaEntity entity = new PostJpaEntity();
        entity.setId(post.getId());
        entity.setTitle(post.getTitle());
        entity.setContent(post.getContent());
        entity.setAuthorId(post.getAuthorId());
        entity.setCreatedAt(post.getCreatedAt());
        entity.setUpdatedAt(post.getUpdatedAt());
        return entity;
    }
}
