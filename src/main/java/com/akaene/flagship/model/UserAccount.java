package com.akaene.flagship.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

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
     * Erases the password.
     * <p>
     * Handy for example before sending the instance outside the application.
     */
    public void erasePassword() {
        this.password = null;
    }
}
