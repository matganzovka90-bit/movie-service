# Movie Service — Spring Boot REST API
REST-сервіс для управління фільмами та режисерами з підтримкою фільтрації, пагінації, CSV-репортів та імпорту з JSON-файлів.

---

## Опис проєкту

Сервіс містить дві основні сутності:

1. **Director** — режисер (унікальне ім’я).
2. **Movie** — фільм з назвою, роком релізу та прив’язкою до режисера.

Функціонал:

- CRUD для Movie та Director
- пагінація та фільтрація для Movie
- генерація CSV-звітів
- імпорт фільмів із JSON файлів через multipart upload
- інтеграційні тести для всіх ендпоїнтів

---

##  Стек технологій

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Liquibase
- MockMvc
- Lombok
- Maven

---

##  Сутності

###  Director

| Поле | Тип | Опис |
|------|------|------|
| id   | Long   | Primary key |
| name | String | **унікальне ім’я режисера**, NOT NULL |

### Movie

| Поле | Тип | Опис |
|------|------|------|
| id          | Long     | Primary key |
| title       | String   | Назва фільму |
| releaseYear | Integer  | Рік |
| director    | Director | Many-to-One |

---

##  REST API

---

## Director API
### Отримати всіх режисерів
GET /api/directors


### Отримати режисера за ID
GET /api/directors/{id}


### Створити режисера
POST /api/directors

Body:
```json
{ "name": "Christopher Nolan" }
```

### Оновити режисера

PUT /api/directors/{id}


### Видалити режисера
DELETE /api/directors/{id}

## Movie API
### Отримати фільм за ID
GET /api/movies/{id}

## Створити фільм
POST /api/movies

Body:
```json
{
"title": "Inception",
"releaseYear": 2010,
"directorId": 1
}
```

## Оновити фільм
PUT /api/movies/{id}

## Видалити фільм
DELETE /api/movies/{id}

## Пошук та фільтрація
POST /api/movies/_list
```json
{
  "titleContains": "",
  "directorId": null,
  "yearFrom": null,
  "yearTo": null,
  "page": 0,
  "size": 20
}
```

Результат
```json
{
  "list": [
    {
      "title": "Inception",
      "releaseYear": 2010,
      "director": {
        "id": 1,
        "name": "Christopher Nolan"
      }
    },
    {
      "title": "Django Unchained",
      "releaseYear": 2012,
      "director": {
        "id": 2,
        "name": "Adam Gellio"
      }
    }
  ],
  "totalPages": 2
}
```

## CSV Звіт
POST /api/movies/_report

## Результат
```
Id,Title,Release_year,DirectorId,DirectorName
1,"Inception",2010,1,"Christopher Nolan"
2,"Django Unchained",2012,2,"Adam Gellio"
3,"Avatar",2009,3,"James Cameron"
```

## Іпорт JSON файлу
POST /api/movies/upload
Content-Type: multipart/form-data

## Приклад JSON
```json
[
  {
    "title": "Inception",
    "releaseYear": 2010,
    "directorId": 1
  },
  {
    "title": "Django Unchained",
    "releaseYear": 2012,
    "directorId": 2
  },
  {
    "title": "Avatar",
    "releaseYear": 2009,
    "directorId": 3
  }
]

```

## Результат
```json
{
  "successCount": 3,
  "failedCount": 0,
  "errors": []
}
```