package com.subway.data;

import com.subway.board.domain.SandWich;

public interface SandWichRepository {
	//sandwich 디자인 정보를 저장한다.
	//sandwich객체 id와 이 객체의 list에 저장된 각 ingredient객체 id를 SandWich_Ingredients 테이블의 행으로 추가한다.
	SandWich save(SandWich design);
}
