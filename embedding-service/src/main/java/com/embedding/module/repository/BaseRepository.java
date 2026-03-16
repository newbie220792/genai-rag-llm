package com.embedding.module.repository;

import org.springframework.data.repository.CrudRepository;

public interface BaseRepository<T, ID> extends CrudRepository<T, ID> {
}
