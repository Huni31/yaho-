package com.kh.member.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.kh.common.JDBCTemplate.*;

import com.kh.member.model.dao.MemberDao;
import com.kh.member.model.vo.MemberVo;

public class MemberService {
	
	public int join(MemberVo m) {
		
		//DB Connection 가져오기
		Connection conn = getConnection();
		
		//쿼리 날리기 //insert
		int result = 0;
		try {
			result = insertMember(conn, m);
			if(result > 0)
				commit(conn);
			else
				rollback(conn);
		} catch (SQLException e) {
			rollback(conn);
			e.printStackTrace();
		} finally {
			close(conn);
		}
		
		//결과 반환해주기
		return result;
	}
	
	public int insertMember(Connection conn, MemberVo m) throws SQLException {
		//dao 불러서 쿼리 실행
		//dao 한테 쿼리 실행 결과 받기
		return new MemberDao().insertMember(conn, m);
	}
	
	public MemberVo login(MemberVo m) {
		//커넥션 가져오기
		Connection conn =  getConnection();
		
		//id 가지고 그 아이디의 비번 조회
		
		MemberVo selectedMember = selectMember(conn, m);
		
		close(conn);
		
		if(selectedMember.getPwd().equals(m.getPwd())) {
			return selectedMember;
		}else {
			return null;
		}
		
		//가져온 pwd랑 사용자입력 pwd랑 같은지 비교
		//결과 리턴
//		return selectedMember.getPwd().equals(m.getPwd());
	}
	
	public MemberVo selectMember(Connection conn, MemberVo m) {
		return new MemberDao().selectMember(conn, m);
	}

	public List<MemberVo> selectMemberAll(Connection conn) {
		return new MemberDao().selectMemberAll(conn);
	}

	public List<MemberVo> search(String type, String value) {
		Connection conn = getConnection();
		List<MemberVo> memberList;
		if(type == null || value == null) {
			memberList = selectMemberAll(conn);
		}else {
			memberList = selectMember(conn, type, value);
		}
		close(conn);
		
		return memberList;
	}
	
	private List<MemberVo> selectMember(Connection conn, String type, String value) {
		return new MemberDao().selectMemberBySearch(conn, type, value);
	}

	public int dupCheck(String id) {
		Connection conn = getConnection();
		int result = selectMemberById(conn, id);
		close(conn);
		return result;
	}

	private int selectMemberById(Connection conn, String id) {
		return new MemberDao().selectMemberById(conn, id);
	}
}
