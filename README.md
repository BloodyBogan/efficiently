# efficiently

> Efficiently manage your study department visits

## Quick Look And Feel Preview

Note that the look and feel changes system to system and often after building the project  

[Click here](https://imgur.com/a/DdTYApL)  

## Quick Functionality Overview

```
Validation

Formatting

Error handling

Login

Register

Admin:
    Update users
    Delete users:
        Students:
            Deletes all associated appointments
            Dates which are still valid are made available
        Correspondents:
            Deletes all associated appointments
            Deletes all associated dates
            
Correspondent:
    View appointment info
    Update appointments
    Add dates & times
    Delete dates & times:
        Associated appointment is deleted

User:
    Book appointments (correspondent speific)
    Only see valid dates
    Queue
    View appointments
    Can't have more than 2 active appointments

"Sessions"

Encryption

Hashing

And many more
```

## Usage

Fill in your database.properties file based on the database.properties.example file and the Jasypt Quick Start section below, be sure to include all necessary libraries & run migration.sql in your efficiently database

```
# In NetBeans

# Clean and Build Project
SHIFT + F11

# Run Project
F6


# Using command line

# Compile the project
ant compile

# Compile the project and build JAR
ant jar

# Run the project from the dist folder
java -jar "efficiently.jar"
```

## Download Dependencies

[JDK 8](https://www.oracle.com/java/technologies/javase-downloads.html)  
[MySQL Connector](https://dev.mysql.com/downloads/connector/j/)  
[BCrypt](https://github.com/patrickfav/bcrypt/releases/tag/v0.9.0)  
[Bytes](https://github.com/patrickfav/bytes-java/releases/tag/v1.4.0)  
[LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker/releases/tag/v11.1.0-Standard)  
[Jasypt](http://www.jasypt.org/download.html)  
[Open Sans font](https://fonts.google.com/specimen/Open+Sans)  

## LGoodDatePicker Quick Start

```
# In NetBeans

Tools -> Palette -> Swing/AWT Components

Add from JAR -> Choose LGoodDatePicker-11.1.0.jar -> Next

DateTimePicker -> Next

Swing Controls -> Finish
```

## Docker MySQL Quick Start

```
# Pull the latest MySQL image
docker pull mysql:latest

# Start MySQL instance
docker run --name mysql-dev -e MYSQL_ROOT_PASSWORD=root -d mysql:latest

# Starting the container each subsequent time
docker container start mysql-dev

# Get the container ID
docker ps

# Get your MySQL HOST & MySQL PORT
docker inspect YOUR_CONTAINER_ID

# Connect to the bash into the running MySQL container
docker exec -t -i mysql-dev /bin/bash

# Set correct MySQL time zone (GMT) and restart the Docker container
echo "default-time-zone = '+01:00'" >> /etc/mysql/my.cnf

# Run MySQL client from bash MySQL container
mysql -uroot -proot

# Create database
CREATE DATABASE efficiently;
```

## Jasypt Quick Start

```
# Encrypt the values in your database.properties file
cd jasypt/bin

chmod +x ./encrypt.sh

./encrypt.sh input="YOUR_VALUE" password="YOUR_PASSWORD"

# Encrypted values should follow this syntax
VAR=ENC(YOUR_ENCRYPTED_VALUE)

# Put the password you used in the previous step into Database.java
encryptor.setPassword("YOUR_PASSWORD");
```

## Database Architecture Diagram  
![Efficiently database architecture diagram](https://imgur.com/hNPXd1c.png)  

## App Info

### Authors

Michal Kaštan & Ladislav Capalaj

### Version

1.0.0

### License

This project is licensed under the MIT License
