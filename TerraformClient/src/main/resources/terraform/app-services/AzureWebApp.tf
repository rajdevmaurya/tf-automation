# Create a Resource Group
resource "azurerm_resource_group" "appservice-rg" {
  name     = "${var.app_code}-${var.environment}_rg"
  location = var.location
  tags = {
    description = "POCs Demo"
    environment = var.environment
    owner       = var.owner   
  }
}

# Create the App Service Plan
resource "azurerm_app_service_plan" "service-plan" {
  name                = "${var.app_code}-${var.environment}_service-plan"
  location            = azurerm_resource_group.appservice-rg.location
  resource_group_name = azurerm_resource_group.appservice-rg.name
  kind                = var.os_type == "Linux" ? "Linux" : "Windows"
  reserved            = var.os_type == "Linux"

  sku {
    tier = "Standard"
    size = "S1"
  }

  tags = {
    description = "POCs Demo"
    environment = var.environment
    owner       = var.owner
  }
}

# Create the App Service
resource "azurerm_app_service" "app-service" {
  count               = var.no_of_app_count
  name                = "${var.app_name}-${count.index + 1}"
  location            = azurerm_resource_group.appservice-rg.location
  resource_group_name = azurerm_resource_group.appservice-rg.name
  app_service_plan_id = azurerm_app_service_plan.service-plan.id

  site_config {
    linux_fx_version   = var.os_type == "Linux" ? "JAVA|17" : null
	windows_fx_version = var.os_type == "Windows" ? "JAVA|17" : null
  }

  tags = {
    description = "POCs Demo"
    environment = var.environment
    owner       = var.owner 
  }
}
