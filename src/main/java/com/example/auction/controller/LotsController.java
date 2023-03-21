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
import liquibase.pro.packaged.F;
import liquibase.repackaged.com.opencsv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

    public LotsController(LotService lotService, BidService bidService) {
        this.lotService = lotService;
        this.bidService = bidService;
    }

////1 Получить информацию о первом ставившем на лот
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

////2 Возвращает имя ставившего на данный лот наибольшее количество раз
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

    ////3 Получить полную информацию о лотеx
    @GetMapping("/{id}")
    public ResponseEntity<FullLotDTO> findAllInfoLotById(@PathVariable Long id) {
        FullLotDTO fullLotDTO = FullLotDTO.fromLot(lotService.findLotById(id).toLot());
        if (fullLotDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lotService.findAllInfoLotById(id));
    }

    //// 4 Начать торги по лоту
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

    //// 5 Сделать ставку по лоту
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

    //// 6  Остановить торги по лоту OK
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

    ////7 Создает новый лот
    @PostMapping
    public ResponseEntity<LotDTO> createLot(@RequestBody CreateLotDTO createLotDTO) {
        if (!lotService.checkMethod(createLotDTO)) {
            return ResponseEntity.badRequest().build();
        }
        LotDTO createLot = lotService.createLot(createLotDTO);
        return ResponseEntity.ok(createLot);
    }

    ////8 Получить все лоты, основываясь на фильтре статуса и номере страницы
    @GetMapping
    public ResponseEntity<Collection<LotDTO>> findAllByStatus(
            @RequestParam Status status,
            @RequestParam(required = false) Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        return ResponseEntity.ok(lotService.findAllByStatus(status, pageNumber));
    }

//// 9 Экспортировать все лоты в файл CSV
//    @GetMapping("/export")
//    public void exportAllLots(HttpServletResponse response)   {
//        Collection<FullLotDTO> lots = lotService.exportAllLots();
//        StringWriter out = new StringWriter();


    }




