CREATE DATABASE mini_amazon_db;
CREATE TABLE users (
    id_user INT PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(200),
    telephone VARCHAR(20),
	role VARCHAR(20) NOT NULL
);

CREATE TABLE categories (
    id_category INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL
);

CREATE TABLE products (
    id_product INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(150) NOT NULL,
    description TEXT,
	fabrication_year INT,
	manufacturer VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    id_category INT,
	image VARCHAR(500),
    FOREIGN KEY (id_category) REFERENCES categories(id_category)
);

CREATE TABLE cart (
    id_cart INT PRIMARY KEY AUTO_INCREMENT,
    id_user INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE cart_detail (
    id_cart_detail INT PRIMARY KEY AUTO_INCREMENT,
    id_cart INT NOT NULL,
    id_product INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (id_cart) REFERENCES cart(id_cart),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

CREATE TABLE orders (
    id_order INT PRIMARY KEY AUTO_INCREMENT,
	order_username VARCHAR(50) NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (id_user) REFERENCES users(id_user)
);

CREATE TABLE order_detail (
    id_order_detail INT PRIMARY KEY AUTO_INCREMENT,
    id_order INT NOT NULL,
    id_product INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_order) REFERENCES orders(id_order),
    FOREIGN KEY (id_product) REFERENCES products(id_product)
);

CREATE TABLE payments (
    id_payment INT PRIMARY KEY AUTO_INCREMENT,
    id_order INT NOT NULL,
    payment_method VARCHAR(50),
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(50) DEFAULT 'Completed',
    FOREIGN KEY (id_order) REFERENCES orders(id_order)
);

INSERT INTO categories (category_name) VALUES
('Electronics'),
('Clothing and Accessories'),
('Home and Kitchen'),
('Beauty and Personal Care'),
('Sports and Fitness'),
('Toys and Games'),
('Books and Education'),
('Automotive');

INSERT INTO products (product_name, description, price, stock, id_category, image) VALUES
('HP Laptop', '15-inch laptop', 799.99, 10, 1, '/img/hp_laptop.jpg'),
('iPhone 13', 'Apple smartphone', 999.99, 8, 1, '/img/iphone13.jpg'),
('Black Shirt', 'Cotton size M', 19.99, 50, 2, '/img/black_shirt.jpg'),
('Sport Shoes', 'Running shoes', 59.99, 30, 2, '/img/sport_shoes.jpg'),
('Modern Sofa', '3-seat sofa', 299.99, 5, 3, '/img/modern_sofa.jpg'),
('LED Lamp', 'Desk lamp', 29.99, 25, 3, '/img/led_lamp.jpg'),
('Dior Perfume', 'Elegant fragrance', 89.99, 15, 4, '/img/dior_perfume.jpg'),
('Makeup Kit', 'Complete set', 49.99, 20, 4, '/img/makeup_kit.jpg'),
('20lb Dumbbells', 'Weight set', 39.99, 18, 5, '/img/dumbbells.jpg'),
('Basketball Ball', 'Official size', 24.99, 40, 5, '/img/basketball.jpg'),
('LEGO Set', 'Creative toy set', 34.99, 22, 6, '/img/lego_set.jpg'),
('PS5 Video Game', 'Action game', 69.99, 12, 6, '/img/ps5_game.jpg'),
('Java Book', 'Learn Java', 39.99, 10, 7, '/img/java_book.jpg'),
('SQL Course', 'Database basics', 19.99, 50, 7, '/img/sql_course.jpg'),
('Motor Oil', '5W-30', 29.99, 35, 8, '/img/motor_oil.jpg'),
('Car Charger', 'Fast USB charger', 14.99, 60, 8, '/img/car_charger.jpg'),
('Jesus Portrait', 'Religious artwork', 49.99, 20, 4, '/img/jessenlacruz_n.jpg'

CREATE TABLE roles (
    roleId VARCHAR(50) NOT NULL,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    role_description VARCHAR(150),
	PRIMARY KEY (RoleId)
);

INSERT INTO roles (roleID, role_name, role_description) VALUES
('rol1', 'Administrator','administrador'),
('rol2', 'Cliente', 'cliente');

CREATE TABLE roleuser (
    UserName VARCHAR(20) NOT NULL,
    RoleId VARCHAR(20) NOT NULL,
    dateAssign DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (UserName, RoleId)
);

 INSERT INTO roleuser (UserName, RoleId)
    VALUES ('user1', 'rol1'),('user2','rol2'),('user3','role2');

CREATE TABLE rolewebpagegood (
    RoleId VARCHAR(10) NOT NULL,
    pageURL VARCHAR(100) NOT NULL,
    dateAssign DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (RoleId, pageURL)
);

 INSERT INTO rolewebpagegood (RoleId, pageURL) VALUES
    ('rol1', 'addProduct.jsp'),
    ('rol1', 'editProduct.jsp'),
    ('rol1', 'removeProduct.jsp'),
    ('rol1', 'listProducts.jsp'),
    ('rol1', 'orders.jsp'),
    ('rol1', 'welcomeMenu.jsp'),
    ('rol2', 'welcomeMenu.jsp'),
    ('rol2', 'index.html');

INSERT INTO users (username, name, email, password, address, telephone)
 VALUES ('user1', 'user1', 'user1@gmail.com', 'pas1', 'New York, USA', '747-888-999');


CREATE TABLE webpagegood (
    pageURL VARCHAR(100) NOT NULL PRIMARY KEY,
    pageTitle VARCHAR(100) NOT NULL,
    Description VARCHAR(255),
    menuID INT NOT NULL
     );

INSERT INTO webpagegood (pageURL, pageTitle, Description, menuID) VALUES ('addProduct.jsp', 'add products','In this page adds product in stock.','1');

 CREATE TABLE webpageprevious (
       currentpageURL VARCHAR(100) NOT NULL,
         previouspageURL VARCHAR(100) NOT NULL
    );

INSERT INTO webpageprevious (currentpageURL, previouspageURL) VALUES
     ('addProduct.jsp', 'listProducts.jsp'),
     ('editProduct.jsp', 'addProduct.jsp'),
     ('listProducts.jsp', 'welcomeMenu.jsp'),
     ('orders.jsp', 'listProducts.jsp'),
     ('removeProduct.jsp', 'orders.jsp'),
     ('welcomeMenu.jsp', 'index.html'),
    ('index.html', 'welcomeMenu.jsp');


ALTER TABLE products
ADD fabrication_year INT;
ALTER TABLE cart
ADD CONSTRAINT unique_user_cart UNIQUE (id_user);
