package com.mytf.model;

import jakarta.validation.constraints.NotBlank;

public record MssqlForm(
		
		String appCode,
		@NotBlank(message = "Application Code can not be Empty!")
		@NotBlank(message = "location can not be Empty!")
		String location,
		@NotBlank(message = "environment can not be Empty!")
		String environment,
		@NotBlank(message = "owner can not be Empty!")
		String owner,		
		@NotBlank(message = "os can not be Empty!")
		String osType,
		@NotBlank(message = "Application name can not be Empty!")
		String appname,
		@NotBlank(message = "Number of resource count can not be Empty!")
		String noOfAppCount,
		Boolean enabled) {

}
