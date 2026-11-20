# AssetGuard: A Centralized Incident Management System for Laboratory Infrastructure

## Live Demo
* **Vercel Web Demo:** [https://assetguard-pbcst304-web-demo.vercel.app](https://assetguard-pbcst304-web-demo.vercel.app)

  
**Course:** PBCST304 – Object Oriented Programming (KTU B.Tech CSE, Semester 3)  
**Institution:** Marian Engineering College, Thiruvananthapuram  
**Project Guide:** Ms. Vijaya Lekshmi S V, Assistant Professor, Dept. of CSE  

## Team Members
* **Hrishikesh B** (`MCE25CS084`)
* **Kanishk Nair** (`MCE25CS092`)
* **Athul G S Nair** (`MCE25CS057`)
* **Savanth R S** (`MCE25CS150`)

## Tech Stack & Architecture
* **Language:** Core Java (JDK 17+)
* **Architecture:** Model-View-Controller (MVC) + DAO Pattern
* **GUI:** Java Swing & AWT
* **Database:** SQLite 3 with JDBC (`sqlite-jdbc`)

## Repository Structure
```text
AssetGuard-PBCST304/
├── src/com/assetguard/
│   ├── util/DBConnection.java
│   ├── model/Incident.java
│   ├── dao/IncidentDAO.java
│   └── view/AssetGuardDashboard.java
├── sql/schema.sql
├── web-demo/index.html
└── README.md
```

## How to Compile and Run (Java Desktop Application)
1. Download `sqlite-jdbc` JAR (e.g., `sqlite-jdbc-3.41.2.2.jar`) and place it in the root directory as `sqlite-jdbc.jar`.
2. Compile the Java packages:
   ```bash
   javac -d out -cp "sqlite-jdbc.jar" src/com/assetguard/util/DBConnection.java src/com/assetguard/model/Incident.java src/com/assetguard/dao/IncidentDAO.java src/com/assetguard/view/AssetGuardDashboard.java
   ```
3. Run the application:
   * **Windows:** `java -cp "out;sqlite-jdbc.jar" com.assetguard.view.AssetGuardDashboard`
   * **Linux/macOS:** `java -cp "out:sqlite-jdbc.jar" com.assetguard.view.AssetGuardDashboard`

## Interactive Web Demo (Vercel Deployment)
The `web-demo/` directory contains `index.html`, an interactive browser-based simulation of the AssetGuard Java Swing GUI featuring separate **Student/Faculty Fault Reporter** and **Lab Administrator (`admin123`)** screens backed by LocalStorage persistence.
