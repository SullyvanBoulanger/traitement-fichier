package fr.traitement_fichier;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.traitement_fichier.entities.Additive;
import fr.traitement_fichier.entities.Allergen;
import fr.traitement_fichier.entities.BaseEntity;
import fr.traitement_fichier.entities.Brand;
import fr.traitement_fichier.entities.Category;
import fr.traitement_fichier.entities.Ingredient;
import fr.traitement_fichier.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class Main {

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

    public static void main(String[] args) {
        Path path = Paths.get("./open-food-facts.csv");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("banque");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            List<String> lines = Files.readAllLines(path);
            lines.remove(0);

            for (String line : lines) {
                createProductInDatabase(entityManager, line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        entityManager.close();

    }

    private static Product createProductInDatabase(EntityManager entityManager, String line) {
        String[] splittedLine = line.split("\\|", -1);
        System.out.println(Arrays.toString(splittedLine));
        System.out.println("Length : " + splittedLine.length);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        Category category = createEntityInDatabase(entityManager, Category.class, splittedLine[ID_CATEGORY]);
        Brand brand = createEntityInDatabase(entityManager, Brand.class, splittedLine[ID_BRAND]);
        String name = splittedLine[ID_NAME];
        String nutrionalScore = splittedLine[ID_NUTRIONAL_SCORE];
        int energy = parseInt(splittedLine[ID_ENERGY]);
        int carbs = parseInt(splittedLine[ID_CARBS]);
        int salt = parseInt(splittedLine[ID_SALT]);
        List<Ingredient> ingredients = createMultipleEntitiesInDatabase(
                entityManager,
                Ingredient.class,
                splittedLine[ID_INGREDIENTS]);
        List<Allergen> allergens = createMultipleEntitiesInDatabase(
                entityManager,
                Allergen.class,
                splittedLine[ID_ALLERGENS]);
        List<Additive> additives = createMultipleEntitiesInDatabase(
                entityManager,
                Additive.class,
                splittedLine[ID_ADDITIVES]);

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

        entityManager.persist(product);

        entityTransaction.commit();

        return product;
    }

    public static <T extends BaseEntity> T createEntityInDatabase(EntityManager entityManager, Class<T> entityType,
            String label) {
        TypedQuery<T> query = entityManager
                .createQuery("SELECT e FROM " + entityType.getSimpleName() + " e WHERE e.label = :label", entityType);
        query.setParameter("label", label);

        List<T> entities = query.getResultList();
        if (entities.isEmpty() && !label.equals("")) {
            T entity = null;
            try {
                entity = entityType.getDeclaredConstructor(String.class).newInstance(label);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            entityManager.persist(entity);
            return entity;
        }

        return entities.get(0);
    }

    public static <T extends BaseEntity> List<T> createMultipleEntitiesInDatabase(EntityManager entityManager,
            Class<T> entityType, String lineToSplit) {
        
        String[] splittedLine = lineToSplit.replace(';', ',').split(",");

        List<T> entities = new ArrayList<>();
        for (String label : splittedLine) {
            entities.add(createEntityInDatabase(entityManager, entityType, label));
        }

        return entities;
    }

    private static int parseInt(String label) {
        try {
            return Integer.parseInt(label);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}