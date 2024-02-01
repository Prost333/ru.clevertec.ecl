package ru.clevertec.ecl.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.ecl.dto.personDto.PersonReq;
import ru.clevertec.ecl.dto.personDto.PersonRes;

import ru.clevertec.ecl.service.PersonService;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest()
@AutoConfigureMockMvc
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;

    public List<PersonRes> persons(){
        PersonRes person1 = new PersonRes();
        person1.setUuid(UUID.randomUUID());
        person1.setName("Jame");
        PersonRes person2 = new PersonRes();
        person2.setUuid(UUID.randomUUID());
        person2.setName("Male");
        List<PersonRes> persons = Arrays.asList(person1, person2);
       return persons;
    }

    @Test
    public void testGetAllPersons() throws Exception {
        Mockito.when(personService.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new PageImpl<>(persons()));

        mockMvc.perform(MockMvcRequestBuilders.get("/person/getAll")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)));

        Mockito.verify(personService, Mockito.times(1)).findAll(0, 10);
    }

    @Test
    public void testSavePerson() throws Exception {
        PersonReq personReq = new PersonReq();
        personReq.setName("John");
        personReq.setSurname("Doe");

        PersonRes personRes = new PersonRes();
        personRes.setUuid(UUID.randomUUID());
        personRes.setName("John");
        personRes.setSurname("Doe");

        Mockito.when(personService.save(Mockito.any(PersonReq.class))).thenReturn(personRes);

        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personReq)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("Doe")));

        Mockito.verify(personService, Mockito.times(1)).save(Mockito.any(PersonReq.class));
    }

    @Test
    public void testFindByUUId() throws Exception {
        UUID uuid = UUID.randomUUID();
        PersonRes personRes = new PersonRes();
        personRes.setUuid(uuid);
        personRes.setName("John");
        personRes.setSurname("Doe");

        Mockito.when(personService.findByUUID(uuid)).thenReturn(personRes);

        mockMvc.perform(MockMvcRequestBuilders.get("/person/" + uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.is(uuid.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("Doe")));

        Mockito.verify(personService, Mockito.times(1)).findByUUID(uuid);
    }
    @Test
    public void testFindByUUIdNotFound() throws Exception {
        UUID uuid = UUID.randomUUID();

        Mockito.when(personService.findByUUID(uuid)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/person/" + uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(personService, Mockito.times(1)).findByUUID(uuid);
    }

    @Test
    public void testDeletePerson() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/person/" + uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(personService, Mockito.times(1)).delete(uuid);
    }
    @Test
    public void testFindPersonWhoLivesInHouse() throws Exception {
        UUID houseId = UUID.randomUUID();
        PersonRes personRes = new PersonRes();
        personRes.setUuid(UUID.randomUUID());
        personRes.setName("John");
        personRes.setSurname("Doe");
        List<PersonRes> persons = List.of(personRes);

        Mockito.when(personService.findPersonWhoLivesInHouse(houseId)).thenReturn(persons);

        mockMvc.perform(MockMvcRequestBuilders.get("/person/livesInHouse/")
                        .param("houseId", houseId.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uuid", Matchers.is(personRes.getUuid().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", Matchers.is("Doe")));

        Mockito.verify(personService, Mockito.times(1)).findPersonWhoLivesInHouse(houseId);
    }

    @Test
    public void testUpdatePerson() throws Exception {
        UUID uuid = UUID.randomUUID();
        PersonReq personReq = new PersonReq();
        personReq.setName("John");
        personReq.setSurname("Doe");
        PersonRes personRes = new PersonRes();
        personRes.setUuid(uuid);
        personRes.setName("John");
        personRes.setSurname("Doe");

        Mockito.when(personService.update(uuid, personReq)).thenReturn(personRes);

        mockMvc.perform(MockMvcRequestBuilders.put("/person/" + uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(personReq)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid", Matchers.is(uuid.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname", Matchers.is("Doe")));

        ArgumentCaptor<PersonReq> personReqCaptor = ArgumentCaptor.forClass(PersonReq.class);
        Mockito.verify(personService, Mockito.times(1)).update(eq(uuid), personReqCaptor.capture());
        assertEquals(personReq, personReqCaptor.getValue());
    }

    @Test
    public void testFindAbsolutText() throws Exception {
        String text = "John";
        PersonRes personRes = new PersonRes();
        personRes.setUuid(UUID.randomUUID());
        personRes.setName("John");
        personRes.setSurname("Doe");
        List<PersonRes> persons = Arrays.asList(personRes);

        Mockito.when(personService.findAbsolutText(text)).thenReturn(persons);

        mockMvc.perform(MockMvcRequestBuilders.get("/person/search/" + text))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].uuid", Matchers.is(personRes.getUuid().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname", Matchers.is("Doe")));

        Mockito.verify(personService, Mockito.times(1)).findAbsolutText(text);
    }



}
