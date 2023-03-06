package tw.jewelrybox.repository;

import java.util.Collection;
import tw.jewelrybox.entity.Accordion;

/**
 * 自訂「手風琴」庫。
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public interface AccordionRepositoryCustom {

	/**
	 * @return 排序過後的手風琴。
	 */
	public Collection<Accordion> findAllOrderBySort();
}
