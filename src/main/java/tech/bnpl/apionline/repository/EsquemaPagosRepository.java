package tech.bnpl.apionline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.bnpl.apionline.model.EsquemaPago;

import java.util.List;

@Repository
public interface EsquemaPagosRepository extends JpaRepository<EsquemaPago, Long> {
    List<EsquemaPago> findByHabilitado(boolean habilitado);
}
