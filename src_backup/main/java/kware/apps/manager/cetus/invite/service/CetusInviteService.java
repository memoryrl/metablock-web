package kware.apps.manager.cetus.invite.service;

import cetus.util.StringUtil;
import kware.apps.manager.cetus.invite.domain.CetusInvite;
import kware.apps.manager.cetus.invite.domain.CetusInviteDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CetusInviteService {

    private final JavaMailSender mailSender;
    private final CetusInviteDao dao;

    @Value("${cetus.base-url}")
    private String baseUrl;

    @Value("${notification.mail.sender}")
    private String sender;

    public void sendSignupInvite(CetusInvite request) throws MessagingException {

        String token = StringUtil.random(10);
        LocalDateTime expirationDate = LocalDateTime.now().plus(5, ChronoUnit.MINUTES);


        request.setUrl(token);
        request.setExpirationDate(expirationDate);
        request.setUseAt("N");
        dao.insertInvite(request);

        String signupUrl = baseUrl + "/invite/validate?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        String subject = "[써드파티] 회원 가입 초대";
        StringBuilder messageCn = new StringBuilder();
        messageCn.append("<!DOCTYPE html>\r\n")
                .append("<html>\r\n")
                .append("<head>\r\n")
                .append("</head>\r\n")
                .append("<body>\r\n")
                .append("  <div class=\"container\" style=\"max-width: 650px; margin: 0 auto; padding: 20px;\">\r\n")
                .append("    <h1 style=\"font-size: 18px; font-weight: bold; color: #333; padding: 10px 0px;\">써드파티</h1>\r\n")
                .append("    <div class=\"content\" style=\"padding: 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\">\r\n")
                .append("      <h2>회원 가입 초대</h2>\r\n")
                .append("      <p>써드파티에서 회원 가입 초대를 보냈습니다.</p>\r\n")
                .append("      <p>아래 버튼을 클릭하여 가입을 진행해주세요.</p>\r\n")
                .append("      <p style='margin-top: 20px; text-align: center;'>\r\n")
                .append("        <a href='").append(signupUrl).append("' ")
                .append("style=\"display: inline-block; padding: 10px 20px; background-color: #3498db; color: white; text-decoration: none; border-radius: 5px; font-family: Arial, sans-serif;\" ")
                .append("onmouseover=\"this.style.backgroundColor='#2980b9'\" ")
                .append("onmouseout=\"this.style.backgroundColor='#3498db'\" target='_blank'>회원가입</a>\r\n")
                .append("      </p>\r\n")
                .append("      <p style='margin-top: 20px;'>본 메일은 발신 전용입니다. 회신되지 않습니다.</p>\r\n")
                .append("    </div>\r\n")
                .append("  </div>\r\n")
                .append("</body>\r\n")
                .append("</html>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        helper.setText(messageCn.toString(), true);
        mailSender.send(message);
    }
}