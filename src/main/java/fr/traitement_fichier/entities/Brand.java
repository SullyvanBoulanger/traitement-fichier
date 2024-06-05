package fr.traitement_fichier.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "MARQUE")
public class Brand extends BaseEntity {

    public Brand() {
        super();
    }
    
    public Brand(String label) {
        super(label);
    }
}
