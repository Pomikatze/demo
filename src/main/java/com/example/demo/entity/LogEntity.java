package com.example.demo.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "test_log")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String log;

    @Column(name = "create_dttm")
    private LocalDateTime createDttm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LogEntity logEntity = (LogEntity) o;
        return id != null && Objects.equals(id, logEntity.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
