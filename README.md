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

##Orquestación en local con VirtualBox

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

Con ello le estaremos diciendo a vagrant donde coger la imagen a montar en la máquina virtual y configurar ansible como método de provisionamiento para la máquina que hemos creado. En mi caso, el nombre del script de aprovisionamiento que desarrollé para el anterior hito se llamaba "script.yml", cambiar ese nombre por el que tiene el script de ansible (playbook) que deseamos usar como provisionador (El script debe estar en el mismo directorio que hemos creado). Ahora ya podemos levantar la máquina virtual usando el comando:

* vagrant up

Tal y como puede verse en la [captura](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_2.png), se levanta la máquina virtual definida y se configura ssh como método de acceso a ella. Se omite el provisionamiento, ya que es innecesario hacerlo en local y se realizará con las instancias en la nube a continuación.

A continuación se proporciona el enlace donde pueden ser encontrado el archivo anteriormente descrito:

* [Vagrantfile que provisiona una instancia en local](https://github.com/manuelbr/Proyecto_CC/blob/master/orquestacion/Vagrantfile_local)

##Orquestación y provisión de una instancia de TryStack

A continuación se probará a utilizar vagrant para aprovisionar una máquina virtual alojada en la nube. En este caso, y tal y como se hizo en el hito anterior, se utilizará TryStack (la versión de prueba de Openstack) como proveedor de instancias virtuales. Antes de comenzar es necesario clarificar que será necesaria la versión 1.8.7 de Vagrant (que puede ser descargada de [aquí](https://releases.hashicorp.com/vagrant/1.8.7/)) para que el trabajo junto a TryStack pueda llevarse a cabo. Una vez se instalado el paquete .deb que contiene la mencionada versión, ya podemos instalar el plugin de openstack necesario para conectar con él, usando la siguiente orden:

* vagrant plugin install vagrant-openstack-provider

El proceso para el provisionamiento es similiar al seguido en el apartado anterior, sólo que la modificación que realizamos sobre el "Vagrantfile" debe incluir la conexión a openstack y la configuración de las instancias. Además, tampoco se descargará la imagen a usar, previamente. Más concretamente se deben seguir los siguientes comandos: 

* cd (Directorio donde se quiere crear la máquina)
* mkdir nombre-del-directorio
* cd nombre-del-directorio
* vagrant init 

La máquina instanciada a través de Vagrant puede obtener una ip flotante pública de forma automática si Trystack no tiene ninguna creada. Sin embargo en este caso es necesario crearla primero en Trystack y asociarla directamente en el código de nuestro Vagrantfile, ya que es necesario saber de antemano la ip para poder dársela a ansible, de cara a poder provisionar la máquina creada.Modificamos ahora el archivo "Vagrantfile" creado para que conecte con nuestras credenciales a Openstack siguiendo la siguiente estructura:

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
    os.floating_ip = la ip asignada a la máquina creada
    os.floating_ip_pool = Nombre del pool creado en las redes de Trystack
    os.networks = "Nombre del grupo de redes que usará la instancia"
    os.keypair_name = "Nombre del par de claves pública-privada con el que se accederá a la instancia"
    os.security_groups = ["Nombre del grupo de seguridad que usará la instancia"]
  end

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "Nombre del script que provisionará la instancia"
    ansible.verbose = "v"
    ansible.limit = "all"
    ansible.raw_arguments = ["-i(ip),"]
  end
end
```

Hay que destacar que en la opción "raw_arguments" se le pasa la opción "-i" seguida de la ip de la máquina creada. Los campos relativos al acceso de la api de TryStack pueden rellenarse con la información que se ofrece en la opción "Ver credenciales" de la pestaña "Acceso a la API", que se encuentra en la sección "Acceso y Seguridad" de la pestaña "Compute", tal y como puede verse en la siguiente [imagen](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_7.png). Ahora, y tal y como se hizo en el caso anterior, se debe colocar el script de aprovisionamiento a usar, en el mismo directorio que este "Vagrantfile". Además, debe crearse otro archivo llamado "ansible.cfg" (en el mismo directorio que el Vagrantfile y el script de provisionamiento) que contendrá la ubicación de la clave pública de acceso a la instancia creada, siguiendo la siguiente arquitectura: 

```
[defaults]
private_key_file = directorio donde se encuentra la clave primaria
```

Además, es necesario que el campo "hosts" del script de provisionamiento esté con valor "all", dado que le pasaremos la ip donde provisionar desde Vagrant, sin tener que modificar el archivo hosts local de la instalación de ansible. Usando la siguiente orden se conectará a Trystack, se creará la instancia con las especificaciones que determinamos y se aprovisionará la máquina:

* sudo vagrant up

Como muestra de lo que se debe obtener por pantalla se muestra las siguientes [captura 1](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_6.png) y [captura 2](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_8.png)

En caso de querer probar de forma reiterada el Vagrantfile, será necesario eliminar la configuración que vagrant ha establecido para la anterior máquina con el siguiente comando, y después hacer "vagrant up":

* rm -R .vagrant/

A continuación se proporciona el enlace donde pueden ser encontrado el archivo anteriormente descrito:

* [Vagrantfile que provisiona una instancia en la nube](https://github.com/manuelbr/Proyecto_CC/blob/master/orquestacion/Vagrantfile_nube)

##Orquestación y provisión de varias instancias de TryStack

Dado que Trystack no permite la creación de más de una ip flotante, me ha sido imposible probar el provisionamiento de varias instancias en él. Sin embargo, si se utilizara otro servicio de proporción de máquinas virtuales, el proceso sería igual que el descrito en la anterior sección, pero cambiando el vagrantfile para que siguiera la siguiente arquitectura:

```
maquinas=[
  {
    :hostname => "maquina1",
    :ip => ip flotante de la máquina 1,
  },
  {
    :hostname => "maquina2",
    :ip => ip flotante de la máquina 2,
  },
  {
    :hostname => "maquina3",
    :ip => ip flotante de la máquina 3,
  }
]

Vagrant.configure('2') do |config|
  config.ssh.username = "Nombre de usuario usado para conectar vía ssh"
  config.ssh.private_key_path = "Directorio donde está la clave privada"

  maquinas.each do |maquina|
	config.vm.define maquina[:hostname] do |node|
   		config.vm.provider "openstack" do |os|
    			 os.openstack_auth_url = "Url de autenticación/tokens"
    			 os.username = "Nombre de usuario"
    			 os.password = "Contraseña de acceso a la API de TryStack"
    			 os.tenant_name = "Nombre del proyecto dentro de TryStack"	

    			#Datos de la instancia a crear	
    			os.server_name = "Nombre que seamos que tenga la instancia a crear"
    			os.flavor = "Nombre asociado al tamaño deseado para la instancia"
    			os.image = "Nombre de la imagen que cargará la instancia"
    			os.floating_ip = maquina[:ip]
    			os.floating_ip_pool = Nombre del pool creado en las redes de Trystack
    			os.networks = "Nombre del grupo de redes que usará la instancia"
    			os.keypair_name = "Nombre del par de claves pública-privada con el que se accederá a la instancia"
    			os.security_groups = ["Nombre del grupo de seguridad que usará la instancia"]
  		end
	end
  end

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "script.yml"
    ansible.verbose = "v"
    ansible.limit = "all"
    ansible.raw_arguments = ["-i"+maquinas[0][:ip]+","+maquinas[1][:ip]+","+maquinas[2][:ip]+","]
  end
end
```

A continuación se proporciona el enlace donde pueden ser encontrado el archivo anteriormente descrito:

* [Vagrantfile que provisiona varias instancias en la nube](https://github.com/manuelbr/Proyecto_CC/blob/master/orquestacion/Vagrantfile_multiple)


##Instalación y orquestación de contenedores con Docker (en local)

Lo primero de todo es la instalación de docker, que se puede realizar siguiendo la siguiente lista de comandos (usando apt-get en ubuntu):

```
sudo apt-get update
sudo apt-get install linux-image-extra-$(uname -r) linux-image-extra-virtual
sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D
sudo apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'
sudo apt-get update
sudo apt-get install -y docker-engine
```

Con los anteriores comandos se actualizan los repositorios, se instalan las cabeceras de linux necesarias para la virtualización que usa docker, se añade la clave de repositorios de docker, el propio repositorio y por último se instala docker engine. Tras ésto, podemos comenzar a desarrollar nuestro dockerfile para la creación de un contenedor que tenga todo lo necesario para realizar las pruebas a nuestros microservicios. Dado que son varios los microservicios a implementar, lo mejor es crear un contenedor independiente para cada una de las herramientas a testear, con lo que nos quedan dos script con la siguiente estructura (Instalaremos git en los dos contenedores porque será una herramienta a utilizar en ambos):


* Contenedor con apache2, mysql, php y git
```
FROM debian
MAINTAINER (Nombre del que mantiene el contenedor) (Correo del que mantiene el contenedor)

RUN apt-get update && apt-get install -y apache2 && apt-get install -y libapache2-mod-php5 && apt-get install -y mysql-server && apt-get install -y git

EXPOSE 80 (Se abre el puerto 80 para apache)

ENTRYPOINT ["/usr/sbin/apache2ctl", "-D", "FOREGROUND"] (una vez realizado lo anterior, se ejecuta apache2 y se queda en segundo plano)
```

* Contenedor con RabbitMQ y git
```
FROM debian
MAINTAINER (Nombre del que mantiene el contenedor) (Correo del que mantiene el contenedor)

RUN apt-get update && apt-get install -y rabbitmq-server && apt-get install -y git
```

Ambos script deben llamarse "Dockerfile", por lo que cada uno debe ir en un directorio diferente para que no se solapen. Para poder probar los anteriores contenedores en la nube (más concretamente en Azure), primero tenemos que subirlos a dock hub, de cara a poder descargarlos en las máquinas virtuales que creemos en la nube. Para ello nos crearemos una cuenta en docker y haremos "docker login", introduciendo los datos proporcionados en el registro (tal y como puede verse [aquí](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_9.png)). Una vez localizado en el directorio de uno de los script, pasamos a crear la imagen con el siguiente comando:

```
sudo docker build -t (Nombre de usuario en docker)/(Nombre elegido para la imagen):1.0 .
```

El anterior comando mostraría la siguiente salida: [salida1](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_1.png) y [salida2](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_2.png) para el primer script de apache, [salida2_1](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_1_2.png) y [salida2_2](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_1_3.png) para el segundo de RabbitMQ. El siguiente paso es crear el contenedor asociado a esta imagen, utilizando el siguiente comando:

```
sudo docker run --name (nombre del contenedor) (Nombre de usuario en docker)/(Nombre de la imagen):1.0
``` 

A continuación, se muestra la salida que debe obtenerse con el comando anterior: [salida3](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_3.png), y la comprobación de que el contenedor se ha cargado con: sudo docker ps: [salida3_1](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_3_1.png). 

##Orquestación de contenedores con Docker en Azure
Procedemos a subir nuestros contenedores con el siguiente comando:

```
sudo docker push (Nombre de usuario en docker)/(Nombre de la imagen)
```

En la siguiente captura puede verse la salida que debemos obtener: [salida4](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_10.png). Ahora es el momento de crear la máquina virtual en nuestro proveedor (Azure) usando docker, desde línea de comandos. Para ello, debemos instalar "docker-machine" con las siguientes órdenes:

```
curl -L https://github.com/docker/machine/releases/download/v0.8.2/docker-machine-`uname -s`-`uname -m` >Escritorio/docker-machine
sudo cp Escritorio/docker-machine /usr/local/bin/docker-machine
sudo chmod +x /usr/local/bin/docker-machine
``` 

Para poder conectar con nuestra cuenta de Azure, primero debemos instalar el "cli" de Azure con el comando: "sudo npm install -g azure-cli", habiendo instalado previamente el gestor npm. Ahora, ya podemos crear una máquina virtual (en mi ejemplo con debian 8) en nuestra cuenta de Azure utilizando la siguiente sintaxis de comando:

```
sudo docker-machine create  --driver azure  --azure-image "credativ:Debian:8:latest"  --azure-location "southcentralus"  --azure-resource-group "grupo-debian"  --azure-size "Standard_D1_v2"  --azure-ssh-user (Nombre de usuario que accederá vía ssh)  --azure-subscription-id ("id de suscripción de la cuenta de Azure") --engine-label "data-host=true"  (Nombre de la máquina virtual que se va a crear).
```

Se nos pedirá que hagamos login con nuestra cuenta de Azure, tras lo cuál obtendremos una salida como la siguiente: [salida5](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_7.png). Podemos observar que se ha creado y arrancado la susodicha máquina virtual en la siguiente captura: [salida6](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_8.png). Tras comprobar que la máquina en cuestión está activa, podemos descargar e instalar el contenedor que subimos anteriormente a los repositorios de docker, con el siguiente comando:

```
sudo docker-machine ssh (Nombre de la máquina virtual) sudo docker run -it (Nombre de usuario en docker)/(Nombre de la imagen):1.0
```

Si todo ha salido bien obtendremos la siguiente salida: [salida7](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_11.png). Podemos comprobar que, efectivamente se ha descargado e instalado el contenedor especificado con la orden: "sudo docker-machine ssh (Nombre de la máquina virtual) sudo docker ps", obteniendo la siguiente salida: [salida8](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito4_11_1.png). En mi caso, para el tutorial se ha utilizado el contenedor creado para contener apache2, mysql, php y git.

* Los dos script "Dockerfile" usados para el hito y este tutorial, pueden ser encontrados [aquí](https://github.com/manuelbr/Proyecto_CC/tree/master/contenedores).


##Hito final: Despliegue de microservicios 
Para terminar con la asignatura, la realización del último hito consiste en el despliegue de todos los microservicios con los que se ha trabajado a lo largo de la asignatura desde una única herramienta, combinando su uso con otras que se han usado a lo largo del cuatrimestre, como son: docker para la orquestación de contenedores o ansible para el provisionamiento de máquinas virtuales. Sin embargo, lo primero de todo es decidir la forma y la estructura que tendrá el despliegue, es decir: cómo se organizarán las citadas herramientas para desplegar los microservicios necesarios.

###Organización del despliegue
¿Cuál debe ser la lógica que nos guie en cuanto a la organización en el despliegue de los microservicios? La respuesta a esta pregunta es el razonamiento puro. Los microservicios a desplegar hacen uso de las herramientas: Apache, php, mySql-server, rabbitMq y Git como sistema de control de versiones. RabbitMq es un servicio de encolado de peticiones que dado su encapsulamiento, puede instalarse en un contenedor ligero en el proveedor que utilizaremos en este hito: Azure, ya que una de las prioridades es minimizar la carga que le damos a nuestras máquinas virtuales alojadas en la nube, no solo por eficiencia sino por el coste que puede acarrearnos.Dado que Apache (en conjunción con php) se usa como servidor del contenido multimedia que se ve referenciado en las bases de datos de mySql-server, sería beneficioso para la arquitectura de microservicios el dividir estos dos en diferentes máquinas virtuales. Además, lo mejor sería incluir git en el despliegue de apache y php, ya que podríamos controlar las versiones de los web-services que alojaramos en el mencionado servidor. Todo ello nos dejaría la siguiente organización de despliegue:

* Apache, php y Git en una máquina virtual.
* Mysql-server en otra máquina virtual.
* RabbitMq en un contenedor ligero en una máquina virtual. 

###Realización del despliegue
Lo primero de todo será aclarar que usaré vagrant como herramienta desde la que realizar todo el despliegue. Para comenzar, se debe instalar el plugin de azure para la citada herramienta (versión 2.0.0 para Vagrant 1.9.1) con el comando (utilizando apt-get en ubuntu):

```
vagrant plugin install vagrant-azure --plugin-version '2.0.0.pre1'
```

Tras ello, lo siguiente es instalar el cli de Azure. Este se instala desde los repositorios de npm, por lo que será necesario instalar esta utilidad antes de ello. Para lograr esto, será necesario seguir las siguientes dos órdenes:

```
sudo apt-get install npm
sudo npm install -g azure-cli
```

Para poder trabajar con mi cuenta de azure, paso a logearme con el siguiente comando (siguiendo las instrucciones de copiado de contraseña y logeo por navegador que obtendremos):

```
sudo azure login
```

Dado que cuando trabajamos con vagrant lo hicimos con trystack, será necesario generar lo necesario para conectar esta herramienta con Azure. Para ello, seguimos la siguiente secuencia de pasos en el panel de cotrol de azure:

* Accedemos a la pestaña "Azure Active Directory" > "Registro de Aplicaciones" > "Agregar", ya que necesitaremos definir una app a través de la cuál conectaremos con Vagrant con Azure.
* Definimos la app con el nombre y la url que deseemos y la guardamos.
* Vamos ahora a la pestaña "Suscripciones" > "Control de acceso IAM" > "Agregar", y seleccionamos como rol el de lector y como usuario, el nombre de la aplicación que definimos antes (aunque no aparezca como tal, si se especifica el nombre completo aparece).
* Tras ello, debemos conseguir los datos: "Tenant ID", "Client ID", "Client Secret" y "Subscription ID", que serán necesarios para poder conectar con nuestra cuenta de azure desde Vagrant. Se pueden obtener estos datos fácilmente siguiendo el siguiente tutorial de Terraform para conectar con Azure: [Enlace](https://www.terraform.io/docs/providers/azurerm/). 

También tendremos que crear un par de claves (privada y pública) que usaremos como método de autenticación con las máquinas que creemos en Azure. Para ello, las generamos con el siguiente comando (en ubuntu): "ssh-keygen -t rsa". Tras rellenar los campos que nos piden se obtendrá un fichero rsa con la clave privada y otro .pub, con la pública. Llegados a este punto, ya podemos definir el fichero [Vagrantfile](https://github.com/manuelbr/Proyecto_CC/blob/master/despliegue/Vagrantfile) que tendrá la siguiente estructura según lo acordado más arriba:

```
#Definición de las tres máquinas y sus parámetros
maquinas=[
  {
    #Nombre de la máquina	
    :nombre => "apachehost",
    #Nombre del grupo al que pertenecerá
    :grupo => "apachehost",
    #Nombre del script de provisión que usará
    :provision => "azure1.yml"
  },
  {
    :nombre => "mysqlhost",
    :grupo => "mysqlhost",
    :provision => "azure2.yml"
  },
  {
    :nombre => "rabbitMQhost",
    :grupo => "rabbitMQhost",
    :provision => "azure3.yml"
  }
]

Vagrant.configure("2") do |config|
  config.vm.box     = 'azure' #Box base que usa azure (vacía, a partir de la cuál se crearán las máquinas)
  config.vm.box_url = 'https://github.com/msopentech/vagrant-azure/raw/master/dummy.box'	

  config.ssh.username = "vagrant" #Nombre de usuario con el que accederemos a las máquinas	
  config.ssh.private_key_path = '~/.ssh/id_rsa' #Ruta a la clave privada que definimos anteriormente

  maquinas.each do |maquina|
    	config.vm.define maquina[:nombre] do |node|
		node.vm.hostname = "vagrant"	#host name de la máquina que creamos	
		config.vm.provider :azure do |azure, override|
    			azure.vm_name     = maquina[:nombre] 
    			azure.vm_image_urn= 'canonical:UbuntuServer:16.04-LTS:16.04.201701130' #Nombre de la distribución que llevarán las máquinas: Ubuntu 16
    			azure.vm_size     = 'Basic_A0' #Tamaño de la máquina virtual
			azure.resource_group_name = maquina[:grupo]
    			azure.location = 'westeurope' #Localización de los servidores de Azure donde estará la máquina.
	
			azure.tcp_endpoints = '80:80'
			azure.tenant_id = "Tenant ID" 
   			azure.client_id = "Client ID"
    			azure.client_secret = "Client Secret"
    			azure.subscription_id  = 'Subscription ID'
  		end

		config.vm.provision "ansible" do |ansible|
       			ansible.sudo = true
       			ansible.playbook = maquina[:provision]
       			ansible.verbose = "v"
  		end
	end
  end
end
```

Una vez creadas las máquinas virtuales, en el mismo Vagrantfile se procederá a provisionarlas de diferente manera, con un script de ansible diferente, en función de las necesidades de cada una: [Apache-PHP-Git](https://github.com/manuelbr/Proyecto_CC/blob/master/despliegue/azure1.yml), [MySql-Server](https://github.com/manuelbr/Proyecto_CC/blob/master/despliegue/azure2.yml), [Docker-RabbitMQ+Alpine](https://github.com/manuelbr/Proyecto_CC/blob/master/despliegue/azure3.yml). En cada uno de estos playbooks de provisionamiento de ansible especificaremos el nombre de host que establecimos en el Vagrantfile con el parámetro "vm_name", para que sólo se ejecute cada script para la máquina para el que está diseñado.

* El script azure1.yml provisiona apache, php, las librerías de php necesarias por Apache2 y git en la máquina "apachehost". Además se encarga de arrancar el servidor de Apache.
* El script azure2.yml se encarga de provisionar mysql-server en la máquina "mysqlhost". Como apunte, decir que se elimina el fichero lock en la ruta "/var/lib/apt/lists/", porque da problemas con la instalación descrita en cuanto al bloqueo de los directorios donde debe alojarse mysql.
* Por último, el script azure3.yml se encarga de levantar un contenedor muy ligero (apenas 35 Mb) con Alpine y RabbitMQ: [Enlace](https://hub.docker.com/r/maryville/rabbitmq/). Para lograrlo, instala docker en la máquina virtual, así como pip, que será necesario para poder instalar el módulo "docker-py" de cara a poder cargar el mencionado contenedor (hecho que se hace el último, dejándolo arrancado). 

Como apunte, antes de ejecutar el Vagrantfile, será necesario tener un fichero "ansible.cfg" (en el mismo directorio que el Vagrantfile y los archivos de provisión) como [este](https://github.com/manuelbr/Proyecto_CC/blob/master/despliegue/ansible.cfg), que tenga la ruta a la clave privada que generamos y que usará ansible para conectar con las máquinas definidas y defina el tamaño de las órdenes usadas por el propio ansible (ya que se pueden dar errores por cadenas muy largas). Ahora, ejecutando el comando: "sudo vagrant up --provider=azure", obtendremos la siguiente [salida](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_1.png), señal de que habrá comenzado el proceso de creación de las máquinas virtuales, mientras podremos observar salidas como: [salida2](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_2.png), [salida3](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_3.png), [salida4](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_4.png), [salida5](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_5.png), [salida6](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito5_6.png), en las que podemos ver cómo mientras algunas máquinas aún no se han creado, las que si lo han hecho comienzan a provisionarse de forma paralela. Tras terminar, podemos observar en el portal de azure cómo tenemos nuestras tres máquinas creadas con los parámetros especificados y provisionadas con el material necesario (tal y como hemos podido ver en las capturas de pantalla).



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
- [x] Actualización del tutorial de uso de vagrant con TryStack en el documento README. (a día 11/12/2016).
- [x] Inclusión del tutorial de Docker en local y en Azure. (a día 29/12/2016).

