package com.as.attendance_springboot.mapper;

import com.as.attendance_springboot.model.Notice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NoticeMapperTest {
    @Autowired
    private NoticeMapper noticeMapper;
    @Test
    void getNoticeById(){
        Notice notice=noticeMapper.selectById(1);
        System.out.println(notice);
    }

}
