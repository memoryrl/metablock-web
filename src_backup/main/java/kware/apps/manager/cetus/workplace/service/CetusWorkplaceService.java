package kware.apps.manager.cetus.workplace.service;


import kware.apps.manager.cetus.workplace.domain.CetusWorkplaceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CetusWorkplaceService {

    private final CetusWorkplaceDao dao;
}
