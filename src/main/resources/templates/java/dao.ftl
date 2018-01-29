package ${project.packageName}.dao;
import com.cfgglobal.test.dao.base.BaseDao;
import com.cfgglobal.test.domain.${entity.name};
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entity.name}Dao extends BaseDao<${entity.name}, Long> {

}

