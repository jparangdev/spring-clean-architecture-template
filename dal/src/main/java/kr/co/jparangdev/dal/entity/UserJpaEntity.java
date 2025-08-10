package kr.co.jparangdev.dal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserJpaEntity extends BaseJpaEntity {
    private String username;
    private String email;
}
