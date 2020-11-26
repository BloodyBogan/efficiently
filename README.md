# efficiently

> Efficiently manage your study department visits

## Usage

Fill in your database.properties file based on the database.properties.example file, be sure to include all necessary libraries & run migration.sql in your efficiently database

```
# In NetBeans

# Clean and Build Project
SHIFT + F11

# Run Project
F6
```

## Download Dependencies

[MySQL Connector](https://dev.mysql.com/downloads/connector/j/)  
[BCrypt](https://github.com/patrickfav/bcrypt/releases/tag/v0.9.0)  
[Bytes](https://github.com/patrickfav/bytes-java/releases/tag/v1.4.0)  
[LGoodDatePicker](https://github.com/LGoodDatePicker/LGoodDatePicker/releases/tag/v11.1.0-Standard)  

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

## App Info

### Authors

Michal Kaštan & Ladislav Capalaj

### Version

1.0.0

### License

This project is licensed under the MIT License
