//package com.hhhh.group.secwealth.mktdata.common.dao.impl;
//
//
//import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
//import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import javax.persistence.EntityManager;
//
//
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class BaseDaoTest {
//
//    @InjectMocks
//    BaseDao baseDao = new BaseDaoImpl();
//
//    @Mock
//    EntityManager entityManager;
//
//    @Mock
//    SystemException systemException;
//
//
//
//    @Test
//    public void getEntityManager()throws Exception{
//        entityManager.setProperty("123","1");
//        baseDao.getEntityManager();
//    }
//
//    @Test
//    public void save()throws Exception{
//        baseDao.save("");
//    }
//
//
//    @Test
//    public void update()throws Exception{
//        baseDao.update("");
//    }
//
//    @Test
//    public void delete()throws Exception{
//        baseDao.delete(BaseDaoTest.class,1);
//    }
//
//    @Test
//    public void delete2()throws Exception{
//        baseDao.delete(BaseDaoTest.class,new int[5]);
//    }
//
//    @Test
//    public void getById()throws Exception{
//        baseDao.getById(BaseDaoTest.class,new int[5]);
//    }
//
//    @Test
//    public void getById2()throws Exception{
//        baseDao.getById(BaseDaoTest.class,1);
//    }
//}
