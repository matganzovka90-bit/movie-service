# Programming Principles

---
## 1. Single Responsibility Principle (SRP)

**Принцип:** кожен клас має лише одну відповідальність.

У проєкті логіка розділена між контролерами, сервісами та репозиторіями.

### Приклад

- Контролер відповідає лише за обробку HTTP-запитів:
    - [controller/MovieController.java](src/main/java/ua/course/moviesservice/controller/MovieController.java)
- Бізнес-логіка знаходиться в сервісі:
    - service/MovieService.java
- Доступ до БД реалізований у репозиторії:
    - repository/MovieRepository.java  

Таким чином кожен клас виконує лише одну роль.