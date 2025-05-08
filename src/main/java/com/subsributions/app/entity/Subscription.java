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
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(description = "Сущность подписки")
@Table(name = "subscriptions")
@Entity
@Getter
@Setter
@Builder
@ToString
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Subscription.withUsers",
                attributeNodes = @NamedAttributeNode("userSubscriptions")
        )
})
@NoArgsConstructor
@AllArgsConstructor
public class Subscription implements Serializable {

    /**
     * id подписки
     */
    @Schema(description = "Уникальный идентификатор",
            example = "123",
            accessMode = READ_ONLY)
    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * название подписки
     */
    @Schema(description = "Название подписки",
            example = "Premium Access",
            requiredMode = REQUIRED)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * дата начала подписки
     */
    @Schema(description = "Дата начала действия",
            example = "2024-01-01")
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * дата окончания подписки
     */
    @Schema(description = "Дата окончания действия",
            example = "2024-12-31")
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * подписка окончилась
     */
    @Schema(description = "подписка окончилась",
            example = "2025-12-31")
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

    /**
     * версия записи для оптимистической блокировки
     */
    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * подписчик
     */
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UserSubscription> userSubscriptions = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Subscription that = (Subscription) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("End date must be after start date");
        }
    }
}
