package com.document.module.respository;

import com.document.module.entity.ChunkText;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChunkTextRepository extends BaseRepository<ChunkText, Long> {

    @Query(value = """
              SELECT *
              FROM documents
              WHERE tsv @@ plainto_tsquery(:query)
              ORDER BY ts_rank(tsv, plainto_tsquery(:query)) DESC
              LIMIT :limit
            """, nativeQuery = true)
    public List<ChunkText> getTopDocument(String query, int limit);
}
