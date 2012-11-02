package tv.esporx.framework.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID extends Serializable> extends Repository<T, ID> {
    public Iterable<T> findAll(Pageable sort);
    public Iterable<T> findAll();
    public T findOne(ID id);
}