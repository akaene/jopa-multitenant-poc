package com.akaene.flagship.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@OWLClass(iri = Vocabulary.s_c_person)
public class UserAccount extends AbstractPerson {

    // This means that password won't be sent to the client, but it is still possible to update it
    // Because it will be deserialized when received in JSON from the client
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OWLDataProperty(iri = Vocabulary.s_p_password)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Encodes password of this person.
     *
     * @param encoder Encoder to user to encode the password
     */
    public void encodePassword(PasswordEncoder encoder) {
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Cannot encode an empty password.");
        }
        this.password = encoder.encode(password);
    }

    /**
     * Erases the password.
     * <p>
     * Handy for example before sending the instance outside the application.
     */
    public void erasePassword() {
        this.password = null;
    }

    /**
     * Checks whether the account represented by this instance is locked.
     *
     * @return Locked status
     */
    public boolean isLocked() {
        return hasType(Vocabulary.s_c_locked);
    }

    /**
     * Locks the account represented by this instance.
     */
    public void lock() {
        addType(Vocabulary.s_c_locked);
    }

    /**
     * Unlocks the account represented by this instance.
     */
    public void unlock() {
        if (types != null) {
            types.remove(Vocabulary.s_c_locked);
        }
    }

    /**
     * Enables the account represented by this instance.
     * <p>
     * Does nothing if the account is already enabled.
     */
    public void enable() {
        if (types != null) {
            types.remove(Vocabulary.s_c_disabled);
        }
    }

    /**
     * Checks whether the account represented by this instance is enabled.
     */
    public boolean isEnabled() {
        return !hasType(Vocabulary.s_c_disabled);
    }

    /**
     * Disables the account represented by this instance.
     * <p>
     * Disabled account cannot be logged into and cannot be used to view/modify data.
     */
    public void disable() {
        addType(Vocabulary.s_c_disabled);
    }

    /**
     * Returns true if this account represents an administrator.
     *
     * @return {@code true} if this user is admin
     */
    public boolean isAdmin() {
        return hasType(Vocabulary.s_c_admin);
    }

    /**
     * Makes this account an admin account.
     */
    public void makeAdmin() {
        if (isLocked()) {
            throw new IllegalStateException("Locked user account cannot be made an admin.");
        }
        if (!isEnabled()) {
            throw new IllegalStateException("Disabled user account cannot be made an admin.");
        }
        addType(Vocabulary.s_c_admin);
    }

    /**
     * Transforms this security-related {@code UserAccount} instance to a domain-specific {@code User} instance.
     *
     * @return new user instance based on this account
     */
    public Person toPerson() {
        final Person user = new Person();
        copyAttributes(user);
        return user;
    }

    private void copyAttributes(cz.cvut.kbss.reporting.model.AbstractPerson target) {
        target.setUri(uri);
        target.setFirstName(firstName);
        target.setLastName(lastName);
        target.setUsername(username);
        if (types != null) {
            target.setTypes(new HashSet<>(types));
        }
    }

    /**
     * Returns a copy of this user account.
     *
     * @return This instance's copy
     */
    public UserAccount copy() {
        final UserAccount clone = new UserAccount();
        copyAttributes(clone);
        clone.password = password;
        return clone;
    }
}
