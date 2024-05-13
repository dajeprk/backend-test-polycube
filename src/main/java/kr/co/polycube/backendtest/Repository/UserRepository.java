package kr.co.polycube.backendtest.Repository;

import kr.co.polycube.backendtest.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
