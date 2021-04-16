package EmailSender;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

	public static int emailSender(String emailID) throws Exception {

		int otp = otpGenerator();
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		String myEmail = "noreplysharesimulator@gmail.com";
		String myPwd = "sharesimulator";

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myEmail, myPwd);

			}
		});

		Message message = prepareMessage(session, myEmail, emailID, otp);
		Transport.send(message);

		System.out.println(otp);
		return otp;

	}

	private static Message prepareMessage(Session session, String myEmail,
			String recipientEmail, int OTP) {
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(myEmail));
			msg.setRecipient(Message.RecipientType.TO,
					new InternetAddress(recipientEmail));
			msg.setSubject("Share Simulator OTP");
			msg.setText("Your OTP is " + String.valueOf(OTP));
			return msg;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int otpGenerator() {
		Random r = new Random();
		int otp = 0;
		int digit = 0;
		for (int i = 0; i < 5; i++) {
			digit = r.nextInt(9) + 1;
			otp += (digit) * (Math.pow(10, i));
			// System.out.println(digit);
		}
		System.out.println(otp);
		return otp;
	}

}
