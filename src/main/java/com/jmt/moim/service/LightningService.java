package com.jmt.moim.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmt.moim.dao.LightningDAO;
import com.jmt.moim.dto.LightningDTO;

@Service
public class LightningService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired  LightningDAO dao;

	public ArrayList<LightningDTO> foodList() {
		logger.info("음식카테고리 가져오기 서비스");
		return dao.foodList();
	}
	
	/*
	public ArrayList<LightningDTO> list() {
		logger.info("전체리스트 서비스 요청");
		return dao.list();
	}
	*/

	public ArrayList<LightningDTO> selectedList(HashMap<String, Object> selectedparams) {
		logger.info("검색필터 옵션 리스트 요청");
		return dao.selectedList(selectedparams);
	}

	public int allCount(HashMap<String, Object> selectedparams) {
		logger.info("총개수 서비스 요청");
		return dao.allCount(selectedparams);
	}

	
	public void changeStatus() {
		dao.changeStatus();
		logger.info("업데이트완료");
	}


	public LightningDTO detail(String lightning_no, String loginId) {
		logger.info("상세보기 서비스 요청");
		return dao.detail(lightning_no,loginId);
	}


	public int register(String loginId,String lightning_no) {
		logger.info("번개 모임 신청 서비스");
		return dao.register(loginId,lightning_no);
	}
	
}
