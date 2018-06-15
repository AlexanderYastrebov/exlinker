package ru.ay.example.controller;

import ru.ay.example.service.BookService;

public class BookController {

    private final BookService bookService = new BookService();

    public void alpha() {
        beta();
    }

    private void beta() {
        gamma();
    }

    private void gamma() {
        bookService.delta();
    }
}

