package org.consulting.ai.mining.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "consulting_usage_metrics")
public class ConsultingUsageMetric extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "execution_timestamp")
    public LocalDateTime executionTimestamp = LocalDateTime.now();

    @Column(name = "match_score")
    public BigDecimal matchScore;

    @Column(name = "seniority_detected")
    public String seniorityDetected;

    @Column(name = "technologies_matched_count")
    public Integer technologiesMatchedCount;

    @Column(name = "gaps_count")
    public Integer gapsCount;

    public String status = "SUCCESS";
}