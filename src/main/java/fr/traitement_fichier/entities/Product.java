package fr.traitement_fichier.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUIT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "NOM")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_CATEGORIE")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_MARQUE")
    private Brand brand;

    @Column(name = "SCORE_NUTRITIONNEL")
    private String nutritionalScore;

    @Column(name = "ENERGIE")
    private int energy;

    @Column(name = "SEL")
    private int salt;
    
    @Column(name = "SUCRE")
    private int carbs;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "PRODUITS_INGREDIENTS",
        joinColumns = @JoinColumn(name = "ID_PRODUIT", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ID_INGREDIENT", referencedColumnName = "ID")
    )
    private List<Ingredient> ingredients;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "PRODUITS_ADDITIFS",
        joinColumns = @JoinColumn(name = "ID_PRODUIT", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ID_ADDITIF", referencedColumnName = "ID")
    )
    private List<Additive> additives;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "PRODUITS_ALLERGENES",
        joinColumns = @JoinColumn(name = "ID_PRODUIT", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ID_ALLERGENE", referencedColumnName = "ID")
    )
    private List<Allergen> allergens;

    public Product() {}

    public Product(String name, Category category, Brand brand, String nutrionalScore, int energy, int salt, int carbs, List<Ingredient> ingredients, List<Additive> additives, List<Allergen> allergens) {
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.nutritionalScore = nutrionalScore;
        this.energy = energy;
        this.salt = salt;
        this.carbs = carbs;
        this.ingredients = ingredients;
        this.additives = additives;
        this.allergens = allergens;
    }
}
