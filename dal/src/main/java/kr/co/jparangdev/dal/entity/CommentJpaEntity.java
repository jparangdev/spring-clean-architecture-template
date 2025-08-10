package kr.co.jparangdev.dal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class CommentJpaEntity extends BaseJpaEntity {
    private String content;
    private Long postId;
    private Long authorId;
}
