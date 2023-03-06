package tw.jewelrybox.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.Accordion;

/**
 * 「手風琴」庫。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface AccordionRepository extends JpaRepository<Accordion, Short>/*, AccordionRepositoryCustom*/ {

	public long countByName(@Param("name") String name);

	public long countByNameAndIdNot(@Param("name") String name, @Param("id") Short id);

	public long countBySortAndIdNot(@Param("sort") short sort, @Param("id") Short id);

	public long countBySort(@Param("sort") short sort);

	/**
	 * @return 升冪排序過的手風琴。
	 */
	@Query(name = "Accordion.findAllOrderBySort")
	public Collection<Accordion> findAllOrderBySort();
}
