package com.mytf.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.mytf.model.Structure;

@Service
public class TempLateGenService {
	
	public void genrateTerraform(Structure structure) {
		File srcDir = new File("src/main/resources/terraform/app-services");
		String newFolderName = structure.appname();
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tmpdir, newFolderName);
		File destTmpDir = new File(destDir, structure.appname()+"_infra");
		try {
			File[] files = srcDir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						FileUtils.copyFileToDirectory(file, destTmpDir);
					}
				}
				System.out.println("All files copied successfully!");
			} else {
				System.out.println("Source directory is empty or does not exist.");
			}
			String filePath = destDir.getAbsolutePath() + "\\" + structure.environment() + ".tf";
			witeToFile(filePath, structure,destDir.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void witeToFile(String filePath,Structure structure,String destDir) {
        String content = "module \"app_service_prod\" {\n" +
                         "\tsource = \"./"+structure.appname()+"_infra"+"/\"\n" +
                         "\tlocation            = \""+structure.location()+"\"\n" +
                         "\tenvironment         = \""+structure.environment()+"\"\n" +
                         "\towner               = \""+structure.owner()+"\"\n" +
                         "\tos_type             = \""+structure.osType()+"\"\n" +
                         "\tapp_name            = \""+structure.appname()+"\"\n" +
                         "\tapp_code            = \""+structure.appCode()+"\"\n" +
                         "\tno_of_app_count     = 2\n" +
                         "}\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("Content written to file successfully.");
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        String srcStrpt="src/main/resources/script.sh";
        try {
        FileUtils.copyFileToDirectory(new File(srcStrpt), new File(destDir));
        }catch (Exception e) {
        	e.printStackTrace();
		}
        executeShellScript(destDir,structure);
    }
	
	 private void executeShellScript(String scriptPath,Structure structure) {
		 String gitBashPath = "C:/Program Files/Git/bin/bash.exe";
		 String tmpdir=scriptPath+"\\"+"script.sh";
	        // Destination directory
		 ProcessBuilder processBuilder = new ProcessBuilder(gitBashPath, tmpdir,structure.appname());
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
