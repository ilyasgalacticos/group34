package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Items;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findAllByPriceGreaterThan(int price);
    Optional<Items> findById(Long id);

}