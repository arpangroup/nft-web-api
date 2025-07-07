package com.trustai.income_service.rank.entity;

import com.trustai.income_service.rank.repository.RankConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RankDataInitializer {
    private final RankConfigRepository rankConfigRepository;

    @PostConstruct
    public void init() {
        // Rank 0:  MinDeposit: 15; No level requirement; No Txn possible;
        RankConfig rank0 = new RankConfig(0, "RANK_0", "TrustAI Member", 15, 15, 0.0f);
        rank0.setTxnPerDay(0);
        rank0.getRequiredLevelCounts().put(1, 0); // Level A
        rank0.getRequiredLevelCounts().put(2, 0); // Level B
        rank0.getRequiredLevelCounts().put(3, 0); // Level C
        rank0.setActive(true);

        // Rank 1: MinDeposit: 40; No level requirement; Only 1 txnPerDay; 1% Commission;
        RankConfig rank1 = new RankConfig(1, "RANK_1", "TrustAI Leader", 40, 0, 1.0f); //1%
        rank1.setTxnPerDay(1);
        rank1.getRequiredLevelCounts().put(1, 0); // Level A
        rank1.getRequiredLevelCounts().put(2, 0); // Level B
        rank1.getRequiredLevelCounts().put(3, 0); // Level C
        rank1.setActive(true);

        // Rank 2: Level A = 4, Level B = 5, Level C = 1
        RankConfig rank2 = new RankConfig(2, "RANK_2", "TrustAI Captain", 300, 100, 1.7f);//10%
        rank2.setTxnPerDay(1);
        rank2.getRequiredLevelCounts().put(1, 4); // Level A
        rank2.getRequiredLevelCounts().put(2, 5); // Level B
        rank2.getRequiredLevelCounts().put(3, 1); // Level C
        rank2.setActive(true);

        // Rank 3: Level A = 6, Level B = 25, Level C = 5
        RankConfig rank3 = new RankConfig(3, "RANK_3", "trustAI Victor", 600, 200, 2.30f);//15%
        rank3.setTxnPerDay(1);
        rank3.getRequiredLevelCounts().put(1, 6);
        rank3.getRequiredLevelCounts().put(2, 25);
        rank3.getRequiredLevelCounts().put(3, 5);
        rank3.setActive(true);

        // Rank 4: Level A = 12, Level B = 35, Level C = 10
        RankConfig rank4 = new RankConfig(4, "RANK_4", "trustAI Champion", 1500, 300, 2.80f);
        rank4.setTxnPerDay(1);
        rank4.getRequiredLevelCounts().put(1, 12);
        rank4.getRequiredLevelCounts().put(2, 35);
        rank4.getRequiredLevelCounts().put(3, 10);
        rank4.setActive(true);


        // Rank 5: Level A = 16, Level B = 70, Level C = 20
        RankConfig rank5 = new RankConfig(5, "RANK_5", "trustAI Silver", 3000, 400, 3.30f);
        rank5.setTxnPerDay(1);
        rank5.getRequiredLevelCounts().put(1, 16);
        rank5.getRequiredLevelCounts().put(2, 70);
        rank5.getRequiredLevelCounts().put(3, 20);
        rank5.setActive(true);


        // Rank 6: Level A = 20, Level B = 160, Level C = 40
        RankConfig rank6 = new RankConfig(6, "RANK_6", "trustAI Gold", 6000, 500, 3.80f);
        rank6.setTxnPerDay(1);
        rank6.getRequiredLevelCounts().put(1, 20);
        rank6.getRequiredLevelCounts().put(2, 160);
        rank6.getRequiredLevelCounts().put(3, 40);
        rank6.setActive(true);


        // Rank 7: Level A = 35, Level B = 350, Level C = 50
        RankConfig rank7 = new RankConfig(7, "RANK_7", "trustAI Platinum", 15000, 500, 4.5f);
        rank7.setTxnPerDay(1);
        rank7.getRequiredLevelCounts().put(1, 35);
        rank7.getRequiredLevelCounts().put(2, 350);
        rank7.getRequiredLevelCounts().put(3, 50);
        rank7.setActive(true);

        rankConfigRepository.saveAll(List.of(rank0, rank1, rank2, rank3, rank4, rank5, rank6, rank7));
    }

}
