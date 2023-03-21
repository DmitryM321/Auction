package com.example.auction.model;

import com.example.auction.DTO.LotDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Status status; // Enum
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    @OneToMany(mappedBy = "lot")
    private List<Bid> bids;

    public Lot() {
    }
    public Lot(Long id, Status status, String title, String description, Integer startPrice, Integer bidPrice, List<Bid> bids) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
        this.bids = bids;
    }
}
