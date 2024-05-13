package kr.co.polycube.backendtest.Repository;

import kr.co.polycube.backendtest.Domain.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnerRepository extends JpaRepository<Winner, String> {
}
