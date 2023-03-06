package tw.jewelrybox.repository;

import java.util.Collection;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.*;

/**
 * 途徑
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface MappingRepository extends JpaRepository<Mapping, Short> {

	public long countByAccordionAndSort(@Param("accordion") Accordion accordion, @Param("sort") short sort);

	public long countByAccordionAndSortAndIdNot(@Param("accordion") Accordion accordion, @Param("sort") short sort, @Param("id") short id);

	/**
	 * @param pageable 可分頁
	 * @return 除了「後臺的首頁」以外的所有途徑。
	 */
	public Page<Mapping> findByAccordionIsNotNull(Pageable pageable);

	public Mapping findOneByAndUriAndMethodAndAccordionIsNull(String uri, Method method);

	public Mapping findOneByAccordionIsNotNullAndUriAndMethod(String uri, Method method);

	/**
	 * @param method 方式
	 * @param pattern 規則
	 * @return 途徑
	 */
	public Mapping findOneByMethodAndPattern(Method method, String pattern);

	@Query(name = "Mapping.findByAccordionOrderBySort")
	public Page<Mapping> findByAccordionOrderBySort(@Param("accordion") Accordion accordion, Pageable pageable);

	/**
	 * @param accordion 手風琴
	 * @return 途徑
	 */
	@Query(name = "Mapping.findByAccordionAndUriNotNullOrderBySort")
	public Collection<Mapping> findByAccordionAndUriNotNullOrderBySort(@Param("accordion") Accordion accordion);

	public Collection<Mapping> findByMethod(@Param("method") Method method);

	public Collection<Mapping> findByAccordionIsNotNullAndMethod(Method method);
}
