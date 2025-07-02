package kware.apps.manager.cetus.workplace.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

@Component
public class CetusWorkplaceDao extends SuperDao<CetusWorkplace> {

    public CetusWorkplaceDao() {
        super("cetusWorkplace");
    }
}
