# Programming Principles

---
## 1. Single Responsibility Principle (SRP)

**Принцип:** кожен клас має лише одну відповідальність.

У проєкті логіка розділена між контролерами, сервісами та репозиторіями.

### Приклад
- Контролер відповідає лише за обробку HTTP-запитів:
    - [controller/MovieController.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/controller/MovieController.java)
- Бізнес-логіка знаходиться в сервісі:
    - [service/MovieService.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java)
- Доступ до БД реалізований у репозиторії:
    - [repository/MovieRepository.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/repository/MovieRepository.java)

Таким чином кожен клас виконує лише одну роль.

---
## 2. Open/Closed Principle (OCP)

**Принцип:** клас відкритий для розширення, але закритий для змін.

Сервіс використовує інтерфейс репозиторію, тому реалізацію можна змінити без редагування сервісу.

### Приклад

- Інтерфейс:
    - [repository/MovieRepository.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/repository/MovieRepository.java#L12)
  
- Використання в сервісі:
    - [service/MovieService.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L28-L36)
  
Сервіс не залежить від конкретної реалізації.

---

## 3. Liskov Substitution Principle (LSP)

**Принцип:** будь-яка реалізація інтерфейсу може замінити базовий тип.

Усі репозиторії реалізують Spring Data JPA інтерфейси та можуть використовуватись однаково.

### Приклад

- [repository/MovieRepository.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/repository/MovieRepository.java#L12)

- MovieService – інʼєкція [MovieRepository](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L28)

MovieRepository можна використовувати як JpaRepository без зміни поведінки.

---

## 4. Interface Segregation Principle (ISP)

**Принцип:** краще мати кілька вузьких інтерфейсів, ніж один великий.

У проєкті використано вузькі DTO та репозиторії.

### Приклад

- [dto/MovieCreateUpdateDto.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/dto/MovieCreateUpdateDto.java)  

- [dto/MovieDetailsDto.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/dto/MovieDetailsDto.java)

Кожен DTO містить лише необхідні поля.

---

## 5. Dependency Inversion Principle (DIP)

**Принцип:** модулі верхнього рівня не повинні залежати від модулів нижнього рівня.

Контролер залежить від сервісу, а сервіс — від інтерфейсу репозиторію.

### Приклад

- [controller/MovieController.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/controller/MovieController.java#L26-L30)  

- [service/MovieService.java](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L32-L36)

Використовується dependency injection через конструктор.

---

## 6. DRY (Don't Repeat Yourself)

**Принцип:** не дублювати код.

Загальні операції збереження та отримання сутностей винесені в сервіс.

### Приклад

- MovieService – [movieMapper.toDetailsDto()](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L43)
- MovieService – використання [movieMapper.toListItemDto()](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L97-L99)
- MovieService – [emptyToNull()](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L104-L106)


Одна реалізація використовується кількома методами.

---

## 7. Separation of Concerns

**Принцип:** розділення відповідальностей між шарами додатку.

У проєкті кожен шар має чітку зону відповідальності:

- controller — обробка HTTP-запитів
- service — бізнес-логіка
- repository — доступ до бази даних
- entity — доменні моделі
- dto — обʼєкти передачі даних

### Приклад структури

- [controller](https://github.com/matganzovka90-bit/movie-service/tree/main/src/main/java/ua/course/moviesservice/controller)
- [service](https://github.com/matganzovka90-bit/movie-service/tree/main/src/main/java/ua/course/moviesservice/service)
- [repository](https://github.com/matganzovka90-bit/movie-service/tree/main/src/main/java/ua/course/moviesservice/repository)
- [entity](https://github.com/matganzovka90-bit/movie-service/tree/main/src/main/java/ua/course/moviesservice/entity)
- [dto](https://github.com/matganzovka90-bit/movie-service/tree/main/src/main/java/ua/course/moviesservice/dto)

### Приклад файлів з різних шарів

- [MovieController](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/controller/MovieController.java)
- [MovieService](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java)
- [MovieRepository](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/repository/MovieRepository.java)
- [Movie](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/entity/Movie.java)
- [MovieDetailsDto](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/dto/MovieDetailsDto.java)

### Пояснення

Контролер відповідає лише за HTTP-рівень, сервіс містить бізнес-логіку,
репозиторій працює з базою даних, а сутності та DTO використовуються для
представлення даних.  
Жоден шар не виконує чужі обовʼязки, що відповідає принципу Separation of Concerns.

---

## 8. Clean Code

Код має зрозумілі та самодокументовані назви класів і методів.

### Приклад

- [MovieService](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java)
- MovieService – метод [getById](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L38-L44)
- MovieService – метод [create](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L46-L54)
- MovieService – метод [delete](https://github.com/matganzovka90-bit/movie-service/blob/main/src/main/java/ua/course/moviesservice/service/MovieService.java#L72-L80)

---

# Висновок

Проєкт побудований за багатошаровою архітектурою та дотримується принципів
SOLID, DRY, Separation of Concerns і Clean Code.