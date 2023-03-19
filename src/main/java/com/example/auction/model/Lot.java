package com.example.auction.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Status status; // Enum
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private List lastBid;

//    enum Status (STARTED, STOPPED, CREATED);
}
