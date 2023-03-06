package tw.jewelrybox.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import tw.jewelrybox.entity.District;
import tw.jewelrybox.entity.Occupation;
import tw.jewelrybox.entity.Someone;
import tw.jewelrybox.entity.Someone_;

/**
 * 「會員」規格
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public class SomeoneSpecs {

	/**
	 * @param pattern 部份或全部的會員全名
	 * @return 會員
	 */
	public static Specification<Someone> nameLike(final String pattern) {
		return new Specification<Someone>() {
			@Override
			public Predicate toPredicate(Root<Someone> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.like(root.<String>get(Someone_.name), "%".concat(pattern).concat("%"));
			}
		};
	}

	/**
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupation 職業需求
	 * @param district 行政區需求
	 * @return 搜尋結果
	 */
	public static Specification<Someone> guesstimate(final Date birthday, final Boolean gender, final Occupation occupation, final District district) {
		return new Specification<Someone>() {
			@Override
			public Predicate toPredicate(Root<Someone> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.<Boolean>get(Someone_.denied), false));
				if (birthday != null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get(Someone_.birthday), birthday));
				}
				if (gender != null) {
					predicates.add(criteriaBuilder.equal(root.<Boolean>get(Someone_.gender), gender));
				}
				if (occupation != null) {
					predicates.add(criteriaBuilder.equal(root.<Occupation>get(Someone_.occupation), occupation));
				}
				if (district != null) {
					predicates.add(criteriaBuilder.equal(root.<District>get(Someone_.district), district));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
