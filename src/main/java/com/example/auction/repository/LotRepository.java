package com.example.auction.repository;

import com.example.auction.model.Lot;
import com.example.auction.model.Status;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {
    Collection<Lot> findAllByStatus(Status status, PageRequest pageRequest);
}
