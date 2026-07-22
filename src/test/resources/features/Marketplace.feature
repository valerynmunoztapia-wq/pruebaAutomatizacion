Feature: Comprar producto en Audiomusica

  @Marketplace @TC-005
  Scenario: Buscar un producto inexistente
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "zzzdddd123"
    And presiono Enter
    Then valido que no existan productos

  @Marketplace @TC-006
  Scenario: Agregar producto al carrito
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    Then valido que el producto "GUITARRA" esté en el carrito


  @Marketplace @TC-007
  Scenario: Agregar múltiples unidades
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And ingreso al Mini-carrito
    And aumento la cantidad del producto
    Then valido que la cantidad sea 2
    And valido que el subtotal se actualice

  @Marketplace @TC-008
  Scenario: Eliminar producto del carrito
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And ingreso al carrito
    When elimino el producto
    Then valido que el carrito quede vacío

  @Marketplace @TC-009
  Scenario: Carrito persistente
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And ingreso al carrito
    When refresco la página
    Then valido que el producto permanezca en el carrito


  @Marketplace @TC-010
  Scenario: Login exitoso
    Given abro el navegador en la url "https://www.audiomusica.com"
    When ingreso al login
    And escribo el correo "usuario@gmail.com" en "login"
    And escribo la contraseña "Password123"
    And presiono el botón Entrar
    Then valido que el usuario inició sesión

  @Marketplace @TC-013
  Scenario: Login inválido
    Given abro el navegador en la url "https://www.audiomusica.com"
    When ingreso al login
    And escribo el correo "usuario@gmail.com" en "login"
    And escribo la contraseña "PasswordIncorrecta123"
    And presiono el botón Entrar
    Then valido que aparece el mensaje de error
    And valido que el usuario permanezca sin sesión

  @Marketplace @TC-014
  Scenario: Inicio del proceso de checkout
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And ingreso al carrito
    And presiono completar la compra
    Then valido que ingreso al checkout
    And escribo el correo "correo@gmail" en "checkout"

  @Marketplace @TC-019
  Scenario: Validar campos obligatorios del checkout
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And presiono proceder al pago
    And presiono continuar sin completar los datos
    Then valido mensaje de campos obligatorios

  @Marketplace @TC-018
  Scenario: Validar formato de correo
    Given abro el navegador en la url "https://www.audiomusica.com"
    When hago clic en el buscador
    And escribo "GUITARRA"
    And presiono Enter
    Then valido que existan productos
    When selecciono el primer producto
    And agrego el producto al carro
    And presiono proceder al pago
    And escribo el correo "correo@gmail" en "checkout"
    And presiono continuar
    Then valido mensaje de correo invalido
