package ${project.packageName}.service;
import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.${entity.name};
import com.cfgglobal.test.service.base.BaseService;
import org.springframework.stereotype.Service;



@Repository
public interface ${entity.name}Service extends BaseService<${entity.name}, Long> {

}
