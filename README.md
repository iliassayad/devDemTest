﻿
# DevisDem Backend

API REST pour l'application DevisDem, développée avec **Spring Boot**, **Spring MVC**, **Spring data jpa** et **MySQL**.

## 📦 Technologies

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- MySQL


## 🔧 Installation locale

```bash
# Cloner le dépôt
git clone https://github.com/iliassayad/devDemTest.git


# Ouvrir avec votre IDE (IntelliJ, VSCode, Eclipse...)

# Lancer la bases de données d'un conteneur docker  
docker run -d --name devdemtest-mysql -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=devdemtest -e MYSQL_USER=devdem -e MYSQL_PASSWORD=devdem123 -p 3306:3306 -v devdemtest_data:/var/lib/mysql --restart unless-stopped mysql:8.0 --default-authentication-plugin=mysql_native_password



