package cetus.bean;

import java.util.List;

import lombok.Getter;

@Getter
public class Page<B> {

    public Page(List<B> list, int count, Pageable pagable) {
        this.list = list;
        this.totalElements = count;
        this.number = pagable.getPageNumber();
        this.size = pagable.getSize();
        this.totalPages = (this.totalElements/this.size) + (this.totalElements % this.size == 0 ? 0 : 1);
        this.countPage = pagable.getCountPage();
        this.startPage = ((this.number-1)/ this.countPage) * this.countPage + 1;
        this.endPage =  this.startPage + countPage -1;
        if(this.totalPages < this.endPage) {
            this.endPage = totalPages;
        }
        this.prev = this.startPage - this.countPage;
        if(this.prev < 1) {
            this.prev = 1;
        }
        this.next = this.startPage + this.countPage;
        if(this.next > this.totalPages) {
            this.next = this.totalPages;
        }
    }
    private int prev;
    private int next;
    private int countPage;
    private List<B> list;
    private int totalElements;
    private int number;
    private int size;
    private int totalPages;
    private int startPage;
    private int endPage;

}
