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

## 3. Liskov Substitution Principle (LSP)

**Принцип:** будь-яка реалізація інтерфейсу може замінити базовий тип.

Усі репозиторії реалізують Spring Data JPA інтерфейси та можуть використовуватись однаково.

### Приклад

- [repository/MovieRepository.java](src/main/java/ua/course/moviesservice/repository/MovieRepository.java#L1-L12)

- [MovieService – інʼєкція MovieRepository](src/main/java/ua/course/moviesservice/service/MovieService.java#L27-L33)

MovieRepository можна використовувати як JpaRepository без зміни поведінки.

---

## 4. Interface Segregation Principle (ISP)

**Принцип:** краще мати кілька вузьких інтерфейсів, ніж один великий.

У проєкті використано вузькі DTO та репозиторії.

### Приклад

- [dto/MovieRequestDto.java](src/main/java/ua/course/moviesservice/dto/MovieCreateUpdateDto.java)  

- [dto/MovieResponseDto.java](src/main/java/ua/course/moviesservice/dto/MovieImportResultDto.java)
Кожен DTO містить лише необхідні поля.

---

## 5. Dependency Inversion Principle (DIP)

**Принцип:** модулі верхнього рівня не повинні залежати від модулів нижнього рівня.

Контролер залежить від сервісу, а сервіс — від інтерфейсу репозиторію.

### Приклад

- [controller/MovieController.java](src/main/java/ua/course/moviesservice/controller/MovieController.java#L20-L22)  

- [service/MovieService.java](src/main/java/ua/course/moviesservice/service/MovieService.java#L27-L41)

Використовується dependency injection через конструктор.

---

## 6. DRY (Don't Repeat Yourself)

**Принцип:** не дублювати код.

Загальні операції збереження та отримання сутностей винесені в сервіс.

### Приклад

- [MovieService – mapToDetailsDto()](src/main/java/ua/course/moviesservice/service/MovieService.java#L55-L62)
- [MovieService – використання mapToDetailsDto](src/main/java/ua/course/moviesservice/service/MovieService.java#L29-L33)

- [MovieService – emptyToNull()](src/main/java/ua/course/moviesservice/service/MovieService.java#L84-L86)


Одна реалізація використовується кількома ендпоінтами.

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

- [controller](src/main/java/ua/course/moviesservice/controller)
- [service](src/main/java/ua/course/moviesservice/service)
- [repository](src/main/java/ua/course/moviesservice/repository)
- [entity](src/main/java/ua/course/moviesservice/entity)
- [dto](src/main/java/ua/course/moviesservice/dto)

### Приклад файлів з різних шарів

- [MovieController](src/main/java/ua/course/moviesservice/controller/MovieController.java)
- [MovieService](src/main/java/ua/course/moviesservice/service/MovieService.java)
- [MovieRepository](src/main/java/ua/course/moviesservice/repository/MovieRepository.java)
- [Movie](src/main/java/ua/course/moviesservice/entity/Movie.java)
- [MovieDetailsDto](src/main/java/ua/course/moviesservice/dto/MovieDetailsDto.java)

### Пояснення

Контролер відповідає лише за HTTP-рівень, сервіс містить бізнес-логіку,
репозиторій працює з базою даних, а сутності та DTO використовуються для
представлення даних.  
Жоден шар не виконує чужі обовʼязки, що відповідає принципу Separation of Concerns.

---

## 8. Clean Code

Код має зрозумілі та самодокументовані назви класів і методів.

### Приклад

- [MovieService](src/main/java/ua/course/moviesservice/service/MovieService.java#L1-L40)
- [MovieService – метод getById](src/main/java/ua/course/moviesservice/service/MovieService.java#L25-L33)
- [MovieService – метод create](src/main/java/ua/course/moviesservice/service/MovieService.java#L35-L44)
- [MovieService – метод delete](src/main/java/ua/course/moviesservice/service/MovieService.java#L58-L70)

---

# Висновок

Проєкт побудований за багатошаровою архітектурою та дотримується принципів
SOLID, DRY, Separation of Concerns і Clean Code.