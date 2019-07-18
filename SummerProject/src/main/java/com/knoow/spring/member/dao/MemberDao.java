package com.knoow.spring.member.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.knoow.spring.member.Member;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository
public class MemberDao implements IMemberDao {

	private JdbcTemplate template;
	
	@Autowired
	public MemberDao(ComboPooledDataSource dataSource) {
		template = new JdbcTemplate(dataSource);
	}
	
	@Override
	public int memberInsert(Member member) {
		
		int result = 0;
		
		String sql = "INSERT INTO member (memId, memPw, memMail) values (?,?,?)";
		result = template.update(sql, member.getMemId(), member.getMemPw(), member.getMemMail());
	
		return result;
	}

	@Override
	public Member memberSelect(Member member) {
		
		List<Member> members = null;
		final String sql = "SELECT * FROM member WHERE memId = ? AND memPw = ?";
		
		members = template.query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, member.getMemId());
				ps.setString(2, member.getMemPw());
			}
		}, new RowMapper<Member>() {

			@Override
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member mem = new Member();
				mem.setMemId(rs.getString("memId"));
				mem.setMemPw(rs.getString("memPw"));
				mem.setMemMail(rs.getString("memMail"));
//				mem.setMemPurcNum(rs.getString("memPurcNum"));
				return mem;
			}
			
		});
		
		if(members.isEmpty()) return null;
		
		return members.get(0);
		
	}

	@Override
	public int memberUpdate(Member member) {
		
		int result = 0;
		String sql = "UPDATE member SET memPw = ?, memMail = ? WHERE memId = ?";
		result = template.update(sql, member.getMemPw(), member.getMemMail(), member.getMemId());
		
		return result;
		
	}

	@Override
	public int memberDelete(Member member) {
		
		int result = 0;
		String sql = "DELETE FROM member WHERE memId = ? AND memPw = ?";
		result = template.update(sql, member.getMemId(), member.getMemPw());
		
		return result;
		
	}

}