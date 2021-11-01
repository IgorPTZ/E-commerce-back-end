package com.backend.ecommerce.config;

import com.backend.ecommerce.entity.Country;
import com.backend.ecommerce.entity.Product;
import com.backend.ecommerce.entity.ProductCategory;
import com.backend.ecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig (EntityManager theEntityManager) {

        entityManager = theEntityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        // Disable HTTP methods for Product: PUT, POST and DELETE
        disableHttpMethods(Product.class, config, theUnsupportedActions);

        // Disable HTTP methods for ProductCategory: PUT, POST and DELETE
        disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);

        // Disable HTTP methods for Country: PUT, POST and DELETE
        disableHttpMethods(Country.class, config, theUnsupportedActions);

        // Diable HTTP methods for State: PUT, POST and DELETE
        disableHttpMethods(State.class, config, theUnsupportedActions);

        // Call an internal helper method
        exposeIds(config);
    }

    private void disableHttpMethods(Class theClass,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] theUnsupportedActions) {

        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // Expose entity ids

        // Get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // Create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // Get the entity types for the entities
        for(EntityType tempEntityType : entities) {

            entityClasses.add(tempEntityType.getJavaType());
        }

        // Expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);

        config.exposeIdsFor(domainTypes);
    }
}
