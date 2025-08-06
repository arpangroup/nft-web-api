package com.trustai.investment_service.reservation.entity;

import com.trustai.investment_service.entity.InvestmentSchema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // stake_reservations
@Table(name = "user_reservations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "reservation_date"}) // Enforced at DB level: "Only One Reservation Per Day"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_id")
    private InvestmentSchema schema;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name = "expiry_at", nullable = false)
    private LocalDateTime expiryAt; // = reservedAt + 1 day

    private boolean isSold = false;
    private LocalDateTime soldAt;

    private BigDecimal incomeEarned = BigDecimal.ZERO;

    @Column(name = "reserved_at_date", nullable = false)
    private LocalDateTime reservationDate; // for uniqueness per day

    @PrePersist
    public void prePersist() {
        if (reservedAt == null) {
            reservedAt = LocalDateTime.now();
        }
        if (reservationDate == null) {
            reservationDate = reservedAt.toLocalDate().atStartOfDay();
        }
        if (expiryAt == null) {
            expiryAt = reservedAt.plusDays(1);
        }
    }

    public boolean isActive() {
        return !isSold && LocalDateTime.now().isBefore(expiryAt);
    }


}
