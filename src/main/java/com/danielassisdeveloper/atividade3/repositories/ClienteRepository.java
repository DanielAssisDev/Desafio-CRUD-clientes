package com.danielassisdeveloper.atividade3.repositories;

import com.danielassisdeveloper.atividade3.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Client, Long> {
}
