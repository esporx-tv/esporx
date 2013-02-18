package tv.esporx.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetails;
import tv.esporx.framework.validation.PasswordRepeatConstraint;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Sets.newHashSet;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@PasswordRepeatConstraint
public class Esporxer implements UserDetails {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;
    @Column(name = "password", nullable = false)
    @NotBlank
    @Length(min = 6, max = 100)
    private String password;
    @ManyToMany
    @JoinTable(name = "users_roles", //
        joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
        inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")})
    private Set<Role> authorities = newHashSet();
    @Transient
    private String passwordConfirmation;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "creation_date", nullable = false)
    private Date accountCreationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Set<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("email", email)
            .toString();
    }
}
