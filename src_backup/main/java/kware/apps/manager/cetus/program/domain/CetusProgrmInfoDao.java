package kware.apps.manager.cetus.program.domain;

import cetus.dao.SuperDao;
import kware.apps.manager.cetus.program.dto.request.ProgrmFullInfoSearch;
import kware.apps.manager.cetus.program.dto.response.ProgrmFullInfo;
import org.springframework.stereotype.Component;

@Component
public class CetusProgrmInfoDao extends SuperDao<CetusProgrmInfo> {

    public CetusProgrmInfoDao() {
        super("cetusProgrmInfo");
    }

    public CetusProgrmInfo getProgramByUrl(ProgrmFullInfoSearch fullInfoSearch) {
        return selectOne("getProgramByUrl", fullInfoSearch);
    }

    public ProgrmFullInfo getProgramFullInfoByUrl(ProgrmFullInfoSearch fullInfoSearch) {
        return selectOne("getProgramFullInfoByUrl", fullInfoSearch);
    }
}
