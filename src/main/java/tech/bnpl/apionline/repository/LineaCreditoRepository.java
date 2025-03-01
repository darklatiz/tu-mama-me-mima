package tech.bnpl.apionline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import tech.bnpl.apionline.model.LineaCredito;

@Service
public interface LineaCreditoRepository extends JpaRepository<LineaCredito, Long> {
}
