package com.bundang.monitor.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PortRepository extends CrudRepository<Port, Long> {
    List<Port> findAll();

    List<Port> findAllByNumber(Integer number);

    List<Port> findAllByState(String state);

    Optional<Port> findById(Long id);

    Optional<Port> findByNumber(Integer number);

    Port save(Port port);
}
