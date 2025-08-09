package com.trustai.income_service.income.repository;

import com.trustai.income_service.income.entity.IncomeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IncomeHistoryRepository extends JpaRepository<IncomeHistory, Long> {
    @Query("""
        SELECT ih.type,
               SUM(CASE WHEN FUNCTION('DATE', ih.createdAt) = :today THEN ih.amount ELSE 0 END) AS dailyIncome,
               SUM(ih.amount) AS totalIncome
        FROM IncomeHistory ih
        WHERE ih.userId = :userId
        GROUP BY ih.type
    """)
    List<Object[]> findIncomeSummaryByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);
}
