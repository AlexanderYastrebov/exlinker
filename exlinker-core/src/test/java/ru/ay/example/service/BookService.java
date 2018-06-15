package ru.ay.example.service;

import ru.ay.example.dao.BookDao;

public class BookService {

    private final BookDao bookDao = new BookDao();

    public void delta() {
        new Task().run();
    }

    private void epsilon() {
        zeta();
    }

    private void zeta() {
        try {
            bookDao.eta();
        } catch (Exception ex) {
            throw new RuntimeException("Zeta failed", ex);
        }
    }

    private class Task {

        void run() {
            epsilon();
        }
    }
}
