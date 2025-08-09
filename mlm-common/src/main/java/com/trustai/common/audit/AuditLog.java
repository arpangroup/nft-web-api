//package com.trustai.common.audit;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "audit_log")
//@NoArgsConstructor
//@Data
//public class AuditLog {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String action;
//    private String method;
//    private String username;
//    private String status; // SUCCESS or FAILURE
//    private String parameters;
//    private String errorMessage;
//
//    private LocalDateTime timestamp;
//
//    // Constructors, getters, setters
//}
