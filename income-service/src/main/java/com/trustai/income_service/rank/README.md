## Rank Calculation
Here is a flexible, extensible rank evaluation system based on your `UserHierarchy` and `RankConfig` entities. The design follows the **Strategy** + **Specification Pattern**, making it easy to add new criteria later.

## 1.  🔧 RankEvaluatorService
````java
@Service
@RequiredArgsConstructor
public class RankEvaluatorService {
    private final RankConfigRepository rankRepo;
    private final List<RankSpecification> specifications;
    private final UserMetricsService metricsService;

    public Optional<RankConfig> evaluate(User user) {
        UserMetrics metrics = metricsService.computeMetrics(user.getId());

        return rankRepo.findAllByActiveTrueOrderByRankOrderDesc().stream()
                .filter(rank -> isEligible(user, metrics, rank))
                .findFirst(); // Highest matched rank
    }

    private boolean isEligible(User user, UserMetrics metrics, RankConfig config) {
        return specifications.stream()
                .allMatch(spec -> spec.isSatisfied(user, metrics, config));
    }
}
````


````java
public interface RankSpecification {
    boolean isSatisfied(User user, UserMetrics metrics, RankConfig config);
}
````
📐 Sample Specifications:
````java
@Component
public class MinDirectReferralsSpec implements RankSpecification {

    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        int directReferrals = metrics.getDirectReferrals(); // from precomputed stats
        return directReferrals >= config.getMinDirectReferrals();
    }
}
````

````java
@Component
public class RequiredLevelCountSpec implements RankSpecification {
    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        Map<Integer, Integer> required = config.getRequiredLevelCounts();
        Map<Integer, Long> actual = metrics.getUserHierarchyStats().getDepthWiseCounts();

        for (Map.Entry<Integer, Integer> entry : required.entrySet()) {
            if (actual.getOrDefault(entry.getKey(), 0L) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
````

````java
@Component
public class MinDepositAmountSpec implements RankSpecification {

    @Override
    public boolean isSatisfied(User user, UserMetrics metrics, RankConfig config) {
        return metrics.getTotalDeposit()
                .compareTo(config.getMinDepositAmount()) >= 0;
    }
}
````


--- 

## 2. 🧮 UserMetricsService
````java
@Service
@RequiredArgsConstructor
public class UserMetricsService {
    private final UserHierarchyRepository hierarchyRepo;
    private final UserRepository userRepository;
    private final DepositService depositService;

    public UserMetrics computeMetrics(Long userId) {
        List<UserHierarchy> downlines = hierarchyRepo.findByAncestor(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IdNotFoundException("userId " + userId + " not found"));

        // Aggregate team counts by depth
        Map<Integer, Long> depthCounts = downlines.stream()
                .filter(uh -> uh.getDepth() > 0)
                .collect(Collectors.groupingBy(
                        UserHierarchy::getDepth,
                        Collectors.counting()
                ));

        long teamSize = depthCounts.values().stream().mapToLong(Long::longValue).sum();

        UserHierarchyStats stats = UserHierarchyStats.builder()
                .depthWiseCounts(depthCounts)
                .totalTeamSize(teamSize)
                .build();

        return UserMetrics.builder()
                .directReferrals(depthCounts.getOrDefault(1, 0L).intValue())
                .userHierarchyStats(stats)
                .totalDeposit(depositService.getTotalDeposit(userId))
                //.totalInvestment()
                .walletBalance(user.getWalletBalance())
                //.totalEarnings()
                .build();
    }
}
````



### 📊 UserMetrics DTO
in `user-hierarchy-service`
````java
@Data
@Builder
public class UserMetrics {
    private int directReferrals;
    private BigDecimal totalDeposit;
    private BigDecimal totalInvestment;
    private BigDecimal walletBalance;
    private BigDecimal totalEarnings;
    private UserHierarchyStats userHierarchyStats;

    private BigDecimal totalReferralDeposit;
    private BigDecimal totalReferralInvestment;
}
````

### 📊 UserHierarchyStats DTO
````java
@Data
@Builder
public class UserHierarchyStats {
    private Map<Integer, Long> depthWiseCounts; // e.g., {1=3, 2=5, 3=10}
    private long totalTeamSize;                 // sum of depth > 0
}
````

---

## 3. 🔄 Trigger Rank Evaluation:
- **User makes a deposit/investment:** User's wallet or earnings are updated
- **User activates:** Triggered when a new user becomes active (e.g., after KYC, payment, etc.)
- **User refers someone:** Triggered when a user (referral) joins under an existing referrer.

### ✅ 3.1. Event-Driven Evaluation (Best for real-time)
````java
@Component
@RequiredArgsConstructor
public class DepositEventListener {
    private final RankEvaluatorService rankEvaluator;
    private final UserProfileService userService;

    @EventListener
    public void onDeposit(DepositEvent event) {
        User user = userService.getUserById(event.getUserId());

        Optional<RankConfig> newRank = rankEvaluator.evaluate(user);
        newRank.ifPresent(rank -> {
            user.setCurrentRank(rank); // update current rank
            userService.save(user);    // persist update
        });
    }
}
````

### ✅ 3.2. Scheduled (Batch) Evaluation (Best for fallback/accuracy
Run a daily job that checks ranks of all active users.
````java
@Scheduled(cron = "0 0 2 * * *") // every day at 2 AM
public void evaluateRanksForAllUsers() {
    List<User> users = userRepository.findAllActiveUsers();
    for (User user : users) {
        rankEvaluator.evaluate(user).ifPresent(newRank -> {
            if (!newRank.equals(user.getCurrentRank())) {
                user.setCurrentRank(newRank);
                userRepository.save(user);
            }
        });
    }
}
````

### ✅ 3.3. Manual Trigger (Admin Tool or CLI)
````java
@PostMapping("/admin/rank/evaluate/{userId}")
public ResponseEntity<String> reEvaluateRank(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    Optional<RankConfig> rank = rankEvaluator.evaluate(user);

    return rank.map(r -> ResponseEntity.ok("New rank: " + r.getDisplayName()))
               .orElse(ResponseEntity.ok("No rank matched"));
}
````


## Example: Publishing Events
````java
eventPublisher.publishEvent(new DepositActivityEvent(this, userId, new BigDecimal("5000"), "WALLET"));
eventPublisher.publishEvent(new ReferralJoinedActivityEvent(this, referrerId, newUserId));
eventPublisher.publishEvent(new UserActivatedActivityEvent(this, userId));
````

---

## ✅ UserActivityLog Entity
````java
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType; // e.g. DEPOSIT, REFERRAL_JOINED, ACTIVATED

    private LocalDateTime occurredAt;

    private String description; // Optional: free-text summary

    // Optional fields based on activity
    private BigDecimal amount;
    private String sourceUserId; // e.g., referral's ID
    private String metadataJson; // JSON-encoded extra data (optional for flexibility)

    public enum ActivityType {
        DEPOSIT,
        ACTIVATED,
        REFERRAL_JOINED,
        LOGIN,
        WITHDRAWAL,
        RANK_UPGRADED
    }
}
````

````java
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    List<UserActivityLog> findByUserIdOrderByOccurredAtDesc(Long userId);
}
````

## ✅ Usage in Listener (Log Every Event)
````java
@Component
public class UserActivityLogger {

    private final UserActivityLogRepository logRepo;

    public UserActivityLogger(UserActivityLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @EventListener
    public void logActivity(UserActivityEvent event) {
        UserActivityLog.UserActivityLogBuilder builder = UserActivityLog.builder()
            .userId(event.getUserId())
            .activityType(toActivityType(event))
            .occurredAt(event.getActivityTime())
            .description("Event: " + event.getClass().getSimpleName());

        if (event instanceof DepositActivityEvent depositEvent) {
            builder.amount(depositEvent.getAmount());
            builder.description("Deposit of " + depositEvent.getAmount() + " via " + depositEvent.getDepositType());
        } else if (event instanceof ReferralJoinedActivityEvent referralEvent) {
            builder.sourceUserId(String.valueOf(referralEvent.getNewReferralUserId()));
            builder.description("New referral joined: " + referralEvent.getNewReferralUserId());
        } else if (event instanceof UserActivatedActivityEvent) {
            builder.description("User activated");
        }

        logRepo.save(builder.build());
    }

    private UserActivityLog.ActivityType toActivityType(UserActivityEvent event) {
        return switch (event.getActivityType()) {
            case "DEPOSIT" -> UserActivityLog.ActivityType.DEPOSIT;
            case "REFERRAL_JOINED" -> UserActivityLog.ActivityType.REFERRAL_JOINED;
            case "ACTIVATED" -> UserActivityLog.ActivityType.ACTIVATED;
            default -> throw new IllegalArgumentException("Unknown activity: " + event.getActivityType());
        };
    }
}
````

---

## Track rank upgrade history or bonus payout events
Tracking Rank Upgrade History and Bonus Payout Events is crucial in any MLM system for:
- Transparency & dispute resolution
- Historical reporting
- Regulatory compliance
- Future performance analysis

### 1. RankUpgradeLog Entity
Tracks when a user’s rank changes (and why).
````java
@Entity
@Table(name = "rank_upgrade_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankUpgradeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String previousRankCode;
    private String newRankCode;

    private LocalDateTime upgradedAt;

    private String reason; // e.g., "Deposit threshold met", "Referral goal reached"

    private String triggeredByEvent; // e.g., "DEPOSIT", "REFERRAL_JOINED", "ACTIVATED"
}
````

### ✅ 2. BonusPayoutLog Entity
Tracks bonuses given due to rank upgrade, team earnings, or product sales.
````java
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bonus_payout_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonusPayoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String bonusType; // e.g., "RANK_UPGRADE", "TEAM_BONUS", "DAILY_INCOME"

    private BigDecimal amount;

    private String description; // Optional: "Rank upgrade to SILVER", "Level 2 downline sale"

    private LocalDateTime paidAt;

    private Long sourceUserId; // optional: who triggered this payout
    private String referenceId; // e.g., transactionId, payout batchId
}
````

### 🛠 Example: Logging a Rank Upgrade
Inside your `RankEvaluatorService` (when rank changes):
````java
rankUpgradeLogRepository.save(RankUpgradeLog.builder()
    .userId(user.getId())
    .previousRankCode(user.getCurrentRankCode())
    .newRankCode(newRank.getCode())
    .upgradedAt(LocalDateTime.now())
    .reason("Criteria met for " + newRank.getDisplayName())
    .triggeredByEvent("DEPOSIT")
    .build());
````

### 🛠 Example: Logging Bonus Payout
When a user earns a bonus:
````java
bonusPayoutLogRepository.save(BonusPayoutLog.builder()
    .userId(user.getId())
    .amount(new BigDecimal("500"))
    .bonusType("RANK_UPGRADE")
    .description("Achieved GOLD rank")
    .paidAt(LocalDateTime.now())
    .sourceUserId(null)
    .referenceId("rank-payout-20250707-0001")
    .build());
````

## 📊 Use Case Summary
| Log Type          | Purpose                          | When to Create                   |
| ----------------- | -------------------------------- | -------------------------------- |
| `UserActivityLog` | Track all activity events        | On every activation/deposit/etc. |
| `RankUpgradeLog`  | Track when/why rank changed      | On `rankEvaluator.evaluate()`    |
| `BonusPayoutLog`  | Track all payouts given to users | On payout, team commission, etc. |


---

## UI layout to fetch and filter these logs (e.g., by date, user, bonus type, rank)?

### 📘 GET /admin/logs/user-activity
````http
GET /admin/logs/user-activity?userId=101&activityType=DEPOSIT&from=2025-07-01&to=2025-07-07
````

### 📘 GET /admin/logs/rank-upgrades
````http
GET /admin/logs/rank-upgrades?rankCode=GOLD
````

### 📘 GET /admin/logs/bonus-payouts
````http
GET /admin/logs/bonus-payouts?bonusType=RANK_UPGRADE
````

### 📘 GET /admin/logs/user/{userId}/summary
Returns a timeline summary for a specific user:
- Activities
- Rank progress
- Bonus history
````http
GET /admin/logs/user/101/summary
````

## ✅ 2. UI Layout (Admin Dashboard)
- User Activity Logs
- Rank Upgrade Logs
- Bonus Payout Logs
- User Summary

## 🧾 Example UI Table: UserActivityLog
| Timestamp        | Type             | Amount | Description           |
| ---------------- | ---------------- | ------ | --------------------- |
| 2025-07-07 10:22 | DEPOSIT          | ₹5,000 | Wallet deposit        |
| 2025-07-06 15:01 | ACTIVATED        | —      | User activated        |
| 2025-07-06 14:20 | REFERRAL\_JOINED | —      | Referral user ID: 205 |
🔍 Filters: date, user, activity type

## 🧾 Example UI Table: RankUpgradeLog
| Date       | User ID | From Rank | To Rank | Reason                   |
| ---------- | ------- | --------- | ------- | ------------------------ |
| 2025-07-06 | 101     | SILVER    | GOLD    | Referral volume achieved |

## 🧾 Example UI Table: BonusPayoutLog
| Date       | User ID | Bonus Type    | Amount | Description                 |
| ---------- | ------- | ------------- | ------ | --------------------------- |
| 2025-07-06 | 101     | RANK\_UPGRADE | ₹1,000 | Achieved GOLD rank          |
| 2025-07-05 | 102     | TEAM\_BONUS   | ₹300   | Level 2 user purchased pack |


---

### ✅ 1. UserActivityLogDTO (Response Model)UserActivityLogDTO 
````java
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserActivityLogDTO(
    Long id,
    Long userId,
    String activityType,
    LocalDateTime occurredAt,
    String description,
    BigDecimal amount,
    String sourceUserId
) {}
````

## ✅ 2. UserActivityLogRepository with Filtering
````java
@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long>, JpaSpecificationExecutor<UserActivityLog> {
}
````

## ✅ 3. UserActivityLogSpecification (Dynamic Query Builder)
````java
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class UserActivityLogSpecification {

    public static Specification<UserActivityLog> filter(
            Long userId,
            String activityType,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (userId != null) {
                predicates.getExpressions().add(cb.equal(root.get("userId"), userId));
            }
            if (activityType != null) {
                predicates.getExpressions().add(cb.equal(root.get("activityType"), activityType));
            }
            if (from != null) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            }
            if (to != null) {
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            }

            return predicates;
        };
    }
}
````

## ✅ 4. AdminLogController
````java
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/logs/user-activity")
public class AdminLogController {

    private final UserActivityLogRepository logRepo;

    public AdminLogController(UserActivityLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @GetMapping
    public Page<UserActivityLogDTO> getLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("occurredAt").descending());

        Specification<UserActivityLog> spec = UserActivityLogSpecification.filter(userId, activityType, from, to);

        return logRepo.findAll(spec, pageable)
                .map(this::toDTO);
    }

    private UserActivityLogDTO toDTO(UserActivityLog log) {
        return new UserActivityLogDTO(
                log.getId(),
                log.getUserId(),
                log.getActivityType().name(),
                log.getOccurredAt(),
                log.getDescription(),
                log.getAmount(),
                log.getSourceUserId()
        );
    }
}
````

## 🔁 Sample API Usage
````http
GET /admin/logs/user-activity?userId=123&activityType=DEPOSIT&from=2025-07-01T00:00:00&to=2025-07-07T23:59:59
````
Returns paginated results like:
````json
{
  "content": [
    {
      "id": 1,
      "userId": 123,
      "activityType": "DEPOSIT",
      "occurredAt": "2025-07-05T10:15:30",
      "description": "Deposit of ₹5000 via WALLET",
      "amount": 5000,
      "sourceUserId": null
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "last": true,
  "size": 20,
  "number": 0,
  "sort": {...},
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
````


---

###  RankUpgradeLog 
````java
public record RankUpgradeLogDTO(
    Long id,
    Long userId,
    String previousRankCode,
    String newRankCode,
    LocalDateTime upgradedAt,
    String reason,
    String triggeredByEvent
) {}
````
### BonusPayoutLogDTO
````java
public record BonusPayoutLogDTO(
    Long id,
    Long userId,
    String bonusType,
    BigDecimal amount,
    String description,
    LocalDateTime paidAt,
    Long sourceUserId,
    String referenceId
) {}
````

## Repository
````java
public interface RankUpgradeLogRepository extends JpaRepository<RankUpgradeLog, Long>, JpaSpecificationExecutor<RankUpgradeLog> {}

public interface BonusPayoutLogRepository extends JpaRepository<BonusPayoutLog, Long>, JpaSpecificationExecutor<BonusPayoutLog> {}

````

## 4. Specifications for Filtering
RankUpgradeLogSpecification
````java
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class RankUpgradeLogSpecification {
    public static Specification<RankUpgradeLog> filter(
            Long userId,
            String rankCode,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (userId != null) {
                predicates.getExpressions().add(cb.equal(root.get("userId"), userId));
            }
            if (rankCode != null) {
                predicates.getExpressions().add(cb.equal(root.get("newRankCode"), rankCode));
            }
            if (from != null) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("upgradedAt"), from));
            }
            if (to != null) {
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("upgradedAt"), to));
            }

            return predicates;
        };
    }
}

````
BonusPayoutLogSpecification:
````java
public class BonusPayoutLogSpecification {
    public static Specification<BonusPayoutLog> filter(
            Long userId,
            String bonusType,
            LocalDateTime from,
            LocalDateTime to
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (userId != null) {
                predicates.getExpressions().add(cb.equal(root.get("userId"), userId));
            }
            if (bonusType != null) {
                predicates.getExpressions().add(cb.equal(root.get("bonusType"), bonusType));
            }
            if (from != null) {
                predicates.getExpressions().add(cb.greaterThanOrEqualTo(root.get("paidAt"), from));
            }
            if (to != null) {
                predicates.getExpressions().add(cb.lessThanOrEqualTo(root.get("paidAt"), to));
            }

            return predicates;
        };
    }
}

````

## AdminLogController (Rank & Bonus)
````java
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/logs")
public class AdminLogController {

    private final RankUpgradeLogRepository rankRepo;
    private final BonusPayoutLogRepository bonusRepo;

    public AdminLogController(RankUpgradeLogRepository rankRepo, BonusPayoutLogRepository bonusRepo) {
        this.rankRepo = rankRepo;
        this.bonusRepo = bonusRepo;
    }

    @GetMapping("/rank-upgrades")
    public Page<RankUpgradeLogDTO> getRankUpgrades(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String rankCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("upgradedAt").descending());
        Specification<RankUpgradeLog> spec = RankUpgradeLogSpecification.filter(userId, rankCode, from, to);

        return rankRepo.findAll(spec, pageable).map(this::toRankDTO);
    }

    @GetMapping("/bonus-payouts")
    public Page<BonusPayoutLogDTO> getBonusPayouts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String bonusType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paidAt").descending());
        Specification<BonusPayoutLog> spec = BonusPayoutLogSpecification.filter(userId, bonusType, from, to);

        return bonusRepo.findAll(spec, pageable).map(this::toBonusDTO);
    }

    private RankUpgradeLogDTO toRankDTO(RankUpgradeLog log) {
        return new RankUpgradeLogDTO(
                log.getId(),
                log.getUserId(),
                log.getPreviousRankCode(),
                log.getNewRankCode(),
                log.getUpgradedAt(),
                log.getReason(),
                log.getTriggeredByEvent()
        );
    }

    private BonusPayoutLogDTO toBonusDTO(BonusPayoutLog log) {
        return new BonusPayoutLogDTO(
                log.getId(),
                log.getUserId(),
                log.getBonusType(),
                log.getAmount(),
                log.getDescription(),
                log.getPaidAt(),
                log.getSourceUserId(),
                log.getReferenceId()
        );
    }
}

````

## API Usage Examples
````http
GET /admin/logs/rank-upgrades?rankCode=SILVER
GET /admin/logs/bonus-payouts?userId=101&bonusType=TEAM_BONUS&from=2025-07-01T00:00:00
````