package kr.co.jparangdev.persistence.post;

import kr.co.jparangdev.application.post.PostRepository;
import kr.co.jparangdev.domain.post.Post;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Post save(Post post) {
        PostJpaEntity postJpaEntity = PostJpaEntity.from(post);
        PostJpaEntity savedEntity = postJpaRepository.save(postJpaEntity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        postJpaRepository.deleteById(id);
    }

    private Post toDomain(PostJpaEntity postJpaEntity) {
        return new Post(
                postJpaEntity.getId(),
                postJpaEntity.getTitle(),
                postJpaEntity.getContent(),
                postJpaEntity.getAuthorId(),
                postJpaEntity.getCreatedAt(),
                postJpaEntity.getUpdatedAt());
    }
}
