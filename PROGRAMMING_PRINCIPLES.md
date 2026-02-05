# Programming Principles

---
## 1. Single Responsibility Principle (SRP)

**Принцип:** кожен клас має лише одну відповідальність.

У проєкті логіка розділена між контролерами, сервісами та репозиторіями.

### Приклад

- Контролер відповідає лише за обробку HTTP-запитів:
    - [controller/MovieController.java](src/main/java/ua/course/moviesservice/controller/MovieController.java)
- Бізнес-логіка знаходиться в сервісі:
    - [service/MovieService.java](src/main/java/ua/course/moviesservice/service/MovieService.java)
- Доступ до БД реалізований у репозиторії:
    - [repository/MovieRepository.java](src/main/java/ua/course/moviesservice/repository/MovieRepository.java)

Таким чином кожен клас виконує лише одну роль.

---

## 2. Open/Closed Principle (OCP)

**Принцип:** клас відкритий для розширення, але закритий для змін.

Сервіс використовує інтерфейс репозиторію, тому реалізацію можна змінити без редагування сервісу.

### Приклад

- Інтерфейс:
    - [repository/MovieRepository.java](src/main/java/ua/course/moviesservice/repository/MovieRepository.java#L14-L28)
  
- Використання в сервісі:
    - [service/MovieService.java](src/main/java/ua/course/moviesservice/service/MovieService.java#L87-L99)
  
Сервіс не залежить від конкретної реалізації.

---