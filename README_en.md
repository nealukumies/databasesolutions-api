# API backend project

This project is an API backend built with Spring Boot done for the course Database Solutions at Metropolia UAS. The API is ready to use for a webstore application for customers. The API supports basic operations for customers, suppliers, products, orders and payments. It also includes some extra queries for products and customers. More admin-level operations are not implemented, but could be added in the future. The focus here was to implement mainly the customer-facing API endpoints, and to ensure that the database design supports the required functionality.

## Features and Implementation Details

The webstore API provides full functionality for managing customers, products, orders, payments, and suppliers. From a customer perspective, it supports browsing products and categories, creating and updating orders, managing addresses, and viewing payments.

### Database and Backend Features

Here are the features of the database and backend implementation that make the API robust and efficient. Some of these features are not directly visible in the API endpoints, but are important for the overall functionality and could be essential for maintaining a production-level application.

- **Indexes**<br>
  Created on customers table for last names to speed up customer lookups. Related to admin-level operations, indexes are also created on orders for order status and on suppliers for supplier names.

- **Views**<br>
  There is a view showing all new and pending orders with delivery details making it efficient to query.

- **Triggers and Events**<br>
  Triggers are implemented for logging changes (for example contact info updates), maintaining stock levels on new orders and summarizing daily orders.

- **Data Integrity and Transactions**<br>
  Row-level locks ensure consistency, especially for inventory updates, preventing reads of uncommitted data during concurrent transactions.

- **Security**<br>
  There are two database users: `webstoreadmin` with full privileges (restricted to localhost) and `webstoreuser` with limited access to essential tables (`customers`, `customeraddresses`, `orders`, `orderitems`, `products`, `productcategories`). This separation enhances the security of the database.

- **Temporal Features**<br>
  There is versioning on orders and product prices using timestamps and validity ranges making it possible to query for historical data.

- **Backup and Maintenance**<br>
  Regular backups and a maintenance plan were considered during development, but no actual backup scripts or maintenance jobs are implemented in this project.

- **SwaggerUI**<br>
  The project includes SwaggerUI which provides an interactive web inteface to explore and test all the API endpoints. When the application is running, you can access SwaggerUI at `http://localhost:8081/swagger-ui/index.html` to see all the available endpoints, their request and response formats, and to test them directly from the browser.
  <br><br>

## Webstore API Documentation

In the following tables are all API endpoints for the webstore. Each endpoint includes the HTTP method and path for the request, a very brief description of the endpoint. For the endpoints that require a request body, there is an example of the JSON format for the request. For all endpoints, there is an example of the expected JSON response. Finally, there are notes about the expected HTTP status codes for each endpoint.

---

### Customers

| Method & Path                | Description                     | Request Example                                                                                                 | Response Example                                                                                                                    | Notes                  |
| ---------------------------- | ------------------------------- | --------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| GET /customers/{id}          | Get customer by ID              | —                                                                                                               | `{ "id": 1, "firstName": "Jane", "lastName": "Doe", "email": "jane@example.com", "phone": "12345678" }`                             | 200 OK / 404 Not Found |
| POST /customers              | Create person customer          | `{ "firstName": "Jane", "lastName": "Doe", "email": "jane@example.com", "phone": "12345678" }`                  | Created Customer JSON                                                                                                               | 201 Created            |
| POST /customers              | Create company customer         | `{ "companyName": "Tech Corp", "vatNumber": "FI1234567", "email": "contact@techcorp.com", "phone": "5551234" }` | Created Customer JSON                                                                                                               | 201 Created            |
| PUT /customers/{id}          | Update customer                 | `{ "firstName": "Jane", "lastName": "Doe Updated", "email": "jane.updated@example.com", "phone": "99999999" }`  | Updated Customer JSON                                                                                                               | 200 OK / 404 Not Found |
| POST /customers/{id}/address | Add customer address            | `{ "streetAddress": "Mannerheimintie 10", "postalCode": "00100", "city": "Helsinki", "country": "Finland" }`    | Created Address JSON                                                                                                                | 201 Created            |
| GET /customers/{id}/address  | Get customer address            | —                                                                                                               | `{ "streetAddress": "Mannerheimintie 10", "postalCode": "00100", "city": "Helsinki", "country": "Finland" }`                        | 200 OK                 |
| PUT /customers/{id}/address  | Update customer address         | `{ "streetAddress": "Mannerheimintie 100", "postalCode": "00100", "city": "Helsinki", "country": "Finland" }`   | Updated Address JSON                                                                                                                | 200 OK                 |
| GET /customers/{id}/orders   | Get all orders for a customer   | —                                                                                                               | `[ { "id": 1, "orderDate": "2026-03-05T10:00:00", "deliveryDate": "2026-03-10T10:00:00", "status": "NEW" } ]`                       | 200 OK                 |
| GET /customers/{id}/payments | Get all payments for a customer | —                                                                                                               | `[ { "id": 1, "orderId": 1, "cardNumber": "1234-5678-9012-3456", "paymentStatus": "PAID", "paymentDate": "2026-03-05T11:00:00" } ]` | 200 OK                 |

---

### Suppliers

| Method & Path       | Description        | Request Example                                                                                                         | Response Example                                                                                                                 | Notes                  |
| ------------------- | ------------------ | ----------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| POST /suppliers     | Create supplier    | `{ "name": "Book Suppliers Ltd", "contactName": "John Supplier", "phone": "5555678", "email": "supplier@example.com" }` | Created Supplier JSON                                                                                                            | 201 Created            |
| PUT /suppliers/{id} | Update supplier    | `{ "name": "Updated Supplier", "contactName": "Alice", "phone": "5550000", "email": "updated@supplier.com" }`           | Updated Supplier JSON                                                                                                            | 200 OK                 |
| GET /suppliers/{id} | Get supplier by ID | —                                                                                                                       | `{ "id": 1, "name": "Book Suppliers Ltd", "contactName": "John Supplier", "phone": "5555678", "email": "supplier@example.com" }` | 200 OK / 404 Not Found |

---

### Products

| Method & Path                       | Description                               | Request Example                                                                                                                                                                                         | Response Example                                                                                                 | Notes                  |
| ----------------------------------- | ----------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- | ---------------------- |
| POST /products                      | Create a product                          | `{ "name": "Bookfind", "description": "Keep finding books", "price": 123.00, "stockQuantity": 10, "categoryId": 2, "startDate": "2026-03-01", "endDate": "2026-12-31", "supplierIds": [1] }`            | Created Product JSON                                                                                             | 201 Created            |
| PUT /products/{id}                  | Update product                            | `{ "name": "Bookfind Updated", "description": "Updated description", "price": 150.00, "stockQuantity": 20, "categoryId": 2, "startDate": "2026-03-01", "endDate": "2026-12-31", "supplierIds": [1,2] }` | Updated Product JSON                                                                                             | 200 OK                 |
| GET /products                       | Get all products                          | —                                                                                                                                                                                                       | `[ { "id": 1, "name": "Bookfind", "price": 123.00, "stockQuantity": 10, "categoryId": 2, "supplierIds": [1] } ]` | 200 OK                 |
| GET /products/{id}                  | Get product by ID                         | —                                                                                                                                                                                                       | `{ "id": 1, "name": "Bookfind", "price": 123.00, "stockQuantity": 10, "categoryId": 2, "supplierIds": [1] }`     | 200 OK / 404 Not Found |
| GET /products/category/{categoryId} | Get products by category                  | —                                                                                                                                                                                                       | `[ { "id": 1, "name": "Bookfind", "price": 123.00, "stockQuantity": 10, "categoryId": 2, "supplierIds": [1] } ]` | 200 OK                 |
| PATCH /products/update-price        | Update all product prices by a percentage | `{ "percentage": 10.0 }`                                                                                                                                                                                | —                                                                                                                | 200 OK                 |
| POST /products/search               | Search products by price range            | `{ "minPrice": 100.0, "maxPrice": 200.0 }`                                                                                                                                                              | `[ { "id": 1, "name": "Bookfind", "price": 123.00, "stockQuantity": 10, "categoryId": 2, "supplierIds": [1] } ]` | 200 OK                 |

---

### Orders

| Method & Path    | Description     | Request Example                                                                                                                               | Response Example                                    | Notes                  |
| ---------------- | --------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------- | ---------------------- |
| POST /orders     | Create order    | `{ "customerId": 1, "orderDate": "2026-03-05T10:00:00", "deliveryDate": "2026-03-10T10:00:00", "shippingAddressId": 5, "status": "NEW" }`     | Created Order JSON                                  | 201 Created            |
| PUT /orders/{id} | Update order    | `{ "customerId": 1, "orderDate": "2026-03-05T10:00:00", "deliveryDate": "2026-03-12T10:00:00", "shippingAddressId": 5, "status": "SHIPPED" }` | Updated Order JSON                                  | 200 OK                 |
| GET /orders/{id} | Get order by ID | —                                                                                                                                             | `{ "id": 1, "customerId": 1, "status": "SHIPPED" }` | 200 OK / 404 Not Found |

---

### Payments

| Method & Path      | Description    | Request Example                                                                                                            | Response Example     | Notes       |
| ------------------ | -------------- | -------------------------------------------------------------------------------------------------------------------------- | -------------------- | ----------- |
| POST /payments     | Create payment | `{ "orderId": 1, "cardNumber": "1234-5678-9012-3456", "paymentStatus": "PAID", "paymentDate": "2026-03-05T11:00:00" }`     | Created Payment JSON | 201 Created |
| PUT /payments/{id} | Update payment | `{ "orderId": 1, "cardNumber": "1234-5678-9012-3456", "paymentStatus": "REFUNDED", "paymentDate": "2026-03-05T12:00:00" }` | Updated Payment JSON | 200 OK      |
