package com.example.bootJPA.controller;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.BoardFileDTO;
import com.example.bootJPA.dto.FileDTO;
import com.example.bootJPA.entity.Board;
import com.example.bootJPA.handler.FileHandler;
import com.example.bootJPA.handler.PagingHandler;
import com.example.bootJPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

    @GetMapping("/register")
    public void register(){}
    @PostMapping("/register")
    public String register(BoardDTO boardDTO,
                           @RequestParam(name = "files", required = false)
                           MultipartFile[] files){
        List<FileDTO> fileList = null;
        if (files != null && files[0].getSize() > 0){
            // 파일 핸들러 작업
            fileList = fileHandler.uploadFiles(files);
        }
        Long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));
        log.info(">>>> insert id >>> {}", bno);
        return "redirect:/board/list";

    }


//    @PostMapping("/register")
//    public String register(BoardDTO boardDTO){
//        // insert, update, delete => return 1 row (성공한 row의 개수)
//        // jpa => insert, update, delete => return id
//        Long bno = boardService.insert(boardDTO);
//        log.info(">>>> insert id >>> {}", bno);
//        return "index";
//
//    }

    /*@GetMapping("/list")
    public void list(Model model){
        *//* paging 없는 일반 리스트 *//*
        List<BoardDTO>list = boardService.getList();
        model.addAttribute("list", list);
    }*/

//    @GetMapping("/list")
//    public void list(Model model, @RequestParam(name = "pageNo", defaultValue = "1",required = false) int pageNo){
//        /* select * from board order by bno desc limit 0, 10 */
//        /* limit 시작번지, 개수 => 시작번지는 0부터 시작 */
//        //int pageNo = 1; // 나중에 페이지네이션 클릭하는 값으로 입력될 값
//        Page<BoardDTO> list = boardService.getPageList(pageNo-1);
//        log.info(">>>> pageList >> {}", list);
//        log.info(">>>> List >> {}", list.toString());
//        log.info(">>>> totalCount >> {}", list.getTotalElements()); // 전체 글 수
//        log.info(">>>> totalPage >> {}", list.getTotalPages()); // 전체 페이지수 (realEndPage)
//        log.info(">>>> pageNumber >> {}", list.getNumber()); // 현재 페이지 번호 (pageNo)
//        log.info(">>>> pageSize >> {}", list.getSize()); // 한 페이지에 표시되는 개수 (qty)
//        log.info(">>>> next >> {}", list.hasNext()); // next 여부
//        log.info(">>>> prev >> {}", list.hasPrevious()); // perv 여부
//
//        PagingHandler pagingHandler = new PagingHandler(list, pageNo);
//        log.info(">>>> pagingHandler >> {}", pagingHandler);
//        model.addAttribute("list",list);
//        model.addAttribute("ph", pagingHandler);
//
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(name = "pageNo", defaultValue = "1", required = false) int pageNo,
                     @RequestParam(name = "type", required = false) String type,
                     @RequestParam(name = "keyword",required = false) String keyword){
        /* Page + search 포함 */
        int page = pageNo-1; // DB에서 0번지 부터 시작하기 위한 변수
        Page<BoardDTO> list = boardService.getList(page, type, keyword);
        PagingHandler ph = new PagingHandler(list, pageNo, type, keyword);
        model.addAttribute("list", list);
        model.addAttribute("ph",ph);

    }

    @GetMapping("/detail")
    public void detail(Model model, @RequestParam("bno") Long bno){
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFileDTO", boardFileDTO);
    }

    @PostMapping("/update")
    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes,
                         @RequestParam(name = "files", required = false) MultipartFile[] files){
        log.info(">>>> boardDTO >> {}", boardDTO);
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            fileList = fileHandler.uploadFiles(files);
        }
        Long bno = boardService.modify(new BoardFileDTO(boardDTO, fileList));
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/detail";
    }
//    @PostMapping("/update")
//    public String modify(BoardDTO boardDTO, RedirectAttributes redirectAttributes){
//        log.info(">>>> boardDTO >> {}", boardDTO);
//        Long bno = boardService.modify(boardDTO);
//        redirectAttributes.addAttribute("bno", boardDTO.getBno());
//        return "redirect:/board/detail";
//    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno")Long bno){
        log.info(">>>> bno >> {}", bno);
        boardService.remove(bno);
        return "redirect:/board/list";
    }

    @DeleteMapping("/file/{uuid}")
    @ResponseBody
    public String fileRemove(@PathVariable("uuid") String uuid){
        long bno = boardService.fileRemove(uuid);
        return bno > 0 ? "1":"0";
    }



}
