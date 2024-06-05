package fr.traitement_fichier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "INGREDIENT")
public class Ingredient extends BaseEntity{

    public Ingredient() {
        super();
    }

    public Ingredient(String label) {
        super(label);
    }
}
