package com.lazaar.ecommerce.Repositories;

import com.lazaar.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // findByEmail retourne un Optional<User> vide si pas trouvé
    Optional<User> findByEmail(String email);

    //add this methode for email verification
    Optional<User> findByVerificationToken(String verificationToken);
}
