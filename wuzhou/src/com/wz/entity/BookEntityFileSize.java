package com.wz.entity;

/**
 *
 * Created by wanghonghui on 2015/6/14.
 */
public class BookEntityFileSize {
    private BookEntity be;
    private boolean neiwen; //检查内文
    private boolean fengmian; //检查封面
    private boolean fencengpdf; // 检查分层PDF
    private boolean contract; //检查合同
    private boolean bookInfo; //检查图书元数据是否填写完整
    public boolean isBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(boolean bookInfo) {
        this.bookInfo = bookInfo;
    }



    public BookEntity getBe() {
        return be;
    }

    public void setBe(BookEntity be) {
        this.be = be;
    }

    public boolean isNeiwen() {
        return neiwen;
    }

    public void setNeiwen(boolean neiwen) {
        this.neiwen = neiwen;
    }

    public boolean isFengmian() {
        return fengmian;
    }

    public void setFengmian(boolean fengmian) {
        this.fengmian = fengmian;
    }

    public boolean isFencengpdf() {
        return fencengpdf;
    }

    public void setFencengpdf(boolean fencengpdf) {
        this.fencengpdf = fencengpdf;
    }

    public boolean isContract() {
        return contract;
    }

    public void setContract(boolean contract) {
        this.contract = contract;
    }
}
