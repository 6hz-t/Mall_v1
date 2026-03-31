package cn.net.susan.service.sys;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import cn.net.susan.authenication.SmsAuthenticationToken;
import cn.net.susan.dto.web.CityDTO;
import cn.net.susan.entity.ResponsePageEntity;
import cn.net.susan.entity.auth.AuthUserEntity;
import cn.net.susan.entity.auth.CaptchaEntity;
import cn.net.susan.entity.auth.JwtUserEntity;
import cn.net.susan.entity.auth.TokenEntity;
import cn.net.susan.entity.common.CommonTaskEntity;
import cn.net.susan.entity.email.RemoteLoginEmailEntity;
import cn.net.susan.entity.shopping.ProductFavoritesConditionEntity;
import cn.net.susan.entity.shopping.ProductViewRecordConditionEntity;
import cn.net.susan.entity.sys.DeptConditionEntity;
import cn.net.susan.entity.sys.DeptEntity;
import cn.net.susan.entity.sys.JobEntity;
import cn.net.susan.entity.sys.RoleEntity;
import cn.net.susan.entity.sys.UserAvatarEntity;
import cn.net.susan.entity.sys.UserConditionEntity;
import cn.net.susan.entity.sys.UserEntity;
import cn.net.susan.entity.sys.UserRoleConditionEntity;
import cn.net.susan.entity.sys.UserRoleEntity;
import cn.net.susan.entity.sys.web.MyCountStatWebEntity;
import cn.net.susan.entity.sys.web.UserPhoneLoginWebEntity;
import cn.net.susan.entity.sys.web.UserWebEntity;
import cn.net.susan.enums.EmailTypeEnum;
import cn.net.susan.enums.SmsTypeEnum;
import cn.net.susan.enums.TaskStatusEnum;
import cn.net.susan.enums.TaskTypeEnum;
import cn.net.susan.exception.BusinessException;
import cn.net.susan.helper.GeoIpHelper;
import cn.net.susan.helper.IdGenerateHelper;
import cn.net.susan.helper.TokenHelper;
import cn.net.susan.mapper.BaseMapper;
import cn.net.susan.mapper.common.CommonTaskMapper;
import cn.net.susan.mapper.shopping.ProductFavoritesMapper;
import cn.net.susan.mapper.shopping.ProductViewRecordMapper;
import cn.net.susan.mapper.sys.DeptMapper;
import cn.net.susan.mapper.sys.JobMapper;
import cn.net.susan.mapper.sys.RoleMapper;
import cn.net.susan.mapper.sys.UserAvatarMapper;
import cn.net.susan.mapper.sys.UserMapper;
import cn.net.susan.mapper.sys.UserRoleMapper;
import cn.net.susan.service.BaseService;
import cn.net.susan.util.AssertUtil;
import cn.net.susan.util.DateFormatUtil;
import cn.net.susan.util.FillUserUtil;
import cn.net.susan.util.IpUtil;
import cn.net.susan.util.PasswordUtil;
import cn.net.susan.util.RedisUtil;
import cn.net.susan.util.TokenUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.net.susan.constant.NumberConstant.NUMBER_1;
import static cn.net.susan.constant.NumberConstant.NUMBER_200;
import static cn.net.susan.util.AssertUtil.ASSERT_ERROR_CODE;
import static cn.net.susan.util.CaptchaKeyUtil.getCaptchaKey;
import static cn.net.susan.util.SmsKeyUtil.getSmsCodePrefixKey;


@Slf4j
@Service
public class UserService extends BaseService<UserEntity, UserConditionEntity> {

    private static final String DEFAULT_PASSWORD = "123456";
    private static final String REGISTER_USER_PREFIX = "registerUser:";
    private static final String LOGIN_ERROR_USER_PREFIX = "loginErrorUser:";
    private static final String LOCKED_USER_PREFIX = "lockedUser:";
    private static final int USER_LOGIN_MAX_ERROR_COUNT = 5;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenHelper tokenHelper;
    @Autowired
    private PasswordUtil passwordUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${mall.mgt.captchaExpireSecond:60}")
    private int captchaExpireSecond;
    @Value("${mall.mgt.remoteLoginDiffHour:48}")
    private int remoteLoginDiffHour;
    @Value("${mall.mgt.lockedUserTime:86400}")
    private int lockedUserTime;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private GeoIpHelper geoIpHelper;
    @Autowired
    private CommonTaskMapper commonTaskMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IdGenerateHelper idGenerateHelper;
    @Autowired
    private UserAvatarMapper userAvatarMapper;

    @Value("${mall.mgt.tokenExpireTimeInRecord:3600}")
    private int tokenExpireTimeInRecord;

    @Autowired
    private ProductFavoritesMapper productFavoritesMapper;
    @Autowired
    private ProductViewRecordMapper productViewRecordMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 我的数量统计web实体
     *
     * @return 用户详情
     */
    public MyCountStatWebEntity myCountStat() {
        JwtUserEntity currentUserInfo = FillUserUtil.getCurrentUserInfo();
        AssertUtil.notNull(currentUserInfo, "请先登录");

        ProductFavoritesConditionEntity productFavoritesConditionEntity = new ProductFavoritesConditionEntity();
        productFavoritesConditionEntity.setUserId(currentUserInfo.getId());
        int favoritesCount = productFavoritesMapper.searchCount(productFavoritesConditionEntity);

        ProductViewRecordConditionEntity productViewRecordConditionEntity = new ProductViewRecordConditionEntity();
        productViewRecordConditionEntity.setUserId(currentUserInfo.getId());
        int viewRecordCount = productViewRecordMapper.searchCount(productViewRecordConditionEntity);

        MyCountStatWebEntity myCountStatWebEntity = new MyCountStatWebEntity();
        myCountStatWebEntity.setFavoritesCount(favoritesCount);
        myCountStatWebEntity.setViewRecordCount(viewRecordCount);
        return myCountStatWebEntity;
    }

    /**
     * 获取当前登录的用户详情
     *
     * @return 用户详情
     */
    public UserWebEntity getUserDetail() {
        UserWebEntity userWebEntity = new UserWebEntity();
        String currentUsername = tokenHelper.getCurrentUsername();

        String jsonData = redisUtil.get(getUserKey(currentUsername));
        UserEntity userEntity = JSONUtil.toBean(jsonData, UserEntity.class);
        if (Objects.isNull(userEntity)) {
            return userWebEntity;
        }

        userWebEntity.setId(userEntity.getId());
        userWebEntity.setUserName(userEntity.getUserName());
        userWebEntity.setNickName(userEntity.getNickName());
        userWebEntity.setSex(userEntity.getSex());

        UserAvatarEntity userAvatarEntity = userAvatarMapper.findById(userEntity.getId());
        if (Objects.nonNull(userAvatarEntity)) {
            userWebEntity.setAvatarUrl(userAvatarEntity.getPath());
        }
        return userWebEntity;
    }

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public UserEntity findById(Long id) {
        return userMapper.findById(id);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param userConditionEntity 用户信息
     * @return 用户集合
     */
    public ResponsePageEntity<UserEntity> searchByPage(UserConditionEntity userConditionEntity) {
        ResponsePageEntity<UserEntity> responsePageEntity = super.searchByPage(userConditionEntity);
        List<UserEntity> data = responsePageEntity.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            initDept(responsePageEntity.getData());
            initRole(responsePageEntity.getData());
            initJob(responsePageEntity.getData());
        }
        return responsePageEntity;
    }

    private void initDept(List<UserEntity> userEntities) {
        if (CollectionUtils.isEmpty(userEntities)) {
            return;
        }

        List<Long> deptIdList = userEntities.stream().map(UserEntity::getDeptId).distinct().collect(Collectors.toList());
        List<DeptEntity> deptList = deptMapper.findByIds(deptIdList);
        Map<Long, List<DeptEntity>> deptMap = deptList.stream().collect(Collectors.groupingBy(DeptEntity::getId));
        for (UserEntity userEntity : userEntities) {
            List<DeptEntity> deptEntities = deptMap.get(userEntity.getDeptId());
            if (CollectionUtils.isNotEmpty(deptEntities)) {
                userEntity.setDept(deptEntities.get(0));
            }
        }
    }

    private void initRole(List<UserEntity> userEntities) {
        List<Long> userIdList = userEntities.stream().map(UserEntity::getId).distinct().collect(Collectors.toList());
        UserRoleConditionEntity userRoleConditionEntity = new UserRoleConditionEntity();
        userRoleConditionEntity.setUserIdList(userIdList);
        List<UserRoleEntity> userRoleEntityList = userRoleMapper.searchByCondition(userRoleConditionEntity);
        Map<Long, List<UserRoleEntity>> userRoleMap = userRoleEntityList.stream().collect(Collectors.groupingBy(UserRoleEntity::getUserId));
        for (UserEntity userEntity : userEntities) {
            List<UserRoleEntity> list = userRoleMap.get(userEntity.getId());
            if (CollectionUtils.isNotEmpty(list)) {
                userEntity.setRoles(list.stream().map(x -> {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setId(x.getRoleId());
                    return roleEntity;
                }).collect(Collectors.toList()));
            }
        }
    }

    private void initJob(List<UserEntity> userEntities) {
        for (UserEntity userEntity : userEntities) {
            if (Objects.isNull(userEntity.getJobId())) {
                continue;
            }
            JobEntity jobEntity = new JobEntity();
            jobEntity.setId(userEntity.getJobId());
            userEntity.setJobs(Lists.newArrayList(jobEntity));
        }
    }

    /**
     * 初始化历史用户数据到Redis
     */
    public void initHistoryUserToRedis() {
        UserConditionEntity userConditionEntity = new UserConditionEntity();
        userConditionEntity.setPageNo(NUMBER_1);
        userConditionEntity.setPageSize(NUMBER_200);

        List<UserEntity> userList = userMapper.searchByCondition(userConditionEntity);

        while (CollectionUtils.isNotEmpty(userList)) {
            saveUserToRedis(userList);

            userConditionEntity.setPageNo(userConditionEntity.getPageNo() + 1);
            userList = userMapper.searchByCondition(userConditionEntity);
        }
    }

    private void saveUserToRedis(List<UserEntity> userList) {
        for (UserEntity userEntity : userList) {
            saveUserToRedis(userEntity);
        }
    }

    private void fillData(List<UserEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        List<Long> deptIdList = dataList.stream()
                .filter(x -> Objects.nonNull(x.getDeptId()))
                .map(UserEntity::getDeptId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deptIdList)) {
            return;
        }
        DeptConditionEntity deptConditionEntity = new DeptConditionEntity();
        deptConditionEntity.setIdList(deptIdList);
        List<DeptEntity> deptEntities = deptMapper.searchByCondition(deptConditionEntity);
        Map<Long, List<DeptEntity>> deptMap = deptEntities.stream().collect(Collectors.groupingBy(DeptEntity::getId));
        for (UserEntity userEntity : dataList) {
            if (Objects.isNull(userEntity.getDeptId())) {
                continue;
            }

            List<DeptEntity> deptEntityList = deptMap.get(userEntity.getDeptId());
            if (CollectionUtils.isNotEmpty(deptEntityList)) {
                DeptEntity deptEntity = deptEntityList.get(0);
                userEntity.setDeptName(deptEntity.getName());
                userEntity.setDept(deptEntity);
            }
        }
        fillDept(dataList);
        fillRole(dataList);
    }


    private void fillDept(List<UserEntity> dataList) {
        List<Long> jobIdList = dataList.stream().filter(x -> Objects.nonNull(x.getJobId())).map(UserEntity::getJobId).collect(Collectors.toList());
        List<JobEntity> deptList = jobMapper.findByIds(jobIdList);
        for (UserEntity userEntity : dataList) {
            Optional<JobEntity> optional = deptList.stream().filter(x -> x.getId().equals(userEntity.getJobId())).findAny();
            if (optional.isPresent()) {
                userEntity.setJobs(Lists.newArrayList(optional.get()));
            }
        }
    }


    private void fillRole(List<UserEntity> dataList) {
        List<Long> userIdList = dataList.stream().map(UserEntity::getId).collect(Collectors.toList());
        UserRoleConditionEntity userRoleConditionEntity = new UserRoleConditionEntity();
        userRoleConditionEntity.setUserIdList(userIdList);
        userRoleConditionEntity.setPageSize(0);
        List<UserRoleEntity> userRoleEntityList = userRoleMapper.searchByCondition(userRoleConditionEntity);
        if (CollectionUtils.isEmpty(userRoleEntityList)) {
            return;
        }

        List<Long> roleIdList = userRoleEntityList.stream().map(UserRoleEntity::getRoleId).distinct().collect(Collectors.toList());
        List<RoleEntity> roleList = roleMapper.findByIds(roleIdList);
        Map<Long, List<UserRoleEntity>> userRoleMap = userRoleEntityList.stream().collect(Collectors.groupingBy(UserRoleEntity::getUserId));
        Map<Long, List<RoleEntity>> roleMap = roleList.stream().collect(Collectors.groupingBy(RoleEntity::getId));
        for (UserEntity userEntity : dataList) {
            List<UserRoleEntity> roleEntities = userRoleMap.get(userEntity.getId());
            if (CollectionUtils.isNotEmpty(roleEntities)) {
                List<RoleEntity> roles = Lists.newArrayList();
                for (UserRoleEntity roleEntity : roleEntities) {
                    List<RoleEntity> matchRoleEntities = roleMap.get(roleEntity.getRoleId());
                    if (CollectionUtils.isNotEmpty(matchRoleEntities)) {
                        roles.add(matchRoleEntities.get(0));
                    }
                }
                userEntity.setRoles(roles);
            }
        }
    }

    /**
     * 用户手机号登录
     *
     * @param userPhoneLoginWebEntity 用户实体
     * @return 影响行数
     */
    public TokenEntity loginByPhone(UserPhoneLoginWebEntity userPhoneLoginWebEntity) {
        checkUserIsLocked(userPhoneLoginWebEntity.getPhone());
        try {
            SmsAuthenticationToken authenticationToken =
                    new SmsAuthenticationToken(userPhoneLoginWebEntity.getPhone(), userPhoneLoginWebEntity.getSmsCode());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtUserEntity jwtUserEntity = (JwtUserEntity) (authentication.getPrincipal());

            String token = tokenHelper.generateToken(jwtUserEntity);
            List<String> roles = jwtUserEntity.getAuthorities().stream()
                    .map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
            return new TokenEntity(jwtUserEntity.getUsername(), token, roles, tokenExpireTimeInRecord);
        } catch (Exception e) {
            log.info("登录失败：", e);
            if (e instanceof BusinessException) {
                recordLoginErrorUser(userPhoneLoginWebEntity.getPhone());
                throw e;
            }

            if (e instanceof BadCredentialsException) {
                recordLoginErrorUser(userPhoneLoginWebEntity.getPhone());
            }
            throw new BusinessException("用户名登录失败");
        }
    }

    /**
     * 用户登录
     *
     * @param authUserEntity 用户录入信息
     */
    public TokenEntity login(AuthUserEntity authUserEntity) {
        UserEntity userEntity = null;
        checkUserIsLocked(authUserEntity.getUsername());

        try {
            String code = redisUtil.get(getCaptchaKey(authUserEntity.getUuid()));
            AssertUtil.hasLength(code, "该验证码已失效");
            AssertUtil.isTrue(code.trim().equals(authUserEntity.getCode().trim()), "验证码错误");

            userEntity = userMapper.findByUserName(authUserEntity.getUsername());
            AssertUtil.notNull(userEntity, "该用户不存在");

            String decodePassword = passwordUtil.decodeRsaPassword(authUserEntity);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUserEntity.getUsername(), decodePassword);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtUserEntity jwtUserEntity = (JwtUserEntity) (authentication.getPrincipal());


            //获取当前用户的IP
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
            //String ip = IpUtil.getIpAddr(httpServletRequest);
            String ip = "220.181.108.177";
            CityDTO cityDTO = geoIpHelper.getCity(ip);
            if (Objects.nonNull(cityDTO)) {
                String city = cityDTO.getCity();
                validateRemoteLogin(userEntity, city);
                userEntity.setLastLoginCity(city);
            }

            String token = tokenHelper.generateToken(jwtUserEntity);
            redisUtil.del(getCaptchaKey(authUserEntity.getUuid()));
            List<String> roles = jwtUserEntity.getAuthorities().stream()
                    .map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());

            //更新用户的最后登录城市
            updateLastLoginCity(userEntity);
            return new TokenEntity(jwtUserEntity.getUsername(), token, roles, tokenExpireTimeInRecord);
        } catch (Exception e) {
            log.info("登录失败：", e);
            if (e instanceof BusinessException) {
                recordLoginErrorUser(authUserEntity.getUsername());
                throw e;
            }

            if (e instanceof BadCredentialsException) {
                recordLoginErrorUser(authUserEntity.getUsername());
            }
            throw new BusinessException(ASSERT_ERROR_CODE, "用户名或密码错误");
        }
    }


    private void checkUserIsLocked(String key) {
        try {
            String value = redisUtil.get(LOCKED_USER_PREFIX + key);
            AssertUtil.isTrue(!StringUtils.hasLength(value), "该用户已被锁定");
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException("登录失败");
        }

    }

    private void recordLoginErrorUser(String key) {
        if (Objects.isNull(key)) {
            return;
        }
        String loginErrorUserKey = LOGIN_ERROR_USER_PREFIX + key;
        String value = redisUtil.get(loginErrorUserKey);
        if (StringUtils.hasLength(value)) {
            Integer count = Integer.parseInt(value);
            if (count >= USER_LOGIN_MAX_ERROR_COUNT) {
                redisUtil.set(LOCKED_USER_PREFIX + key, "true", lockedUserTime);
                throw new BusinessException(ASSERT_ERROR_CODE, "该用户已被锁定");
            } else {
                count++;
                redisUtil.set(loginErrorUserKey, String.valueOf(count), lockedUserTime);
            }
        } else {
            redisUtil.set(loginErrorUserKey, "1", lockedUserTime);
        }
    }

    private void updateLastLoginCity(UserEntity userEntity) {
        FillUserUtil.fillUpdateUserInfo(userEntity);
        userEntity.setLastLoginTime(new Date());
        userMapper.update(userEntity);
    }

    private void validateRemoteLogin(UserEntity userEntity, String nowCity) {
        if (!StringUtils.hasLength(userEntity.getLastLoginCity())) {
            return;
        }

        Date lastLoginTime = userEntity.getLastLoginTime();
        if (Objects.nonNull(lastLoginTime)) {
            long betweenHours = DateUtil.between(new Date(), lastLoginTime, DateUnit.HOUR);
            if (betweenHours > remoteLoginDiffHour) {
                return;
            }
        }

        if (!userEntity.getLastLoginCity().equals(nowCity)) {
            //记录异地登录请求
            recordRemoteLoginData(userEntity, nowCity);
            throw new BusinessException("您的账号处于异地登录，为了安全考虑，请修改密码之后重新登录");
        }
    }


    private void recordRemoteLoginData(UserEntity userEntity, String nowCity) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RemoteLoginEmailEntity remoteLoginEmailEntity = new RemoteLoginEmailEntity();
        remoteLoginEmailEntity.setUsername(userEntity.getUserName());
        remoteLoginEmailEntity.setNickName(userEntity.getNickName());
        remoteLoginEmailEntity.setIp(IpUtil.getIpAddr(httpServletRequest));
        remoteLoginEmailEntity.setDevice(httpServletRequest.getHeader("user-agent"));
        remoteLoginEmailEntity.setCityName(nowCity);
        remoteLoginEmailEntity.setEmail(userEntity.getEmail());
        remoteLoginEmailEntity.setLoginTime(DateFormatUtil.parseToString(userEntity.getUpdateTime()));

        CommonTaskEntity commonTaskEntity = createCommonTaskEntity();
        commonTaskEntity.setRequestParam(JSONUtil.toJsonStr(remoteLoginEmailEntity));
        commonTaskMapper.insert(commonTaskEntity);
    }


    private CommonTaskEntity createCommonTaskEntity() {
        CommonTaskEntity commonTaskEntity = new CommonTaskEntity();
        commonTaskEntity.setName("发送异地登录邮件");
        commonTaskEntity.setStatus(TaskStatusEnum.WAITING.getValue());
        commonTaskEntity.setFailureCount(0);
        commonTaskEntity.setType(TaskTypeEnum.SEND_EMAIL.getValue());
        commonTaskEntity.setBizType(EmailTypeEnum.REMOTE_LOGIN.getValue());
        FillUserUtil.fillCreateUserInfo(commonTaskEntity);
        return commonTaskEntity;
    }

    /**
     * 用户登出
     *
     * @param request 请求
     */
    public void logout(HttpServletRequest request) {
        String token = TokenUtil.getTokenForAuthorization(request);
        AssertUtil.hasLength(token, "请重新登录");
        tokenHelper.delToken(token);
    }


    /**
     * 获取当前登录的用户信息
     *
     * @return 用户信息
     */
    public JwtUserEntity getUserInfo() {
        String currentUsername = tokenHelper.getCurrentUsername();
        return (JwtUserEntity) userDetailsService.loadUserByUsername(currentUsername);
    }


    public CaptchaEntity getCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = "";
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        String uuid = "C" + IdUtil.simpleUUID();
        // 保存验证码到Redis中
        redisUtil.set(getCaptchaKey(uuid), result, captchaExpireSecond);
        return new CaptchaEntity(uuid, captcha.toBase64());
    }


    /**
     * 新增用户
     *
     * @param userEntity 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Throwable.class)
    public void insert(UserEntity userEntity) {
        UserConditionEntity userConditionEntity = new UserConditionEntity();
        userConditionEntity.setUserName(userEntity.getUserName());
        AssertUtil.isTrue(CollectionUtils.isEmpty(userMapper.searchByCondition(userConditionEntity)), "用户名称已存在");

        userConditionEntity = new UserConditionEntity();
        userConditionEntity.setEmail(userEntity.getEmail());
        AssertUtil.isTrue(CollectionUtils.isEmpty(userMapper.searchByCondition(userConditionEntity)), "邮箱已存在");
        if (!StringUtils.hasLength(userEntity.getPassword())) {
            userEntity.setPassword(DEFAULT_PASSWORD);
        }
        userEntity.setPassword(passwordUtil.encode(userEntity.getPassword()));
        fillData(userEntity);

        userMapper.insert(userEntity);

        userRoleMapper.deleteByUserId(userEntity.getId());
        List<UserRoleEntity> userRoleEntities = buildUserRoleEntityList(userEntity);
        if (CollectionUtils.isNotEmpty(userRoleEntities)) {
            userRoleMapper.batchInsert(userRoleEntities);
        }
        //秒杀系统会用到
        saveUserToRedis(userEntity);
    }

    private void saveUserToRedis(UserEntity userEntity) {
        redisUtil.setIfAbsent(getUserKey(userEntity.getUserName()), JSON.toJSONString(userEntity));
    }

    private String getUserKey(String userName) {
        return String.format("%s%s", REGISTER_USER_PREFIX, userName);
    }

    private void fillData(UserEntity userEntity) {
        if (Objects.nonNull(userEntity.getDept())) {
            userEntity.setDeptId(userEntity.getDept().getId());
        }

        if (CollectionUtils.isNotEmpty(userEntity.getJobs())) {
            userEntity.setJobId(userEntity.getJobs().get(0).getId());
        }
    }

    private List<UserRoleEntity> buildUserRoleEntityList(UserEntity userEntity) {
        if (CollectionUtils.isNotEmpty(userEntity.getRoles())) {
            return userEntity.getRoles().stream().map(x -> {
                UserRoleEntity userRoleEntity = new UserRoleEntity();
                userRoleEntity.setId(idGenerateHelper.nextId());
                userRoleEntity.setUserId(userEntity.getId());
                userRoleEntity.setRoleId(x.getId());
                return userRoleEntity;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 修改用户
     *
     * @param userEntity 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = Throwable.class)
    public int update(UserEntity userEntity) {
        fillData(userEntity);
        userRoleMapper.deleteByUserId(userEntity.getId());
        List<UserRoleEntity> userRoleEntities = buildUserRoleEntityList(userEntity);
        if (CollectionUtils.isNotEmpty(userRoleEntities)) {
            userRoleMapper.batchInsert(userRoleEntities);
        }
        return userMapper.update(userEntity);
    }


    /**
     * 删除岗位对象
     *
     * @param ids 系统ID
     * @return 结果
     */
    public int deleteByIds(List<Long> ids) {
        List<UserEntity> userEntities = userMapper.findByIds(ids);
        AssertUtil.notEmpty(userEntities, "用户已被删除");

        UserEntity userEntity = new UserEntity();
        FillUserUtil.fillUpdateUserInfo(userEntity);
        return userMapper.deleteByIds(ids, userEntity);
    }

    /**
     * 批量重置用户密码
     *
     * @param ids 用户ID
     * @return
     */
    public int resetPwd(List<Long> ids) {
        List<UserEntity> userEntities = userMapper.findByIds(ids);
        AssertUtil.notEmpty(userEntities, "用户不存在");

        for (UserEntity userEntity : userEntities) {
            userEntity.setPassword(passwordUtil.encode(DEFAULT_PASSWORD));
            FillUserUtil.fillUpdateUserInfo(userEntity);
        }
        return userMapper.updateForBatch(userEntities);
    }

    @Override
    protected BaseMapper getBaseMapper() {
        return userMapper;
    }
}
