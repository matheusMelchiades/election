package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Election;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ElectionRepository extends CrudRepository<Election, Long> {
    List<Election> findByYear(Integer year);
    Optional<Election> findFirstByYear(Integer year);
}
