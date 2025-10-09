package com.lazaar.ecommerce.Repositories;

import com.lazaar.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Cette méthode est une méthode de requête dérivée.
    // Spring Data JPA génère automatiquement la requête SQL en fonction du nom de la méthode.
    // Elle permet de récupérer une page de produits (via l’interface Pageable)
    // dont l’ID du statut (productStatus.id) correspond à l’ID donné en paramètre.
    //
    // Explication :
    // Paramètre : Long id correspond à l’ID du statut du produit (productStatus).
    // Pageable pageable : Le paramètre Pageable permet de définir
    // la pagination et l’ordonnancement des résultats.
    // Retour : Un objet Page<Product>, qui est une page de résultats paginée.
    Page<Product> findByProductStatusId(Long id, Pageable pageable);

    // L’annotation @Query pour définir une requête JPQL personnalisée.
    // La requête sélectionne les produits dont l’ID du statut est égal à celui fourni en paramètre,
    // les trie de manière aléatoire (ORDER BY RAND()), et limite les résultats à 4 (LIMIT 4).
    @Query("SELECT p FROM Product p WHERE p.productStatus.id = ?1 ORDER BY RAND() LIMIT 4")
    List<Product> findByProductStatusIdLimit(Long id);

    Page<Product> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 ORDER BY RAND() LIMIT 4")
    List<Product> findByCategoryIdLimit(Long id);
}
