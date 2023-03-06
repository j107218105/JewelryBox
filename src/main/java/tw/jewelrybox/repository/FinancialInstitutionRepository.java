package tw.jewelrybox.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.*;
import tw.jewelrybox.entity.FinancialInstitution;

/**
 * 金融機構
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Repository
public interface FinancialInstitutionRepository extends JpaRepository<FinancialInstitution, Short> {

	@Query(name = "FinancialInstitution.findByShownTrueOrderByCode")
	public Collection<FinancialInstitution> findByShownTrueOrderByCode();
}
