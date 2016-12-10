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

##Guia de instalación y uso de vagrant en local con VirtualBox

Para poder usar vagrant en local, lo primero de todo hay que instalar una máquina virtual local con la que poder usar esta herramienta de aprovisionamiento. En mi caso usaré VirtualBox para ello. Para instalarlo lo podemos hacer (en el caso de ubuntu, usando apt-get) con los siguientes comandos:

* sudo apt-get update

* sudo apt-get install virtualbox dkms

Tras ésto, ya podemos instalar vagrant ejecutando la siguiente órden:

* sudo apt-get -y install vagrant

Una vez hecho ésto, ya podemos comenzar a usar vagrant para provisionar en VirtualBox. Lo primero de todo es la elección de la imagen que instalaremos. En mi caso, tal y como expliqué en el apartado anterior la mejor opción es usar ubuntu 14.04 (ya que dispone de python 2.7 instalado por defecto: requisito indispensable para el funcionamiento de ansible, herramienta que usaré en conjunto a vagrant para provisionar.):

* vagrant box add ubuntu/trusty64 https://vagrantcloud.com/ubuntu/boxes/trusty64

El anterior comando descargará la imagen que hemos solicitado: ubuntu 14.04. Lo siguiente es crear un directorio para la máquina virtual que montará dicha imagen y crear un fichero de configuración vagrant para dicha máquina. Todo ello lo hacemos con los siguientes comandos:

* cd (Directorio donde se quiere crear la máquina)
* mkdir nombre-del-directorio
* cd nombre-del-directorio
* vagrant init

El último comando creará el archivo de configuración de la máquina virtual llamado "Vagrantfile", lo modificamos cambiando la línea

* config.vm.box = "base" 

por

* config.vm.box = "ubuntu/trusty64"

Añadimos justo debajo de esa línea, las siguientes:

* config.vm.provision "ansible" do |ansible|
*   ansible.verbose = "v"
*   ansible.playbook = "script.yml"
* end 

Con ello le estaremos diciendo a vagrant donde coger la imagen a montar en la máquina virtual y configurar ansible como método de provisionamiento para la máquina que hemos creado. En mi caso, el nombre del script de aprovisionamiento que desarrollé para el anterior hito se llamaba "script.yml", cambiar ese nombre por el que tiene el script de ansible (playbook) que deseamos usar como provisionador (El script debe estar en el mismo directorio que hemos creado). Ahora ya podemos levantar la máquina virtual usando el comando:

* vagrant up

Tal y como puede verse en la [captura](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_2.png), se levanta la máquina virtual definida, se configura ssh como método de acceso a ella y por último se ejecuta el playbook que aprovisionará la máquina.

##Guia de instalación y uso de vagrant en ejecución con OpenStack

A continuación se probará a utilizar vagrant para aprovisionar una máquina virtual alojada en la nube. En este caso, y tal y como se hizo en el hito anterior, se utilizará TryStack (la versión de prueba de Openstack) como proveedor de instancias virtuales. Antes de comenzar es necesario clarificar que será necesaria la versión 1.8.7 de Vagrant (que puede ser descargada de [aquí](https://releases.hashicorp.com/vagrant/1.8.7/)) para que el trabajo junto a TryStack pueda llevarse a cabo. Una vez se instalado el paquete .deb que contiene la mencionada versión, ya podemos instalar el plugin de openstack necesario para conectar con él, usando la siguiente orden:

* vagrant plugin install vagrant-openstack-provider

El proceso para el provisionamiento es similiar al seguido en el apartado anterior, sólo que la modificación que realizamos sobre el "Vagrantfile" debe incluir la conexión a openstack y la configuración de las instancias. Además, tampoco se descargará la imagen a usar, previamente. Más concretamente se deben seguir los siguientes comandos: 

* cd (Directorio donde se quiere crear la máquina)
* mkdir nombre-del-directorio
* cd nombre-del-directorio
* vagrant init

Modificamos ahora el archivo "Vagrantfile" creado para que conecte con nuestras credenciales a Openstack siguiendo la siguiente estructura:

```
Vagrant.configure('2') do |config|
  config.ssh.username = "Nombre de usuario usado para conectar vía ssh"
  config.ssh.private_key_path = "Directorio donde está la clave privada"

  config.vm.provider "openstack" do |os|
    os.openstack_auth_url = "Url de autenticación/tokens"
    os.username = "Nombre de usuario"
    os.password = "Contraseña de acceso a la API de TryStack"
    os.tenant_name = "Nombre del proyecto dentro de TryStack"	

    #Datos de la instancia a crear	
    os.server_name = "Nombre que seamos que tenga la instancia a crear"
    os.flavor = "Nombre asociado al tamaño deseado para la instancia"
    os.image = "Nombre de la imagen que cargará la instancia"
    os.floating_ip_pool = "public"
    os.networks = "Nombre del grupo de redes que usará la instancia"
    os.keypair_name = "Nombre del par de claves pública-privada con el que se accederá a la instancia"
    os.security_groups = ["Nombre del grupo de seguridad que usará la instancia"]
  end

  config.vm.provision "ansible" do |op|
    op.verbose = "v"	
    op.playbook = "Nombre del script que provisionará la instancia"
  end 	
end
```

Los campos relativos al acceso de la api de TryStack pueden rellenarse con la información que se ofrece en la opción "Ver credenciales" de la pestaña "Acceso a la API", que se encuentra en la sección "Acceso y Seguridad" de la pestaña "Compute", tal y como puede verse en la siguiente [imagen](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_7.png). Ahora, y tal y como se hizo en el caso anterior, se debe colocar el script de aprovisionamiento a usar, en el mismo directorio que este "Vagrantfile" y usando la siguiente orden se conectará a Trystack, se creará la instancia con las especificaciones que determinamos y se aprovisionará la máquina:

* sudo vagrant up

Como muestra de lo que se debe obtener por pantalla se muestra la siguiente [captura](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_6.png).

En caso de querer probar de forma reiterada el Vagrantfile, será necesario eliminar la configuración que vagrant ha establecido para la anterior máquina con el siguiente comando, y después hacer "vagrant up":

* rm -R .vagrant/


# Actualizaciones

- [x] Actualización de los objetivos de la segunda y tercera semana (a día 24/10/2016).  
- [x] Añadido del tipo de arquitectura del software (a día 26/10/2016). 
- [x] Modificado el tipo de arquitectura del software, y añadido de los componentes que tendrá (a día 01/11/2016). 
- [x] Actualizados los componentes de la aquitectura (a día 02/11/2016).
- [x] Actualizados los microservicios a implementar (a día 18/11/2016).
- [x] Inclusión de la documentación que hace referencia al aprovisionamiento de la nube a usar. (a día 18/11/2016).
- [x] Eliminación del material sobrante del documento README. (a día 20/11/2016).
- [x] Inclusión del tutorial de uso de ansible en el documento README. (a día 21/11/2016).
- [x] Inclusión del tutorial de uso de vagrant con TryStack en el documento README. (a día 10/12/2016).

