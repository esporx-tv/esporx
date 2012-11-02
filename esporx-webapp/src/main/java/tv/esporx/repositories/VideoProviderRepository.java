package tv.esporx.repositories;

import org.springframework.data.repository.CrudRepository;
import tv.esporx.domain.VideoProvider;

public interface VideoProviderRepository extends CrudRepository<VideoProvider, Long>, VideoProviderRepositoryCustom {}
