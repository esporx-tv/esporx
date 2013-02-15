package tv.esporx.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static com.google.common.base.Objects.toStringHelper;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "role", nullable = false, unique = true)
    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String toString() {
        return toStringHelper(this)
            .add("authority", authority)
            .toString();
    }

}
