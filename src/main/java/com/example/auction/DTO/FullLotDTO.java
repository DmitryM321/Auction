package com.example.auction.DTO;

import com.example.auction.model.Lot;
import com.example.auction.model.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class FullLotDTO {
    private Long id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private BidDTOFullLot lastBid;
    public static FullLotDTO fromLot(Lot lot) {
        FullLotDTO fullLotDTO = new FullLotDTO();
        fullLotDTO.setId(lot.getId());
        fullLotDTO.setStatus(lot.getStatus());
        fullLotDTO.setTitle(lot.getTitle());
        fullLotDTO.setDescription(lot.getDescription());
        fullLotDTO.setStartPrice(lot.getStartPrice());
        fullLotDTO.setBidPrice(lot.getBidPrice());
        return fullLotDTO;
    }
    public Lot toLot() {
        Lot lot = new Lot();
        lot.setId(this.getId());
        lot.setStatus(this.getStatus());
        lot.setTitle(this.getTitle());
        lot.setDescription(this.getDescription());
        lot.setStartPrice(this.getStartPrice());
        lot.setBidPrice(this.getBidPrice());
        return lot;
    }
}
