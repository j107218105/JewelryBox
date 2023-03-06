package tw.com.egmanga.publishing;
/*
import java.util.*;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import tw.com.egmanga.publishing.entity.*;
import tw.com.egmanga.publishing.repository.*;
*/
/**
 * 服務
 *
 * @author	P-C Lin (a.k.a 高科技黑手)
 */
@org.springframework.stereotype.Service
public class Services {
/*
	@javax.persistence.PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private RequestURIRepository requestURIRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private org.springframework.orm.jpa.JpaTransactionManager transactionManager;

	public boolean isBanned(short employee, String uri, String method) {
		//撈出扮演的角色
		List<Role> roles = roleRepository.findByEmployee(employee);
		if (roles.isEmpty()) {
			return true;
		}

		//撈出對應的群組
		List<tw.com.egmanga.publishing.entity.Character> characters = new ArrayList<>();
		for (Role role : roles) {
			characters.add(role.getCharacter());
		}
		if (characters.isEmpty()) {
			return true;
		}

		//逐一比對路徑
		List<RequestURI> requestURIs = requestURIRepository.findByMethod(method);
		RequestURI requestURI = null;
		for (RequestURI rURI : requestURIs) {
			if (uri.matches(rURI.getPattern())) {
				requestURI = rURI;
				break;
			}
		}
		if (requestURI == null) {
			return true;
		}

		//逐一比對權限
		for (tw.com.egmanga.publishing.entity.Character character : characters) {
			if (privilegeRepository.findOneByCharacterAndRequestURI(character, requestURI) != null) {
				return false;
			}
		}

		return true;
	}

	public boolean signOn(HttpSession session, String number, String shadow) {
		if (whoAmI(session) == null) {
			try {
				Short me = tw.com.egmanga.publishing.Employee.findByNumberAndShadow(number, shadow);
				session.setAttribute("me", me);
			} catch (Exception ignore) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("null")
	public Short whoAmI(HttpSession session) {
		Short me;
		try {
			me = (Short) session.getAttribute("me");
			if (new Employee(me.shortValue()) == null) {
				me = null;
			}
		} catch (Exception exception) {
			me = null;
		}

		return me;
	}
*/
}
