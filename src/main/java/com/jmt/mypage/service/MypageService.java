package com.jmt.mypage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmt.mypage.dao.MypageDAO;

@Service
public class MypageService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired  MypageDAO dao;

}
