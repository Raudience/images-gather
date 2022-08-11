package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.LoginLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:08 Thursday
 */
@Repository
public interface LoginLogMapper {
    /**
     * Save the login log of user
     */
    @Insert("insert into log_login(ip, location, login_datetime, user_id) values (#{ip},#{location},#{loginDatetime},#{userId})")
    int saveLoginLog(LoginLog loginLog);

    /**
     * Get user all login log data
     */
    @Select("select * from log_login where user_id = #{userId} order by login_datetime desc")
    List<LoginLog> getUserLoginLogs(@Param("userId") Integer userId);
}