package tv.esporx.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import tv.esporx.domain.Esporxer;

import java.util.Date;

public interface EsporxerRepository extends CrudRepository<Esporxer, Long> {

    @Query("FROM Esporxer e LEFT JOIN FETCH e.authorities WHERE e.email = :email")
    Esporxer findByEmail(@Param("email") String email);

    Esporxer findByEmailAndAccountConfirmationHashAndAccountCreationDateAfter(String email, String hash, Date date);
}
