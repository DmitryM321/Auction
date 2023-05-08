package com.example.auction.service;

import com.example.auction.DTO.BidDTO;
import com.example.auction.model.Bid;
import com.example.auction.model.Lot;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
public class BidService {
    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    @Autowired
    public BidService(BidRepository bidRepository, LotService lotService, LotRepository lotRepository) {
        this.bidRepository = bidRepository;
        this.lotRepository = lotRepository;
    }
    public BidDTO makeBid(BidDTO bidDTO) {
        Bid bid = bidDTO.toBid();
        Lot lot = lotRepository.findById(bidDTO.getLotId()).get();
        bid.setLot(lot);
        bid.setBidDate(LocalDateTime.now());
        Bid makeBid = bidRepository.save(bid);
        return BidDTO.fromBid(makeBid);
    }
}
