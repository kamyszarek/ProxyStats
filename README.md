# Proxy Management Application

The application is designed for creating proxies and connecting them between the client application and the database. This allows tracking the traffic and displaying the number of queries to the database on a chart. The presented application is an MVP, currently supporting PostgreSQL, and can be extended to other types of databases in the future. Additionally, many other improvements can be added. The PostgreSQL driver is included in the provided files, but it can be replaced with another version for custom needs and compatibility with the database.

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
![screen1](https://github.com/kamyszarek/ProxyStats/assets/102061208/f68238d8-8713-461a-b5d6-6c336a18983b)

- Dashboard tab
![screen2](https://github.com/kamyszarek/ProxyStats/assets/102061208/7327bc73-49e9-4016-af87-5c1bfb030121)

