Спринт 1:
1. Создание репозитория и структуры: слои(сервис, репозиторий, дто, контроллер, сущность)
2. Добавить зависимости (Spring Boot starter web, jpa, PostgreSQL connector)
3. Для обоих: скачать PostgreSQL и установить на компьютере, прописать все проперти для коннекта, договорится за
пароль логин, создать одинаковую базу данных каждый на своем компьютере
4. Создаем сущности(user(1), product(2), category(1), favorite(2))
5. Создать для каждой сущности дто, репозитории, сервисы и контроллеры для всех сущностей
(для user, product, category дто в обе стороны, для favorite только responseDto)
5.1 Для всех сущностей должны быть реализованы методы create(), getAll(), getById(), deleteById() (для favorite только getAll() и get())
6. Добавить файлы schema.sql и data.sql (в ресурсы) (можно в data.sql указать запрос для очистки данных и не добавлять schema.sql)
7. Написать unit тесты на слой сервисов 
8. Разобрать и использовать библиотеку mockito со Spring Boot тестами 

Спринт 2:
1. Поменять порт на стандартный 5432
2. Добавить для Product метод edit(), delete(); для Category edit(), delete(); 
для Shop User edit(), delete()
3. Метод фильтрации для продуктов(category, minPrice, maxPrice, discount, sort)
4. Избавится от авто генерации таблиц и подключить liquibase
5. Перехватить исключения Handler Exception 

Спринт 3:
1. Сделать корзину Cart должны быть id, user_id; 
2. CartItem -> товар, который находится в корзине, должны быть id, product_id, quantity, card_id(внешний ключ)
3. Order -> заказ, должен быть status, created, etc;
OrderItem
4. Добавить в Cart и Order методы: getById(), create(создается только первый раз при попытке добавить товар в корзину
реализация только в сервисе), add(), clear(один товар удаляет), edit(), delete(удаляет корзину); 
Для Order create(передаем корзину, сверяем, удаляем товары из CartItem и добавляем в OrderItem), 
get(показывает статус заказа)
5. User -> Cart -> CartItem -> User -> Order(товары из таблицы CartItem, должны перетекать в таблицу OrderItem)
6. Payment - сущность, должны быть user_id, amount, date, order_id;
Payment - controller, service etc
7. Добавить JACOCO - плагин