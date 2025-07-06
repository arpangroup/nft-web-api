package com.trustai.income_service.rank.entity;

import com.trustai.income_service.rank.repository.RankConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.trustai.income_service.rank.entity.Rank.*;


@Component
@RequiredArgsConstructor
public class RankDataInitializer {
    private final RankConfigRepository rankConfigRepository;

    /*@PostConstruct
    public void init() {
        // Rank 1: No level requirement
        RankConfig rank1 = new RankConfig(RANK_1, 50, 100, new BigDecimal("1.8"));

        // Rank 2: Level A = 3, Level B = 4, Level C = 1
        RankConfig rank2 = new RankConfig(RANK_2, 500, 2000, new BigDecimal("2.1"));
        rank2.getRequiredLevelCounts().put(1, 3); // Level A
        rank2.getRequiredLevelCounts().put(2, 4); // Level B
        rank2.getRequiredLevelCounts().put(3, 1); // Level C

        // Rank 3: Level A = 6, Level B = 18, Level C = 2
        RankConfig rank3 = new RankConfig(RANK_3, 2000, 5000, new BigDecimal("2.6"));
        rank3.getRequiredLevelCounts().put(1, 6);
        rank3.getRequiredLevelCounts().put(2, 18);
        rank3.getRequiredLevelCounts().put(3, 2);

        // Rank 4: Level A = 15, Level B = 30, Level C = 5
        RankConfig rank4 = new RankConfig(RANK_4, 5000, 10000, new BigDecimal("3.1"));
        rank4.getRequiredLevelCounts().put(1, 15);
        rank4.getRequiredLevelCounts().put(2, 30);
        rank4.getRequiredLevelCounts().put(3, 5);

        rankConfigRepository.saveAll(List.of(rank1, rank2, rank3, rank4));
    }*/

    @PostConstruct
    public void init() {
        // Rank 0: No level requirement; No Txn possible
        RankConfig rank0 = new RankConfig(RANK_0, 15, 40, new BigDecimal("0")); //0%
        rank0.setTxnPerDay(0);
        rank0.getRequiredLevelCounts().put(1, 0); // Level A
        rank0.getRequiredLevelCounts().put(2, 0); // Level B
        rank0.getRequiredLevelCounts().put(3, 0); // Level C
        rank0.setStakeValue(15);

        // Rank 1: No level requirement
        RankConfig rank1 = new RankConfig(RANK_1, 40, 300, new BigDecimal("1.00")); //1%
        rank1.setTxnPerDay(1);
        rank1.getRequiredLevelCounts().put(1, 0); // Level A
        rank1.getRequiredLevelCounts().put(2, 0); // Level B
        rank1.getRequiredLevelCounts().put(3, 0); // Level C
        rank1.setStakeValue(0);

        // Rank 2: Level A = 4, Level B = 5, Level C = 1
        RankConfig rank2 = new RankConfig(RANK_2, 300, 600, new BigDecimal("1.70"));//10%
        rank2.setTxnPerDay(1);
        rank2.getRequiredLevelCounts().put(1, 4); // Level A
        rank2.getRequiredLevelCounts().put(2, 5); // Level B
        rank2.getRequiredLevelCounts().put(3, 1); // Level C
        rank2.setStakeValue(100);

        // Rank 3: Level A = 6, Level B = 25, Level C = 5
        RankConfig rank3 = new RankConfig(RANK_3, 600, 1500, new BigDecimal("2.30"));//15%
        rank3.setTxnPerDay(1);
        rank3.getRequiredLevelCounts().put(1, 6);
        rank3.getRequiredLevelCounts().put(2, 25);
        rank3.getRequiredLevelCounts().put(3, 5);
        rank3.setStakeValue(200);

        // Rank 4: Level A = 12, Level B = 35, Level C = 10
        RankConfig rank4 = new RankConfig(RANK_4, 1500, 3000, new BigDecimal("2.80"));
        rank4.setTxnPerDay(1);
        rank4.getRequiredLevelCounts().put(1, 12);
        rank4.getRequiredLevelCounts().put(2, 35);
        rank4.getRequiredLevelCounts().put(3, 10);
        rank4.setStakeValue(300);


        // Rank 5: Level A = 16, Level B = 70, Level C = 20
        RankConfig rank5 = new RankConfig(RANK_5, 3000, 6000, new BigDecimal("3.30"));
        rank5.setTxnPerDay(1);
        rank5.getRequiredLevelCounts().put(1, 16);
        rank5.getRequiredLevelCounts().put(2, 70);
        rank5.getRequiredLevelCounts().put(3, 20);
        rank5.setStakeValue(400);


        // Rank 6: Level A = 20, Level B = 160, Level C = 40
        RankConfig rank6 = new RankConfig(RANK_6, 6000, 15000, new BigDecimal("3.80"));
        rank6.setTxnPerDay(1);
        rank6.getRequiredLevelCounts().put(1, 20);
        rank6.getRequiredLevelCounts().put(2, 160);
        rank6.getRequiredLevelCounts().put(3, 40);
        rank6.setStakeValue(500);


        // Rank 7: Level A = 35, Level B = 350, Level C = 50
        RankConfig rank7 = new RankConfig(RANK_7, 15000, 99000, new BigDecimal("4.5"));
        rank7.setTxnPerDay(1);
        rank7.getRequiredLevelCounts().put(1, 35);
        rank7.getRequiredLevelCounts().put(2, 350);
        rank7.getRequiredLevelCounts().put(3, 50);
        rank7.setStakeValue(500);

        rankConfigRepository.saveAll(List.of(rank0, rank1, rank2, rank3, rank4, rank5, rank6, rank7));
    }
}
