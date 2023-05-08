package com.example.auction.DTO;

import com.example.auction.model.Lot;
import lombok.Data;

@Data
public class CreateLotDTO{
    private Long id;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;

   public static CreateLotDTO fromCreateLotDTO(Lot lot) {
      CreateLotDTO createLotDTO = new CreateLotDTO();
       createLotDTO.setTitle(lot.getTitle());
       createLotDTO.setDescription(lot.getDescription());
       createLotDTO.setStartPrice(lot.getStartPrice());
       createLotDTO.setBidPrice(lot.getBidPrice());
    return createLotDTO;
 }
   public Lot toLot() {
      Lot lot = new Lot();
      lot.setTitle(this.getTitle());
      lot.setDescription(this.getDescription());
      lot.setStartPrice(this.getStartPrice());
      lot.setBidPrice(this.getBidPrice());
    return lot;
 }

}
