package com.mytf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TemplateUtils {

	public static void executeShellScript(String scriptPath, String appCode) {
		String gitBashPath = "C:/Program Files/Git/bin/bash.exe";
		String tmpdir = scriptPath + "\\" + "script.sh";
		// Destination directory
		ProcessBuilder processBuilder = new ProcessBuilder(gitBashPath, tmpdir,appCode);
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitCode = process.waitFor();
			System.out.println("Script executed with exit code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
