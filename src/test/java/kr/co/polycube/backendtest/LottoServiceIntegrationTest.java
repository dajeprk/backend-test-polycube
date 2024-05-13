package kr.co.polycube.backendtest;

import kr.co.polycube.backendtest.Domain.Lotto;
import kr.co.polycube.backendtest.Domain.Winner;
import kr.co.polycube.backendtest.Repository.LottoRepository;
import kr.co.polycube.backendtest.Repository.WinnerRepository;
import kr.co.polycube.backendtest.Service.LottoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootTest
@Import(LottoService.class)
public class LottoServiceIntegrationTest {
    @Autowired
    private LottoService lottoService;

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @BeforeEach
    public void setup() {
        lottoRepository.deleteAll();
        winnerRepository.deleteAll();
    }

    /*@Test
    public void testProcessWinners() {
        for(int i = 0; i < 100; i++) {
            lottoService.generateLotto();
        }

        lottoService.processWinners();

        List<Winner> winners = winnerRepository.findAll();

        for(Winner winner : winners) {
            System.out.println(winner.getLotto_id() +  " " + winner.getRank());
        }
    }*/
}
