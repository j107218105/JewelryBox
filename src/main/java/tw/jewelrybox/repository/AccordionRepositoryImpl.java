package tw.jewelrybox.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import tw.jewelrybox.entity.Accordion;

/**
 * 實作「自訂候選者庫」。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public class AccordionRepositoryImpl implements AccordionRepositoryCustom {

	@Autowired
	EntityManager entityManager;

	@Override
	public Collection<Accordion> findAllOrderBySort() {
		return entityManager.createQuery("SELECT a FROM Accordion a ORDER BY a.sort", Accordion.class).getResultList();
	}
}
