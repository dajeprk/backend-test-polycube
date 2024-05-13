package kr.co.polycube.backendtest.Controller;

import kr.co.polycube.backendtest.Domain.Lotto;
import kr.co.polycube.backendtest.Service.LottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lottos")
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @PostMapping
    public ResponseEntity<Map<String, List<Integer>>> generateLotto() {
        Lotto lotto = lottoService.generateLotto();
        Map<String, List<Integer>> response = new HashMap<>();
        response.put("numbers", lotto.getNumbers());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
