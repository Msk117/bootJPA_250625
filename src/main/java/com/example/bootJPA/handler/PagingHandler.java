package com.example.bootJPA.handler;

import com.example.bootJPA.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Setter
@ToString
public class PagingHandler<T> {
    private int startPage;
    private int endPage;
    private int totalPage;
    private boolean hasPrev, hasNext;
    private int pageNo;
    private long totalCount;

    private String type;
    private String keyword;
    private List<T> list;

    public PagingHandler(Page<T> list, int pageNo, String type, String keyword){
        this(list, pageNo);
        this.type = type;
        this.keyword = keyword;

    }
    public PagingHandler(Page<T> list, int pageNo){
        this.list = list.getContent();
        this.pageNo = pageNo;
        this.totalPage = list.getTotalPages();
        this.totalCount = list.getTotalElements();
        this.endPage = (int)Math.ceil(this.pageNo / 10.0) * 10;
        this.startPage = endPage - 9;

        this.endPage = (endPage > totalPage) ? totalPage : endPage;
        this.hasPrev = this.startPage > 10;
        this.hasNext = this.endPage < this.totalPage;
    }
}
