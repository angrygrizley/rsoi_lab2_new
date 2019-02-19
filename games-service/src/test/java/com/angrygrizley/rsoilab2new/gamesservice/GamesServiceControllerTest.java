package com.angrygrizley.rsoilab2new.gamesservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GamesServiceControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GamesServiceController gamesServiceController;

    @MockBean
    GamesService gamesService;

    @MockBean
    GamesRepository gamesRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        gamesService = new GamesServiceImplementation(gamesRepository);
        gamesServiceController = new GamesServiceController(gamesService);
        mvc = MockMvcBuilders.standaloneSetup(gamesServiceController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addGame() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        given(gamesService.addGame(game)).willReturn(game);

        String json = mapper.writeValueAsString(game);
        mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk())
       ;// .andExpect((ResultMatcher) jsonPath("title", is(game.getTitle())));
    }


    @Test
    public void getGames() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        List<Game> lg = new ArrayList<>();
        lg.add(game);

        Page<Game> pg = new PageImpl<>(lg);

        PageRequest p = PageRequest.of(1,5);
        given(gamesService.getGames(p)).willReturn(pg);

        mvc.perform(get("/games"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void getGameById() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        given(gamesRepository.findById(1L)).willReturn(Optional.of(game));
       // given(gamesService.getGameById(1L)).willReturn(game);

        mvc.perform(get("/games/id/1"))

                .andExpect(status().isOk())
        ;
    }

    @Test
    public void putGame() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        given(gamesRepository.findById(1L)).willReturn(Optional.of(game));
        given(gamesRepository.save(game)).willReturn(game);
        //given(gamesService.putGame(game)).willReturn(game);

        String json = mapper.writeValueAsString(game);
        mvc.perform(put("/games/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk())
        ;
    }

    @Test
    public void searchGames() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);
        List<Game> lg = new ArrayList<>();
        lg.add(game);

        given(gamesRepository.findAllByPlNumBetweenMinNumAnAndMaxNum(3)).willReturn(lg);
        given(gamesRepository.findAllByGenre("strategy")).willReturn(lg);
        given(gamesRepository.findAllByPlNumBetweenMinNumAnAndMaxNumAndGenre("strategy", 3)).willReturn(lg);

        String json = mapper.writeValueAsString(game);
        mvc.perform(get("/games/search")
                .param("genre", "strategy")
                .param("playernum", "3"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void deleteGame() throws Exception {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);
        List<Game> lg = new ArrayList<>();
        lg.add(game);

        String json = mapper.writeValueAsString(game);
        mvc.perform(delete("/games/delete/1"))

                .andExpect(status().isOk())
        ;
    }
}