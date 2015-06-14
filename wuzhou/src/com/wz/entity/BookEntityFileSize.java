package com.wz.entity;

/**
 *
 * Created by wanghonghui on 2015/6/14.
 */
public class BookEntityFileSize {
    private BookEntity be;
    private boolean neiwen;
    private boolean fengmian;
    private boolean fencengpdf;
    private boolean contract;

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
