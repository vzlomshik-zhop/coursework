# A simple client-server application written on Java.
Both the client and the server use sockets to establish connection between each other and threads to perform task without blocking the application.
# Server
The server consist of 5 separate classes(excluding Main):
1. Server - a class that is responsible for establishing connections and dedicating 2 threads for each connection
2. Msg - a structure, that keeps an information about a message(its sender, receiver and the message itself)
3. DataBase - a class that manages messages operations and keeps messages list
4. RequestProcessor - a thread, that handles incoming requests and sends responses
5. MessageChecker - a thread, that searches for new messages in the database for current user

# Client
The client consists of 3 separate functional classes(excluding Main and 3 graphic components):
1. Messages - a structure for storing messages, directed to the current user
2. ClientApi - a class, that manages information exchange and processing between a client and the server
3. MessageChecker - a thread for reading responses from the server

# How to run
1. Launch the server
2. Launch the client
## Note: both the client and the server are running on the localhost
