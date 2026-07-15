package com.danielassisdeveloper.atividade3.repositories;

import com.danielassisdeveloper.atividade3.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
