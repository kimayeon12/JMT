package com.jmt.mypage.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jmt.member.dto.MemberDTO;
import com.jmt.member.dto.photoDTO;
import com.jmt.mypage.dao.MypageDAO;

@Service
public class MypageService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired  MypageDAO dao;

	public MemberDTO mypage(String loginId) {
		MemberDTO params = dao.mypage(loginId);
		int food_no = params.getFood_no();
		int grade_no = params.getGrade_no();
		if(food_no == 1) {
			params.setFood_name("한식");
		}else if(food_no == 2) {
			params.setFood_name("중식");
		}else if(food_no == 3) {
			params.setFood_name("일식");
		}else if(food_no == 4) {
			params.setFood_name("양식");
		}else if(food_no == 5) {
			params.setFood_name("기타외국음식");
		}else if(food_no == 6) {
			params.setFood_name("디저트/까페");
		}
		
		if(grade_no == 1) {
			params.setGrade_name("나무수저");
		}else if(grade_no == 2) {
			params.setGrade_name("동수저");
		}else if(grade_no == 3) {
			params.setGrade_name("은수저");
		}else if(grade_no == 4) {
			params.setGrade_name("금수저");
		}else if(grade_no == 5) {
			params.setGrade_name("다이아수저");
		}
		
		return params;
	}

	public int blindCount(String loginId) {
		
		return dao.blindCount(loginId);
	}

	public int follower(String loginId) {
		
		return dao.follower(loginId);
	}

	public int following(String loginId) {
		
		return dao.following(loginId);
	}

	public int profile_no(String loginId) {
		
		return dao.profile_no(loginId);
	}
	
	public photoDTO photoList(int profile_no) {
		
		return dao.photoList(profile_no);
	}

	public ArrayList<MemberDTO> foodList() {
		
		return dao.foodList();
	}

	public String oriPass(HashMap<String, String> params) {
		
		return dao.oriPass(params);
	}

	public int passUpdate(HashMap<String, String> params) {
		
		return dao.passUpdate(params);
	}

	public void profileUpdate(Model model ,MultipartFile[] photos, HashMap<String, Object> params) {
		
		MemberDTO dto = new MemberDTO();
		dto.setFood_no(Integer.parseInt((String) params.get("food_no")));
		dto.setMember_id((String) params.get("loginId"));
		dto.setEat_speed((String) params.get("speed"));
		dto.setProfile_gender((String) params.get("gender"));
		dto.setProfile_job((String) params.get("job"));
		dto.setMember_name((String) params.get("name"));
		
		int profile_no = dao.profile_num(dto); //프로필 번호 가져오기
		logger.info("프로필 번호 : "+profile_no);
		
		//프로필 업데이트
		int profileUpdate = dao.profileUpdate(dto);
		//회원정보(이름) 업데이트
		int memberUpdate = dao.memberUpdate(dto);
		//사진 업데이트는 하든 안하든 무조건 기존 사진 삭제하고 다시 올리기
		int photoDel = dao.photoDel(profile_no); //기존 사진 삭제
		logger.info("사진삭제 : "+ photoDel);
		if(photoDel > 0) { //사진 삭제에 성공하면
			fileSave(photos, profile_no); //사진 다시등록
		}
		
		if(profileUpdate > 0 || memberUpdate >0 || photoDel > 0) {
			model.addAttribute("msg", "정보수정이 완료되었습니다.");
		}else {
			model.addAttribute("msg", "정보수정에 실패했습니다.");
		}
	}
	
	public void fileSave(MultipartFile[] photos, int profile_no) { //photo, profile 테이블 idx를 알아야함
		// 3. 파일 업로드
		for (MultipartFile photo : photos) {
			String oriFileName = photo.getOriginalFilename(); // 3-1. 파일명 추출
			if(!oriFileName.equals("")) {
				logger.info("업로드 진행");
				// 3-2. 확장자 분리
				String ext = oriFileName.substring(oriFileName.lastIndexOf(".")).toLowerCase();
				// 3-3. 새 이름 만들기
				String newFileName = System.currentTimeMillis()+ext;
				
				logger.info(oriFileName + "=>" + newFileName); 
				
				// 3-4. 파일 받아서 저장하기
				try {
					byte[] arr = photo.getBytes();
					Path path = Paths.get("C:/upload/"+newFileName);
					Files.write(path,arr);
					logger.info(newFileName+" - save ok");
					// 4. 업로드 후 photo 테이블에 데이터 입력
					dao.fileWrite(oriFileName, newFileName, profile_no);
					
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
	}


}
