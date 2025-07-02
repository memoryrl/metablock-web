package kware.apps.manager.cetus.menu.domain;

import cetus.annotation.Key;
import cetus.bean.AuditBean;
import kware.apps.manager.cetus.enumstatus.UserAuthorCd;
import kware.apps.manager.cetus.menu.dto.request.MenuChange;
import kware.apps.manager.cetus.menu.dto.request.MenuSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CetusMenuInfo extends AuditBean {

	@Key
	private Long menuNo;
	private Long programUid;
	private Long upperMenuNo;
	private String menuNm;
	private String menuIcon;
	private Integer sortNo;
	private String menuDc;
	private String useAt;
	private String deleteAt;

	private String authorCd;
	private String menuStyle;
	private String menuStyle1;
	private String menuStyle2;
	private String rootMenuCd;
	private Long workplaceUid;

	public CetusMenuInfo(MenuSave request, Long workplaceUid) {
		this.upperMenuNo = request.getUpperMenuNo();
		this.sortNo = request.getSortNo();
		this.menuNm = request.getMenuNm();
		this.authorCd = request.getAuthorCd();
		this.workplaceUid = workplaceUid;
	}

	public CetusMenuInfo changeMenu(Long menuNo, MenuChange request) {
		this.menuNo = menuNo;
		this.programUid = (request.getProgramUid() != null) ? request.getProgramUid() : this.programUid;
		this.menuNm = (request.getMenuNm() != null) ? request.getMenuNm() : this.menuNm;
		this.menuIcon = (request.getMenuIcon() != null) ? request.getMenuIcon() : this.menuIcon;
		this.sortNo = (request.getSortNo() != null) ? request.getSortNo() : this.sortNo;
		this.menuDc = (request.getMenuDc() != null) ? request.getMenuDc() : this.menuDc;
		this.useAt = (request.getUseAt() != null) ? request.getUseAt() : this.useAt;
		this.menuStyle = (request.getMenuStyle() != null) ? request.getMenuStyle() : this.menuStyle;
		this.menuStyle1 = (request.getMenuStyle1() != null) ? request.getMenuStyle1() : this.menuStyle1;
		this.menuStyle2 = (request.getMenuStyle2() != null) ? request.getMenuStyle2() : this.menuStyle2;
		return this;
	}

}
