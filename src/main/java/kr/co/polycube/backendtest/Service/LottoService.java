package kr.co.polycube.backendtest.Service;

import jakarta.annotation.PostConstruct;
import kr.co.polycube.backendtest.Domain.Lotto;
import kr.co.polycube.backendtest.Domain.Winner;
import kr.co.polycube.backendtest.Repository.LottoRepository;
import kr.co.polycube.backendtest.Repository.WinnerRepository;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LottoService {

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    // Maybe creating new Random() everytime messes with the probability
    private static final Random random = new Random();

    public LottoService(
        LottoRepository lottoRepository,
        WinnerRepository winnerRepository
    ) {
        this.lottoRepository = lottoRepository;
        this.winnerRepository = winnerRepository;
    }

    @Getter
    private int[] winningNumbers;

    @PostConstruct
    public void init() {
        this.winningNumbers = generateWinningLotto();
    }

    public int[] generateWinningLotto() {
        return random
                .ints(1, 50)
                .distinct()
                .limit(6)
                .toArray();
    }

    @Bean
    public ItemReader<Lotto> lottoItemReader() {
        List<Lotto> lottos = lottoRepository.findAll();

        // 더 효율적인 방법이 있을테나 모르겠습니다.
        Map<Lotto, Integer> lottoMatchedCounts = new HashMap<>();
        for(Lotto lotto : lottos) {
            int matchedCount = (int) lotto.getNumbers().stream()
                    .filter(num -> Arrays.stream(winningNumbers).anyMatch(tmp -> tmp == num))
                    .count();
            lottoMatchedCounts.put(lotto, matchedCount);
        }

        List<Lotto> entries = lottoMatchedCounts.entrySet().stream()
                        .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        return new ListItemReader<>(entries);
    }

    @Bean
    public ItemProcessor<Lotto, Winner> lottoItemProcessor() {
        return new ItemProcessor<Lotto, Winner>() {
            private int rank = 1;
            private int prevCount = Integer.MAX_VALUE;

            @Override
            public Winner process(Lotto item) {
                int matchCount = calculateMatchCount(item);

                if(matchCount < prevCount) {
                    rank++;
                }

                if(rank > 6 || matchCount == 0) {
                    return null;
                }

                Winner winner = new Winner();
                winner.setLotto_id(item.getId());
                winner.setRank(rank);
                prevCount = matchCount;

                return winner;
            }

            private int calculateMatchCount(Lotto lotto) {
                // holy the code is going spaghetti mode
                return (int) lotto.getNumbers().stream()
                        .filter(num -> Arrays.stream(winningNumbers).anyMatch(win -> win == num))
                        .count();
            }
        };
    }

    @Bean
    public ItemWriter<Winner> winnerItemWriter() {
        return winners -> {
            for(Winner winner : winners) {
                if(winner != null) {
                    winnerRepository.save(winner);
                }
            }
        };
    }

    public Lotto generateLotto() {
        // 발행된 로또 티켓중 증복을 허용합니다
        // 실제 로또에서 증복 및 공동 우승자가 발생하는것을 따릅니다
        List<Integer> lottoNumbers = random
                .ints(1, 50)
                .distinct()
                .limit(6)
                .boxed()
                .toList();

        Lotto lotto = new Lotto();
        lotto.setNumbers(lottoNumbers);
        return lottoRepository.save(lotto);
    }

    /*public void processWinners() {
        List<Lotto> lottos = lottoRepository.findAll();

        int[] winningNumbers = random
                .ints(1, 50)
                .distinct()
                .limit(6)
                .toArray();

        // 더 효율적인 방법이 있을테나 모르겠습니다.
        Map<Lotto, Integer> lottoMatchedCounts = new HashMap<>();
        for(Lotto lotto : lottos) {
            int matchedCount = (int) lotto.getNumbers().stream()
                    .filter(num -> Arrays.stream(winningNumbers).anyMatch(tmp -> tmp == num))
                    .count();
            lottoMatchedCounts.put(lotto, matchedCount);
        }

        List<Map.Entry<Lotto, Integer>> entries = new ArrayList<>(lottoMatchedCounts.entrySet());
        entries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int rank = 1;
        int prevCount = Integer.MAX_VALUE;

        // 공동 우승자가 있다고 가정을 합니다
        for(Map.Entry<Lotto, Integer> entry : entries) {
            if(entry.getValue() < prevCount) { rank++; }
            if(rank > 6) { break; }
            if(entry.getValue() == 0) { break; }
            Winner winner = new Winner();
            winner.setLotto_id(entry.getKey().getId());
            winner.setRank(rank);
            prevCount = entry.getValue();
            winnerRepository.save(winner);
        }
    }*/
}


