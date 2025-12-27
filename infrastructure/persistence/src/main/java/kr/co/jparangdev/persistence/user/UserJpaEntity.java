package kr.co.jparangdev.persistence.user;

import jakarta.persistence.*;
import kr.co.jparangdev.persistence.common.BaseJpaEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserJpaEntity extends BaseJpaEntity {
    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime lastLoginAt;

    public enum Status {
        ACTIVE, DORMANT, DELETED
    }
}
