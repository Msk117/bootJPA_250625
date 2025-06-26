package com.example.bootJPA;

import com.example.bootJPA.dto.BoardDTO;
import com.example.bootJPA.dto.BoardFileDTO;
import com.example.bootJPA.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BootJpaApplicationTests {

	@Autowired
	private BoardService boardService;

	@Test
	void contextLoads() {
		for (int i = 0; i < 300; i++) {
			BoardDTO boardDTO = BoardDTO.builder()
					.title("test title" + ((int)(Math.random()*100)+1))
					.writer("tester"+((int)(Math.random()*50)+1)+"@test.com")
					.content("test content" + i)
					.build();
			boardService.insert(new BoardFileDTO());

		}
	}

}
