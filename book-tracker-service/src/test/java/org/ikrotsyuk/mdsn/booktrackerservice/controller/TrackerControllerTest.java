package org.ikrotsyuk.mdsn.booktrackerservice.controller;

import org.ikrotsyuk.mdsn.booktrackerservice.controllers.implementation.TrackerController;
import org.ikrotsyuk.mdsn.booktrackerservice.dto.AvailableBookDTO;
import org.ikrotsyuk.mdsn.booktrackerservice.service.implementation.TrackerService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TrackerControllerTest {
    @InjectMocks
    private TrackerController trackerController;
    @Mock
    private TrackerService trackerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(trackerController).build();
    }

    @Test
    void getAvailableBooks() throws Exception {
        AvailableBookDTO availableBookDTO1 = new AvailableBookDTO(1, true, null, null);
        AvailableBookDTO availableBookDTO2 = new AvailableBookDTO(2, true, null, null);
        when(trackerService.getAvailableBooks()).thenReturn(List.of(availableBookDTO1, availableBookDTO2));
        mockMvc.perform(get("/book-tracker/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(trackerService, times(1)).getAvailableBooks();
    }

    @Test
    void takeBook() throws Exception {
        LocalDateTime returnBy = LocalDateTime.now().plusDays(1);
        AvailableBookDTO availableBookDTO = new AvailableBookDTO(1, false, LocalDateTime.now(), returnBy);
        String returnByStr = returnBy.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        when(trackerService.takeBook(1, returnByStr)).thenReturn(availableBookDTO);
        mockMvc.perform(patch("/book-tracker/take/{id}", 1)
                        .param("returnBy", returnByStr)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(trackerService, times(1)).takeBook(anyInt(), eq(returnByStr));
    }

    @Test
    void returnBook() throws Exception {
        AvailableBookDTO availableBookDTO = new AvailableBookDTO(1, true, null, null);
        when(trackerService.returnBook(anyInt())).thenReturn(availableBookDTO);
        mockMvc.perform(patch("/book-tracker/return{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(trackerService, times(1)).returnBook(anyInt());
    }
}
