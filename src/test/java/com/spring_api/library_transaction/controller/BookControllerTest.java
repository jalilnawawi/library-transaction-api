package com.spring_api.library_transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_api.library_transaction.model.dto.DataResponse;
import com.spring_api.library_transaction.model.dto.DatatableResponse;
import com.spring_api.library_transaction.model.dto.PageDataResponse;
import com.spring_api.library_transaction.model.dto.book.request.CreateBookRequest;
import com.spring_api.library_transaction.model.dto.book.response.BookResponse;
import com.spring_api.library_transaction.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        BookController controller = new BookController();
        // inject mock into private field
        Field f = BookController.class.getDeclaredField("bookService");
        f.setAccessible(true);
        f.set(controller, bookService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createBook_returnsCreated() throws Exception {
        CreateBookRequest req = new CreateBookRequest("Clean Code", "Robert C. Martin", 2008, 5);
        BookResponse resp = new BookResponse(1L, "Clean Code", "Robert C. Martin", 2008, 5);
        DataResponse<BookResponse> dataResponse = new DataResponse<>("Created", LocalDateTime.now(), 201, null, null, resp);

        given(bookService.createBook(any(CreateBookRequest.class))).willReturn(dataResponse);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.bookId").value(1))
            .andExpect(jsonPath("$.data.title").value("Clean Code"))
            .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    void getAllBooks_returnsList() throws Exception {
        BookResponse book = new BookResponse(2L, "Refactoring", "Martin Fowler", 1999, 3);
        PageDataResponse<BookResponse> pageData = new PageDataResponse<>(1, 10, 1, 1, List.of(book));
        DatatableResponse<BookResponse> datatableResponse = new DatatableResponse<>("OK", LocalDateTime.now(), 200, null, null, pageData);

        given(bookService.getAllBooks(1, 10)).willReturn(datatableResponse);

        mockMvc.perform(get("/api/books?page=1&limit=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.list[0].bookId").value(2))
            .andExpect(jsonPath("$.data.list[0].title").value("Refactoring"))
            .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void getBookById_returnsBook() throws Exception {
        BookResponse resp = new BookResponse(3L, "Domain-Driven Design", "Eric Evans", 2003, 2);
        DataResponse<BookResponse> dataResponse = new DataResponse<>("OK", LocalDateTime.now(), 200, null, null, resp);

        given(bookService.getBookById(3L)).willReturn(dataResponse);

        mockMvc.perform(get("/api/books/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.bookId").value(3))
            .andExpect(jsonPath("$.data.title").value("Domain-Driven Design"));
    }

    @Test
    void updateBookStock_updatesAndReturns() throws Exception {
        BookResponse resp = new BookResponse(4L, "The Pragmatic Programmer", "Andrew Hunt", 1999, 8);
        DataResponse<BookResponse> dataResponse = new DataResponse<>("Updated", LocalDateTime.now(), 200, null, null, resp);

        given(bookService.updateBookStock(eq(4L), eq(2))).willReturn(dataResponse);

        mockMvc.perform(put("/api/books/4/stock?additionalStock=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.bookId").value(4))
            .andExpect(jsonPath("$.data.title").value("The Pragmatic Programmer"))
            .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void deleteBookById_returnsOk() throws Exception {
        BookResponse resp = new BookResponse(5L, "Test-Driven Development", "Kent Beck", 2002, 0);
        DataResponse<BookResponse> dataResponse = new DataResponse<>("Deleted", LocalDateTime.now(), 200, null, null, resp);

        given(bookService.deleteBookById(5L)).willReturn(dataResponse);

        mockMvc.perform(delete("/api/books/5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.bookId").value(5))
            .andExpect(jsonPath("$.data.title").value("Test-Driven Development"));
    }
}
