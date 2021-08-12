# Tokyo-Task

This Java Spring Boot application is a representation of a backend service for orders in a small online store system. In which users (customers) can place, modify, remove or submit orders. The orders are filled with available items. The service domain is described using JPA, and accesed using Repositories, and the endpoints are implemented using Spring Boot Rest Controllers.

The service implements a RESTfull API which enables tech agnostic usage. Error and usage handling is implement using standard HTTP status codes.

## Usage

Sample data is provided via the `DatabaseLoader` class.

Request to the service can be made using standard HTTP request (using CURL for example). 

### Create an order
`curl -i -X POST "http://localhost:8080/order/create?customerId=3"`

### Add an item to a order

`curl -i -X PUT "http://localhost:8080/order/add/11?customerId=3&itemId=6&count=4"`

### Remove an item from a order

`curl -i -X PUT "http://localhost:8080/order/remove/11?customerId=3&itemId=6&count=3"`

### Delete an order

`curl -i -X DELETE "http://localhost:8080/order/delete/11?customerId=3"`

### Submit an order

`curl -i -X PUT "http://localhost:8080/order/submit/11?customerId=3"`

## Additonal functionalites

### Get a customer

`curl -i -X GET "http://localhost:8080/customer/get/3"`

### Get a customers orders

`curl -i -X GET "http://localhost:8080/customer/getOrders/3"`

### Get a item

`curl -i -X GET "http://localhost:8080/item/get/6"`

### Get all items

`curl -i -X GET "http://localhost:8080/item/getAll"`