package tw.jewelrybox.specification;

import java.util.*;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import tw.jewelrybox.entity.*;

/**
 * 「活動」規格
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
public class ActivitySpecs {

	/**
	 * @param name 活動名稱
	 * @param headcount 人數
	 * @param score 價值點數
	 * @param since 開始日期
	 * @param until 結束日期
	 * @param age 年齡需求
	 * @param gender 性別需求
	 * @param occupation 職業需求
	 * @param district 行政區需求
	 * @return 搜尋結果
	 */
	public static Specification<Activity> anySpec(final String name, final Integer headcount, final Short score, final Date since, final Date until, final Short age, final Boolean gender, final Occupation occupation, final District district) {
		return new Specification<Activity>() {
			@Override
			public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (name != null) {
					predicates.add(criteriaBuilder.like(root.<String>get(Activity_.name), "%".concat(name).concat("%")));
				}
				if (headcount != null) {
					predicates.add(criteriaBuilder.equal(root.<Integer>get(Activity_.headcount), headcount));
				}
				if (score != null) {
					predicates.add(criteriaBuilder.equal(root.<Short>get(Activity_.score), score));
				}
				if (since != null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get(Activity_.since), since));
				}
				if (until != null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get(Activity_.until), until));
				}
				if (age != null) {
					predicates.add(criteriaBuilder.equal(root.<Short>get(Activity_.age), age));
				}
				if (gender != null) {
					predicates.add(criteriaBuilder.equal(root.<Boolean>get(Activity_.gender), gender));
				}
				if (occupation != null) {
					predicates.add(criteriaBuilder.equal(root.<Occupation>get(Activity_.occupation), occupation));
				}
				if (district != null) {
					predicates.add(criteriaBuilder.equal(root.<District>get(Activity_.district), district));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
}
