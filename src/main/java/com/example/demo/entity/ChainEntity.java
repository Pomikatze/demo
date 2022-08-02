package com.example.demo.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "organization_chain")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chain_seq")
    @SequenceGenerator(name = "chain_seq", sequenceName = "chain_seq",allocationSize = 1)
    private Long id;

    @Column(name = "name_chain", length = 128)
    private String nameChain;

    private String nameManager;

    @Column(length = 64)
    private String phone;

    @Column(length = 128)
    private String mail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demo_user")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChainEntity that = (ChainEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
