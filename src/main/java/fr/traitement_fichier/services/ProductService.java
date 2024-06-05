package fr.traitement_fichier.services;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.traitement_fichier.entities.Additive;
import fr.traitement_fichier.entities.Allergen;
import fr.traitement_fichier.entities.BaseEntity;
import fr.traitement_fichier.entities.Brand;
import fr.traitement_fichier.entities.Category;
import fr.traitement_fichier.entities.Ingredient;
import fr.traitement_fichier.entities.Product;
import fr.traitement_fichier.utils.ParseUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ProductService {
    private static int ID_CATEGORY = 0;
    private static int ID_BRAND = 1;
    private static int ID_NAME = 2;
    private static int ID_NUTRIONAL_SCORE = 3;
    private static int ID_INGREDIENTS = 4;
    private static int ID_ENERGY = 5;
    private static int ID_CARBS = 7;
    private static int ID_SALT = 10;
    private static int ID_ALLERGENS = 28;
    private static int ID_ADDITIVES = 29;
    public static int BATCH_SIZE = 1300;

    private Map<String, BaseEntity> inMemoryEntities = new HashMap<>();

    public void processProductsInBatches(EntityManager entityManager, List<Product> products) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
    
        for (Product product : products) {
            entityManager.persist(product);
        }
    
        transaction.commit();
    }

    public Product createProductInDatabase(EntityManager entityManager, String line) {
        String[] splittedLine = line.split("\\|", -1);

        Category category = createEntityInDatabase(entityManager, Category.class, splittedLine[ID_CATEGORY]);
        Brand brand = createEntityInDatabase(entityManager, Brand.class, splittedLine[ID_BRAND]);
        String name = splittedLine[ID_NAME];
        String nutrionalScore = splittedLine[ID_NUTRIONAL_SCORE];
        int energy = ParseUtils.parseInt(splittedLine[ID_ENERGY]);
        int carbs = ParseUtils.parseInt(splittedLine[ID_CARBS]);
        int salt = ParseUtils.parseInt(splittedLine[ID_SALT]);

        List<Ingredient> ingredients = createMultipleEntitiesInDatabase(
                entityManager,
                Ingredient.class,
                splittedLine[ID_INGREDIENTS],
                ",|;|-");
        List<Allergen> allergens = createMultipleEntitiesInDatabase(
                entityManager,
                Allergen.class,
                splittedLine[ID_ALLERGENS],
                ",");
        List<Additive> additives = createMultipleEntitiesInDatabase(
                entityManager,
                Additive.class,
                splittedLine[ID_ADDITIVES],
                ",");

        Product product = new Product(
                name,
                category,
                brand,
                nutrionalScore,
                energy,
                salt,
                carbs,
                ingredients,
                additives,
                allergens);

        return product;
    }

    private <T extends BaseEntity> T createEntityInDatabase(EntityManager entityManager, Class<T> entityType,
            String label) {
        T existingEntity = (T) inMemoryEntities.get(generateCompositeKey(entityType, label));

        if (label.equals("")) {
            return null;
        }

        if (existingEntity == null) {
            T entity = null;
            try {
                entity = entityType.getDeclaredConstructor(String.class).newInstance(label);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            inMemoryEntities.put(generateCompositeKey(entityType, label), entity);

            return entity;
        }

        return existingEntity;
    }

    private <T extends BaseEntity> List<T> createMultipleEntitiesInDatabase(EntityManager entityManager,
            Class<T> entityType, String lineToSplit, String regexSeparator) {

        String[] splittedLine = lineToSplit.split(regexSeparator);

        List<T> entities = new ArrayList<>();

        if (splittedLine.length == 0)
            return entities;

        for (String label : splittedLine) {
            entities.add(createEntityInDatabase(entityManager, entityType, label));
        }

        return entities;
    }

    private String generateCompositeKey(Class<? extends BaseEntity> entityType, String entityValue) {
        return entityType.getSimpleName() + ":" + entityValue;
    }
    
}
