package com.akaene.flagship.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.vocabulary.DC;

@OWLClass(iri = "http://onto.fel.cvut.cz/ontologies/documentation/report")
public class Report extends AbstractEntity {

    @OWLDataProperty(iri = DC.Terms.TITLE)
    private String title;

    @OWLDataProperty(iri = DC.Terms.DESCRIPTION)
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
