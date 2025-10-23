We followed a bank admin Monolith Spring Boot App for our project. 
Features include : 
 - User And Role Management (Lesandu)
 - Customer and Account Management (Krish)
 - Transactions (Brandon)
 - Audit & Reporting (Tomi)
 - Loan & Credit (Joel)



Prerequisites (Tutor Machine)
	Java 21 (Temurin/Adoptium)
	•	macOS: brew install --cask temurin
	•	Ubuntu: sudo apt-get install -y temurin-21-jdk
	•	Windows: Install Temurin 21 MSI
	Maven 3.9+
	•	macOS: brew install maven
	•	Ubuntu: sudo apt-get install -y maven
	•	Windows: Chocolatey choco install maven
	PostgreSQL 14+ (local dev DB)
	•	macOS: Postgres.app or brew install postgresql@14
	•	Ubuntu: sudo apt-get install -y postgresql
	•	Windows: Installer from postgresql.org
 
To Run Project: 
1. Create a Postgresql Database with a user and password that has admin privelleges on the database 
2. Run the Postgresql on your local machine
3. Open the project through intellij
4. go to resources/application.properties - replace the name of database to the one you created, enter the username and password of the database you created.
5. Press run on intellij

The dependencies are all inside the project in pom.xml -> no need to install via your local machines terminal like python

