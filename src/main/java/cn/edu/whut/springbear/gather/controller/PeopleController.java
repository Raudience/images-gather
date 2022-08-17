package cn.edu.whut.springbear.gather.controller;

import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Response;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.PeopleService;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.SchoolService;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 20:08 Thursday
 */
@RestController
public class PeopleController {
    @Autowired
    private UserService userService;
    @Autowired
    private PeopleService peopleService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/people.do")
    public Response getPeopleWithUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Keep the user info in the session scope with people information
        People people = peopleService.queryPeople(user.getId());
        user.setPeople(people);
        session.setAttribute("user", user);
        // Get the latest login log of the user
        LoginLog loginLog = recordService.getUserLatestLoginLog(user.getId());
        return Response.success("个人信息查询成功").put("item", user).put("loginLog", loginLog);
    }

    @PutMapping("/people.do")
    public Response updatePeople(@RequestParam String newSex, @RequestParam String newPhone, @RequestParam String newEmail, HttpSession session) {
        // Verify the data entered by the user
        if (!Pattern.matches("^1[3-9]\\d{9}$", newPhone)) {
            return Response.error("手机号格式不正确，请重新输入");
        }
        if (!Pattern.matches("\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?", newEmail)) {
            return Response.error("邮箱格式不正确，请重新输入");
        }

        User user = (User) session.getAttribute("user");
        People people = user.getPeople();

        // Update the people info
        if (!peopleService.updatePeopleInfo(newSex, newEmail, newPhone, people.getId())) {
            return Response.error("个人信息更新失败");
        }
        // Update the user info
        if (!userService.updateUserEmailAndPhone(newEmail, newPhone, user.getId())) {
            return Response.error("用户信息更新失败");
        }

        // Keep the user info in the session scope be latest
        user.setPhone(newPhone);
        user.setEmail(newEmail);
        people.setSex(newSex);
        people.setPhone(newPhone);
        people.setEmail(newEmail);
        user.setPeople(people);
        session.setAttribute("user", user);
        return Response.success("个人信息更新成功").put("item", user.getUserType());
    }

    @GetMapping("/people/class.do")
    public Response getClassPeopleList(@RequestParam("classId") Integer classId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止查看班级人员信息");
        }
        List<People> peopleList = schoolService.getClassPeopleList(classId);
        if (peopleList == null || peopleList.size() == 0) {
            return Response.info("班级中暂无人员信息");
        }
        return Response.success("查询班级人员信息成功").put("list", peopleList);
    }

    @PostMapping("/people.do")
    public Response saveHeadTeacher(People people, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getUserType() != User.TYPE_ADMIN) {
            return Response.error("权限不足，禁止新增年级主任");
        }
        people.setClassId(0);
        // Save people information
        if (!peopleService.saveHeadTeacher(people)) {
            return Response.error("保存人员信息失败");
        }
        return Response.success("新增年级主任成功");
    }
}
