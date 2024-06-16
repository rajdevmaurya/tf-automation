package com.mytf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mytf.model.FnAppForm;
import com.mytf.utils.TemplateUtils;
@Service
public class AzureFnAppService {
	@Async("threadPoolTaskExecutor")
	public void createFnAppTemplate(FnAppForm fnAppForm) {
		File srcDir = new File("src/main/resources/terraform/fn-app");
		String newFolderName = fnAppForm.appCode();
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tmpdir, newFolderName);
		File destTmpDir = new File(destDir, fnAppForm.appCode()+"_infra");
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
			String filePath = destDir.getAbsolutePath() + "\\" +fnAppForm.appCode()+"_"+fnAppForm.environment() + ".tf";
			witeToFile(filePath, fnAppForm,destDir.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void witeToFile(String filePath,FnAppForm structure,String destDir) {
		String content="";
		content="terraform {\r\n"
				+ "  required_providers {\r\n"
				+ "    azurerm = {\r\n"
				+ "      source  = \"hashicorp/azurerm\"\r\n"
				+ "      version = \">= 3.0.0\"\r\n"
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
				content= content +"module \"fnapp_service_prod\" {\n" +
                "\tsource = \"./"+structure.appCode()+"_infra"+"/\"\n" +
                "\tlocation            = \""+structure.location()+"\"\n" +
                "\tenvironment         = \""+structure.environment()+"\"\n" +
                "\towner               = \""+structure.owner()+"\"\n" +
                "\tos_type             = \""+structure.osType()+"\"\n" +
                "\tapp_name            = \""+structure.appname()+"\"\n" +
                "\tapp_code            = \""+structure.appCode()+"\"\n" +
                "\tno_of_app_count     = "+structure.noOfAppCount()+"\n" 
                
				+ "    resource_group_name  = \"rg-cloudquickpocs\"\r\n"
				+ "    tag = \"create-function-app-consumption-python\"\r\n"
				+ "    function_app_name = \"my-serverless-python-function\"\r\n"
				+ "    sku_storage = \"sku_storage\"\r\n"
				+ "    functions_version =  4\r\n"
				+ "    python_version  =  3.9\r\n"
				+ "    storage_account_name       = \"ccpterraformstatefile\"\r\n" +
                
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
		TemplateUtils.executeShellScript(destDir,structure.appCode());
   }
}
