package com.example.auction.service;

import com.example.auction.repository.BidRepository;
import com.example.auction.repository.LotRopository;

public class BidService {
    private final BidRepository bidRepository;
    private final LotRopository lotRopository;


    public BidService(BidRepository bidRepository, LotRopository lotRopository) {
        this.bidRepository = bidRepository;
        this.lotRopository = lotRopository;
    }
}
