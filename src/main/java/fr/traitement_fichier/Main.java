package fr.traitement_fichier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import fr.traitement_fichier.entities.Product;
import fr.traitement_fichier.services.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        Path path = Paths.get("./open-food-facts.csv");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("yuka_produit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ProductService productService = new ProductService();

        try (Stream<String> lines = Files.lines(path)) {
            List<Product> products = new ArrayList<>();
            lines.skip(1).forEach(line -> {
                products.add(productService.createProduct(entityManager, line));
                if (products.size() >= ProductService.BATCH_SIZE) {
                    productService.processProductsInBatches(entityManager, products);
                    products.clear();
                }
            });

            if (!products.isEmpty()) {
                productService.processProductsInBatches(entityManager, products);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        entityManager.close();
    }
}