package tech.bnpl.apionline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bnpl.apionline.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
