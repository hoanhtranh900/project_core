package com.osp.core.contants;

/**
 * TODO: write you class description here
 *
 * @author
 */

public class Constants {

	private static final String app_config = "application-" + ConfigProperties.getConfigProperties("spring.profiles.active", "application.properties") + ".properties";

	public static final String mail_user_name = ConfigProperties.getConfigProperties("mail.user_name", app_config);
	public static final String mail_pass_word = ConfigProperties.getConfigProperties("mail.pass_word", app_config);
	public static final String mail_host = ConfigProperties.getConfigProperties("mail.host", app_config);
	public static final Integer mail_port = Integer.parseInt(ConfigProperties.getConfigProperties("mail.port", app_config));
	public static final String mail_to = ConfigProperties.getConfigProperties("mail.to", app_config);
	public static final String mail_debug = ConfigProperties.getConfigProperties("mail.debug", app_config);

	public static final String sso_client_id = ConfigProperties.getConfigProperties("sso.client_id", app_config);
	public static final String sso_secret_id = ConfigProperties.getConfigProperties("sso.secret_id", app_config);
	public static final String sso_redirect_uri = ConfigProperties.getConfigProperties("sso.redirect_uri", app_config);
	public static final String sso_url = ConfigProperties.getConfigProperties("sso.tokenUrl", app_config);
	public static final String sso_tokenUrl = ConfigProperties.getConfigProperties("sso.tokenUrl", app_config);
	public static final String sso_getUsernameUrl = ConfigProperties.getConfigProperties("sso.getUsernameUrl", app_config);
	public static final String sso_attributes = ConfigProperties.getConfigProperties("sso.attributes", app_config);

	public static final String error = "urn:ietf:params:scim:api:messages:2.0:Error";
	public static final String SearchRequest = "urn:ietf:params:scim:api:messages:2.0:SearchRequest";

	public static final String webAddress = ConfigProperties.getConfigProperties("wb.address", app_config);
	public static final String supper_admin = ConfigProperties.getConfigProperties("supper.admin", app_config);

	public static final String ckfinder_baseDir = ConfigProperties.getConfigProperties("ckfinder.baseDir", app_config);

	public static final String jobGroup = "SPRING_BOOT_JOB";
	public static final String job_login_domain = "LOGIN_DOMAIN";
	public static final String cron_login_domain = "0 */30 * ? * *";//30p 1 lần
	public static final String job_CategoryBuffer = "LOGIN_DOMAIN";
	public static final String cron_CategoryBuffer = "0 0 0 * * ?";// hàng ngày vào lúc 00h

	/* spring không hỗ trợ sét defautl ngôn ngữ cho @Valid */
	public static final String NOTE_API = "Truyền header: Accept-Language để response trả về lỗi theo ngôn ngữ chỉ định, hỗ trợ 2 ngôn ngữ (vi, en), mặc định ngôn ngữ hệ thống \n";
	public static final String NOTE_API_PAGEABLE = "example ví dụ: ?search=&page=0&size=20&sort=name,desc&sort=status,asc ( lưu ý param sort là muntiple ) \n";
	public static final String API_KEY = "JWT Authentication";
	public static final String AUTHORIZATION = "Authorization";
	public static final String HEADER = "header";
	public static final String ANONYMOUS = "anonymousUser";
	public static final String view = "Xem";
	public static final String add = "Thêm mới";
	public static final String edit = "Sửa";
	public static final String delete = "Xóa";

	public static final String EQUAL = "=";
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String FIVE_STAR = "*****";
	public static final String MINUS = "-";
	public static final String COLON = ":";
	public static final String GZ = "gz";
	public static final String DOT = ".";
	public static final String AND = "&";
	public static final String SLASH_RIGHT = "/";
	public static final String SLASH_LEFT = "\\";
	public static final String EMPTY = "";

	public static final String[] WEB_IGNORING = {
			// -- swagger ui
			"/swagger-resources/**",
			"/swagger-ui.html**",
			"/swagger-ui/**",
			"/v2/api-docs/**",
			"/webjars/**",
			"/public/**"
	};

	public final class HEADER_FIELD {
		public static final String AUTHORIZATION = "Authorization";
		public static final String CONTENT_TYPE = "Content-Type";
	}

	public interface FILE_EXTENSION {
		String IMAGE_PNG = ".png";
	}

	public interface IMAGE_EXTRACT_STATUS {
		String ORIGINAL = "original";
		String COMMENT = "comment";
	}

	public interface CONTENT_TYPE {
		String PDF = "application/pdf";
		String DOC = "application/msword";
		String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	}

	public interface DOWNLOAD_OPTION {
		interface TYPE {
			String ORIGINAL = "original";
			String PDF = "pdf";
			String IMAGE = "image";
		}
	}
}
