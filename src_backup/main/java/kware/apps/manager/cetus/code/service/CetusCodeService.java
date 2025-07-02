package kware.apps.manager.cetus.code.service;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.code.domain.CetusCode;
import kware.apps.manager.cetus.code.domain.CetusCodeDao;
import kware.apps.manager.cetus.code.dto.request.*;
import kware.apps.manager.cetus.code.dto.response.CodePage;
import kware.apps.manager.cetus.code.dto.response.CodeView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CetusCodeService {

	private final CetusCodeDao dao;

	@Transactional
	public int addCode(CodeSave request) {
		return dao.insert(new CetusCode(request));
	}

	@Transactional
	public void merge(CodeForm formRequest) {
		Long parentUid = formRequest.getParent().getUid();
		String parentCode = formRequest.getCode();

		// 1. update parent code
		CetusCode myView = dao.findByUid(parentUid).orElseThrow(NoSuchElementException::new);
		dao.update(myView.updateParentCode(parentUid, formRequest.getParent()));

		// 2. update child code
		for(CodeChildForm form : formRequest.getAddList()) {
			dao.insert(new CetusCode(form, parentCode));
		}

		// 3. insert child code
		for(CodeChildForm form : formRequest.getUpdtList()) {
			Long uid = form.getUid();
			CetusCode childView = dao.findByUid(uid).orElseThrow(NoSuchElementException::new);
			dao.update(childView.updateChild(uid, form));
		}
	}

	@Transactional(readOnly = true)
	public Page<CodePage> codePage(CodePageSearch request, Pageable pageable) {
		return dao.findPagingAll(request, pageable);
	}

	@Transactional(readOnly = true)
	public List<CodeView> getCodesByParentCode(String upperCode) {
		return dao.findByUpperCode(upperCode);
	}

	@Transactional(readOnly = true)
	public List<CodeView> getChildCodes(String code, String useAt) {
		return dao.findByCodeAndUseAt(code, useAt);
	}

	@Transactional(readOnly = true)
	public CetusCode code(Long uid) {
		return dao.findByUid(uid).orElseThrow(NoSuchElementException::new);
	}

	@Transactional(readOnly = true)
	public int isExistCode(String code) {
		return dao.isExistByCode(code).orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	public void changeCodeUseAt(CodeChangeUseAt request) {
		dao.updateCodeUseAt(new CetusCode(request));
	}
}
