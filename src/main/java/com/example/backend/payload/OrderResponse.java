package com.example.backend.payload;

import java.util.List;

public class OrderResponse {
   private int pageSize;
    private int pageNumber;
    private int totalPage;
    private long totalElement;
    private List<OrderDto> content;
    private boolean isLastPage;



    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(long totalElement) {
        this.totalElement = totalElement;
    }

    public List<OrderDto> getContent() {
        return content;
    }

    public void setContent(List<OrderDto> content) {
        this.content = content;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
}
