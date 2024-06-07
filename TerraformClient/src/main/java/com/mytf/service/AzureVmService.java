package com.mytf.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mytf.model.VmForm;
@Service
public class AzureVmService {
	@Async("threadPoolTaskExecutor")
	public void createAzVmTemplate(VmForm vmForm) {
		File srcDir = new File("src/main/resources/terraform/vm-services");
		String newFolderName = vmForm.appCode();
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tmpdir, newFolderName);
		File destTmpDir = new File(destDir, vmForm.appCode()+"_infra");
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
			String filePath = destDir.getAbsolutePath() + "\\" +vmForm.appCode()+"_"+vmForm.environment() + ".tf";
			witeToFile(filePath, vmForm,destDir.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void witeToFile(String filePath,VmForm structure,String destDir) {
		String content="";
		content="terraform {\r\n"
				+ "  required_providers {\r\n"
				+ "    azurerm = {\r\n"
				+ "      source  = \"hashicorp/azurerm\"\r\n"
				+ "      version = \"=2.99.0\"\r\n"
				+ "    }\r\n"
				+ "  }\r\n"
				+ "\r\n";
				if(structure.enabled()) {
					content=content+"  backend \"azurerm\" {\r\n"
				+ "    resource_group_name  = \"rg-cloudquickpocs\"\r\n"
				+ "    storage_account_name = \"ccpsazuretf0001\"\r\n"
				+ "    container_name       = \"ccpterraformstatefile\"\r\n"
				+ "    key                  = \""+structure.appCode()+".tfstate\"\r\n"
				+ "  }\r\n";
				}
				content= content + "}\r\n"
				+ "\r\n"
				+ "provider \"azurerm\" {\r\n"
				+ "  features {}\r\n"
				+ "}\n";
				content= content +"module \"vm_service_prod\" {\n" +
                "\tsource = \"./"+structure.appCode()+"_infra"+"/\"\n" +
                "\tlocation            = \""+structure.location()+"\"\n" +
                "\tenvironment         = \""+structure.environment()+"\"\n" +
                "\towner               = \""+structure.owner()+"\"\n" +
                "\tos_type             = \""+structure.osType()+"\"\n" +
                "\tapp_name            = \""+structure.appname()+"\"\n" +
                "\tapp_code            = \""+structure.appCode()+"\"\n" +
                "\tno_of_app_count     = "+structure.noOfAppCount()+"\n" +
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
	
	 public void executeShellScript(String scriptPath,VmForm structure) {
		 String gitBashPath = "C:/Program Files/Git/bin/bash.exe";
		 String tmpdir=scriptPath+"\\"+"script.sh";
	        // Destination directory
		 ProcessBuilder processBuilder = new ProcessBuilder(gitBashPath, tmpdir,structure.appCode());
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
