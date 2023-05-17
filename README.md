
> 开发时间：2022.06.02 - 2022.09.15

# 一、快速开始

1. 克隆仓库：使用 Git 克隆仓库或直接下载仓库压缩包到您的计算机
2. 打开工程：使用 `IntelliJ IDEA`  打开克隆的仓库或解压的工程文件，而后使用 `Maven` 工具更新工程依赖
3. 创建数据库和表：
   - 登录 MySQL ，创建 `images_gather` 数据库
   - 将 `src/main/resources/images_gather.sql` 文件中的数据库表导入 images_gather 数据库中
4. 修改配置信息：

   - 修改 `src/main/webapp/static/js/gather-common.js` 中的 `contextPath` 变量值，此为前端请求路径

   - 修改 `src/main/resources/jdbc.properties` 中的数据库连接信息，设置你自己的用户名和密码
   - 修改 `src/main/resources/qiniu.properties` 中的七牛云配置信息，设置你自己的 [七牛云](https://www.qiniu.com/) 账号信息
   - 修改 `src/main/resources/email.properties` 中的邮箱配置信息，设置你自己的邮箱账号信息（[smtp 开启](https://blog.csdn.net/smilehappiness/article/details/108145215)）
   - 修改 `src/main/resources/baidu.properties` 中的百度地图 IP 解析配置信息，设置你自己的 [百度地图](https://lbsyun.baidu.com/) IP 解析 URL
5. 部署访问：在 IntelliJ IDEA 中配置 `Tomcat`，即可访问两码一查首页

# 二、系统设计

## 4.1、网络架构

![在这里插入图片描述](https://img-blog.csdnimg.cn/b852c6e796da45c48f3b3db434d00fb5.png#pic_center)


## 4.2、系统架构

![在这里插入图片描述](https://img-blog.csdnimg.cn/4f89710ee1bb478dab8e9ecc17f68dc2.png#pic_center)


## 4.3、数据库设计

### 4.3.1、实体及属性

1. 用户：用户 ID，用户名，密码，电话，邮箱，用户类型，用户状态，姓名，性别，学校 ID，年级 ID，班级 ID，登录时间，创建时间
2. 学校：学校 ID，校名
3. 年级：年级 ID，年级名称
4. 班级：班级ID，班级名称
5. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID，创建时间
6. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID
7. 邮件记录：邮件记录 ID，邮箱，验证码，发送时间，用户ID

### 4.3.2、实体间关系

1. 用户与学校：一个用户对应一个学校，一个学校下包含多个用户，故用户与学校间是多对一关系
1. 用户与年级：一个用户对应一个年级，一个年级下包含多个用户，故用户与年级间是多对一关系
1. 用户与班级：一个用户对应一个班级，一个班级下包含多个用户，故用户与班级间是多对一关系
1. 用户与上传记录：一个用户可以有多条上传记录，一条上传记录对应一个用户，所以用户与上传记录间是一对多关系
1. 用户与登录记录：一个用户可以有多条登录记录，一条登录记录对应一个用户，所以用户与登录记录间是一对多关系
1. 用户与邮件记录：一个用户可以有多条邮件记录，一条邮件记录对应一个用户，所以用户与邮件记录间是一对多关系
1. 学校与年级：一所学校下可有多个年级，一个年级对应一所学校，所以学校与年级间是一多对关系
1. 年级与班级：一个年级下可有多个班级，一个班级对应一个年级，所以年级与班级间是一对多关系

### 4.3.3、逻辑结构设计

1. 用户：用户 ID，用户名，密码，电话，邮箱，用户类型，用户状态，姓名，性别，学校 ID，年级 ID，班级 ID，登录时间，创建时间

   | 数据项               | 数据类型     |
      | :------------------- | :----------- |
   | id（主键、自增）     | int          |
   | username（唯一索引） | varchar(255) |
   | password             | char(32) |
   |phone|varchar(255)|
   |email|varchar(255)|
   |user_type|int|
   |user_status|int|
   |name|varchar(255)|
   |sex|char(2)|
   |school_id（外键）|int|
   |grade_id（外键）|int|
   | class_id（外键）     | int          |
   | last_login_datetime  | datetime     |
   | create_datetime      | datetime     |

2. 上传记录：上传记录 ID，上传记录状态，上传时间，健康码本地 url，行程码本地 url，密接查本地 url，健康码云 url，行程码云 url，密接查云 url，用户 ID

   | 数据项             | 数据类型     |
   | :----------------- | :----------- |
   | id（主键、自增）   | int          |
   | upload_status      | int          |
   | upload_datetime    | datetime     |
   | local_health_url   | varchar(255) |
   | local_schedule_url | varchar(255) |
   | local_closed_url   | varchar(255) |
   | cloud_health_url   | varchar(255) |
   | cloud_schedule_url | varchar(255) |
   | cloud_closed_url   | varchar(255) |
   | user_id（外键）    | int          |

3. 登录记录：登录记录 ID，IP，登录地点，登录时间，用户 ID

   | 数据项           | 数据类型     |
   | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | ip               | char(15)     |
   | location         | varchar(255) |
   | login_datetime   | datetime     |
   | user_id（外键）  | int          |

4. 邮件记录：邮件记录 ID，邮箱，验证码，发送时间，用户 ID

   | 数据项            | 数据类型     |
   | :---------------- | :----------- |
   | id（主键、自增）  | int          |
   | email             | varchar(255) |
   | code              | varchar(255) |
   | delivery_datetime | datetime     |
   | user_id（外键）   | int          |

5. 学校：学校 ID，校名

   | 数据项           | 数据类型     |
      | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | school           | varchar(255) |

6. 年级：年级 ID，年级名称

   | 数据项           | 数据类型     |
      | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | grade            | varchar(255) |

7. 班级：班级ID，班级名称

   | 数据项           | 数据类型     |
      | :--------------- | :----------- |
   | id（主键、自增） | int          |
   | class_name       | varchar(255) |


## 4.4、功能设计

![在这里插入图片描述](https://img-blog.csdnimg.cn/b04af3a7149747a6af4e7329469b6abb.png#pic_center)


# 五、功能演示

## 5.1、用户登录

1. 用户名不存或密码错误

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/e85be9dabaf24fd080f72ad38f0c87df.png#pic_center)


2. 记住我，当用户勾选记住我并且输入的用户名和密码无误时，服务器将从响应头中告知浏览器保存用户名和密码信息到 Cookie 中，当用户下次登录系统时直接从 Cookie 中读取用户名和密码的值并自动填充到输入框中

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/4a9bff95957a49efa028f544a5c2ad94.png#pic_center)


3. 用户类型和用户状态判断，若用户状态异常或类型不存在则不允许用户使用系统

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/5155b49e3fb24bfdaa632c1bbde9ccab.png#pic_center)


## 5.2、重置密码

1. 获取邮箱验证码时用户名非空校验，邮箱地址格式校验

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/de6c68a6b2a74bddb8d4371c79ae85df.png#pic_center)


2. 用户名和邮箱地址不匹配时不允许修改密码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/6a5fb26c89a6446994f5041904befb9f.png#pic_center)


3. 点击确认按钮时再次校验邮箱、用户名格式，额外校验验证码长度位数必须为 6 位，新密码长度不小于 6 位

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2629a310a6a04a53a47d265e5f29daaa.png#pic_center)


4. 在用户点击确认按钮重置个人密码之前，必须点击获取按钮获取邮箱验证码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/83aeaba81f224396a1ab3d6d7a7f1a12.png#pic_center)


5. 邮箱验证码发送，用户填入正确的用户名和邮箱，即可请求服务器发送一封邮箱验证码邮件到个人邮箱中作为重置密码的身份校验

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/060e8d1aef564d84ad8279433410f39d.png#pic_center)




6. 服务端会再次对用户输入的各项内容进行校验，校验通过则发送邮件，内容如下

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/a9d6ecea65924ed59e3ea932ccb995d4.png#pic_center)


7. 错误的邮箱验证码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/63a8353186fd43aaa676a73c53ae865e.png#pic_center)


8. 密码重置成功 3s 后会自动跳转到登录页面

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/59fb5005a0e94fdcb456f0b0d9f1f38d.png#pic_center)


## 5.3、系统界面

![在这里插入图片描述](https://img-blog.csdnimg.cn/bab11e9fbf8f4a6684b3eb991daed671.png#pic_center)


## 5.4、修改密码

1. 系统点击主页面右上角的改密按钮，弹出密码修改模态框

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/9721530d35014ec389f0e6239945f768.png#pic_center)


2. 新密码长度不小于 6 位且两次输入的新密码需保持一致，新密码与原密码一致时不允许修改

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/ce0da8bc41c54c12a07121ac416a9438.png#pic_center)


3. 服务端校验新旧密码是否一致，一致时不允许修改个人密码，原密码有误时亦不允许修改个人密码

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/55e76099a7c949468615a54333dff730.png#pic_center)


4. 密码修改成功

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/8fd61600db57402da330606555ae75cc.png#pic_center)


## 5.5、图片上传

1. 选择个人健康码图片、行程码图片、密接查图片后方可上传

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/35f1278c15f1433ba2a4c3c8b1f3d168.png#pic_center)


2. 图片上传完成，在服务器留存的同时在七牛云平台留存图片信息，数据安全双重保障

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b1b35feeee164914b5ba880f149958bb.png#pic_center)


> 七牛云平台图片数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/383ea3edaaec42f884b28ce9f14e04a1.png#pic_center)


> 服务器本地数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/1018e6320f8341a886192f0161c30902.png#pic_center)


## 5.6、历史记录

1. 今日上传记录查询，当用户一天内两次及以上登入系统时，若用户已完成图片上传显示已上传的两码一查图片信息，用户可以重新上传图片

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/1afa117a89c64ea493954af10c74eb7b.png#pic_center)



2. 个人历史登录记录数据分页查看，显示登录的时间、登录IP、地点（由百度地图开发者 API 提供解析服务）信息

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/2f8236b4863748baaeacd6b5c8f05744.png#pic_center)


3. 上传记录查看，用户可查看已上传的两码一查图片信息，并提供图片查看的功能

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/33eef26f43af4fa6a61c6d532e0fdd89.png#pic_center)



## 5.7、个人资料

1. 提供用户个人资料查看的入口即系统主页面点击【我的】可查看自己的详细信息

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/cc7ff4f793334217926d0a69574491e6.png#pic_center)


2. 个人信息修改，用户可以修改自己的性别、电话、邮箱等信息，修改过程中对信息进行校验，前后数据未发生变化、数据格式不正确则不允许修改

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/08feb8a14ff1486f80ae893465150ce0.png#pic_center)


3. 个人信息修改成功，3s 后自动刷新页面

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/0e74d03e24bf4302acca9f96d9663abf.png#pic_center)


## 5.8、退出系统

每个主页面中均有【两码一查】回到主页、【退出】退出系统功能

![在这里插入图片描述](https://img-blog.csdnimg.cn/c04fb34ca1df42fe9096b3c1095bc000.png#pic_center)


## 5.9、班级记录

1. 班长可查看班级上传记录，PC 端可查看最近 14 的上传记录，移动端查看最近 7 天的上传记录，分为未登录人员名单、登录未上传人员名单和已完成人员名单并生成各位同学的图片预览

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/228ce947793441dc9c00fbc72d550a1f.png#pic_center)


2. 班长可点击日期链接上载该天该班级的两码一查上传情况，包含了已上传的图片数据和班级名单 README.txt

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/3a24ddb3e1964ea5ac3578ef927d3d4e.png#pic_center)


![在这里插入图片描述](https://img-blog.csdnimg.cn/65743129779e4987a2907b72f8a0e880.png#pic_center)


![在这里插入图片描述](https://img-blog.csdnimg.cn/77bc8a68c45043e78513321a87ce576f.png#pic_center)


![在这里插入图片描述](https://img-blog.csdnimg.cn/e2345aed6d984fd7b066b6a843e91c86.png#pic_center)


## 5.10、权限控制

配置了登录权限拦截器进行权限控制，当对应的用户账号未登入系统时则拦截除登录、邮件、重置外的所有请求

![在这里插入图片描述](https://img-blog.csdnimg.cn/2787bbd8b19f41af9e579e768e297c29.png#pic_center)

## 5.11、班主任

班主任功能与班长类似，不同的是班主任无需跳转到上传页面且显示当前的登录信息，包含登录时间、地点、IP 等

![在这里插入图片描述](https://img-blog.csdnimg.cn/e9ee29bca09e46f1ad41b2b37df21398.png#pic_center)


## 5.12、年级主任

年级主任登录记录、我的资料、修改密码功能与班主任相同，唯一不同的是年级主任可查看年级上传记录

![在这里插入图片描述](https://img-blog.csdnimg.cn/3c4d95575ac74f9d9744a90ec0e3a94b.png#pic_center)


## 5.13、年级记录

1. 班级未上传信息概览

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b95c15a252274eabb78b1894d2f85f11.png#pic_center)


2. 班级上传详情查看，点击对应班级即可查看该班人员信息，包含了未登录名单、未上传名单、已完成名单和图片概览

   ![在这里插入图片描述](https://img-blog.csdnimg.cn/b302b82984954d099d9855f0504f5547.png#pic_center)


## 5.13、导入班级

管理员可上传班级信息模板 Excel 文件，根据自定义的 sheet-bean-converter.jar 包完成 Excel 数据与 Java Bean 对象间的转换，后可将相应的学生数据转换为用户数据并批量导入到数据库中

![在这里插入图片描述](https://img-blog.csdnimg.cn/4073c99a7bc84323bbb9722451ec1ec8.png#pic_center)


## 5.14、添加主任

管理员可填入年级主任信息并选择学校和班级，从而设置对应的年级主任用户账号

![在这里插入图片描述](https://img-blog.csdnimg.cn/408f7573bc964b09a3d6de1cfcfb029f.png#pic_center)


## 5.13、班级管理

管理员可选择学校、年级、班级，后显示改班级下的所有学生，并供管理员修改学生类型的功能

![在这里插入图片描述](https://img-blog.csdnimg.cn/ff08d60d20fc4f2aabeb90ab5dd299d1.png#pic_center)


## 5.15、角色修改

![在这里插入图片描述](https://img-blog.csdnimg.cn/c2355fa6e89e4a2496e2d69881670d5a.png#pic_center)


# 六、技术选型

## 6.1、涉及技术

| Front-end technology                                         |
| :----------------------------------------------------------- |
| HTML5、CSS3、JavaScript、jQuery1.7.2、Bootstrap3.4.1、Ajax、正则表达式 |

| Backend technology                                           |
| :----------------------------------------------------------- |
| Spring5.3.1、SpringMVC5.3.1、MyBatis1.3.2、SpringTest5.3.1、JUnit4.12、Thymeleaf3.0.12、MySQL8.0.27、Druid1.1.15 |
| Logback1.2.3、Lombok1.18.20、Jackson2.9.8、PageHelper5.2.0、Qiniu7.8.0、JavaxMail1.6.2、SheetBeanConverter1.0.0 |
| Tomcat8.0.50、Maven3.8.4、Git2.35.1、百度地图 IP 归属地解析、网易邮箱验证码服务、七牛云平台数据存储 |

## 6.2、开发工具

| Develop tools                                                |
| :----------------------------------------------------------- |
| IntelliJ IDEA2021.1、Visual Studio Code、Java1.8.0_311、MySQL8.0.28、Navicat16.0.13、Git2.35.1、Maven3.8.4 |
| Tomcat8.0.50、CentOS7.6、XShell6、Xftp6、Typora1.3.6、Snipaste、ioDraw、XMind |

# 七、TODO

| 事项                                                         | 进度                                                       |
| :----------------------------------------------------------- | :----------------------------------------------------------: |
| 密码加密传输和保存 | ✓ |
| 支持多种登录方式（用户名、电话、邮箱）                  | ✓ |
| 后台管理功能设计实现                   |  ✓   |
| 前后台真正意义上动静分离               |  ✓   |
| 年级上传记录分班级概览与详情查看       |✓|
|班级上传资料压缩下载（图片、名单）|✓|
| 负载均衡                               |      |
|持续化集成与部署||
|七牛云平台图片防盗（私有空间）||
| 邮箱验证码的最大有效时间               |      |
| Python 两码一查状态识别                |      |
| 请求发送邮件时增加人机验证             |      |
| Redis 缓存提高系统并发能力             |      |
