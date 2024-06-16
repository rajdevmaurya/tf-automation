# Create RG for Function App
resource "azurerm_resource_group" "funcdeploy" {
 name     = "CloudquickPocs-rg-functionApp"
  location = "East US"
  tags = {
    Environment = var.tag
  }
}

# Create Azure Storage Account
resource "azurerm_storage_account" "funcdeploy" {
  name                     = "uniquestorageaccount346"
  resource_group_name      = azurerm_resource_group.funcdeploy.name
  location                 = azurerm_resource_group.funcdeploy.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

# Create Azure Storage Container
resource "azurerm_storage_container" "funcdeploy" {
  name                  = "contents"
  storage_account_name  = azurerm_storage_account.funcdeploy.name
  container_access_type = "private"
}

# Create Azure Application Insights
resource "azurerm_application_insights" "funcdeploy" {
  name                = "funcapp-appinsights-001"
  location            = azurerm_resource_group.funcdeploy.location
  resource_group_name = azurerm_resource_group.funcdeploy.name
  application_type    = "web"
  tags = {
    "Monitoring" = "functionApp"
  }
}

# Create Azure App Service Plan
resource "azurerm_app_service_plan" "funcdeploy" {
  name                = "functions-consumption-asp"
  location            = azurerm_resource_group.funcdeploy.location
  resource_group_name = azurerm_resource_group.funcdeploy.name
  kind                = "FunctionApp"
  reserved            = true

  sku {
    tier = "Dynamic"
    size = "Y1"
  }
}

# Create Azure Function App
resource "azurerm_function_app" "funcdeploy" {
  name                       = "cloudquickpocsfuncapp"
  location                   = azurerm_resource_group.funcdeploy.location
  resource_group_name        = azurerm_resource_group.funcdeploy.name
  app_service_plan_id        = azurerm_app_service_plan.funcdeploy.id
  storage_account_name       = azurerm_storage_account.funcdeploy.name
  storage_account_access_key = azurerm_storage_account.funcdeploy.primary_access_key
  https_only                 = true
  version                    = "~4"
  os_type                    = "linux"

  app_settings = {
    "WEBSITE_RUN_FROM_PACKAGE"         = "1"
    "FUNCTIONS_WORKER_RUNTIME"         = "python"
    "APPINSIGHTS_INSTRUMENTATIONKEY"   = azurerm_application_insights.funcdeploy.instrumentation_key
    "AzureWebJobsStorage"              = azurerm_storage_account.funcdeploy.primary_connection_string
  }

  site_config {
    linux_fx_version = "Python|3.9"
    ftps_state       = "Disabled"
  }

  identity {
    type = "SystemAssigned"
  }
}