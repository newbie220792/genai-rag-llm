-- Create a table to store documents with vector embeddings for RAG (Retrieval-Augmented Generation) system
CREATE TABLE documents
(
    id       serial primary key,
    title    varchar(255),
    content  text,
    embedding vector(768),
    metadata text
);

-- Create an HNSW index on the embedding column for efficient vector similarity search using cosine distance
CREATE INDEX documents_embedding_idx
    ON documents
    USING hnsw (embedding vector_cosine_ops)
    WITH (
    m = 16,
    ef_construction = 200
    );

-- Set the HNSW search parameter to control the accuracy/speed trade-off during vector searches
SET hnsw.ef_search = 100;

-- Drop the documents table if it exists
drop table documents;

-- Add a tsvector column to store full-text search vectors for the content field
ALTER TABLE documents
    ADD COLUMN tsv;

-- Create a trigger function that automatically updates the tsv column with a tsvector representation of the content
    CREATE FUNCTION documents_tsv_trigger() RETURNS trigger AS $$
    BEGIN NEW.tsv :=
          to_tsvector('english', COALESCE(NEW.content,''));
    RETURN NEW;
    END
    $$ LANGUAGE plpgsql;

-- Create a trigger that fires before insert or update operations to automatically maintain the tsv column
CREATE TRIGGER tsv_update
    BEFORE INSERT OR UPDATE
    ON documents
    FOR EACH ROW
EXECUTE FUNCTION documents_tsv_trigger();