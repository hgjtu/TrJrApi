# Журнал путешествий - API
описание проекта

## Содержание
- [Технологии](#технологии)
- [Использование](#использование)
- [Автор](#автор)

## Технологии
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Spring Boot 3.4.3](https://spring.io/blog/2025/02/20/spring-boot-3-4-3-available-now)
- [PostgreSQL](https://www.postgresql.org/)
- [MinIO](https://min.io/)

## Использование
Чтобы установить проект:
1. Создайте базу данных PostgreSQL
2. Запустите MinIO, создайте бакет
3. Отредактируйте файл со свойствами (внесите данные о бд и minio)
4. Соберите контейнер
```bash
docker build -t travel-journal .
```
5. После успешного билда - запуск
```bash
docker run -d -p 8010:8010 travel-journal
```

## Автор
Калинина Марина Павловна, студент МИРЭА, группа ИКБО-02-22\
[Почта для связи](kalinina.m.p@edu.mirea.ru)