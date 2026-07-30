@MarketplaceF @TC-001
Feature: Navegacion en Marketplace
  Scenario: Abro la página principal
    Given abro facebook marketplace en la url "https://www.facebook.com/marketplace/"
    Then la página principal debería mostrarse correctamente

@MarketplaceF @TC-002
  Scenario Outline: Login exitoso simulado
    Given  estoy en la página de login
  When ingreso el nombre "<nombre>" el correo "<correo>" y la contraseña "<contraseña>"
    And hago click en el botón de login
    Then se muestra un mensaje de "Login simulado exitoso"

  Examples:
    | nombre       | correo             | contraseña |
    | Test User  | test01@test.com    | Test1234   |
    | Test User2 | test02@test.com    | Test1234   |

@MarketplaceF @TC-003
  Scenario Outline: Login inválido
    Given  estoy en la página de login
    When ingreso el nombre "<nombre>" el correo "<correo>" y la contraseña "<contraseña>"
    And hago click en el botón de login
    Then debería mostrarse un mensaje de error de login

  Examples:
    | nombre       | correo             | contraseña |
    | Test User  | test01@test.com    | Test1235   |
    | Test User2 | test02@test.com    | Test1236   |

@MarketplaceF @TC-004
  Scenario Outline: Registro de usuario simulado
    Given estoy en la página de login
    When ingreso el nombre "<nombre>" el correo "<correo>" y la contraseña "<contraseña>"
    And completo los datos requeridos
    And confirmo el registro
    Then debo visualizar "Account Created!"

  Examples:
    | nombre       | correo          | contraseña |
    | Test User  | test01@test.com   | Test1234   |
    | Test User2 | test02@test.com   | Test1234   |

@MarketplaceF @TC-005
  Scenario Outline: Buscar productos en Marketplace
    Given abro el Marketplace sin iniciar sesión
    When ingresa el nombre de un producto "<producto>" en la barra de búsqueda
    Then debería mostrar los resultados de la búsqueda relacionados con "<producto>"

  Examples:
    | producto   |
    | laptop     |
    | bicicleta  |
    | celular    |

@MarketplaceF @TC-006
  Scenario Outline: Persistencia de búsqueda
    Given abro el Marketplace sin iniciar sesión
    When ingresa el nombre de un producto "<producto>" en la barra de búsqueda
    And actualizo la página
    Then debería mostrar los resultados de la búsqueda relacionados con "<producto>"


  Examples:
    | producto   |
    | laptop     |
    | bicicleta  |
    | celular    |

@MarketplaceF @TC-007
  Scenario Outline: Filtrar productos por categoría
    Given abro el Marketplace sin iniciar sesión
    When selecciono la categoría de marketplace "<categoría>"
    Then debería mostrar productos relacionados con "<categoría>"

  Examples:
    | categoría   |
    | Electrónica |
    | Ropa        |
    | Hogar       |

@MarketplaceF @TC-008
Scenario Outline: Ver detalle de un producto
  Given abro el Marketplace sin iniciar sesión
  When busco el producto "<producto>"
  And selecciono el primer resultado
  Then debería mostrar el detalle del producto con nombre, precio y vendedor

  Examples:
    | producto   |
    | bicicleta  |
    | laptop     |
    | celular    |

@MarketplaceF @TC-009
  Scenario Outline: Agregar producto al carrito
    Given abro el Marketplace sin iniciar sesión
    When busco el producto "<producto>"
    And selecciono el primer resultado
    When agrego el producto "<producto>" al carrito
    Then debería aparecer en el carrito

  Examples:
    | producto   |
    | bicicleta  |
    | laptop     |
    | celular    |

@MarketplaceF @TC-010
  Scenario Outline: Eliminar producto del carrito
    Given tengo un producto "<producto>" en el carrito
    When elimino el producto "<producto>"
    Then el carrito debería quedar vacío

  Examples:
    | producto   |
    | bicicleta  |
    | laptop     |
    | celular    |

@MarketplaceF @TC-011
  Scenario Outline: Persistencia de carrito
    Given agrego el producto "<producto>" al carrito
    When actualizo la página
    Then el producto "<producto>" debería seguir en el carrito

  Examples:
    | producto   |
    | bicicleta  |
    | laptop     |
    | celular    |

@MarketplaceF @TC-012
  Scenario Outline: Inicio del proceso de Checkout
    Given abro el Marketplace sin iniciar sesión
    When busco el producto "<producto>"
    And selecciono el primer resultado
    And agrego el producto "<producto>" al carrito
    And ingreso al carrito de marketplace
    And presiono completar la compra en marketplace
    Then debería mostrarse la pantalla de checkout

    Examples:
      | producto   |
      | bicicleta  |
      | laptop     |
      | celular    |

@MarketplaceF @TC-013
  Scenario Outline: Validar campos obligatorios del checkout
    Given busco el producto "<producto>"
    And selecciono el primer resultado
    When agrego el producto "<producto>" al carrito
    And inicio el checkout simulado
    Then debería mostrar mensajes de error en los campos obligatorios

    Examples:
      | producto   |
      | bicicleta  |
      | laptop     |
      | celular    |

@MarketplaceF @TC-014
  Scenario Outline: Flujo real de compra en Marketplace
    Given busco el producto "<producto>"
    And selecciono el primer resultado
    When abro Messenger para contactar al vendedor
    Then debería poder enviar un mensaje al vendedor

    Examples:
      | producto   |
      | bicicleta  |
      | laptop     |
      | celular    |

