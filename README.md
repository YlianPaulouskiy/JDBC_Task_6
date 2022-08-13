# Задача 06 из "Java методы программирования" И.Н. Блинов, В.С. Романчик

## Заказ

В БД храниться информация о заказах магазина и товарах в них.  
### Для заказа необходимо хранить:
  1. номер заказа;
  2. товары в заказе;
  3. дату поступления;  
### Для товаров в заказе необходимо зранить:
  1. товар;
  2. количество;  
### Для товара необходимо хранить:
  1. название;
  2. описание;
  3. цену;
  
  - Вывести полную информацию о заданном заказе;
  - Вывести номера заказов, сумма которых не превосходит заданную и количество различных товаров равно заданному;
  - Вывести номера заказов, содержащих заданный товар;
  - Вывести номера заказов, не содержащих заданный товар и поступивших в течение текущего дня;
  - Сформировать новый заказ, состоящий из товаров, заказанных в текущий день;
  - Удалить все заказы, в которых присутствует заданное количество заданного товара.  
    
    # Для создания таблиц бд:  
   ### Таблица продуктов
   CREATE TABLE `products` (  
  `id` bigint NOT NULL AUTO_INCREMENT,  
  `name` varchar(255) NOT NULL,  
  `description` varchar(255) NOT NULL,  
  `price` decimal(11,2) NOT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;  
  ### Таблица заказов
  CREATE TABLE `order` (  
  `id` bigint NOT NULL AUTO_INCREMENT,  
  `date` datetime NOT NULL,  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
  ### Связующая таблица (многие ко многим)
  CREATE TABLE `order_products` (  
  `id` bigint NOT NULL AUTO_INCREMENT,  
  `order_id` bigint NOT NULL,  
  `products_id` bigint NOT NULL,  
  PRIMARY KEY (`id`),  
  KEY `order_id` (`order_id`),  
  KEY `products_id` (`products_id`),  
  CONSTRAINT `order_goods_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),  
  CONSTRAINT `order_goods_ibfk_2` FOREIGN KEY (`products_id`) REFERENCES `products` (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
  
# Описание 
## connection
Осуществляет подключение к базе данных
## constants
Хранит константы необходимые для подключения к БД(URL, userName, password)
## exception
Хранит ошибки которые могут возникнуть при работе программы
## model
хранит модели (Order and Product)
## dao
Осуществляет запросы к БД и получение из нее нужных моделей (конверт из базы данных в модель класса)
## service
Улчшает работу dao слоя, т.к. для безопасности dao методы возвращают Optional<T>, сервис делает проверку и возвращает уже объект
## facade
Выполняет задачи, которые указаны в упражнении
