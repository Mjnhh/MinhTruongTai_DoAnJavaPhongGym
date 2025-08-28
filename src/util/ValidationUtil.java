package util;

import java.util.regex.Pattern;

// Tiện ích kiểm tra/chuẩn hóa dữ liệu nhập
public class ValidationUtil {
	// Mẫu email
	private static final String EMAIL_PATTERN = 
		"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
		"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	
	// Mẫu số điện thoại VN
	private static final String PHONE_PATTERN = "^(0[3|5|7|8|9])+([0-9]{8})$";
	
	private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
	
	// Chuỗi rỗng/null
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
	
	// Email hợp lệ
	public static boolean isValidEmail(String email) {
		if (isEmpty(email)) return false;
		return emailPattern.matcher(email).matches();
	}
	
	// SĐT hợp lệ
	public static boolean isValidPhone(String phone) {
		if (isEmpty(phone)) return false;
		return phonePattern.matcher(phone).matches();
	}
	
	// Số > 0
	public static boolean isValidNumber(String number) {
		if (isEmpty(number)) return false;
		try {
			double value = Double.parseDouble(number);
			return value > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	// Số nguyên >= 0
	public static boolean isValidInteger(String number) {
		if (isEmpty(number)) return false;
		try {
			int value = Integer.parseInt(number);
			return value >= 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	// Viết hoa chữ cái đầu mỗi từ
	public static String normalizeName(String name) {
		if (isEmpty(name)) return "";
		String[] words = name.trim().toLowerCase().split("\\s+");
		StringBuilder result = new StringBuilder();
		for (String word : words) {
			if (word.length() > 0) {
				result.append(Character.toUpperCase(word.charAt(0)))
						.append(word.substring(1))
						.append(" ");
			}
		}
		return result.toString().trim();
	}
	
	// Sinh mã prefix + số thứ tự 3 chữ số
	public static String generateCode(String prefix, int sequence) {
		return String.format("%s%03d", prefix, sequence);
	}
	
	// Kiểm tra độ dài chuỗi
	public static boolean isValidLength(String str, int minLength, int maxLength) {
		if (str == null) return false;
		int length = str.trim().length();
		return length >= minLength && length <= maxLength;
	}
	
	// Làm sạch input (trim + bỏ ký tự nguy hiểm)
	public static String sanitizeInput(String input) {
		if (input == null) return "";
		return input.trim().replaceAll("[<>\"'%;()&+]", "");
	}
	
	// Kiểm tra mã ID theo prefix + 3 số
	public static boolean isValidId(String id, String prefix) {
		if (isEmpty(id)) return false;
		String pattern = "^" + prefix + "\\d{3}$";
		return Pattern.matches(pattern, id);
	}
}