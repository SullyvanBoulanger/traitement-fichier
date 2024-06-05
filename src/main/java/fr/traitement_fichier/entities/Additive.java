package fr.traitement_fichier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "ADDITIF")
public class Additive extends BaseEntity{

    public Additive() {
        super();
    }

    public Additive(String label) {
        super(label);
    }
}
