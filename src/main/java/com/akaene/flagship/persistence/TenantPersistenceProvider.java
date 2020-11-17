package com.akaene.flagship.persistence;

import com.akaene.flagship.util.Constants;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.*;

@Configuration
public class TenantPersistenceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TenantPersistenceProvider.class);

    private static final Map<String, String> DEFAULT_PARAMS = initParams();

    private final Environment environment;

    private final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

    public TenantPersistenceProvider(Environment environment) {
        this.environment = environment;
    }

    private static Map<String, String> initParams() {
        final Map<String, String> map = new HashMap<>();
        map.put(LANG, Constants.PU_LANGUAGE);
        map.put(SCAN_PACKAGE, "com.akaene.flagship");
        map.put(JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return map;
    }

    public void registerEntityManagerFactory(EntityManagerFactory emf) {
        Objects.requireNonNull(emf);
        final String repoUrl = emf.getProperties().get(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY);
        assert repoUrl != null;
        LOG.debug("Registering EntityManagerFactory for repository <{}>.", repoUrl);
        emfs.put(repoUrl, emf);
    }

    public EntityManagerFactory connectTo(String repoUrl) {
        Objects.requireNonNull(repoUrl);
        LOG.info("Connecting to repository at <{}>.", repoUrl);
        final Map<String, String> properties = new HashMap<>(DEFAULT_PARAMS);
        properties.put(ONTOLOGY_PHYSICAL_URI_KEY, repoUrl);
        properties.put(DATA_SOURCE_CLASS, environment.getRequiredProperty("driver"));
        // TODO: It would be nice to be able to share the metamodel, so that it does not have to be built (and stored) over and over again
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory(repoUrl + "_PU", properties);
        registerEntityManagerFactory(emf);
        checkConnection(emf);
        return emf;
    }

    private void checkConnection(EntityManagerFactory emf) {
        LOG.debug("Checking connection to repository...");
        final EntityManager em = emf.createEntityManager();
        try {
            final int size = em.createNativeQuery("SELECT (count(*) as ?cnt) WHERE {  ?x ?y ?z. }", Integer.class)
                               .getSingleResult();
            LOG.debug("Connection successfully established. Repository contains {} statements.", size);
        } finally {
            em.close();
        }
    }

    public List<String> getConnectedRepositories() {
        return emfs.values().stream().map(emf -> emf.getProperties().get(ONTOLOGY_PHYSICAL_URI_KEY)).sorted()
                   .collect(Collectors.toList());
    }

    @PreDestroy
    private void shutDown() {
        emfs.values().stream().filter(EntityManagerFactory::isOpen).forEach(EntityManagerFactory::close);
    }
}
