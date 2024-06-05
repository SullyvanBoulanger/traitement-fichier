package fr.traitement_fichier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "CATEGORIE")
public class Category extends BaseEntity{

    public Category() {
        super();
    }
    
    public Category(String label) {
        super(label);
    }
}
