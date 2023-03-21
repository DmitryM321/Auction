package com.example.auction.repository;

import com.example.auction.model.Bid;
import com.example.auction.projection.BidderNameBidDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query(value = "SELECT bidder_name AS bidderName, bid_date AS bidDate " +
            "FROM bid AS s WHERE s.lot_id = ?1 ORDER BY bid_date LIMIT 1", nativeQuery = true)
    BidderNameBidDate findInfoFirstBidder(Long id);

    @Query(value = "SELECT bidder_name AS bidderName, MAX(s.bids) AS max_bids, MAX(last_bid_date) AS bidDate " +
            "FROM (SELECT bidder_name, COUNT(*) AS bids, MAX(bid_date) AS last_bid_date " +
            "FROM bid WHERE lot_id = ?1 GROUP BY bidder_name) AS s " +
            "GROUP BY bidder_name ORDER BY max_bids DESC LIMIT 1", nativeQuery = true)
    BidderNameBidDate findInfoMaxBetsBidder(Long id);

    @Query(value = "SELECT bidder_name AS bidderName, bid_date AS bidDate FROM bid WHERE " +
            "lot_id = ?1 ORDER BY bid_date DESC LIMIT 1", nativeQuery = true)
    BidderNameBidDate getInfoLastBid(Long id);

    @Query(value = "SELECT COUNT(*) FROM bid WHERE lot_id = ?1", nativeQuery = true)
    Long bidCount(Long id);
}
