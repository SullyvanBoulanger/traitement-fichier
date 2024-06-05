package fr.traitement_fichier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ALLERGENE")
public class Allergen extends BaseEntity {

    public Allergen() {
        super();
    }

    public Allergen(String label) {
        super(label);
    }
}
