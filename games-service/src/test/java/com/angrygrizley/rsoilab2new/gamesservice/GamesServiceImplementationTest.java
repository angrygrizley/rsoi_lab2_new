package com.angrygrizley.rsoilab2new.gamesservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GamesServiceImplementationTest {

    @Mock
    GamesRepository gamesRepository;

    private GamesService gamesService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        gamesService = new GamesServiceImplementation(gamesRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addGame() {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        given(gamesRepository.save(game)).willReturn(game);

        Game g = gamesService.addGame(game);
        assertEquals(game, g);
    }

    @Test
    public void getGames() {
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

        given(gamesRepository.findAll(p)).willReturn(pg);


        Page<Game> pageg = gamesService.getGames(p);
        assertEquals(pageg, pg);
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

        Game g = gamesService.getGameById(1L);
        assertEquals(g, game);
    }

    @Test
    public void putGame() throws GameNotFoundException {
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
        Game g = gamesService.putGame(game);
        assertEquals(g, game);
    }

    @Test
    public void searchGames() {
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

        List<Game> list = gamesService.searchGames("strategy", 3);
        assertEquals(list.get(0), lg.get(0));
    }

    @Test
    public void deleteGame() {
        Game game = new Game();
        game.setGroupNum(0);
        game.setId(1L);
        game.setTitle("Carcasson");
        game.setGenre("strategy");
        game.setMinNum(2);
        game.setMaxNum(5);

        gamesService.addGame(game);
        gamesService.deleteGame(1L);
        assertEquals(gamesService.getGames(PageRequest.of(1,5)).isEmpty(), true);
    }
}