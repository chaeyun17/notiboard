package notiboard.member.dao;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import notiboard.member.domain.QMember;
import notiboard.member.dto.MemberDto;

public class MemberProjection {

  public static QBean<MemberDto.Response> projection(QMember member) {
    return Projections.fields(MemberDto.Response.class,
        member.id,
        member.username,
        member.nickname);
  }
}
