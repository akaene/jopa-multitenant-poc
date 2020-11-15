package com.akaene.flagship.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.Set;

public class UserAccountDto {

    URI uri;

    String firstName;

    String lastName;

    String username;

    // This means that password won't be sent to the client, but it is still possible to update it
    // Because it will be deserialized when received in JSON from the client
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    Set<String> types;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccountDto{" + firstName + " " + lastName + "(" + username + ")<" + uri + ">}";
    }
}
