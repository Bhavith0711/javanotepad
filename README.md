# Boss Notepad Elite 🚀

A professional-grade word processor built with **Java Swing**, managed by **Maven**, and backed by a **PostgreSQL** database.

## ✨ Features
- **Database Persistence:** Save and load notes directly to/from PostgreSQL.
- **Smart Upsert:** Automatically updates existing notes or creates new ones based on the title.
- **Dark Mode:** Eye-friendly dark interface toggle.
- **File Export:** Export your database notes to local `.txt` files.
- **Real-time Analytics:** Live word count and status tracking.
- **Search:** Case-insensitive keyword searching across all saved notes.

## 🛠️ Tech Stack
- **Language:** Java 17+
- **GUI Framework:** Swing
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **Driver:** JDBC (postgresql-42.7.2)

## 🚀 Setup & Installation
1. **Database:** Create a database named `notepad_db` and run:
   ```sql
   CREATE TABLE notes (
       id SERIAL PRIMARY KEY,
       title VARCHAR(255) UNIQUE NOT NULL,
       body TEXT,
       last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );

2.Configuration: Update DatabaseConfig.java with your PostgreSQL username and password.

3.Build: Run mvn clean install to download dependencies.

4.Run: Execute the NotepadApp class.

My first real project after i started learning java full stack made with ❤️ Bhavith
