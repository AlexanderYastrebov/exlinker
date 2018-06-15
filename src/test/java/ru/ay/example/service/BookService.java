package ru.ay.example.service;

import ru.ay.example.dao.BookDao;

public class BookService {

    private final BookDao bookDao = new BookDao();

    public void delta() {
        epsilon();
    }

    private void epsilon() {
        zeta();
    }

    private void zeta() {
        bookDao.eta();
    }
}
