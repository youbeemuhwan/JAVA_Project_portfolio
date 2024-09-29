package Project.commercial.repository;


import Project.commercial.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {


}
