package com.akaene.flagship.persistence;

import com.akaene.flagship.util.Constants;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.*;
import static cz.cvut.kbss.jopa.model.PersistenceProperties.JPA_PERSISTENCE_PROVIDER;

@Configuration
public class MainPersistenceFactory {

    private static final Map<String, String> DEFAULT_PARAMS = initParams();

    private final Environment environment;

    private EntityManagerFactory emf;

    public MainPersistenceFactory(Environment environment) {
        this.environment = environment;
    }

    @Bean("mainEmf")
    @Primary
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>(DEFAULT_PARAMS);
        properties.put(ONTOLOGY_PHYSICAL_URI_KEY, environment.getRequiredProperty("repository.url"));
        properties.put(DATA_SOURCE_CLASS, environment.getRequiredProperty("driver"));
        this.emf = Persistence.createEntityManagerFactory("mainPU", properties);
    }

    @PreDestroy
    private void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    private static Map<String, String> initParams() {
        final Map<String, String> map = new HashMap<>();
        map.put(LANG, Constants.PU_LANGUAGE);
        map.put(SCAN_PACKAGE, "com.akaene.flagship");
        map.put(JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return map;
    }

    static Map<String, String> getDefaultParams() {
        return Collections.unmodifiableMap(DEFAULT_PARAMS);
    }
}
