package com.example.auction.DTO;

import com.example.auction.model.Lot;
import com.example.auction.model.Status;
import lombok.Data;

@Data
public class LotDTO {
    private Long id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;

    public static LotDTO fromLot(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        return lotDTO;
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
