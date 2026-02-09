package com.ecom.Shopping_cart.repository;
import com.ecom.Shopping_cart.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
    public UserDtls findByEmail(String email);

    public List<UserDtls> findByRole(String role);

    public UserDtls findByResetToken(String token);
}
