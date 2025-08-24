CREATE USER 'user'@'%' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON autenticacion_db.* TO 'user'@'%';
FLUSH PRIVILEGES;