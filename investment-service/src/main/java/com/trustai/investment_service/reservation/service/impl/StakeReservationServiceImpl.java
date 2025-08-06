package com.trustai.investment_service.reservation.service.impl;

import com.trustai.common.api.UserApi;
import com.trustai.common.dto.UserInfo;
import com.trustai.investment_service.entity.InvestmentSchema;
import com.trustai.investment_service.repository.SchemaRepository;
import com.trustai.investment_service.reservation.dto.UserReservationDto;
import com.trustai.investment_service.reservation.entity.UserReservation;
import com.trustai.investment_service.reservation.mapper.UserReservationMapper;
import com.trustai.investment_service.reservation.repository.StakeReservationRepository;
import com.trustai.investment_service.reservation.service.StakeReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class StakeReservationServiceImpl implements StakeReservationService {
    private final StakeReservationRepository reservationRepository;
    private final SchemaRepository schemaRepository;
    private final UserReservationMapper mapper;
    private final UserApi userApi;

    @Override
    public UserReservation autoReserve(Long userId) {
        log.info("Attempting to reserve - userId: {}", userId);
        UserInfo user = userApi.getUserById(userId);

        // 1. Check existing reservation for today
        boolean alreadyReserved = reservationRepository.existsByUserIdAndReservationDate(userId, LocalDate.now());
        if (alreadyReserved) {
            log.warn("User has already reserved today - userId: {}", userId);
            throw new IllegalStateException("User has already reserved a stake today");
        }

        // 2. Get max-priced eligible stake schema
        InvestmentSchema schema = schemaRepository
                .findTopByInvestmentSubTypeAndIsActiveTrueOrderByPriceDesc(InvestmentSchema.InvestmentSubType.STAKE)
                .orElseThrow(() -> {
                    log.error("No suitable stake schema found for reservation");
                    return new IllegalArgumentException("No suitable stake schema found for reservation");
                });

        // 3. Validate user wallet balance
        BigDecimal walletBalance = user.getWalletBalance();
        if (walletBalance.compareTo(schema.getMinimumInvestmentAmount()) < 0 ||
                walletBalance.compareTo(schema.getMaximumInvestmentAmount()) > 0) {
            throw new IllegalStateException("Wallet balance not in range for reservation");
        }

        // 4. Create and save reservation
        UserReservation reservation = UserReservation.builder()
                .userId(userId)
                .schema(schema)
                .price(schema.getPrice())
                .reservedAt(LocalDateTime.now())
                .expiryAt(LocalDateTime.now().plusDays(1))
                .incomeEarned(BigDecimal.ZERO)
                .isSold(false)
                .build();

        UserReservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation successful - reservationId: {}, userId: {}", savedReservation.getId(), userId);

        return savedReservation;
    }


    @Deprecated
    @Override
    public UserReservation reserve(Long userId, Long schemaId, BigDecimal amount) {
        return null;
    }


    @Override
    public void sellReservation(Long reservationId, Long userId) {
        log.info("Attempting to sell reservation - reservationId: {}, userId: {}", reservationId, userId);

        UserReservation reservation = reservationRepository
                .findByIdAndUserIdAndIsSoldFalse(reservationId, userId)
                .orElseThrow(() -> {
                    log.warn("Sell failed - reservation not found or already sold. reservationId: {}, userId: {}", reservationId, userId);
                    return new IllegalArgumentException("Reservation not found or already sold.");
                });

        reservation.setSold(true);
        reservation.setSoldAt(LocalDateTime.now());

        reservationRepository.save(reservation);
        log.info("Reservation sold successfully - reservationId: {}, userId: {}", reservationId, userId);
    }


    @Override
    public List<UserReservationDto> getActiveReservations(Long userId) {
        log.info("Fetching active reservations - userId: {}", userId);
        List<UserReservation> activeReservations = reservationRepository
                .findByUserIdAndIsSoldFalseAndExpiryAtAfter(userId, LocalDateTime.now());

        List<UserReservationDto> dtos =  activeReservations.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.info("Found {} active reservations for userId: {}", dtos.size(), userId);
        return dtos;
    }


    @Override
    public void expireUnclaimedReservations() {
        log.info("Running expiration of unclaimed reservations...");
        List<UserReservation> expired = reservationRepository.findByIsSoldFalseAndExpiryAtBefore(LocalDateTime.now());

        if (expired.isEmpty()) {
            log.info("No unclaimed reservations to expire.");
            return;
        }

        for (UserReservation r : expired) {
            log.info("Expiring reservation id={} for user={}", r.getId(), r.getUserId());
            // You could also move to a collection or user investment if needed
        }

        reservationRepository.saveAll(expired);
        log.info("Expired {} reservations.", expired.size());
    }
}
