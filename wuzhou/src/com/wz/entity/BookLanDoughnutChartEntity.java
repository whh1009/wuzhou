package com.wz.entity;

/**
 * 语种饼状图实体类
 * Created by Administrator on 2015/5/3.
 */
public class BookLanDoughnutChartEntity {
    String book_language;



    Integer bookCount;

    public String getBook_language() {
        return book_language;
    }

    public void setBook_language(String book_language) {
        this.book_language = book_language;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }
}
