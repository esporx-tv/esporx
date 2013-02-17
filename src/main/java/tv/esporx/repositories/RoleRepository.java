package tv.esporx.repositories;

import tv.esporx.domain.Role;
import tv.esporx.framework.repositories.ReadOnlyRepository;

public interface RoleRepository extends ReadOnlyRepository<Role, Long> {
    Role findByAuthority(String authority);
}
