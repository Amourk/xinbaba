package cn.yy.core.service;

//import cn.yy.core

import cn.yy.core.bean.test;
import cn.yy.core.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("testService")
@Transactional
public class TestServiceImpl implements TestService{

    @Autowired
    private TestDao testDao;

    @Override
    public boolean insertTest(test test) {
        return testDao.insertTest(test);
    }

}
