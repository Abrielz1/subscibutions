package com.subsributions.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;


/**
 * Класс описывающий сущность пользователь (User)
 */
@Schema(description = "Сущность пользователя")
@Table(name = "users")
@Entity
@Getter
@Setter
@Builder
@ToString
@NamedEntityGraph(
        name = "User.withSubscriptions",
        attributeNodes = @NamedAttributeNode(value = "subscriptions", subgraph = "subscription-details"),
        subgraphs = @NamedSubgraph(
                name = "subscription-details",
                attributeNodes = @NamedAttributeNode("subscription")
        )
)
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     * id пользователя
     */
    @Schema(description = "Уникальный идентификатор пользователя",
            example = "12345",
            accessMode = READ_ONLY)
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * электронная почта подписчика
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * пароль подписчика
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * дата регистрации пользователя
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * подписки созданные пользователем
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UserSubscription> subscriptions = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
