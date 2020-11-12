package com.akaene.flagship.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import org.springframework.security.crypto.password.PasswordEncoder;

@OWLClass(iri = "http://onto.fel.cvut.cz/ontologies/ufo/person")
public class UserAccount extends AbstractPerson {

    // This means that password won't be sent to the client, but it is still possible to update it
    // Because it will be deserialized when received in JSON from the client
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OWLDataProperty(iri = "http://onto.fel.cvut.cz/ontologies/reporting-tool/password")
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
}
