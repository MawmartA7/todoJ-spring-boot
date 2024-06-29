# TODOJ application

This API was created for learning purposes and study practice. It is a simple CRUD application with 8 endpoints, designed to manipulate a task entity in a database of your choice.

## Tecnologies stack

|tecnologie|version|download_requirement|
|:----:|:---:|:--:|
| [java](https://www.oracle.com/java) | 21 |require|
| [Maven](https://maven.apache.org) | 3.9.8 |require|
| [Spring boot](https://spring.io/) | 3.3.1 |don't have download|
| [postgresql](https://www.postgresql.org)| 16 |optional|

---

## Setup application

### 1. Clone the Repository

Clone the repository to your local machine using the command below:

``` bash
    git clone https://github.com/MawmartA7/todoJ-spring-boot
    cd todoJ-spring-boot
```

## Database Configuration

### Switching between PostgreSQL and H2

This project is configured to use both PostgreSQL and H2 databases. Depending on the environment and the availability of the PostgreSQL database, you may choose to use the H2 in-memory database for testing and development.

#### Configuration for PostgreSQL

If you have a PostgreSQL database set up, follow the instructions below:

1. Ensure that the following lines are **uncommented** in your `application.properties` file (path: `./src/main/resources/application.properties`):

    ```properties
    # PostgreSQL database Configuration

    spring.config.import=file:.env[.properties]
    spring.datasource.url=${SPRING_DATASOURCE_URL}
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

2. Create a `.env` file in the root directory of your project with the following content:

    ```env
    SPRING_DATASOURCE_URL=jdbc:postgresql://<your_postgresql_host>:<your_postgresql_port>/<your_database_name>
    SPRING_DATASOURCE_USERNAME=<your_postgresql_username>
    SPRING_DATASOURCE_PASSWORD=<your_postgresql_password>
    ```

3. Comment out the lines related to H2 Database Configuration:

    ```properties
    # H2 Database Configuration

    # spring.datasource.url=jdbc:h2:mem:testdb
    # spring.datasource.driver-class-name=org.h2.Driver
    # spring.datasource.username=sa
    # spring.datasource.password=
    # spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    # spring.jpa.hibernate.ddl-auto=update
    ```

#### Configuration for H2

If you do not have a PostgreSQL database set up, or prefer to use H2 for testing and development, follow the instructions below:

1. Comment out the lines related to PostgreSQL Configuration:

    ```properties
    # PostgreSQL database Configuration

    # spring.config.import=file:.env[.properties]
    # spring.datasource.url=${SPRING_DATASOURCE_URL}
    # spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    # spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    # spring.jpa.hibernate.ddl-auto=update
    # spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
    ```

2. Uncomment the lines related to H2 Database Configuration:

    ```properties
    # H2 Database Configuration

    spring.datasource.url=jdbc:h2:mem:testdb
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.jpa.hibernate.ddl-auto=update
    ```

By following these instructions, you can easily switch between using a PostgreSQL database and an H2 in-memory database, as needed for your development or production environment.

### 3. Install Dependencies

Run one of the following commands to install the dependencies:

``` bash
    ./mvnw clean install
```

### 4. Run the Project

After configuring the environment variables and installing the dependencies, you can run the project using the following command:

``` bash
    ./mvnw spring-boot:run
```

### 5. Access the Application

The TodoJ application will be running and accessible on the default port 8080 (or another port configured in application.properties). Open your API test (postman or insomnia or any other) and go to <http://localhost:8080>.

---

## Data base table task structure

None of the table columns can be **null**

|`id`|`name`|`description`|`priority`|`done`|
|:--:|:--:|:--:|:--:|:--:|
| bigint and primary key | varchar(255) | varchar(255) | integer | boolean |

---

## endpoints

|method|url|description|specification|
|:--:|:--:|:--:|:--:|
| `GET` | `/todo-list` | Get all tasks already registered in the data base | [specification](#get-todo-list) |
| `GET` | `/todo-list/done` | Get all tasks completeds in the data base | [specification](#get-todo-listdone) |
| `GET` | `/todo-list/pending` | Get all tasks pending in the data base | [specification](#get-todo-listpending) |
| `GET` | `/todo-list/{id}` | Get task of the data base by id | [specification](#get-todo-listid) |
| `POST` | `/todo-list` | Register a new task in the the data base | [specification](#post-todo-list) |
| `PUT` | `/todo-list/{id}` | Update a task in the the data base | [specification](#put-todo-listid) |
| `PATCH` | `/todo-list/{id}` | Partial update a task in the the data base | [specification](#patch-todo-listid) |
| `DELETE` | `/todo-list/{id}` | Delete a task in the the data base | [specification](#delete-todo-listid) |

### All specifications endpoints

#### GET: /todo-list

Get all tasks already registered in the data base

**Format:**

- Method: `GET`
- URL: <http://localhost:8080/todo-list>
- Path parameters: none
- body: none

**Exemple:**

**Request:**

- Method: `GET`
- URL: <http://localhost:8080/todo-list>

**Response:**

- HTTP Status: `200 Ok`

``` json
    [
        {
            "id": 1,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 1,
            "done": false
        }
        {
            "id": 2,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 3,
            "done": true
        }
        // etc...
    ]
```

#### GET: /todo-list/done

Get all tasks completeds in the data base

**Format:**

- Method: `GET`
- URL: <http://localhost:8080/todo-list/done>
- Path parameters: none
- body: none

**Exemple:**

Request:

- Method: `GET`
- URL: <http://localhost:8080/todo-list/done>

**Response:**

- HTTP Status: `200 Ok`

``` json
    [
        {
            "id": 2,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 3,
            "done": true
        }
        {
            "id": 3,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 2,
            "done": true
        }
        // etc...
    ]
```

#### GET: /todo-list/pending

Get all tasks pending in the data base

**Format:**

- Method: `GET`
- URL: <http://localhost:8080/todo-list/pending>
- Path parameters: none
- body: none

**Exemple:**

Request:

- Method: `GET`
- URL: <http://localhost:8080/todo-list/pending>

**Response:**

- HTTP Status: `200 Ok`

``` json
    [
        {
            "id": 1,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 1,
            "done": false
        }
        {
            "id": 4,
            "name": "name of the task",
            "description": "description of the task",
            "priority": 1,
            "done": false
        }
        // etc...
    ]
```

<!-- markdownlint-disable-next-line MD033 -->
<h4 id="get-todo-listid">GET: /todo-list/{id}</h4>

Get task of the data base by id

**Format:**

- Method: `GET`
- URL: <http://localhost:8080/todo-list/{id}>
- Path parameters:
  - `id`: Integer and required
- body: none

**Exemple:**

Request:

- Method: `GET`
- URL: <http://localhost:8080/todo-list/2>
- Path parameters:
  - `id`: 2

**Response:**

- HTTP Status: `200 Ok`

``` json
   {
        "id": 2,
        "name": "name of the task",
        "description": "description of the task",
        "priority": 3,
        "done": true
    } 
```

#### POST: /todo-list/

register a new task in the database

**Format:**

- Method: `POST`
- URL: <http://localhost:8080/todo-list/>
- Path parameters: none
- body:
  - `id`: none
  - `name`: String and required
  - `description`: String and required
  - `priority`: Integer and required
  - `done`: Boolean and required

**Exemple:**

Request:

- Method: `POST`
- URL: <http://localhost:8080/todo-list>
- body:

``` json
   {
        "name": "name of the task",
        "description": "description of the task",
        "priority": 2,
        "done": false
    }
```

**Response:**

- HTTP Status: `201 Created`

``` json
   {
        "id": 2,
        "name": "name of the task",
        "description": "description of the task",
        "priority": 2,
        "done": false
    }
```

<!-- markdownlint-disable-next-line MD033 -->
<h4 id="put-todo-listid">PUT: /todo-list/{id}</h4>

Update a task in the database

**Format:**

- Method: `PUT`
- URL: <http://localhost:8080/todo-list/{id}/>
- Path parameters:
  - `id`: Integer and required
- body:
  - `id`: none
  - `name`: String and required
  - `description`: String and required
  - `priority`: Integer and required
  - `done`: Boolean and required

**Exemple:**

Request:

- Method: `PUT`
- URL: <http://localhost:8080/todo-list/3>
- Path parameters:
  - `id`: 3
- body:

``` json
   {
        "name": "new name of the task",
        "description": "new description of the task",
        "priority": 3,
        "done": false
    }
```

**Response:**

- HTTP Status: `200 Ok`

``` json
   {
        "id": 2,
        "name": "new name of the task",
        "description": "new description of the task",
        "priority": 3,
        "done": false
    }
```

<!-- markdownlint-disable-next-line MD033 -->
<h4 id="patch-todo-listid">PATCH: /todo-list/{id}</h4>

Partial update a task in the the data base

**Format:**

- Method: `PATCH`
- URL: <http://localhost:8080/todo-list/{id}/>
- Path parameters:
  - `id`: Integer and required
- body:
  - `id`: none
  - `name`: String and optional but requires at least 1
  - `description`: String and optional but requires at least 1
  - `priority`: Integer and optional but requires at least 1
  - `done`: Boolean and optional but requires at least 1

**Exemple:**

Request:

- Method: `PATCH`
- URL: <http://localhost:8080/todo-list/3>
- Path parameters:
  - `id`: 3
- body:

``` json
   {
        "priority": 2,
        "done": true
    }
```

**Response:**

- HTTP Status: `200 Ok`

``` json
   {
        "id": 2,
        "name": "new name of the task",
        "description": "new description of the task",
        "priority": 2,
        "done": true
    }
```

<!-- markdownlint-disable-next-line MD033 -->
<h4 id="delete-todo-listid">DELETE: /todo-list/{id}</h4>

Delete a task in the the data base by id

**Format:**

- Method: `DELETE`
- URL: <http://localhost:8080/todo-list/{id}/>
- Path parameters:
  - `id`: Integer and required
- body: none

**Exemple:**

Request:

- Method: `DELETE`
- URL: <http://localhost:8080/todo-list/3>
- Path parameters:
  - `id`: 3

**Response:**

- HTTP Status: `204 No Content`
