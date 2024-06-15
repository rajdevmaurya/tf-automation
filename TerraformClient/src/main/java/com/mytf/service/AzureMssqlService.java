package com.mytf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mytf.model.MssqlForm;
import com.mytf.utils.TemplateUtils;
@Service
public class AzureMssqlService {
	@Async("threadPoolTaskExecutor")
	public void createMssqlTemplate(MssqlForm mssqlForm) {
		File srcDir = new File("src/main/resources/terraform/mssql-service");
		String newFolderName = mssqlForm.appCode();
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tmpdir, newFolderName);
		File destTmpDir = new File(destDir, mssqlForm.appCode()+"_infra");
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
			String filePath = destDir.getAbsolutePath() + "\\" +mssqlForm.appCode()+"_"+mssqlForm.environment() + ".tf";
			witeToFile(filePath, mssqlForm,destDir.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void witeToFile(String filePath,MssqlForm structure,String destDir) {
		
		String content="";
		content="terraform {\r\n"
				+ "  required_providers {\r\n"
				+ "    azurerm = {\r\n"
				+ "      source  = \"hashicorp/azurerm\"\r\n"
				+ "      version = \"=3.6.0\"\r\n"
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
				content= content +"module \"mssql_service_prod\" {\n" +
                "\tsource = \"./"+structure.appCode()+"_infra"+"/\"\n" 
                + "	resource_group_name     = \"rg-terraform-web-sql-db\"\r\n"
        		+ "	resource_group_location = \"East US\"\r\n"
        		+ "	app_service_plan_name   = \"appserviceplan-web-21\"\r\n"
        		+ "	app_service_name        = \"terraform-web-021\"\r\n"
        		+ "	sql_server_name         = \"terraform-sqlserver-02133\"\r\n"
        		+ "	sql_database_name       = \"ProductsDB\"\r\n"
        		+ "	sql_admin_login         = \"user01\"\r\n"
        		+ "	sql_admin_password      = \"@Aa123456789!\"\r\n"
        		+ "}\r\n";
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
