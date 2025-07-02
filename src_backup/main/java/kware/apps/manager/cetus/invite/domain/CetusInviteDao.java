package kware.apps.manager.cetus.invite.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

@Component
public class CetusInviteDao extends SuperDao<CetusInvite> {
    public CetusInviteDao() {
        super("cetusInvite");
    }

    public int insertInvite(CetusInvite invite) {
        return insert("insertInvite", invite);
    }

    public CetusInvite getInviteByToken(String token) {
        return selectOne("getInviteByToken", token);
    }

    public int activateInvite(String token) {
        return update("activateInvite", token);
    }

    public CetusInvite getLatestInviteByEmail(String email) {
        return selectOne("getLatestInviteByEmail", email);
    }

}
