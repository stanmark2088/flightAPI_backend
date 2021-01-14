package com.codecool.flight_api_project.airline;//package com.codecool.flight_api_project.airline;
//
//import com.codecool.flight_api_project.airline.Airline;
//import com.codecool.flight_api_project.airline.AirlineRepository;
//import com.codecool.flight_api_project.airline.AirlineService;
//import com.codecool.flight_api_project.airport.Airport;
//import com.codecool.flight_api_project.city.City;
//import com.codecool.flight_api_project.flight.Flight;
//import com.codecool.flight_api_project.flight.FlightRepository;
//import com.codecool.flight_api_project.flight.FlightService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
////import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//public class AirlineTests {
//
//    @Autowired
//    private AirlineService airlineService;
//    @MockBean
//    private AirlineRepository airlineRepository;
//
//    @Autowired
//    private FlightService flightService;
//    @MockBean
//    private FlightRepository flightRepository;
//
//
//    @Test
//    void getAllAirlinesTest() {
//        when(airlineRepository.findAll()).thenReturn(
//                Stream
//                        .of(new Airline(10000L, "Tarom", "TRO"),
//                                new Airline(10001L, "Wizz", "WIZZ"),
//                                new Airline(10002L, "BlueAir", "BLA"))
//                        .collect(Collectors.toList()));
//        assertEquals(3, airlineService.getAllAirlines().size());
//    }
//
//    @Test
//    void getAirlineByIdTest(){
//        Airline airline = new Airline(10000L, "Tarom", "TRO");
//        Long airlineId = airline.getId();
//        when(airlineRepository.getOne(airlineId)).thenReturn(airline);
//        System.out.println(airlineRepository.getOne(airlineId));
//        assertEquals(airline, airlineService.getAirlineById(airlineId));
//    }
//
//    @Test
//    void addAirlineToDbTest(){
//        Airline airline = new Airline(123L,"British", "BRT");
//        when(airlineRepository.save(airline)).thenReturn(airline);
//        assertEquals(airline, airlineService.saveAirline(airline));
//    }
//
//    @Test
//    void deleteAirlineFromDbTest(){
//        Long airlineId = 10000L;
//        airlineService.deleteAirlineById(airlineId);
//        verify(airlineRepository, times(1)).deleteById(airlineId);
//    }
//
//    @Test
//    void getFlightsByParamsTest(){
//        City cityFrom = new City(12L, "Bucuresti");
//        City cityTo = new City(21L, "Londra");
//        Airport airportFrom = new Airport(1L,"Otopeni", "OTP",cityFrom);
//        Airport airportTo = new Airport(2L,"Heathrow", "HTW",cityTo);
//        Airline airline = new Airline(10000L, "Tarom", "TRO");
//
//        when(flightRepository.getFlightByParams(airportFrom.getAirportName(),
//                airportTo.getAirportName(), LocalDate.of(2021, 1,15))).thenReturn(
//                (Stream.of(new Flight(1L, LocalDate.of(2021, 1,15), 100L, LocalTime.of(9, 20),LocalTime.of(11, 20),airportFrom,airportTo,airline),
//                        new Flight(1L, LocalDate.of(2021, 1,15), 100L, LocalTime.of(10, 20),LocalTime.of(12, 20),airportFrom,airportTo,airline),
//                        new Flight(1L, LocalDate.of(2021, 1,15), 100L, LocalTime.of(10, 20),LocalTime.of(13, 20),airportFrom,airportTo,airline)
//                )).collect(Collectors.toList()));
//        System.out.println(flightService.getFlightByParams(airportFrom.getAirportName(),airportTo.getAirportName(), LocalDate.of(2021, 1,15)).size());
//        assertEquals(3,flightService.getFlightByParams(airportFrom.getAirportName(),airportTo.getAirportName(), LocalDate.of(2021, 1,15)).size());
//    }
//}
//



import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;


//@WebMvcTest(AirlineController.class)
@ExtendWith(SpringExtension.class)
//@WebMvcTest(value = AirlineController.class,  excludeAutoConfiguration = {ApplicationSecurityConfig.class})
//@ContextConfiguration(classes = FlightApiProjectApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AirlineControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirlineService airlineService;

    @Test
    @WithMockUser
    @DisplayName("Test all airlines found - GET /api/v1/airlines")
    public void testAllAirlinesFound() throws Exception {
        Airline tarom = new Airline(10000L, "Tarom", "TRO");
        Airline wizz = new Airline(10001L, "Wizz", "WIZZ");
        Airline blueAir = new Airline(10002L, "BlueAir", "BLA");

        List<Airline> airlines = new ArrayList<>();
        airlines.add(tarom);
        airlines.add(wizz);
        airlines.add(blueAir);

        doReturn(airlines).when(airlineService).getAllAirlines();

        mockMvc.perform(get("/api/v1/airlines"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("Tarom")))
                .andExpect(jsonPath("$[1].name", is("Wizz")))
                .andExpect(jsonPath("$[2].name", is("BlueAir")));

    }
}