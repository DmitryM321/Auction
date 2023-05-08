package com.example.auction.service;

import com.example.auction.DTO.*;
import com.example.auction.model.Lot;
import com.example.auction.model.Status;
import com.example.auction.projection.BidderNameBidDate;
import com.example.auction.repository.BidRepository;
import com.example.auction.repository.LotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class LotService {
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;
    @Autowired
    public LotService(LotRepository lotRepository, BidRepository bidRepository) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
    }
    public BidderNameBidDate findInfoFirstBidder(Long id) {
        return bidRepository.findInfoFirstBidder(id);
    }

    public BidderNameBidDate findInfoMaxBetsBidder(Long id) {
        return bidRepository.findInfoMaxBetsBidder(id);
    }

    public FullLotDTO findAllInfoLotById(Long id) {
        FullLotDTO fullLotDTO = FullLotDTO.fromLot(findLotById(id).toLot());
        Integer currentPrice = (currentPrice(id, fullLotDTO.getBidPrice(), fullLotDTO.getStartPrice()));
        fullLotDTO.setCurrentPrice(currentPrice);
        fullLotDTO.setLastBid(findInfoABoutLastBid(id));
        //
        return fullLotDTO;
    }

    public void startLot(Long id) {
        Lot lot = lotRepository.findById(id).get();
        lot.setStatus(Status.STARTED);
        lotRepository.save(lot);
    }

    public void stopLot(Long id) {
     Lot lot = lotRepository.findById(id).get();
    lot.setStatus(Status.STOPPED);
    Lot stopLot = lotRepository.save(lot);
}

    public LotDTO createLot(CreateLotDTO createLotDTO){
        Lot lot = createLotDTO.toLot();
        lot.setStatus(Status.CREATED);
        Lot createLot = lotRepository.save(lot);
        return  LotDTO.fromLot(createLot);
        }

    public Collection<LotDTO> findAllByStatus(Status status, Integer pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        return lotRepository.findAllByStatus(status, pageRequest)
                .stream()
                .map(LotDTO::fromLot)
                .collect(Collectors.toList());
    }

    public Collection<FullLotDTO> exportAllLots() {
        return lotRepository.findAll()
                    .stream()
                    .map(FullLotDTO::fromLot)
                    .peek(lot -> lot.setCurrentPrice(currentPrice(lot.getId(), lot.getBidPrice(), lot.getStartPrice())))
                    .peek(lot -> lot.setLastBid(findInfoABoutLastBid(lot.getId())))
                    .collect(Collectors.toList());
        }
    private Integer currentPrice(Long id, Integer bidPrice, Integer startPrice) {
        return (int)(bidRepository.bidCount(id) * bidPrice + startPrice);
    }
    public LotDTO findLotById(Long id) {
        return LotDTO.fromLot(lotRepository.findById(id).get());
    }
    public boolean checkMethod(CreateLotDTO createdLotDTO) {
        if(createdLotDTO.getTitle() == null || createdLotDTO.getTitle().isEmpty()) {
            return false;
        } else if (createdLotDTO.getDescription() == null || createdLotDTO.getDescription().isEmpty()) {
            return false;
        } else if (createdLotDTO.getStartPrice() == null || createdLotDTO.getBidPrice() == null) {
            return false;
        } else {
            return true;
        }
    }
    private BidDTOFullLot findInfoABoutLastBid(Long id) {
        if(bidRepository.bidCount(id) != 0) {
            BidDTOFullLot bidDTO = new BidDTOFullLot();
            bidDTO.setBidderName(bidRepository.getInfoLastBid(id).getBidderName());
            bidDTO.setBidDate(bidRepository.getInfoLastBid(id).getBidDate());
            return bidDTO;
        } else {
            return null;
        }
    }
}
