# Proyecto de Manuel Blanco Rienda en la asignatura CC
Proyecto de la asignatura CC 2016-2017. Este proyecto consistirá en una adaptación del trabajo de fin de grado de Manuel Blanco Rienda (Proyecto People-Emotions), para preparar el software fehaciente a dicho proyecto para un despliegue en la nube.

# ¿Qué es este proyecto?
El sistema que sobre el que trata este Proyecto tiene a bien autodenominarse “Pople-Emotions”, queriendo reflejar que su objetivo principal es despertar emociones en los usuarios que lo utilicen.  Dichas impresiones no serán producto del uso directo del software, sino de la aplicación de los resultados que sugiere éste.

Una vez explicado de manera general es hora de entrar más en profundidad, dentro del concepto que el autor ha desarrollado a lo largo del tiempo. El sistema del que se está hablando será implementado en forma de aplicación móvil (para sistemas Operativos Android) conectada a una base de datos desplegada en la nube, el cuál a través de varios tipos de “tests” identificará la situación emocional de la persona a la que han sido sometidas dichas pruebas. Una vez tenga la información necesaria, el software proporcionará una sugerencia filmográfica acorde al estado emocional del sujeto. A grandes rasgos, la funcionalidad del proyecto se basará en ello. Sin embargo, hay que destacar que los pilares sobre los que se fundamenta este trabajo son las emociones.

# ¿Qué arquitectura usará el software?

El software usará una arquitectura basada en microservicios, según la cuál tendremos varias partes diferenciadas dentro de la aplicación. Por un lado estarán los microservicios que se comunicarán entre sí, a saber:
	
- [x] Microservicio de conexión y lectura de datos con una API conectada a la base de datos de películas de IMDB, que devuelva la información de forma tratable por la aplicación.
- [x] Microservicio de conexión, lectura y modificación con una base de datos relacional que contenga las preguntas que conformarán los cuestionarios emocionales de la aplicación y los datos de los usuarios que se registran.
- [x] Microservicio de encolado de peticiones de usuario, de cara a tener un orden en cuanto a las peticiones que se realizan a la base de datos.

Las peticiones de los usuarios se irán almacenando en el microservicio de encolado de peticiones, de donde se irán sacando por el controlador de los
microservicios, que en función de las peticiones que saque, redirigirá a uno u otro microservicio. Dicho controlador será la puerta de conexión
entre estos componentes y el software Android de la aplicación.

# Aprovisionamiento de la nube

Para poder desplegar los servicios anteriormente descritos en la nube, será necesario tener una forma
de aprovisionar rápidamente dicha nube, de cara a tener todas las herramientas necesarias para poder
desplegar estos servicios.

Como plataforma de despliegue de la imagen necesaria, se ha elegido TryStack: web para 
usuarios que quieran probar openstack, el servicio cloud de código abierto. La elección de esta 
herramienta se ha debido principalmente a que no hay que proporcionar números de cuenta bancarios
ni datos comprometedores. Ello es porque esta plataforma es totalmente gratuita, aunque
evidentemente cuenta con sus limitaciones: las instancias creadas se destruyen veinticuatro horas
después de ser creadas, solo permiten tres intancias a la vez, etc.

El sistema de aprovisionamiento elegido es "ansible", ya que es muy sencillo de usar: funciona
mediante ficheros ".YAML" (playbooks), scripts fáciles de entender a simple vista, ya que se organizan en tareas a realizar: instalar apache, php...etc. Además de ello, ansible no necesita usuario root, utiliza ssh como método de autenticación con uso de claves (sistema  de autenticación facilitado por
openstack en la creación de sus instancias) y no usa agentes como otros sistemas de aprovisionamiento más complejos de entender.

Por otro lado, en TryStack se ha elegido ubuntu 14.04 como imagen que conformará la máquina virtual
que usaremos como nube. La principal razón de esta decisión es porque este sistema operativo cuenta
con Python 2.7 instalado de serie, requisito fundamental para que funcione ansible, ya que aún no
ha sido actualizado para funcionar con versiones superiores a esa y que sí están instaladas por defecto
en distribuciones más nuevas de ubuntu.

Las herramientas que están preparadas para instalarse como aprovisionamiento en la máquina dada son las siguientes: 

- [x] Apache2, para poder tener un servidor donde alojar la base de datos relacional y los diversos scripts a utilizar.
- [x] El módulo Php que requiere Apache2 para poder funcionar.
- [x] MySql server para poder alojar la base de datos en comunión con Apache2.
- [x] RabbitMQ para poder tener el microservicio de gestión de colas de peticiones.
- [x] Git para la gestión de versiones del proyecto.

Además de ello, en el script se realizan otras tareas como: la configuración del DNS de la máquina host, ya que por defecto openstack no la configura y la puesta en marcha de apache2 una vez instalado.

##Guia de instalación y funcionamiento de ansible con la máquina virtual
      
Tal y como se ha dicho antes, ansible funciona con la versión 2.7 de Python, por lo que el primer paso es instalarlo en la máquina donde se hará
el aprovisionamiento, por ejemplo usando apt-get en ubuntu utilizando las siguientes ordenes por línea de comandos:

* sudo add-apt-repository ppa:fkrull/deadsnakes
* sudo apt-get update
* sudo apt-get install python2.7
  
Tras esto, llega el momento de instalar ansible en local, por ejemplo usando apt-get en ubuntu con la siguiente lista de órdenes:

* sudo apt-get install software-properties-common
* sudo apt-add-repository ppa:ansible/ansible
* sudo apt-get update
* sudo apt-get install ansible


Ahora, es el momento de configurarlo modificando el archivo /etc/ansible/hosts y añadiendo al final
el nombre del grupo de host a definir y las direcciones ip que lo definirán, junto con sus nombres. Como ejemplo se puede tomar
las siguientes líneas:


* [hosts]
* host1 ansible_ssh_host=ip_maquina_a_proviosionar

 
Tras ésto, se debe crear una carpeta llamada "group_vars" que contenga un único archivo que se llame como el grupo que se ha definido en el archivo hosts anterior (el nombre asignado al grupo de hosts en el fichero descrito anteriormente), que contenga
las siguientes líneas:


* ---
* ansible_ssh_user: nombre_maquina_a_provisionar
 
El significado de estas dos líneas es decidir el nombre de usuario que utilizará ssh para autenticarse
dentro del host establecido. Ahora, ya se puede ejecutar el script en la máquina host con la
siguiente orden:

* ansible-playbook -i (Directorio donde está el fichero hosts: /etc/ansible/hosts) (Directorio donde se encuentre el script.yml) --private-key (Directorio donde se encuentre el archivo ".pem" con la clave privada a utilizar en la autenticación via ssh)

A continuación se proporciona el script necesario para realizar el aprovisionamiento:

* [Script de aprovisionamiento](https://github.com/manuelbr/Proyecto_CC/tree/master/provision/script.yml)

# Actualizaciones

- [x] Actualización de los objetivos de la segunda y tercera semana (a día 24/10/2016).  
- [x] Añadido del tipo de arquitectura del software (a día 26/10/2016). 
- [x] Modificado el tipo de arquitectura del software, y añadido de los componentes que tendrá (a día 01/11/2016). 
- [x] Actualizados los componentes de la aquitectura (a día 02/11/2016).
- [x] Actualizados los microservicios a implementar (a día 18/11/2016).
- [x] Inclusión de la documentación que hace referencia al aprovisionamiento de la nube a usar. (a día 18/11/2016).
- [x] Eliminación del material sobrante del documento README. (a día 20/11/2016).
- [x] Inclusión del tutorial de uso de ansible en el documento README. (a día 21/11/2016).

