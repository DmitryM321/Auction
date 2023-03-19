package com.example.auction.controller;

import com.example.auction.DTO.BidDTO;
import com.example.auction.DTO.LotDTO;
import com.example.auction.model.Status;
import com.example.auction.service.BidService;
import com.example.auction.service.LotService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RequestMapping("/lot")
@RestController
public class LotsController {

    private final LotService lotService;
    private final BidService bidService;
    public LotsController(LotService lotService, BidService bidService) {
        this.lotService = lotService;
        this.bidService = bidService;
    }

//1
    @GetMapping("{id}/first")
    public ResponseEntity<BidDTO> findFirstBidder(Long id) {
        return ResponseEntity.ok(bidService.findFirstBidder(id));
    }
//2
    @GetMapping("{id}/frequent")
    public ResponseEntity<BidDTO> findFirstBidder(Long id) {
        return ResponseEntity.ok(bidService.findFirstBidder(id));
    }
//3
    @GetMapping("/{id}")
    public ResponseEntity<LotDTO> getLotById(@PathVariable Long id) {
        LotDTO lotDTO = lotSetvice.getLotById(id);
        if (lotDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lotDTO);
    }

// 4
    @PostMapping("/{id}/start")
    public LotDTO startLot(@RequestBody LotDTO lotDTO) {
        return lotSetvice.startLot(lotDTO);
    }
// 5
    @PostMapping("/{id}/bid")
    public LotDTO makeBid(@RequestBody LotDTO lotDTO) {
    return lotSetvice.makeBid(lotDTO);
}

// 6
    @PostMapping("/{id}/stop")
    public LotDTO stopBid(@RequestBody LotDTO lotDTO) {
        return lotSetvice.stopBid(lotDTO);
    }

//7
    @PostMapping
    public LotDTO createLot(@RequestBody LotDTO lotDTO) {
        return lotSetvice.createLot(lotDTO);
    }

//8
    @GetMapping
    public ResponseEntity<Collection<LotDTO>> findAll(@RequestParam Status status,
        @RequestParam Integer pageSize) {

    return ResponseEntity.ok(lotService.findAll(status, pageSize));
}
// 9
@GetMapping("export")
public ResponseEntity<Collection<LotDTO>> exportCSV(


}

