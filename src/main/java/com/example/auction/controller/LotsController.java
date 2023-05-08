package com.example.auction.controller;

import com.example.auction.DTO.BidDTO;
import com.example.auction.DTO.CreateLotDTO;
import com.example.auction.DTO.FullLotDTO;
import com.example.auction.DTO.LotDTO;
import com.example.auction.model.Bid;
import com.example.auction.model.Lot;
import com.example.auction.model.Status;
import com.example.auction.projection.BidderNameBidDate;
import com.example.auction.service.BidService;
import com.example.auction.service.LotService;
import com.fasterxml.jackson.annotation.JsonView;
import liquibase.pro.packaged.F;
import liquibase.repackaged.com.opencsv.CSVParser;
import liquibase.repackaged.com.opencsv.ICSVWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@RequestMapping("/lot")
@RestController
public class LotsController {

    private final LotService lotService;
    private final BidService bidService;
    @Autowired
    public LotsController(LotService lotService, BidService bidService) {
        this.lotService = lotService;
        this.bidService = bidService;
    }

    @GetMapping("{id}/first")
    public ResponseEntity<BidderNameBidDate> findInfoFirstBidder(@PathVariable Long id) {
        if (lotService.findLotById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lotService.findLotById(id).getStatus().equals(Status.CREATED)) {
            return ResponseEntity.badRequest().build();
        }
        BidderNameBidDate firstBidder = lotService.findInfoFirstBidder(id);
        return ResponseEntity.ok(firstBidder);
    }

    @GetMapping("{id}/frequent")
    public ResponseEntity<BidderNameBidDate> findInfoMaxBetsBidder(@PathVariable Long id) {
        if (lotService.findLotById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lotService.findLotById(id).getStatus().equals(Status.CREATED)) {
            return ResponseEntity.ok().build();
        }
        BidderNameBidDate maxBidder = lotService.findInfoMaxBetsBidder(id);
        return ResponseEntity.ok(maxBidder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullLotDTO> findAllInfoLotById(@PathVariable Long id) {
        FullLotDTO fullLotDTO = FullLotDTO.fromLot(lotService.findLotById(id).toLot());
        if (fullLotDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lotService.findAllInfoLotById(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<LotDTO> startLot(@PathVariable Long id) {
        LotDTO startLot = lotService.findLotById(id);
        if (startLot == null) {
            return ResponseEntity.notFound().build();
        }
        if (startLot.getStatus().equals(Status.STARTED)) {
            return ResponseEntity.ok().build();
        }
        if (startLot.getStatus().equals(Status.STOPPED)) {
            return ResponseEntity.badRequest().build();
        }
        if (startLot.getStatus().equals(Status.CREATED)) {
            lotService.startLot(id);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<Bid> makeBid(@PathVariable Long id, @RequestBody BidDTO bidDTO) {
        LotDTO lotDTO = lotService.findLotById(id);
        if (lotDTO == null) {
            return ResponseEntity.notFound().build();
        }
        if (lotDTO.getStatus().equals(Status.CREATED) || lotDTO.getStatus().equals(Status.STOPPED)) {
            return ResponseEntity.badRequest().build();
        }
        bidDTO.setLotId(id);
        bidService.makeBid(bidDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<LotDTO> stopLot(@PathVariable Long id) {
        LotDTO stopLot = lotService.findLotById(id);
        if (stopLot == null) {
            return ResponseEntity.notFound().build();
        }
        if (stopLot.getStatus().equals(Status.STOPPED)) {
            return ResponseEntity.ok().build();
        }
        if (stopLot.getStatus().equals(Status.CREATED)) {
            return ResponseEntity.badRequest().build();
        }
        if (stopLot.getStatus().equals(Status.STARTED)) {
            lotService.stopLot(id);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<LotDTO> createLot(@RequestBody CreateLotDTO createLotDTO) {
        if (!lotService.checkMethod(createLotDTO)) {
            return ResponseEntity.badRequest().build();
        }
        LotDTO createLot = lotService.createLot(createLotDTO);
        return ResponseEntity.ok(createLot);
    }

    @GetMapping
    public ResponseEntity<Collection<LotDTO>> findAllByStatus(
            @RequestParam Status status,
            @RequestParam(required = false) Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        return ResponseEntity.ok(lotService.findAllByStatus(status, pageNumber));
    }

    @GetMapping("/export")
    public void exportAllLotsToCSV(HttpServletResponse response) throws IOException {
        Collection<FullLotDTO> lots = lotService.exportAllLots();
        StringWriter out = new StringWriter();
        CSVPrinter print = new CSVPrinter(out, CSVFormat.DEFAULT);
        lots.stream().forEach(fullLotDTO ->
        {
            try { print.printRecord(fullLotDTO.getId(),
                    fullLotDTO.getTitle(),
                    fullLotDTO.getStatus(),
                    fullLotDTO.getLastBid(),
                    fullLotDTO.getCurrentPrice());
            } catch (IOException e) {
                throw new RuntimeException(e);}
        });
        print.flush();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"lot.csv\"");
        PrintWriter writer = response.getWriter();
        writer.write(out.toString());
        writer.flush();
        writer.close();
    }
}
//    response.setContentType("text/csv");
//    String fileName = "lot.csv";
//    String headerKey = "Content-Disposition";
//    String headerValue = "attachment; filename= " + fileName;
//    response.setHeader(headerKey, headerValue);
//
//    Collection<FullLotDTO> listLots = lotService.exportAllLots();
//    StringWriter out = new StringWriter();
//    CSVPrinter print = new CSVPrinter(out, CSVFormat.DEFAULT);
//    String[] nameMapping = {"id, title, status, lastBid, currentPrice"};






