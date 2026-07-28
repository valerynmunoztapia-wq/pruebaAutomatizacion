@MarketplaceF @TC-001
Feature: Navegacion en Marketplace
  Scenario: Abrir la página principal
    Given abro facebook marketplace en la url "https://www.facebook.com/marketplace/"
    Then la página principal debería mostrarse correctamente

@MarketplaceF @TC-002
Scenario Outline: Login exitoso
  Given abro facebook marketplace en la url "https://www.facebook.com/login/"
  And inicio sesión con el correo "<email>" y la contraseña "<password>"
  Then la página principal debería mostrarse correctamente

  Examples:
    | email             | password  |
    | test01@test.com   | Test1234  |
    | test02@test.com   | Test1234  |

@MarketplaceF @Busqueda
Scenario Outline: Buscar productos en Marketplace
  Given que el usuario abre el Marketplace sin iniciar sesión
  When ingresa el nombre de un producto "<producto>" en la barra de búsqueda
  Then debería mostrar los resultados de la búsqueda relacionados con "<producto>"

  Examples:
    | producto   |
    | laptop     |
    | bicicleta  |
    | celular    |



