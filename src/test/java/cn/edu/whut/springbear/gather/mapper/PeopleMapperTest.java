package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 22:42 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class PeopleMapperTest {
    @Autowired
    private PeopleMapper peopleMapper;

    @Test
    public void getPeopleByUserId() {
        System.out.println(peopleMapper.getPeopleByUserId(311));
    }
}