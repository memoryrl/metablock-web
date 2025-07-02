package kware.apps.manager.cetus.code.domain;

import cetus.bean.AuditBean;
import kware.apps.manager.cetus.code.dto.request.CodeChangeUseAt;
import kware.apps.manager.cetus.code.dto.request.CodeChildForm;
import kware.apps.manager.cetus.code.dto.request.CodeParentForm;
import kware.apps.manager.cetus.code.dto.request.CodeSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusCode extends AuditBean {

	private Long uid;
	private String code;
	private String upperCode;
	private String codeNm;
	private String codeDc;
	private String useAt;
	private String rmDc;
	private String item1Val;
	private String item2Val;
	private Integer sortNo;

	public CetusCode(CodeSave request) {
		this.code = request.getCode();
		this.upperCode = request.getUpperCode();
		this.codeNm = request.getCodeNm();
		this.codeDc = request.getCodeDc();
		this.useAt = request.getUseAt();
		this.rmDc = request.getRmDc();
		this.item1Val = request.getItem1Val();
		this.item2Val = request.getItem2Val();
		this.sortNo = request.getSortNo() != null ? request.getSortNo() : 0;
	}

	public CetusCode(CodeChildForm child, String upperCode) {
		this.code = child.getCode();
		this.upperCode = upperCode;
		this.codeNm = child.getCodeNm();
		this.codeDc = child.getCodeDc();
		this.useAt = child.getUseAt();
		this.rmDc = child.getRmDc();
		this.item1Val = child.getItem1Val();
		this.item2Val = child.getItem2Val();
		this.sortNo = child.getSortNo() != null ? child.getSortNo() : 0;
	}

	public CetusCode updateChild(Long uid, CodeChildForm child) {
		this.uid = uid;
		this.upperCode = this.upperCode;
		this.codeNm = child.getCodeNm() != null ? child.getCodeNm() : this.codeNm;
		this.useAt = child.getUseAt() != null ? child.getUseAt() : this.useAt;

		this.codeDc = child.getCodeDc() != null ? child.getCodeDc() : null;
		this.rmDc = child.getRmDc() != null ? child.getRmDc() : null;
		this.item1Val = child.getItem1Val() != null ? child.getItem1Val() : null;
		this.item2Val = child.getItem2Val() != null ? child.getItem2Val() : null;
		this.sortNo = child.getSortNo() != null ? child.getSortNo() : null;
		return this;
	}

	public CetusCode updateParentCode(Long uid, CodeParentForm request) {
		this.uid = uid;
		this.codeNm = request.getCodeNm() != null ? request.getCodeNm() : this.codeNm;
		this.codeDc = request.getCodeDc() != null ? request.getCodeDc() : this.codeDc;
		this.useAt = request.getUseAt() != null ? request.getUseAt() : this.useAt;
		this.rmDc = request.getRmDc() != null ? request.getRmDc() : this.rmDc;
		this.item1Val = request.getItem1Val() != null ? request.getItem1Val() : this.item1Val;
		this.item2Val = request.getItem2Val() != null ? request.getItem2Val() : this.item2Val;
		this.sortNo = request.getSortNo() != null ? request.getSortNo() : this.sortNo;
		return this;
	}

	public void setExecStatusCode(String name ,String codeNm) {
		this.code = name;
		this.codeNm = codeNm;
	}

	public CetusCode(CodeChangeUseAt request) {
		this.uid = request.getUid();
		this.useAt = request.getUseAt();
	}
}
