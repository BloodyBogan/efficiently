# efficiently

> Efficiently manage your study department visits

## Usage

Fill in your database.properties file based on the database.properties.example file & be sure to include all necessary libraries

```
# Clean and Build Project
SHIFT + F11

# Run Project
F6
```

## Download Dependencies

[MySQL Connector](https://dev.mysql.com/downloads/connector/j/)

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

# Run MySQL client from bash MySQL container
mysql -uroot -proot

# Create database
CREATE DATABASE efficiently;
```

## App Info

### Author

Michal Kaštan & Ladislav Capalaj

### Version

1.0.0

### License

This project is licensed under the MIT License
