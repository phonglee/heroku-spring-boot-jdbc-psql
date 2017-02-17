package com.tgfashion.repository;

import com.tgfashion.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("from Customer cu where cu.name = ?1")
    Customer findByName(String name);

    @Modifying
    @Query("delete from Customer where id = ?1")
    void deleteCustomerId(Integer id);
}
