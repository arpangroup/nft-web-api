package com.trustai.income_service.income.entity;

import com.trustai.income_service.income.repository.TeamIncomeConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TeamIncomeConfigDataInitializer {
    private final TeamIncomeConfigRepository teamIncomeConfigRepository;


    /*@PostConstruct
    public void init() {
        TeamRebateConfig level2 = new TeamRebateConfig();
        level2.setRank(Rank.RANK_2);
        level2.setIncomePercentages(Map.of(
                1, new BigDecimal("12"),  // Lv.A
                2, new BigDecimal("5"),   // Lv.B
                3, new BigDecimal("2")    // Lv.C
        ));

        TeamRebateConfig level3 = new TeamRebateConfig();
        level3.setRank(Rank.RANK_3);
        level3.setIncomePercentages(Map.of(
                1, new BigDecimal("13"),
                2, new BigDecimal("6"),
                3, new BigDecimal("3")
        ));

        TeamRebateConfig level4 = new TeamRebateConfig();
        level4.setRank(Rank.RANK_4);
        level4.setIncomePercentages(Map.of(
                1, new BigDecimal("15"),
                2, new BigDecimal("7"),
                3, new BigDecimal("3")
        ));

        TeamRebateConfig level5 = new TeamRebateConfig();
        level5.setRank(Rank.RANK_5);
        level5.setIncomePercentages(Map.of(
                1, new BigDecimal("16"),
                2, new BigDecimal("8"),
                3, new BigDecimal("7")
        ));*/

    @PostConstruct
    public void init() {
        TeamIncomeConfig level2 = new TeamIncomeConfig();
        level2.setRankCode("RANK_2");
        level2.setIncomePercentages(Map.of(
                1, new BigDecimal("5"),  // Lv.A ==> 5%
                2, new BigDecimal("4"),   // Lv.B ==> 2%
                3, new BigDecimal("1")    // Lv.C
        ));

        TeamIncomeConfig level3 = new TeamIncomeConfig();
        level3.setRankCode("RANK_3");
        level3.setIncomePercentages(Map.of(
                1, new BigDecimal("6"), // 6%
                2, new BigDecimal("3"), // 3%
                3, new BigDecimal("2")
        ));

        TeamIncomeConfig level4 = new TeamIncomeConfig();
        level4.setRankCode("RANK_4");
        level4.setIncomePercentages(Map.of(
                1, new BigDecimal("0"),
                2, new BigDecimal("0"),
                3, new BigDecimal("0")
        ));

        TeamIncomeConfig level5 = new TeamIncomeConfig();
        level5.setRankCode("RANK_5");
        level5.setIncomePercentages(Map.of(
                1, new BigDecimal("0"),
                2, new BigDecimal("0"),
                3, new BigDecimal("0")
        ));

        teamIncomeConfigRepository.saveAll(List.of(level2, level3, level4, level5));
    }
}
