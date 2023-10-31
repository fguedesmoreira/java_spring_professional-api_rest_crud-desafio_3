package br.com.flavioguedes.desafio3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.flavioguedes.desafio3.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}