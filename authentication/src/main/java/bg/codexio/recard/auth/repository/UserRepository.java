package bg.codexio.recard.auth.repository;

import bg.codexio.recard.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    default <T> Optional<T> getIfFree(String username, T newUser) {
        return this.findByUsername(username)
                .or(() -> Optional.of(new User()))
                .filter(user -> user.getId() == null)
                .map(user -> newUser);
    }
}