package com.mytf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mytf.model.AppsvcForm;
import com.mytf.utils.TemplateUtils;

@Service
public class AzureAppService {
	@Async("threadPoolTaskExecutor")
	public void createAzAppServiceTemplate(AppsvcForm appsvcForm) {
		File srcDir = new File("src/main/resources/terraform/app-services");
		String newFolderName = appsvcForm.appCode();
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tmpdir, newFolderName);
		File destTmpDir = new File(destDir, appsvcForm.appCode()+"_infra");
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
			String filePath = destDir.getAbsolutePath() + "\\" +appsvcForm.appCode()+"_"+appsvcForm.environment() + ".tf";
			witeToFile(filePath, appsvcForm,destDir.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void witeToFile(String filePath,AppsvcForm structure,String destDir) {
        String content = "module \"app_service_prod\" {\n" +
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
        TemplateUtils.executeShellScript(destDir,structure.appCode());
    }
}
