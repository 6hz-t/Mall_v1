package cn.net.susan;

import cn.net.susan.service.email.EmailService;
import cn.net.susan.service.sms.ISmsService;
import cn.net.susan.service.sms.SmsService;
import cn.net.susan.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/job")
public class TestController {

    public static final String SUCCESS = "success";

    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;

    @GetMapping("/test")
    public String test() {
        return SUCCESS;
    }

    @GetMapping("/sendEmail")
    public String sendEmail() {
        emailService.sendEmail("12lisu@163.com", "test", "test123");
        return SUCCESS;
    }

    @GetMapping("/sendRegisterCode")
    public Boolean sendRegisterCode(@RequestParam("phone") String phone) {
        return smsService.sendRegisterSms(phone);
    }
}
