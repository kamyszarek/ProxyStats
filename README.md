# Proxy Management Application

The application is designed for creating proxies and connecting them between the client application and the database. This allows tracking the traffic and displaying the number of queries to the database on a chart. The presented application is an MVP, currently supporting PostgreSQL, and can be extended to other types of databases in the future. Additionally, many other improvements can be added. The PostgreSQL driver is included in the provided files, but it can be replaced with another version for custom needs and compatibility with the database.

The application is divided into several modules. To begin using it, you need to start the "arkaserver" module. Configure your proxies there. Once you have done that and activated the proxy on the frontend, the JAR file of the "arkaproxy" module will be executed (so make sure you have built the project for the "arkaproxy" module).

# This App is built using the following technologies:

- **Java 17**: The backend of the application is developed using Java, providing a robust and scalable foundation.

- **Spring Boot**: The application is powered by the Spring Boot framework, simplifying the development of Java applications with convention over configuration.

- **JavaScript**: JavaScript is used for client-side scripting, enhancing the interactivity and dynamic behavior of the application.

- **AngularJS**: The frontend is developed using AngularJS, a JavaScript-based open-source front-end web application framework. It facilitates the development of single-page applications and helps create a seamless user experience.

- **Bootstrap**: Bootstrap is employed for responsive and mobile-first front-end development. It provides a set of tools and styles for designing clean and visually appealing user interfaces.

## Communication

The communication between the frontend and backend is established through a RESTful API. This API ensures seamless data exchange and interaction between the different components of the application.

## Getting Started

The simplest way to test the application:

1. Create your proxy in the Wizard tab by providing the necessary details.
2. Start the proxy in the Manage Proxy tab and try to connect to it, for example, using dBeaver. (Currently, there is no option to edit the proxy configuration, so if something is incorrect, create a new proxy.)
3. If you successfully connected through the proxy, you can start sending queries and track their progress on the Dashboard tab.

## Notes

- The application is currently in the Minimum Viable Product (MVP) stage and supports only PostgreSQL.
- The PostgreSQL driver is included, but it can be replaced for compatibility with other databases.
- Future enhancements and features can be added to the application.

## Screenshots

- Manage Proxy tab
![Screen1](https://github.com/kamyszarek/ProxyStats/assets/102061208/ce4c3d9b-86e2-4835-8c97-838197ecefe6)


- Dashboard tab
![screen2](https://github.com/kamyszarek/ProxyStats/assets/102061208/7327bc73-49e9-4016-af87-5c1bfb030121)

