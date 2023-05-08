package com.example.auction.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BidDTOFullLot {
    private String bidderName;
    private LocalDateTime bidDate;
    }
