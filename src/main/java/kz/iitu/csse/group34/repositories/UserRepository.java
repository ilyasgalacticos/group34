package kz.iitu.csse.group34.repositories;

import kz.iitu.csse.group34.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

}
