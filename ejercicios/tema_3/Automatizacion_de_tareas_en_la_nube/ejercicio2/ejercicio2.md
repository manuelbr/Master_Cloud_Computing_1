##Ejercicio 2: Crear una instancia de una máquina virtual Debian y provisionarla usando alguna de las aplicaciones vistas en el tema sobre herramientas de aprovisionamiento

Dado que dispongo de poco espacio en disco, utilizaré como máquina objetivo a provisionar la que se ha utilizado en el hito 3 en Trystack, con el Vagrantfile que sigue la siguiente estructura:

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
En este Vagrantfile se provisiona usando el script desarrollado para el hito 2 y que puede ser encontrado [aquí](https://github.com/manuelbr/Proyecto_CC/blob/master/provision/script.yml). Tras hacer "vagrant up", se ve que se ha ejecutado correctamente con la siguiente salida: [captura](https://github.com/manuelbr/Proyecto_CC/blob/gh-pages/images/hito3_8.png)


