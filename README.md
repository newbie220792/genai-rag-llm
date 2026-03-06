# 🧠 Internal Knowledge AI Assistant (RAG System)

A production-style Retrieval-Augmented Generation (RAG) system built with Java and local LLM.

This project demonstrates how to build an AI assistant that can:

- 📄 Ingest internal PDF documents
- ✂️ Perform semantic chunking
- 🧬 Generate embeddings
- 🔎 Store and retrieve vectors from PostgreSQL (pgvector)
- 💬 Answer questions with source citation
- 🧠 Run fully local using Ollama + Llama3

---

# 🚀 Tech Stack

- Java 17
- Spring Boot
- Ollama (Local LLM runtime)
- Llama 3 (8B quantized)
- PostgreSQL
- pgvector
- Apache PDFBox
- Docker / Docker Compose

---

# 🏗 Architecture

    PDF Upload API
    ↓
    Text Extractor (PDFBox)
    ↓
    Semantic Chunker (800 tokens + 100 overlap)
    ↓
    Embedding Generator (Ollama)
    ↓
    pgvector Storage
    ↓
    User Question
    ↓
    Vector Similarity Search (Top-K)
    ↓
    Context Injection
    ↓
    LLM Response (Llama3)

---

# 🎯 Key Features

## 1️⃣ Semantic Chunking

Instead of naive substring splitting, the system:

- Splits by paragraph
- Merges paragraphs into ~800 token chunks
- Applies 100 token overlap
- Preserves document structure
- Stores metadata (page, section, filename)

This improves retrieval accuracy and context continuity.

---

## 2️⃣ Vector Search

- Embeddings stored in PostgreSQL using pgvector
- Cosine similarity search
- Top-K retrieval
- Supports hybrid search (optional extension)

---

## 3️⃣ Source Citation

Each answer includes metadata such as:

- Page number
- Section title
- Source file name

This improves explainability and trustworthiness.

---

# 📂 Project Structure

    genai-rag-java/
    ├── document-service/
    ├── embedding-service/
    ├── vector-store/
    ├── chat-service/
    ├── docker-compose.yml
    └── README.md

---

# 🛠 Setup Instructions

## Run docker with ollama and llama3 image

```bash
docker run -d \
  --name ollama \
  -p 8181:11434 \
  -v ollama:/root/wso2/.ollama \
  ollama/ollama
```

## Run docker command on ollama container

```bash
docker exec -it ollama
```

## 1️⃣ Install Ollama

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

## 2️⃣ Pull models

```bash
ollama pull llama3:8b
```

## 3️⃣ Run models

```bash
ollama run llama3:8b
```

## 3️⃣ Unload models from memory

```bash
ollama stop llama3:8b
```

🧪 Future Improvements

    Hybrid Search (BM25 + Vector)
    Re-ranking model
    Multi-document support
    Streaming responses
    Evaluation pipeline (RAGAS)
    UI Dashboard
    Multi-language support

## Install PostgreSQL + pgvector bằng Docker

### 1.Chạy container có sẵn pgvector.

```bash
    docker run -d \
      --name pgvector \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=123456# \
      -e POSTGRES_DB=ragdb \
      -p 5432:5432 \
      ankane/pgvector
```