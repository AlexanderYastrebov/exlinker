# Exception Linker

Substitutes class file name in stack trace to parametrized template.

For example
```java
public class Main {

    public static void main(String[] args) {
        Predicate<String> matcher = name -> name.startsWith("ru.ay.example.service");
        String template = "https://github.com/AlexanderYastrebov/exlinker/blob/{rev}/exlinker-core/src/test/java/{packagePath}/{fileName}#L{lineNumber}";
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
java.lang.RuntimeException: Zeta failed
	at ru.ay.example.service.BookService.zeta(BookService.java:21)
	at ru.ay.example.service.BookService.epsilon(BookService.java:14)
	at ru.ay.example.service.BookService.access$100(BookService.java:5)
	at ru.ay.example.service.BookService$Task.run(BookService.java:28)
	at ru.ay.example.service.BookService.delta(BookService.java:10)
	at ru.ay.example.controller.BookController.gamma(BookController.java:18)
	at ru.ay.example.controller.BookController.beta(BookController.java:14)
	at ru.ay.example.controller.BookController.alpha(BookController.java:10)
	...
Caused by: java.lang.RuntimeException: Omega server not available
	at ru.ay.example.dao.BookDao.iota(BookDao.java:14)
	at ru.ay.example.dao.BookDao.theta(BookDao.java:10)
	at ru.ay.example.dao.BookDao.eta(BookDao.java:6)
	at ru.ay.example.service.BookService.zeta(BookService.java:19)
	...
```
before linkage and
```
java.lang.RuntimeException: Zeta failed
	at ru.ay.example.service.BookService.zeta('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L21')
	at ru.ay.example.service.BookService.epsilon('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L14')
	at ru.ay.example.service.BookService.access$100('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L5')
	at ru.ay.example.service.BookService$Task.run('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L28')
	at ru.ay.example.service.BookService.delta('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L10')
	at ru.ay.example.controller.BookController.gamma(BookController.java:18)
	at ru.ay.example.controller.BookController.beta(BookController.java:14)
	at ru.ay.example.controller.BookController.alpha(BookController.java:10)
	...
Caused by: java.lang.RuntimeException: Omega server not available
	at ru.ay.example.dao.BookDao.iota(BookDao.java:14)
	at ru.ay.example.dao.BookDao.theta(BookDao.java:10)
	at ru.ay.example.dao.BookDao.eta(BookDao.java:6)
	at ru.ay.example.service.BookService.zeta('https://github.com/AlexanderYastrebov/exlinker/blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L19')
	...
```
after.

## FAQ

### Why link is quoted?

Quotes signal that file name was already linked so it is safe to call `Exlinker.link` multiple times on the same throwable. 
