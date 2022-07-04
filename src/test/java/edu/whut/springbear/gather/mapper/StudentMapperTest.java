package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:54 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class StudentMapperTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void getStudentById() {
        System.out.println(studentMapper.getStudentById(1));
    }
}