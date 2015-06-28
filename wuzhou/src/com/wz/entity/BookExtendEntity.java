package com.wz.entity;

import java.util.Date;

/**
 * Created by wanghonghui on 2015/6/29.
 * <p/>
 * <p/>
 * -- ----------------------------
 * -- Table structure for wz_book_extend
 * -- ----------------------------
 * DROP TABLE IF EXISTS `wz_book_extend`;
 * CREATE TABLE `wz_book_extend` (
 * `book_id` int(11) NOT NULL,
 * `book_create_time` datetime DEFAULT NULL COMMENT '元数据录入时间',
 * `book_print_time` datetime DEFAULT NULL COMMENT '责编打印时间',
 * `book_file_flag` char(1) DEFAULT NULL COMMENT '电子文件是否上传',
 * `book_info_flag` char(1) DEFAULT NULL COMMENT '图书元数据是否录入完整',
 * PRIMARY KEY (`book_id`),
 * KEY `book_id` (`book_id`),
 * KEY `book_create_time` (`book_create_time`),
 * KEY `book_print_time` (`book_print_time`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * insert into wz_book_extend (book_id) select book_id from wz_book where book_flag = 0 and book_serial_number in (select DISTINCT(book_serial_number) from wz_log where modify_type = '新书保存')
 */
public class BookExtendEntity {
    private Integer book_id;
    private Date book_create_time;
    private Date book_print_time;
    private String book_file_flag;
    private String book_info_flag;

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public Date getBook_create_time() {
        return book_create_time;
    }

    public void setBook_create_time(Date book_create_time) {
        this.book_create_time = book_create_time;
    }

    public Date getBook_print_time() {
        return book_print_time;
    }

    public void setBook_print_time(Date book_print_time) {
        this.book_print_time = book_print_time;
    }

    public String getBook_file_flag() {
        return book_file_flag;
    }

    public void setBook_file_flag(String book_file_flag) {
        this.book_file_flag = book_file_flag;
    }

    public String getBook_info_flag() {
        return book_info_flag;
    }

    public void setBook_info_flag(String book_info_flag) {
        this.book_info_flag = book_info_flag;
    }
}
