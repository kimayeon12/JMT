package com.jmt.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jmt.member.dao.MemberDAO;

@Service
public class MemberService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired MemberDAO dao;

}
