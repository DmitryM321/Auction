package com.example.auction.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bidderName;
    private LocalDateTime bidDate;  // TIMESTAMP
    @ManyToOne(fetch = FetchType.LAZY)
    private Lot lot;

    public Bid() {
    }
    public Bid(Long bidId, String bidderName, LocalDateTime bidDate, Lot lot) {
        this.id = id;
        this.bidderName = bidderName;
        this.bidDate = bidDate;
        this.lot = lot;
    }
}
