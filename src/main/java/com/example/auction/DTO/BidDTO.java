package com.example.auction.DTO;

import com.example.auction.model.Bid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BidDTO {
    @JsonIgnore
    private Long id;
    private String bidderName;
    private LocalDateTime bidDate;
    @JsonIgnore
    private Long lotId;
    public static BidDTO fromBid(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());
        bidDTO.setLotId(bid.getLot().getId());
        return bidDTO;
    }
    public Bid toBid() {
        Bid bid = new Bid();
        bid.setId(this.getId());
        bid.setBidderName(this.getBidderName());
//        bid.setBidDate(this.getBidDate());
        return bid;
    }

}
