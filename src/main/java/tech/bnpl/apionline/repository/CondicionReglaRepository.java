package tech.bnpl.apionline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bnpl.apionline.model.CondicionRegla;

@Repository
public interface CondicionReglaRepository extends JpaRepository<CondicionRegla, Long> {
}

