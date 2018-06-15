# Exception Linker

Substitutes class file name in stack trace to parametrized template.

For example
```java
public class Main {

    public static void main(String[] args) {
        String template = "https://github.com/AlexanderYastrebov/exlinker/blob/{rev}/src/test/java/{packagePath}/{fileName}#L{lineNumber}";
        Predicate<String> matcher = name -> name.startsWith("ru.ay.example.service");
        Exlinker exlinker = new Exlinker(matcher, template, "rev", "master");

        try {
            new BookController().alpha();
        } catch (Exception ex) {
            ex.printStackTrace();

            exlinker.link(ex);

            ex.printStackTrace();
        }
    }
}
```
will print 
```
java.lang.RuntimeException: Omega server not available
	at ru.ay.example.dao.BookDao.iota(BookDao.java:14)
	at ru.ay.example.dao.BookDao.theta(BookDao.java:10)
	at ru.ay.example.dao.BookDao.eta(BookDao.java:6)
	at ru.ay.example.service.BookService.zeta(BookService.java:18)
	at ru.ay.example.service.BookService.epsilon(BookService.java:14)
	at ru.ay.example.service.BookService.delta(BookService.java:10)
	at ru.ay.example.controller.BookController.gamma(BookController.java:18)
	at ru.ay.example.controller.BookController.beta(BookController.java:14)
	at ru.ay.example.controller.BookController.alpha(BookController.java:10)
	...
```
before linking and
```
java.lang.RuntimeException: Omega server not available
	at ru.ay.example.dao.BookDao.iota(BookDao.java:14)
	at ru.ay.example.dao.BookDao.theta(BookDao.java:10)
	at ru.ay.example.dao.BookDao.eta(BookDao.java:6)
	at ru.ay.example.service.BookService.zeta(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L18)
	at ru.ay.example.service.BookService.epsilon(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L14)
	at ru.ay.example.service.BookService.delta(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L10)
	at ru.ay.example.controller.BookController.gamma(BookController.java:18)
	at ru.ay.example.controller.BookController.beta(BookController.java:14)
	at ru.ay.example.controller.BookController.alpha(BookController.java:10)
	...
```
after.
