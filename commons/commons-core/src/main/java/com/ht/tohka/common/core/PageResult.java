package com.ht.tohka.common.core;


import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 以下这个类给mybatis使用，同jpa的Page保持一致，保证给前端返回的数据结构相同
 *
 * @param <T>
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 8656597559014685635L;
    private long totalElements;        //总记录数
    private List<T> content;    //结果集
    private int number;    // 第几页
    private int size;    // 每页记录数
    private int totalPages;        // 总页数
    private int numberOfElements;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性

    /**
     * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理，
     * 而出现一些问题。
     *
     * @param page
     */
    public PageResult(Page<T> page) {
        this.number = page.getPageNum(); // 当前页
        this.size = page.getPageSize(); // 每页的数量
        this.totalElements = page.getTotal(); // 总记录数
        this.totalPages = page.getPages(); // 总页数
        this.numberOfElements = page.size(); // 当前页的数量
        this.content = page; // 结果集
    }

    private PageResult() {

    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(page);
    }

    /**
     * 分页转换成另一个分页，保留页码属性
     *
     * @param page   需要转换的分页对象
     * @param mapper 转换的具体逻辑
     * @return
     */
    public static <T, R> PageResult<R> convert(Page<T> page, Function<T, R> mapper) {
        PageResult<R> pageResult = new PageResult<>();
        pageResult.setNumber(page.getPageNum());
        pageResult.setSize(page.getPageSize());
        pageResult.setTotalElements(page.getTotal());
        pageResult.setTotalPages(page.getPages());
        pageResult.setNumberOfElements(page.size());
        pageResult.content = page.stream().map(mapper).collect(Collectors.toList());
        return pageResult;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    public boolean isFirst() {
        return !hasPrevious();
    }

    public boolean hasNext() {
        return getNumber() < getTotalPages();
    }

    public boolean isLast() {
        return !hasNext();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}
