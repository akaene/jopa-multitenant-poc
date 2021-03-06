package com.akaene.flagship.model;

import com.akaene.flagship.model.util.HasDerivableUri;
import cz.cvut.kbss.jopa.model.annotations.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Namespace(prefix = "foaf", namespace = "http://xmlns.com/foaf/0.1/")
@MappedSuperclass
public class AbstractPerson implements HasDerivableUri, Serializable {

    @Id
    URI uri;

    @NotBlank
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = "foaf:firstName")
    String firstName;

    @NotBlank
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = "foaf:lastName")
    String lastName;

    @NotBlank
    @Email
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = "foaf:accountName", simpleLiteral = true)
    String username;

    @Types
    Set<String> types;

    public AbstractPerson() {
        this.types = new HashSet<>(4);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    /**
     * Adds the specified type to this instance's ontological types.
     *
     * @param type The type to add
     */
    public void addType(String type) {
        Objects.requireNonNull(type);
        if (types == null) {
            this.types = new HashSet<>();
        }
        types.add(type);
    }

    public boolean hasType(String type) {
        return types != null && types.contains(type);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " <" + uri + ">";
    }

    /**
     * Generates URI using the person's first and last name.
     * <p>
     * If the URI is already set, nothing happens.
     */
    @PrePersist
    @Override
    public void generateUri() {
        if (uri != null) {
            return;
        }
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalStateException("Cannot generate Person URI without first name.");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalStateException("Cannot generate Person URI without last name.");
        }
        this.uri = URI.create(getClass().getAnnotation(OWLClass.class).iri() + "/" + firstName + "+" + lastName);
    }

    /**
     * Returns true if the first name and last name of this instance are equal to those of the other instance.
     * <p>
     * Instance uri and username are not compared, because they are assumed to be read-only and password has to be
     * compared using a password encoder.
     *
     * @param other The other instance to compare to this one
     * @return true if the selected attributes are equal, false otherwise
     */
    public boolean nameEquals(AbstractPerson other) {
        return other != null && firstName.equals(other.firstName) && lastName.equals(other.lastName);
    }
}
