package academy.devdojo.springboot.repository;

import academy.devdojo.springboot.domain.Book;
import academy.devdojo.springboot.domain.BookUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookUsersRepository extends JpaRepository<BookUsers, Long> {

    BookUsers findByUsername(String name);
}
