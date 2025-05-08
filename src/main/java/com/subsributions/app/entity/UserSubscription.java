package com.subsributions.app.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;
import java.time.LocalDate;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Связь пользователя с подпиской")
@Table(name = "user_subscriptions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "subscription_id"}))

@Where(clause = "is_declined = false")
@Entity
@Getter
@Setter
@Builder
@ToString
@SQLDelete(sql = "UPDATE user_subscriptions SET is_declined = true WHERE user_subscription_id = ?")
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "UserSubscription.full",
                attributeNodes = {
                        @NamedAttributeNode("user"),
                        @NamedAttributeNode("subscription")
                }
        )
})
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription  {

    @Schema(description = "Уникальный идентификатор связи",
            example = "456",
            accessMode = READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_subscription_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    /**
     * подписчик отказался от подписки
     */
    @Schema(description = "Статус подписки",
            example = "false")
    @Column(name = "is_declined", nullable = false)
    private boolean isDeclined;

    @Schema(description = "Дата активации",
            example = "2024-01-01")
    @Column(name = "activation_date", nullable = false)
    private LocalDate activationDate = LocalDate.now();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserSubscription that = (UserSubscription) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
