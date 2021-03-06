package com.github.ka4ok85.wca.pod;

import java.util.Arrays;
import java.util.List;

public class Pod {
	private static String ACCESS_URL = "https://apiPOD.silverpop.com/oauth/token";
	private static String XML_API_URL = "https://apiPOD.silverpop.com/XMLAPI";
	private static String SFTP_URL = "transferPOD.silverpop.com";
	private static List<Integer> podList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

	public static String getOAuthEndpoint(int podNumber) {
		if (false == isValidPodNumber(podNumber)) {
			throw new RuntimeException("Unsupported Pod Number");
		}

		return ACCESS_URL.replaceAll("POD", String.valueOf(podNumber));
	}

	public static String getXMLAPIEndpoint(int podNumber) {
		if (false == isValidPodNumber(podNumber)) {
			throw new RuntimeException("Unsupported Pod Number");
		}

		return XML_API_URL.replaceAll("POD", String.valueOf(podNumber));
	}

	public static String getSFTPHostName(int podNumber) {
		if (false == isValidPodNumber(podNumber)) {
			throw new RuntimeException("Unsupported Pod Number");
		}

		return SFTP_URL.replaceAll("POD", String.valueOf(podNumber));
	}

	private static boolean isValidPodNumber(int podNumber) {
		return podList.contains(podNumber);
	}
}
