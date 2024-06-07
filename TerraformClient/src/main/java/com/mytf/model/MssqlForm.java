package com.mytf.model;

import jakarta.validation.constraints.NotBlank;

public record MssqlForm(
		@NotBlank(message = "Application resourcegroupname can not be Empty!")
		String resourcegroupname,
		@NotBlank(message = "Application Code can not be Empty!")
		String appCode,
		@NotBlank(message = "location can not be Empty!")
		String location,
		@NotBlank(message = "environment can not be Empty!")
		String environment,
		@NotBlank(message = "owner can not be Empty!")
		String owner,		
		@NotBlank(message = "Application name can not be Empty!")
		String appname,
		@NotBlank(message = "Number of resource count can not be Empty!")
		String sqlservername, 
		@NotBlank(message = "Number of resource count can not be Empty!")
		String sqldatabasename,     
		@NotBlank(message = "Number of resource count can not be Empty!")
		String sqladminlogin,     
		@NotBlank(message = "Number of resource count can not be Empty!")
		String sqladminpassword,     
		
		Boolean enabled) {

}
