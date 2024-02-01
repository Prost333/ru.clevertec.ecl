package ru.clevertec.ecl.controller;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.ecl.dto.HouseDto.HouseReq;
import ru.clevertec.ecl.dto.HouseDto.HouseRes;
import ru.clevertec.ecl.dto.ResponsePage;
import ru.clevertec.ecl.service.HouseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HouseService houseService;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");


    @BeforeClass
    public static void setUpClass() {
        postgreSQLContainer.start();
    }

    @AfterClass
    public static void tearDownClass() {
        postgreSQLContainer.stop();
    }



    @Test
    public void findByIdTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        HouseRes houseRes = new HouseRes();
        houseRes.setCity("Test House");
        when(houseService.findByUUID(uuid)).thenReturn(houseRes);

        mockMvc.perform(get("/house/" + uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(houseRes)));
    }
    @Test
    public void saveTest() throws Exception {
        HouseReq houseReq = new HouseReq();
        HouseRes houseRes = new HouseRes();
        when(houseService.save(houseReq)).thenReturn(houseRes);

        mockMvc.perform(post("/house")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(houseReq)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(houseRes)));
    }

    @Test
    public void findAllTest() throws Exception {
        int page = 0;
        int pageSize = 10;
        Page<HouseRes> housePage = new PageImpl<>(Collections.emptyList());
        when(houseService.findAll(page, pageSize)).thenReturn(housePage);

        mockMvc.perform(get("/house")
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(new ResponsePage<>(housePage))));
    }

    @Test
    public void deleteTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        doNothing().when(houseService).delete(uuid);

        mockMvc.perform(delete("/house/" + uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        HouseReq houseReq = new HouseReq();
        HouseRes houseRes = new HouseRes();
        when(houseService.update(uuid, houseReq)).thenReturn(houseRes);

        mockMvc.perform(put("/house/" + uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(houseReq)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(houseRes)));
    }


    @Test
    public void findHousesWhichOwnPersonTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        List<HouseRes> houses = new ArrayList<>();
        when(houseService.findOwners(uuid)).thenReturn(houses);

        mockMvc.perform(get("/house/lives/" + uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(houses)));
    }

    @Test
    public void findAbsolutTextTest() throws Exception {
        String text = "test";
        List<HouseRes> houses = new ArrayList<>();
        when(houseService.findAbsolutText(text)).thenReturn(houses);

        mockMvc.perform(get("/house/search/" + text)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(new ObjectMapper().writeValueAsString(houses)));
    }

    @Test
    public void buyHouseTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        doNothing().when(houseService).buyHouse(uuid, personId);

        mockMvc.perform(put("/house/Buy/" + uuid.toString() + "/" + personId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}