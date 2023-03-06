package tw.jewelrybox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.jewelrybox.entity.Method;

/**
 * 方式
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@Repository
public interface MethodRepository extends JpaRepository<Method, Short> {

	public Method findOneByName(String name);
}
