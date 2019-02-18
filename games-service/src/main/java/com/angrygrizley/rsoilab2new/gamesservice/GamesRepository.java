package com.angrygrizley.rsoilab2new.gamesservice;

        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

        import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByGenre(String genre);

    @Query("select g from #{#entityName} g where g.minNum <= ?2 AND g.maxNum >= ?2 AND g.genre = ?1")
    List<Game> findAllByPlNumBetweenMinNumAnAndMaxNumAndGenre(String _genre, int pl_num);

    @Query("select g from #{#entityName} g where g.minNum <= ?1 AND g.maxNum >= ?1")
    List<Game> findAllByPlNumBetweenMinNumAnAndMaxNum(int pl_num);
}
