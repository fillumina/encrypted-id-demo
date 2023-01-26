package com.fillumina.demo.encryptedid.shop.domain;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This entity uses a sequential UUID as surrogate primary key.
 *
 * An universal identifier is useful in a context of multiple systems so that they can
 * refer to entities in an unique way.
 * Being a sequential UUID its value will be encrypted before
 * being sent to public API to protect against sequence guessing attack.
 * <br>
 * <b>ALERT:</b> don't use 'user' as entity name because it is a reserved SQL word!
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
public class WebUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private static TimeBasedGenerator UUID_GENERATOR =
            Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    /**
     * Note that:
     * <ul>
     * <li>the actual SQL type depends on the database (i.e. PostgreSQL has a primitive UUID type)
     * <li>the UUID created should be a database friendly version 1, 5, 6, 7 <b>sequential UUID</b>
     * (avoid random version 4 or any non sequential type because they works very badly with DB
     * indexes).
     * </ul>
     *
     * <p>
     * Hibernate will use the {@link org.hibernate.id.uuid.CustomVersionOneStrategy} class to
     * generate a UUID based on IETF RFC 4122 version 1 from the main IP address of the host.
     * <br>
     * Using Hibernate generator is not advisable because the UUID will be generated and available
     * only after being persisted voiding the advantages of having it always ready for
     * {@link Object#equals(Object)} comparisons. By initiating it directly in the constructor
     * we can immediately and safely put the entity in a set without problems.
     *
     *<pre>{@code
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                @Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                )
            }
        )
     * }</pre>
     *
     * @see <a href='https://thorben-janssen.com/generate-uuids-primary-keys-hibernate/'>
     * How to generate UUIDs as primary keys with Hibernate</a>
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String login;

    /**
     * Hibernate suggests to prefer {@link List} for bidirectional associations (the ones controlled
     * by a FK in the @ManyToOne side). Also, because the {@link ShoppingCart#hashCode()} value is the
     * same for all {@link Addresse}s using {@link java.util.Set} would be very inefficient.
     */
    @OneToMany(mappedBy = "webUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();

    // called by Hibernate and Jackson libraries
    protected WebUser() {
    }

    public WebUser(String login) {
        // the field initialization would be called even when hibernate just need a new object
        // to copy database data into so it's better to initialize the field here.
        this.id = UUID_GENERATOR.generate();
        this.login = login;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<ShoppingCart> getShoppingCarts() {
        return this.shoppingCarts;
    }

    @Override
    public int hashCode() {
        // having UUID that's all we need here
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof WebUser)) {
            return false;
        }
        // having the id set in the constructor we can safely use it here
        return Objects.equals(this.id, ((WebUser) object).id);
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + '}';
    }

}
