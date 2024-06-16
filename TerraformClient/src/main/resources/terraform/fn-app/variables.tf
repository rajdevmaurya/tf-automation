variable "app_name" {
  type        = string
  description = "This variable defines the application name used to build resources.  It must be unique on Azure."
}

variable "environment" {
  type        = string
  description = "This variable defines the environment to be built"
}

variable "location" {
  type        = string
  description = "Azure region where the resource group will be created"
  default     = "East US"
}

variable "owner" {
  type        = string
  description = "Specify the owner of the resource"
}

variable "no_of_app_count" {
  type        = number
  default     = 2
  description = "Provide a no_of_app_count of the resource"
}
variable "os_type" {
  type        = string
  description = "Provide a os_type of the resource"
}

variable "app_code" {
  type        = string
  description = "This variable defines the application code used to build resources.  It must be unique on Azure."
}
 
variable "random_identifier" {
  default = "34324"
}

variable "resource_group_name" {
  default = "my-azure-functions-rg"
}

variable "tag" {
  default = "create-function-app-consumption-python"
}

variable "storage_account_name" {
  default = "my-acc-324324"
}

variable "function_app_name" {
  default = "my-serverless-python-function"
}

variable "sku_storage" {
  default = "Standard_LRS"
}

variable "functions_version" {
  default = "4"
}

variable "python_version" {
  default = "3.9"
}

 