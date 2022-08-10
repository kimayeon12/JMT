package com.jmt.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.jmt.admin.dao.ReportDAO;
import com.jmt.admin.dto.ReportDTO;
import com.jmt.admin.dto.ReportPostDto;

@Service
public class ReportService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired ReportDAO dao;

	public HashMap<String, Object> reportList(HashMap<String, String> params) {
		
		
	HashMap<String, Object> map = new HashMap<String, Object>(); 
		
		int cnt =Integer.parseInt(params.get("cnt"));  
		int page =Integer.parseInt(params.get("page"));
		String keyword = params.get("keyword");
		String report_status = params.get("report_status");
		String report_sort = params.get("report_sort");
		
		Map<String, Object> data = new HashMap<String, Object>(); 
		data.put("keyword", keyword);
		data.put("report_status", report_status);
		data.put("report_sort", report_sort);
		
		logger.info("보여줄 페이지:"+page);
		
		// 총 게시글 갯수 (allCnt)/ 페이지당 보여줄 갯수 (cnt) = 생성 가능한 페이지 (pages).
		int allCnt = dao.allCount(data);
		logger.info("총 allCnt:"+allCnt); // 총 페이지 수 
		int pages = allCnt%cnt>0 ? (allCnt/cnt)+1 :(allCnt/cnt);  // 현재 만들 수 있는 최대 페이지 수 
		if(pages==0) {pages=1;}
		if(page>pages) {  // 페이지 바뀌는 경우 . 현재 페이지 가 
			page=pages;
		}
		logger.info("pages"+pages);
		
		map.put("pages", pages);// 만들 수 있는 최대 페이지 수 
		map.put("currPage",page); // 현재 페이지
		/* currPage>pages? : page */
		int offset =(page-1)*cnt;
		logger.info("offset:"+offset);
	//	ArrayList<HashMap<String, Object>> list =dao.reportList(cnt,offset);
		data.put("cnt", cnt);
		data.put("offset", offset);
	ArrayList<ReportDTO> list =dao.reportList(data);
	
	
		//ArrayList<HashMap<String, Object>> list = dao.reportList();
		map.put("list", list);
		return map;
	}

	public ReportDTO reportDetail(int report_no) {
		return dao.reportDetail(report_no);
	}

	public int reportUpdate(Map<String, Object> data) {
		return dao.reportUpdate(data);
	}
	
	// 상세보기 내 신고 게시글 
	public ReportPostDto reportPost(Map<String, Object> data) {
		int class_no=(int) data.get("class_no");
		int idx = (int) data.get("idx");
		logger.info("class_no 정보 {} , idx 정보 {}",class_no,idx);
		ReportPostDto reportPost = null; 
		
		if(class_no==1) {
			 reportPost= dao.select_no1(idx);
		}
		
		else if(class_no==2) {
			 reportPost= dao.select_no2(idx);
		}
		
		else if(class_no==3) {
			 reportPost= dao.select_no3(idx);
		}
		
		else if(class_no==4) {
			reportPost= dao.select_no4(idx);
		}
		
		else if(class_no==5) { // 도장깨기 댓글
			reportPost= dao.select_no2(idx);
		}
		
		else if(class_no==6) {// 모임 후기 
			 reportPost= dao.select_no6(idx);
		}
		
		else if(class_no==7) { // 모임후기 댓글 
			reportPost= dao.select_no2(idx);
		}
		
		else if(class_no==8) { // 맛집리뷰 댓글 
			reportPost= dao.select_no2(idx);
		}
		
		return reportPost;
	}

	public int blind(ArrayList<String> blindPost) {
		int i=0; 
		for (String idx : blindPost) {
			i += dao.blind(idx);
		}
		
		return i;
	}
	
	// 블라인드 리스트 .. 
	public Map<String, Object> blindList() {
		Map<String, Object> map = new HashMap<String, Object>(); 
		//리스트 뿌려주기 . 
		ArrayList<ReportDTO> blindList= dao.blindList(); 

		map.put("blindList", blindList);
		
		return map;
	}

	

}
