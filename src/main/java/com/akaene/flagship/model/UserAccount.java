package com.akaene.flagship.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import javax.validation.constraints.NotBlank;

@OWLClass(iri = "http://onto.fel.cvut.cz/ontologies/ufo/person")
public class UserAccount extends AbstractPerson {

    @NotBlank
    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = "http://onto.fel.cvut.cz/ontologies/reporting-tool/password", simpleLiteral = true)
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

    @Override
    public String toString() {
        return "UserAccount{" + firstName + " " + lastName + "(" + username + ")<" + uri + ">}";
    }
}
