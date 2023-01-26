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
 * {@link WebUser} uses a sequential UUID as surrogate primary key.
 * <p>
 * An universal identifier is useful in a context of multiple systems so that they can
 * create unique identifier for entities independently.
 * <p>
 * <b>ALERT:</b> don't use 'User' as the entity name because it is a reserved SQL word!
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
public class WebUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private static TimeBasedGenerator UUID_GENERATOR =
            Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    /**
     * Using an UUID as primary key means that this entity can be uniquely addressed by whatever
     * system, on the other side, being UUID 128 bits long (double the size of a default long) the
     * db will be slightly bigger and slightly slower (how much will depend on the actual db).
     * <p>
     * Note that:
     * <ul>
     * <li>the actual SQL type depends on the database (i.e. PostgreSQL has a primitive UUID type)
     * <li>the UUID created should be a database friendly version 1, 5, 6, 7 <b>sequential UUID</b>
     * (avoid random version 4 or any non sequential type because they cannot be indexed efficiently
     * by B-Trees that are the main way of indexing data in the database world).
     * </ul>
     *
     * <p>
     * <h4>Note about Hibernate UUID auto-generation</h4>
     * Hibernate will use the {@link org.hibernate.id.uuid.CustomVersionOneStrategy} class to
     * generate a UUID based on IETF RFC 4122 version 1 from the main IP address of the host.
     * <p>
     * Using Hibernate generator is <b>not advisable</b> because the UUID will be generated and
     * thus available only after the entity has being persisted voiding the advantages of having it always ready for
     * {@link Object#equals(Object)} comparisons. By initiating it directly in the constructor we
     * can immediately and safely put the entity in a set without problems.
     * <p>
     * This is the annotations for the hibernate creator:
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
